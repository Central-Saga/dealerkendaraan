package com.example.deallerapp.model;

public class Kendaraan {
    private String nama;
    private String merk;
    private int harga;
    private int imageResId;
    private String imageBase64;

    private int penumpang;

    public Kendaraan(String nama, String merk, int harga, int penumpang, int imageResId, String imageBase64) {
        this.nama = nama;
        this.merk = merk;
        this.harga = harga;
        this.penumpang = penumpang;
        this.imageResId = imageResId;
        this.imageBase64 = imageBase64;
    }

    public int getPenumpang() {return penumpang;}
    public String getImageBase64() {return imageBase64;}
    public String getNama() { return nama; }
    public String getMerk() { return merk; }
    public int getHarga() { return harga; }
    public int getImageResId() { return imageResId; }
}
