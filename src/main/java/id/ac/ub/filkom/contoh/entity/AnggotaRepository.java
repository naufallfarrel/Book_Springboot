package id.ac.ub.filkom.contoh.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/** Akses tabel anggota (implementasi otomatis Spring Data JPA). */
public interface AnggotaRepository extends JpaRepository<Anggota, String> {
}
