package com.mycompany.stokfiy;

import com.mycompany.stokfiy.model.Admin;
import com.mycompany.stokfiy.model.Produk;
import com.mycompany.stokfiy.model.Transaksi;
import com.mycompany.stokfiy.service.ManajemenStok;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final Scanner INPUT = new Scanner(System.in);
    private static final NumberFormat RUPIAH = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    private static final ManajemenStok MANAJEMEN = new ManajemenStok();

    public static void main(String[] args) {
        Admin admin = new Admin("Admin UMKM", "admin", "Stokfiy UMKM");
        boolean berjalan = true;

        System.out.println("==================================================");
        System.out.println("             STOKFIY - MANAJEMEN UMKM             ");
        System.out.println("==================================================");
        System.out.println("Selamat datang, " + admin.getNama() + " | Toko: " + admin.getNamaToko());

        while (berjalan) {
            tampilkanMenu();
            int pilihan = bacaInt("Pilih menu (1-9): ");
            switch (pilihan) {
                case 1 -> tambahProduk();
                case 2 -> tampilkanProduk(MANAJEMEN.getDaftarProduk());
                case 3 -> cariProduk();
                case 4 -> ubahStok(true);
                case 5 -> ubahStok(false);
                case 6 -> transaksiPenjualan();
                case 7 -> tampilkanRiwayat();
                case 8 -> tampilkanLaporan();
                case 9 -> {
                    MANAJEMEN.simpanData();
                    System.out.println("Data disimpan. Terima kasih telah menggunakan Stokfiy!");
                    berjalan = false;
                }
                default -> System.out.println("Menu tidak tersedia. Pilih angka 1 sampai 9.");
            }
            if (berjalan) System.out.println();
        }
    }

    private static void tampilkanMenu() {
        System.out.println("\n---------------- MENU UTAMA ----------------");
        System.out.println("1. Tambah Produk");
        System.out.println("2. Lihat Daftar Produk");
        System.out.println("3. Cari Produk");
        System.out.println("4. Tambah Stok");
        System.out.println("5. Kurangi Stok");
        System.out.println("6. Transaksi Penjualan");
        System.out.println("7. Lihat Riwayat Penjualan");
        System.out.println("8. Lihat Laporan Penjualan");
        System.out.println("9. Keluar");
        System.out.println("--------------------------------------------");
    }

    private static void tambahProduk() {
        System.out.println("\n--- TAMBAH PRODUK ---");
        String id = bacaTeks("ID produk: ");
        if (MANAJEMEN.cariProdukById(id) != null) {
            System.out.println("Gagal: ID produk sudah digunakan.");
            return;
        }
        String nama = bacaTeks("Nama produk: ");
        String kategori = bacaTeks("Kategori: ");
        double harga = bacaDouble("Harga: ");
        int stok = bacaInt("Stok awal: ");
        try {
            Produk produk = new Produk(id, nama, kategori, harga, stok);
            if (MANAJEMEN.tambahProduk(produk)) System.out.println("Produk berhasil ditambahkan.");
            else System.out.println("Produk gagal ditambahkan. Periksa kembali ID produk.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Gagal menambahkan produk: " + ex.getMessage());
        }
    }

    private static void cariProduk() {
        System.out.println("\n--- CARI PRODUK ---");
        String kataKunci = bacaTeks("Masukkan ID atau nama produk: ");
        List<Produk> hasil = MANAJEMEN.cariProduk(kataKunci);
        if (hasil.isEmpty()) System.out.println("Produk tidak ditemukan.");
        else tampilkanProduk(hasil);
    }

    private static void ubahStok(boolean tambah) {
        System.out.println(tambah ? "\n--- TAMBAH STOK ---" : "\n--- KURANGI STOK ---");
        String id = bacaTeks("ID produk: ");
        int jumlah = bacaInt("Jumlah: ");
        boolean berhasil = tambah ? MANAJEMEN.tambahStok(id, jumlah) : MANAJEMEN.kurangiStok(id, jumlah);
        if (berhasil) {
            Produk produk = MANAJEMEN.cariProdukById(id);
            System.out.println("Stok berhasil diperbarui. Stok sekarang: " + produk.getStok());
        } else {
            System.out.println("Gagal memperbarui stok. Pastikan produk ada, jumlah lebih dari 0, dan stok mencukupi.");
        }
    }

    private static void transaksiPenjualan() {
        System.out.println("\n--- TRANSAKSI PENJUALAN ---");
        if (MANAJEMEN.getDaftarProduk().isEmpty()) {
            System.out.println("Belum ada produk. Tambahkan produk terlebih dahulu.");
            return;
        }
        tampilkanProduk(MANAJEMEN.getDaftarProduk());
        String id = bacaTeks("ID produk yang dibeli: ");
        int jumlah = bacaInt("Jumlah pembelian: ");
        Transaksi transaksi = MANAJEMEN.buatTransaksi(id, jumlah);
        if (transaksi == null) {
            System.out.println("Transaksi gagal. Produk tidak ditemukan atau stok tidak mencukupi.");
        } else {
            System.out.println("Transaksi berhasil!");
            System.out.println("ID transaksi : " + transaksi.getIdTransaksi());
            System.out.println("Produk       : " + transaksi.getNamaProduk());
            System.out.println("Jumlah       : " + transaksi.getJumlah());
            System.out.println("Total        : " + formatRupiah(transaksi.getTotalHarga()));
            System.out.println("Sisa stok    : " + MANAJEMEN.cariProdukById(id).getStok());
        }
    }

    private static void tampilkanProduk(List<Produk> daftar) {
        System.out.println("\n--- DAFTAR PRODUK ---");
        if (daftar.isEmpty()) {
            System.out.println("Belum ada produk.");
            return;
        }
        System.out.printf("%-12s %-22s %-16s %16s %8s%n", "ID", "NAMA", "KATEGORI", "HARGA", "STOK");
        System.out.println("-------------------------------------------------------------------------------");
        for (Produk produk : daftar) {
            System.out.printf("%-12s %-22s %-16s %16s %8d%n",
                    potong(produk.getId(), 12), potong(produk.getNama(), 22),
                    potong(produk.getKategori(), 16), formatRupiah(produk.getHarga()), produk.getStok());
        }
    }

    private static void tampilkanRiwayat() {
        System.out.println("\n--- RIWAYAT PENJUALAN ---");
        List<Transaksi> daftar = MANAJEMEN.getDaftarTransaksi();
        if (daftar.isEmpty()) {
            System.out.println("Belum ada transaksi.");
            return;
        }
        System.out.printf("%-12s %-20s %-22s %8s %18s%n", "ID TRX", "TANGGAL", "PRODUK", "JUMLAH", "TOTAL");
        System.out.println("--------------------------------------------------------------------------------");
        for (Transaksi transaksi : daftar) {
            System.out.printf("%-12s %-20s %-22s %8d %18s%n", transaksi.getIdTransaksi(),
                    transaksi.getTanggalFormat(), potong(transaksi.getNamaProduk(), 22),
                    transaksi.getJumlah(), formatRupiah(transaksi.getTotalHarga()));
        }
    }

    private static void tampilkanLaporan() {
        System.out.println("\n--- LAPORAN PENJUALAN ---");
        System.out.println("Total transaksi       : " + MANAJEMEN.getDaftarTransaksi().size());
        System.out.println("Total produk terjual  : " + MANAJEMEN.getTotalProdukTerjual());
        System.out.println("Total pendapatan      : " + formatRupiah(MANAJEMEN.getTotalPendapatan()));
    }

    private static String bacaTeks(String pesan) {
        while (true) {
            System.out.print(pesan);
            String nilai = INPUT.nextLine().trim();
            if (!nilai.isEmpty()) return nilai;
            System.out.println("Input tidak boleh kosong. Silakan coba lagi.");
        }
    }

    private static int bacaInt(String pesan) {
        while (true) {
            System.out.print(pesan);
            String nilai = INPUT.nextLine().trim();
            try {
                return Integer.parseInt(nilai);
            } catch (NumberFormatException ex) {
                System.out.println("Masukkan bilangan bulat yang valid.");
            }
        }
    }

    private static double bacaDouble(String pesan) {
        while (true) {
            System.out.print(pesan);
            String nilai = INPUT.nextLine().trim().replace(',', '.');
            try {
                double angka = Double.parseDouble(nilai);
                if (!Double.isFinite(angka) || angka < 0) {
                    System.out.println("Nilai tidak boleh negatif atau tidak valid.");
                    continue;
                }
                return angka;
            } catch (NumberFormatException ex) {
                System.out.println("Masukkan angka yang valid. Contoh: 15000 atau 15000.50");
            }
        }
    }

    private static String formatRupiah(double angka) {
        return RUPIAH.format(angka);
    }

    private static String potong(String teks, int batas) {
        if (teks.length() <= batas) return teks;
        return teks.substring(0, batas - 3) + "...";
    }
}
