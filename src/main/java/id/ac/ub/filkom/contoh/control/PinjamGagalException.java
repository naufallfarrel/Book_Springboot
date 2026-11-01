package id.ac.ub.filkom.contoh.control;

/**
 * Menandai alur alternatif UC-01 Pinjam Buku yang gagal.
 * Dilempar oleh «control» (PinjamService), ditangkap oleh «boundary».
 */
public class PinjamGagalException extends RuntimeException {

    private final boolean bisaReservasi;

    private PinjamGagalException(String pesan, boolean bisaReservasi) {
        super(pesan);
        this.bisaReservasi = bisaReservasi;
    }

    /** [13] pesanHabis — alt [stok habis] (alur 4a). */
    public static PinjamGagalException stokHabis() {
        return new PinjamGagalException("Stok buku habis", true);
    }

    /** Alur 4b — melewati batas pinjam. */
    public static PinjamGagalException melewatiBatas() {
        return new PinjamGagalException("Melewati batas pinjam", false);
    }

    /** Validasi input — data tidak terdaftar. */
    public static PinjamGagalException tidakDitemukan(String objek) {
        return new PinjamGagalException(objek + " tidak ditemukan", false);
    }

    /** true bila boundary perlu menampilkan tautan «extend» Ajukan Reservasi. */
    public boolean isBisaReservasi() {
        return bisaReservasi;
    }
}
