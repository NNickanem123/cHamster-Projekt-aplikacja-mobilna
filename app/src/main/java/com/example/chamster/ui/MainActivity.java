package com.example.chamster.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.chamster.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new MainFragment())
                    .commit();
        }
    }
}