# Stokfiy — Manajemen Stok dan Penjualan UMKM

Project tugas Pemrograman Berorientasi Objek (PBO) berbasis Java 17, Maven, dan Console/Terminal. Aplikasi membantu admin UMKM mencatat produk, mengelola stok, melakukan transaksi, serta melihat riwayat dan laporan penjualan.

## Teknologi
- Java 17
- Apache Maven
- Apache NetBeans
- `Scanner` untuk input terminal
- `ArrayList` untuk mengelola data
- `ObjectOutputStream` dan `ObjectInputStream` untuk serialisasi objek

## Struktur project

```text
Stokfiy/
├── pom.xml
├── README.md
└── src/
    └── main/java/com/mycompany/stokfiy/
        ├── Main.java
        ├── model/
        │   ├── Pengguna.java
        │   ├── Admin.java
        │   ├── Produk.java
        │   └── Transaksi.java
        └── service/
            └── ManajemenStok.java
```

## Konsep PBO yang diterapkan
1. **Class dan Object:** `Main`, `Admin`, `Produk`, `Transaksi`, dan `ManajemenStok` dibuat sebagai class dengan objek yang digunakan aplikasi.
2. **Encapsulation:** atribut model menggunakan `private`, dilengkapi getter dan setter (data transaksi menyediakan getter).
3. **Inheritance:** `Admin` mewarisi `Pengguna`; admin memiliki identitas umum pengguna dan nama toko.
4. **Control flow:** `while` untuk menu dan validasi input, `switch` untuk pilihan menu, `if/else` untuk validasi, serta `for` untuk menampilkan/menghitung data.
5. **File I/O dan serialization:** daftar produk dan transaksi ditulis sebagai objek menggunakan `ObjectOutputStream`, lalu dibaca kembali menggunakan `ObjectInputStream`.

## Fitur
1. Tambah produk (ID, nama, kategori, harga, stok awal).
2. Lihat daftar produk.
3. Cari produk berdasarkan ID atau nama.
4. Tambah stok.
5. Kurangi stok (ditolak jika stok tidak cukup).
6. Transaksi penjualan (total otomatis dan stok otomatis berkurang).
7. Riwayat penjualan.
8. Laporan jumlah transaksi, jumlah barang terjual, dan pendapatan.
9. Keluar dan menyimpan data.

## Menjalankan di MacBook melalui NetBeans

1. Ekstrak `Stokfiy.zip`.
2. Buka Apache NetBeans.
3. Pilih **File → Open Project**, lalu pilih folder `Stokfiy` hasil ekstrak (folder yang berisi `pom.xml`).
4. Pastikan NetBeans menggunakan JDK 17 atau lebih baru. Source dikompilasi dengan target Java 17.
5. Buka `Main.java`, lalu klik kanan **Run File**, atau pilih project dan tekan **F6 (Run Project)**.
6. Jika NetBeans meminta Main Class, isi `com.mycompany.stokfiy.Main`.
7. Input dilakukan pada jendela Output/Terminal NetBeans.

## Menjalankan melalui Terminal

Pastikan `java -version` menunjukkan Java 17 atau lebih baru dan Maven sudah terpasang.

```bash
cd /lokasi/folder/Stokfiy
mvn clean compile
mvn exec:java
```

## Lokasi penyimpanan data

Program membuat folder `data` di working directory saat program dijalankan dan menyimpan data di `data/stokfiy.dat`. File tersebut berisi dua objek `ArrayList`: daftar `Produk` dan daftar `Transaksi`. Jangan hapus file ini jika ingin mempertahankan data antar-sesi. Jalankan program dari folder project yang sama agar file data yang sama digunakan.

## Skenario pengujian manual

1. Jalankan program, pilih `1`, lalu masukkan:
   - ID: `P001`
   - Nama: `Kopi Lampung`
   - Kategori: `Minuman`
   - Harga: `15000`
   - Stok awal: `10`
2. Pilih `2`. Pastikan produk tampil dengan stok 10.
3. Pilih `4`, ID `P001`, jumlah `5`. Pastikan stok menjadi 15.
4. Pilih `6`, ID `P001`, jumlah `3`. Pastikan total Rp45.000 dan stok menjadi 12.
5. Pilih `7`. Pastikan transaksi tercatat.
6. Pilih `8`. Pastikan total transaksi 1, produk terjual 3, pendapatan Rp45.000.
7. Pilih `9` untuk keluar. Jalankan ulang program, pilih `2` dan `7`. Produk serta transaksi sebelumnya harus tetap tampil.
8. Uji validasi: masukkan teks pada menu, harga negatif, ID duplikat, jumlah stok negatif/0, dan transaksi melebihi stok. Program harus menolak input/tindakan tersebut tanpa berhenti mendadak.

## Contoh gambaran output

```text
STOKFIY - MANAJEMEN UMKM
Selamat datang, Admin UMKM | Toko: Stokfiy UMKM

1. Tambah Produk
2. Lihat Daftar Produk
...
Pilih menu (1-9): 1
ID produk: P001
Nama produk: Kopi Lampung
Kategori: Minuman
Harga: 15000
Stok awal: 10
Produk berhasil ditambahkan.

Pilih menu (1-9): 6
ID produk yang dibeli: P001
Jumlah pembelian: 3
Transaksi berhasil!
Produk       : Kopi Lampung
Jumlah       : 3
Total        : Rp45.000,00
Sisa stok    : 7
```

> Output di atas adalah ilustrasi skenario pengujian. Format mata uang dan waktu dapat mengikuti locale/JDK yang digunakan.

## Catatan
- ID produk dibandingkan tanpa membedakan huruf besar/kecil.
- Harga menggunakan `double` untuk menjaga implementasi tetap sederhana pada tugas PBO; aplikasi kasir produksi sebaiknya memakai `BigDecimal`.
- ID transaksi dibuat berurutan berdasarkan jumlah transaksi yang tersimpan.
- Project ini tidak memakai database atau library eksternal.
