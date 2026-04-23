package com.example.chamster;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.chamster.ui.HamsterListActivity;
import com.example.chamster.ui.LoginActivity;
import com.example.chamster.ui.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAnimals = findViewById(R.id.btnAnimals);
        Button btnSettings = findViewById(R.id.btnSettings);

        btnAnimals.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
        btnAnimals.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, HamsterListActivity.class));
        });
    }
}
