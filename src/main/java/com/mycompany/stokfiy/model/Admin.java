package com.mycompany.stokfiy.model;

/** Admin merupakan jenis Pengguna yang mengelola data toko. */
public class Admin extends Pengguna {
    private static final long serialVersionUID = 1L;

    private String namaToko;

    public Admin(String nama, String username, String namaToko) {
        super(nama, username);
        setNamaToko(namaToko);
    }

    public String getNamaToko() {
        return namaToko;
    }

    public void setNamaToko(String namaToko) {
        if (namaToko == null || namaToko.isBlank()) {
            throw new IllegalArgumentException("Nama toko tidak boleh kosong.");
        }
        this.namaToko = namaToko.trim();
    }
}
