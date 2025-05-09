package com.example.deallerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.deallerapp.model.Kendaraan;
import com.example.deallerapp.model.Penjualan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class JualProdukActivity extends AppCompatActivity {

    private ImageView imgKendaraan;
    private TextView tvNama, tvCC, tvPenumpang, tvHarga;
    private Button btnPrev, btnNext, btnJual;

    private ArrayList<Kendaraan> kendaraanList;
    private ArrayList<Penjualan> penjualanList;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private int posisi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jual_produk);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgKendaraan = findViewById(R.id.imgKendaraan);
        tvNama = findViewById(R.id.tvNama);
        tvCC = findViewById(R.id.tvCC);
        tvPenumpang = findViewById(R.id.tvPenumpang);
        tvHarga = findViewById(R.id.tvHarga);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnJual = findViewById(R.id.btnJual);

        gson = new Gson();
        sharedPreferences = getSharedPreferences("data_kendaraan", MODE_PRIVATE);
        kendaraanList = loadKendaraan();
        penjualanList = loadPenjualan();

        if (kendaraanList.isEmpty()) {
            Toast.makeText(this, "Belum ada kendaraan", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            tampilkanKendaraan();
        }

        btnPrev.setOnClickListener(v -> {
            if (posisi > 0) {
                posisi--;
                tampilkanKendaraan();
            } else {
                Toast.makeText(this, "Sudah di awal", Toast.LENGTH_SHORT).show();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (posisi < kendaraanList.size() - 1) {
                posisi++;
                tampilkanKendaraan();
            } else {
                Toast.makeText(this, "Sudah di akhir", Toast.LENGTH_SHORT).show();
            }
        });

        btnJual.setOnClickListener(v -> {
            Penjualan penjualan = new Penjualan(kendaraanList.get(posisi));
            penjualanList.add(penjualan);
            savePenjualan();
            Toast.makeText(this, "Kendaraan terjual!", Toast.LENGTH_SHORT).show();
        });
    }

    private void tampilkanKendaraan() {
        Kendaraan k = kendaraanList.get(posisi);

        // Tampilkan gambar dari Base64
        if (k.getImageBase64() != null) {
            byte[] bytes = Base64.decode(k.getImageBase64(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imgKendaraan.setImageBitmap(bitmap);
        } else {
            imgKendaraan.setImageResource(android.R.drawable.ic_menu_report_image);
        }

        tvNama.setText("Nama: " + k.getNama());
        tvCC.setText("CC Kendaraan: " + k.getMerk());
        tvPenumpang.setText("Jml Penumpang: " + k.getPenumpang());
        tvHarga.setText("Harga: Rp " + k.getHarga());
    }

    private ArrayList<Kendaraan> loadKendaraan() {
        String json = sharedPreferences.getString("kendaraan_list", null);
        Type type = new TypeToken<ArrayList<Kendaraan>>() {}.getType();
        ArrayList<Kendaraan> list = gson.fromJson(json, type);
        return list != null ? list : new ArrayList<>();
    }

    private ArrayList<Penjualan> loadPenjualan() {
        String json = sharedPreferences.getString("penjualan_list", null);
        Type type = new TypeToken<ArrayList<Penjualan>>() {}.getType();
        ArrayList<Penjualan> list = gson.fromJson(json, type);
        return list != null ? list : new ArrayList<>();
    }

    private void savePenjualan() {
        String json = gson.toJson(penjualanList);
        sharedPreferences.edit().putString("penjualan_list", json).apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_tambah) {
            startActivity(new Intent(this, TambahProdukActivity.class));
            return true;
        } else if (id == R.id.menu_jual) {
            return true;
        } else if (id == R.id.menu_pendapatan) {
            startActivity(new Intent(this, PendapatanActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
