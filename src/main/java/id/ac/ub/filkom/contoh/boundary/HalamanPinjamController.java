package id.ac.ub.filkom.contoh.boundary;

import id.ac.ub.filkom.contoh.control.Bukti;
import id.ac.ub.filkom.contoh.control.PinjamGagalException;
import id.ac.ub.filkom.contoh.control.PinjamService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * «boundary» HalamanPinjam (bagian penerima request; tampilannya di
 * templates/pinjam.html dan bukti.html).
 * Hanya membaca input, memanggil method «control», dan memilih template.
 */
@Controller
@RequestMapping("/pinjam")
public class HalamanPinjamController {

    private static final String ATTR_ANGGOTA = "idAnggota";
    private static final String ANGGOTA_DEFAULT = "A001";   // simulasi anggota yang login

    private final PinjamService pinjamService;               // «control»

    public HalamanPinjamController(PinjamService pinjamService) {
        this.pinjamService = pinjamService;
    }

    /**
     * Langkah 1-2 scenario: tampilkan daftar buku beserta stok.
     * Opsional: /pinjam?anggota=A002 untuk berganti anggota saat menguji skenario.
     */
    @GetMapping
    public String tampilHalaman(@RequestParam(name = "anggota", required = false) String anggota,
                                HttpSession session, Model model) {
        if (anggota != null && !anggota.isBlank()) {
            session.setAttribute(ATTR_ANGGOTA, anggota.trim());
        }
        return isiHalaman(session, model);
    }

    /** [1] pilihBuku(idBuku) dari aktor Anggota. */
    @PostMapping
    public String pilihBuku(@RequestParam String idBuku,
                            HttpSession session, Model model) {
        String idAnggota = idAnggotaLogin(session);
        try {
            // [2] pinjamBuku(idAnggota, idBuku)
            Bukti bukti = pinjamService.pinjamBuku(idAnggota, idBuku);
            return tampilBukti(bukti, model);                // [12] tampilkan bukti
        } catch (PinjamGagalException e) {                   // [13] pesanHabis
            model.addAttribute("pesan", e.getMessage());
            model.addAttribute("bisaReservasi", e.isBisaReservasi());
            return isiHalaman(session, model);               // [14] pesan & reservasi
        }
    }

    /** +tampilBukti() pada class diagram 8a. */
    private String tampilBukti(Bukti bukti, Model model) {
        model.addAttribute("bukti", bukti);
        return "bukti";                                      // templates/bukti.html
    }

    /** Mengisi Model untuk halaman pinjam, lalu memilih template. */
    private String isiHalaman(HttpSession session, Model model) {
        model.addAttribute("idAnggota", idAnggotaLogin(session));
        model.addAttribute("daftarBuku", pinjamService.daftarBuku());
        return "pinjam";                                     // templates/pinjam.html
    }

    /** Id anggota disimpan di session (bukan di field: @Controller itu singleton). */
    private String idAnggotaLogin(HttpSession session) {
        Object id = session.getAttribute(ATTR_ANGGOTA);
        return id != null ? id.toString() : ANGGOTA_DEFAULT;
    }
}
