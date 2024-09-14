package com.example.myproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myproject.R;

public class AccountActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView emailTextView;
    Button btnBack, btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);

        nameTextView = findViewById(R.id.textName);
        emailTextView = findViewById(R.id.textEmail);
        btnBack = findViewById(R.id.Back);
        btnLogout = findViewById(R.id.Logout);

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");

        if (name != null) {
            nameTextView.setText("Hello: " + name);
        }
        if (email != null) {
            emailTextView.setText("Email: " + email);
        }

        btnBack.setOnClickListener(v -> finish());

        btnLogout.setOnClickListener(v -> {


            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            startActivity(intent);

            finish();
        });
    }
}