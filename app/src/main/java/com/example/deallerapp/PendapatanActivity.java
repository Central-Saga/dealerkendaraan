package com.example.deallerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.deallerapp.adapter.PenjualanAdapter;
import com.example.deallerapp.model.Penjualan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PendapatanActivity extends AppCompatActivity {

    private ListView listPenjualan;
    private TextView tvTotal;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendapatan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listPenjualan = findViewById(R.id.listPenjualan);
        tvTotal = findViewById(R.id.tvTotalPendapatan);

        sharedPreferences = getSharedPreferences("data_kendaraan", MODE_PRIVATE);
        gson = new Gson();

        ArrayList<Penjualan> list = loadPenjualan();
        listPenjualan.setAdapter(new PenjualanAdapter(this, list));

        int total = 0;
        for (Penjualan p : list) {
            total += p.getKendaraan().getHarga();
        }
        tvTotal.setText("Total: Rp " + total);
    }


    private ArrayList<Penjualan> loadPenjualan() {
        String json = sharedPreferences.getString("penjualan_list", null);
        Type type = new TypeToken<ArrayList<Penjualan>>() {}.getType();
        ArrayList<Penjualan> list = gson.fromJson(json, type);
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
            startActivity(new Intent(this, TambahProdukActivity.class));
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
