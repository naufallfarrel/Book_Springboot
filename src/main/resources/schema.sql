-- Skema tabel dari rancangan data desain 8a
CREATE TABLE anggota (
    id_anggota   VARCHAR(10)  NOT NULL PRIMARY KEY,
    nama         VARCHAR(100) NOT NULL,
    batas_pinjam INT
);

CREATE TABLE buku (
    id_buku VARCHAR(10)  NOT NULL PRIMARY KEY,
    judul   VARCHAR(200) NOT NULL,
    stok    INT
);

CREATE TABLE transaksi_pinjam (
    id_transaksi  VARCHAR(10) NOT NULL PRIMARY KEY,
    id_anggota    VARCHAR(10) NOT NULL,
    id_buku       VARCHAR(10) NOT NULL,
    tanggal_pinjam DATE,
    status        VARCHAR(20),
    CONSTRAINT fk_transaksi_anggota FOREIGN KEY (id_anggota) REFERENCES anggota (id_anggota),
    CONSTRAINT fk_transaksi_buku    FOREIGN KEY (id_buku)    REFERENCES buku (id_buku)
);
