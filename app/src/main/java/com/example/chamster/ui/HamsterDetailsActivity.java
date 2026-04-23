package com.example.chamster.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chamster.R;

import java.io.IOException;
import java.io.InputStream;

public class HamsterDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        ImageView img = findViewById(R.id.imgAnimal);
        TextView name = findViewById(R.id.txtName);
        TextView desc = findViewById(R.id.txtDesc);

        String n = getIntent().getStringExtra("name");
        String d = getIntent().getStringExtra("desc");
        String imgPath = getIntent().getStringExtra("img");

        name.setText(n);
        desc.setText(d);

        try {
            AssetManager am = getAssets();
            InputStream is = am.open(imgPath);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            img.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
