package com.movetoplay.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.movetoplay.R;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Sign_in extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private CheckBox checkStatus;
    private Button btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initViews();
        initListeners();
    }

    private void initListeners() {
        btnEnter.setOnClickListener(view -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (!email.isEmpty() && !password.isEmpty()) {
                if (checkStatus.isChecked()) {
                    startActivity(new Intent(getApplicationContext(), SetupProfileActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        checkStatus = findViewById(R.id.checkBox);
        btnEnter = findViewById(R.id.btn_enter);
    }
}