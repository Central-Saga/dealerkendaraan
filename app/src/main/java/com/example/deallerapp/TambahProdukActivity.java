package com.example.deallerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.deallerapp.model.Kendaraan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class TambahProdukActivity extends AppCompatActivity {

    private EditText etNama, etMerk, etHarga, etPenumpang;
    private ImageView imgPreview;
    private Spinner spinnerGambar;
    private Button btnSimpan;

    private String imageBase64 = null;

    private ArrayList<Kendaraan> listKendaraan;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etNama = findViewById(R.id.etNama);
        etMerk = findViewById(R.id.etMerk);
        etHarga = findViewById(R.id.etHarga);
        etPenumpang = findViewById(R.id.etPenumpang);
        imgPreview = findViewById(R.id.imgPreview);
        spinnerGambar = findViewById(R.id.spinnerGambar);
        btnSimpan = findViewById(R.id.btnSimpan);

        gson = new Gson();
        sharedPreferences = getSharedPreferences("data_kendaraan", MODE_PRIVATE);
        listKendaraan = loadData();

        setupSpinner();

        btnSimpan.setOnClickListener(v -> {
            String nama = etNama.getText().toString().trim();
            String merk = etMerk.getText().toString().trim();
            String hargaStr = etHarga.getText().toString().trim();
            String penumpangStr = etPenumpang.getText().toString().trim();

            if (nama.isEmpty() || merk.isEmpty() || hargaStr.isEmpty() || penumpangStr.isEmpty() || imageBase64 == null) {
                Toast.makeText(this, "Lengkapi semua data & pilih gambar!", Toast.LENGTH_SHORT).show();
                return;
            }

            int harga = Integer.parseInt(hargaStr);
            int penumpang = Integer.parseInt(penumpangStr);

            Kendaraan kendaraan = new Kendaraan(nama, merk, harga, penumpang, 0, imageBase64);
            listKendaraan.add(kendaraan);
            saveData(listKendaraan);

            Toast.makeText(this, "Kendaraan berhasil disimpan!", Toast.LENGTH_SHORT).show();

            etNama.setText("");
            etMerk.setText("");
            etHarga.setText("");
            etPenumpang.setText("");
            spinnerGambar.setSelection(0);
            imgPreview.setImageResource(android.R.drawable.ic_input_add);
            imageBase64 = null;
        });
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gambar_kendaraan,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGambar.setAdapter(adapter);

        spinnerGambar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int resId = R.drawable.car_red;
                switch (position) {
                    case 0: resId = R.drawable.car_red; break;
                    case 1: resId = R.drawable.car_blue; break;
                    case 2: resId = R.drawable.car_black; break;
                }

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
                imgPreview.setImageBitmap(bitmap);
                imageBase64 = encodeToBase64(bitmap);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private String encodeToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void saveData(ArrayList<Kendaraan> list) {
        String json = gson.toJson(list);
        sharedPreferences.edit().putString("kendaraan_list", json).apply();
    }

    private ArrayList<Kendaraan> loadData() {
        String json = sharedPreferences.getString("kendaraan_list", null);
        Type type = new TypeToken<ArrayList<Kendaraan>>() {}.getType();
        ArrayList<Kendaraan> list = gson.fromJson(json, type);
        return list != null ? list : new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_tambah) return true;
        if (id == R.id.menu_jual) {
            startActivity(new Intent(this, JualProdukActivity.class));
            return true;
        }
        if (id == R.id.menu_pendapatan) {
            startActivity(new Intent(this, PendapatanActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
