package com.mycompany.stokfiy.service;

import com.mycompany.stokfiy.model.Produk;
import com.mycompany.stokfiy.model.Transaksi;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/** Mengelola produk, transaksi, laporan, dan penyimpanan data. */
public class ManajemenStok {
    private ArrayList<Produk> daftarProduk;
    private ArrayList<Transaksi> daftarTransaksi;
    private final Path fileData;

    public ManajemenStok() {
        this(Paths.get("data", "stokfiy.dat"));
    }

    // Konstruktor ini memudahkan pengujian dengan lokasi file tersendiri.
    public ManajemenStok(Path fileData) {
        this.fileData = fileData;
        this.daftarProduk = new ArrayList<>();
        this.daftarTransaksi = new ArrayList<>();
        muatData();
    }

    public boolean tambahProduk(Produk produk) {
        if (produk == null || cariProdukById(produk.getId()) != null) return false;
        daftarProduk.add(produk);
        simpanData();
        return true;
    }

    public List<Produk> getDaftarProduk() {
        return new ArrayList<>(daftarProduk);
    }

    public List<Transaksi> getDaftarTransaksi() {
        return new ArrayList<>(daftarTransaksi);
    }

    public Produk cariProdukById(String id) {
        if (id == null) return null;
        for (Produk produk : daftarProduk) {
            if (produk.getId().equalsIgnoreCase(id.trim())) return produk;
        }
        return null;
    }

    public List<Produk> cariProduk(String kataKunci) {
        ArrayList<Produk> hasil = new ArrayList<>();
        if (kataKunci == null) return hasil;
        String kata = kataKunci.trim().toLowerCase();
        for (Produk produk : daftarProduk) {
            if (produk.getId().toLowerCase().contains(kata)
                    || produk.getNama().toLowerCase().contains(kata)) {
                hasil.add(produk);
            }
        }
        return hasil;
    }

    public boolean tambahStok(String id, int jumlah) {
        Produk produk = cariProdukById(id);
        if (produk == null || jumlah <= 0) return false;
        try {
            produk.tambahStok(jumlah);
            simpanData();
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public boolean kurangiStok(String id, int jumlah) {
        Produk produk = cariProdukById(id);
        if (produk == null || jumlah <= 0 || !produk.kurangiStok(jumlah)) return false;
        simpanData();
        return true;
    }

    public Transaksi buatTransaksi(String idProduk, int jumlah) {
        Produk produk = cariProdukById(idProduk);
        if (produk == null || jumlah <= 0 || jumlah > produk.getStok()) return null;
        Transaksi transaksi = new Transaksi("TRX-" + (daftarTransaksi.size() + 1), produk, jumlah);
        produk.kurangiStok(jumlah);
        daftarTransaksi.add(transaksi);
        simpanData();
        return transaksi;
    }

    public int getTotalProdukTerjual() {
        int total = 0;
        for (Transaksi transaksi : daftarTransaksi) total += transaksi.getJumlah();
        return total;
    }

    public double getTotalPendapatan() {
        double total = 0;
        for (Transaksi transaksi : daftarTransaksi) total += transaksi.getTotalHarga();
        return total;
    }

    public void simpanData() {
        try {
            Path folder = fileData.getParent();
            if (folder != null) Files.createDirectories(folder);
            try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(fileData))) {
                output.writeObject(daftarProduk);
                output.writeObject(daftarTransaksi);
            }
        } catch (IOException ex) {
            System.out.println("Peringatan: data belum berhasil disimpan: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void muatData() {
        if (!Files.exists(fileData)) return;
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(fileData))) {
            Object produkObject = input.readObject();
            Object transaksiObject = input.readObject();
            if (produkObject instanceof ArrayList<?> && transaksiObject instanceof ArrayList<?>) {
                ArrayList<?> produkMentah = (ArrayList<?>) produkObject;
                ArrayList<?> transaksiMentah = (ArrayList<?>) transaksiObject;
                if (semuaBerisi(produkMentah, Produk.class) && semuaBerisi(transaksiMentah, Transaksi.class)) {
                    daftarProduk = (ArrayList<Produk>) produkMentah;
                    daftarTransaksi = (ArrayList<Transaksi>) transaksiMentah;
                } else {
                    System.out.println("Peringatan: format data tidak sesuai. Data dimulai dari kosong.");
                }
            } else {
                System.out.println("Peringatan: format file data tidak dikenali.");
            }
        } catch (IOException | ClassNotFoundException | ClassCastException ex) {
            System.out.println("Peringatan: data gagal dibaca: " + ex.getMessage());
            System.out.println("Program tetap berjalan dengan data kosong.");
        }
    }

    private boolean semuaBerisi(List<?> daftar, Class<?> tipe) {
        for (Object item : daftar) if (!tipe.isInstance(item)) return false;
        return true;
    }
}
