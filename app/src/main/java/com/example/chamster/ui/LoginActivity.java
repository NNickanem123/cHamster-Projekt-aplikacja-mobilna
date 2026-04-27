package com.example.chamster.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chamster.R;
import com.example.chamster.data.DataManager;

public class LoginActivity extends AppCompatActivity {

    private EditText inputUsername;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DataManager.init(this);

        inputUsername = findViewById(R.id.inputUsername);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String username = inputUsername.getText().toString().trim();
            if (username.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Wpisz nazwę użytkownika", Toast.LENGTH_SHORT).show();
                return;
            }

            DataManager.setCurrentUser(username);

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}