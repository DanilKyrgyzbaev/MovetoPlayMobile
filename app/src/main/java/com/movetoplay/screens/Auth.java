package com.movetoplay.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.movetoplay.R;

public class Auth extends AppCompatActivity {

    Button btn_e;
    Button btn_g;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


        btn_e = findViewById(R.id.email_btn);
        btn_g = findViewById(R.id.btn_enter);
        txt = findViewById(R.id.no_acc);

        initListeners();
    }

    private void initListeners() {
        txt.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Register.class));
            finish();
        });

        btn_g.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "Пока что это функция недоступна", Toast.LENGTH_LONG).show());

        btn_e.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        });
    }
}