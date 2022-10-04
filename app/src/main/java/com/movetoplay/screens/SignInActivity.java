package com.movetoplay.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.movetoplay.R;
import com.movetoplay.screens.applock.LimitationAppActivity;

public class SignInActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private CheckBox checkStatus;
    private Button btnEnter;
    private TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initViews();
        initListeners();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void initListeners() {
        btnEnter.setOnClickListener(view -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (!email.isEmpty() && !password.isEmpty()) {
                if (checkStatus.isChecked()) {
                    startActivity(new Intent(getApplicationContext(), SetupProfileActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), LimitationAppActivity.class));
                }
                finish();
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            }
        });

        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ResetPass.class));
            finish();
        });
    }

    private void initViews() {
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        checkStatus = findViewById(R.id.checkBox);
        btnEnter = findViewById(R.id.btn_enter);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);

    }
}