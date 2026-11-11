package com.mycompany.stokfiy.model;

import java.io.Serializable;

/** Superclass untuk pengguna aplikasi Stokfiy. */
public class Pengguna implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nama;
    private String username;

    public Pengguna(String nama, String username) {
        setNama(nama);
        setUsername(username);
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username tidak boleh kosong.");
        }
        this.username = username.trim();
    }
}
