package com.mycompany.stokfiy.model;

import java.io.Serializable;

public class Produk implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nama;
    private String kategori;
    private double harga;
    private int stok;

    public Produk(String id, String nama, String kategori, double harga, int stok) {
        setId(id);
        setNama(nama);
        setKategori(kategori);
        setHarga(harga);
        setStok(stok);
    }

    public String getId() { return id; }
    public void setId(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID tidak boleh kosong.");
        this.id = id.trim();
    }

    public String getNama() { return nama; }
    public void setNama(String nama) {
        if (nama == null || nama.isBlank()) throw new IllegalArgumentException("Nama tidak boleh kosong.");
        this.nama = nama.trim();
    }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) {
        if (kategori == null || kategori.isBlank()) throw new IllegalArgumentException("Kategori tidak boleh kosong.");
        this.kategori = kategori.trim();
    }

    public double getHarga() { return harga; }
    public void setHarga(double harga) {
        if (!Double.isFinite(harga) || harga < 0) throw new IllegalArgumentException("Harga tidak boleh negatif atau tidak valid.");
        this.harga = harga;
    }

    public int getStok() { return stok; }
    public void setStok(int stok) {
        if (stok < 0) throw new IllegalArgumentException("Stok tidak boleh negatif.");
        this.stok = stok;
    }

    public void tambahStok(int jumlah) {
        if (jumlah <= 0) throw new IllegalArgumentException("Jumlah harus lebih dari 0.");
        if (jumlah > Integer.MAX_VALUE - stok) throw new IllegalArgumentException("Jumlah stok terlalu besar.");
        stok += jumlah;
    }

    public boolean kurangiStok(int jumlah) {
        if (jumlah <= 0) throw new IllegalArgumentException("Jumlah harus lebih dari 0.");
        if (jumlah > stok) return false;
        stok -= jumlah;
        return true;
    }
}
