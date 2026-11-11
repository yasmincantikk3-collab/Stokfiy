package com.mycompany.stokfiy.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaksi implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMAT_TANGGAL = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private String idTransaksi;
    private String idProduk;
    private String namaProduk;
    private int jumlah;
    private double totalHarga;
    private LocalDateTime tanggal;

    public Transaksi(String idTransaksi, Produk produk, int jumlah) {
        if (idTransaksi == null || idTransaksi.isBlank()) throw new IllegalArgumentException("ID transaksi tidak valid.");
        if (produk == null) throw new IllegalArgumentException("Produk tidak boleh kosong.");
        if (jumlah <= 0) throw new IllegalArgumentException("Jumlah harus lebih dari 0.");
        this.idTransaksi = idTransaksi;
        this.idProduk = produk.getId();
        this.namaProduk = produk.getNama();
        this.jumlah = jumlah;
        this.totalHarga = produk.getHarga() * jumlah;
        this.tanggal = LocalDateTime.now();
    }

    public String getIdTransaksi() { return idTransaksi; }
    public String getIdProduk() { return idProduk; }
    public String getNamaProduk() { return namaProduk; }
    public int getJumlah() { return jumlah; }
    public double getTotalHarga() { return totalHarga; }
    public LocalDateTime getTanggal() { return tanggal; }
    public String getTanggalFormat() { return tanggal.format(FORMAT_TANGGAL); }
}
