package com.example.deallerapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deallerapp.R;
import com.example.deallerapp.model.Penjualan;

import java.util.List;

public class PenjualanAdapter extends ArrayAdapter<Penjualan> {

    public PenjualanAdapter(Context context, List<Penjualan> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_penjualan, parent, false);
        }

        Penjualan data = getItem(pos);
        TextView nama = convertView.findViewById(R.id.tvNama);
        TextView harga = convertView.findViewById(R.id.tvHarga);
        ImageView img = convertView.findViewById(R.id.imgKendaraan);

        if (data != null && data.getKendaraan() != null) {
            nama.setText(data.getKendaraan().getNama() + " (" + data.getKendaraan().getMerk() + ")");
            harga.setText("Rp " + data.getKendaraan().getHarga());

            // Tampilkan gambar dari Base64
            String base64 = data.getKendaraan().getImageBase64();
            if (base64 != null && !base64.isEmpty()) {
                try {
                    byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    img.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Log.e("PenjualanAdapter", "Gagal decode gambar", e);
                    img.setImageResource(R.drawable.ic_launcher_background);
                }
            } else {
                img.setImageResource(R.drawable.ic_launcher_background);
            }
        }

        return convertView;
    }
}
