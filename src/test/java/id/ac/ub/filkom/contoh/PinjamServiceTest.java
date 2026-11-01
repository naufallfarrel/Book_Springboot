package id.ac.ub.filkom.contoh;

import id.ac.ub.filkom.contoh.control.Bukti;
import id.ac.ub.filkom.contoh.control.PinjamGagalException;
import id.ac.ub.filkom.contoh.control.PinjamService;
import id.ac.ub.filkom.contoh.entity.BukuRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Menguji skenario UC-01 Pinjam Buku end-to-end pada level control.
 * Sekaligus membuktikan entity cocok dengan schema.sql (ddl-auto=validate lulus).
 */
@SpringBootTest
@Transactional          // setiap test di-rollback, data awal data.sql selalu bersih
class PinjamServiceTest {

    @Autowired PinjamService pinjamService;
    @Autowired BukuRepository bukuRepository;

    @Test
    void stokTersedia_menghasilkanBukti_dan_stokBerkurang() {
        Bukti bukti = pinjamService.pinjamBuku("A001", "B001");

        assertEquals("DIPINJAM", bukti.getStatus());
        assertEquals("Budi Santoso", bukti.getNamaAnggota());
        assertEquals("Pemrograman Java", bukti.getJudulBuku());
        assertEquals(2, bukuRepository.findById("B001").orElseThrow().getStok());
    }

    @Test
    void stokHabis_ditolak_dan_bisaReservasi() {
        PinjamGagalException e = assertThrows(PinjamGagalException.class,
                () -> pinjamService.pinjamBuku("A001", "B002"));
        assertEquals("Stok buku habis", e.getMessage());
        assertTrue(e.isBisaReservasi());
    }

    @Test
    void melewatiBatas_ditolak_padaPinjamanKeTiga() {
        pinjamService.pinjamBuku("A001", "B001");
        pinjamService.pinjamBuku("A001", "B003");
        PinjamGagalException e = assertThrows(PinjamGagalException.class,
                () -> pinjamService.pinjamBuku("A001", "B001"));
        assertEquals("Melewati batas pinjam", e.getMessage());
    }

    @Test
    void dataTidakValid_ditolak() {
        PinjamGagalException e = assertThrows(PinjamGagalException.class,
                () -> pinjamService.pinjamBuku("A001", "XXXX"));
        assertEquals("Buku tidak ditemukan", e.getMessage());
    }
}
