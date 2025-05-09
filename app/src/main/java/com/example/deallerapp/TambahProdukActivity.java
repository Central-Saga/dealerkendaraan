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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.deallerapp.model.Kendaraan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class TambahProdukActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 100;

    private EditText etNama, etMerk, etHarga, etPenumpang;
    private ImageView imgPreview;
    private Button btnSimpan;
    private String imageBase64 = null;

    private ArrayList<Kendaraan> listKendaraan;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etNama = findViewById(R.id.etNama);
        etMerk = findViewById(R.id.etMerk);
        etHarga = findViewById(R.id.etHarga);
        etPenumpang = findViewById(R.id.etPenumpang);
        imgPreview = findViewById(R.id.imgPreview);
        btnSimpan = findViewById(R.id.btnSimpan);
        Button btnUpload = findViewById(R.id.btnUpload);

        gson = new Gson();
        sharedPreferences = getSharedPreferences("data_kendaraan", MODE_PRIVATE);
        listKendaraan = loadData();

        btnUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnSimpan.setOnClickListener(v -> {
            String nama = etNama.getText().toString().trim();
            String merk = etMerk.getText().toString().trim();
            String hargaStr = etHarga.getText().toString().trim();
            String penumpangStr = etPenumpang.getText().toString().trim();

            if (nama.isEmpty() || merk.isEmpty() || hargaStr.isEmpty() || penumpangStr.isEmpty() || imageBase64 == null) {
                Toast.makeText(this, "Lengkapi semua data & upload gambar!", Toast.LENGTH_SHORT).show();
                return;
            }

            int harga = Integer.parseInt(hargaStr);
            int penumpang = Integer.parseInt(penumpangStr);

            Kendaraan kendaraan = new Kendaraan(nama, merk, harga, penumpang, 0, imageBase64);
            listKendaraan.add(kendaraan);
            saveData(listKendaraan);

            Toast.makeText(this, "Kendaraan berhasil disimpan!", Toast.LENGTH_SHORT).show();

            // Reset form
            etNama.setText("");
            etMerk.setText("");
            etHarga.setText("");
            etPenumpang.setText("");
            imgPreview.setImageResource(android.R.drawable.ic_input_add);
            imageBase64 = null;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgPreview.setImageBitmap(bitmap);
                imageBase64 = encodeToBase64(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        if (id == R.id.menu_tambah) {
            return true;
        } else if (id == R.id.menu_jual) {
            startActivity(new Intent(this, JualProdukActivity.class));
            return true;
        } else if (id == R.id.menu_pendapatan) {
            startActivity(new Intent(this, PendapatanActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
