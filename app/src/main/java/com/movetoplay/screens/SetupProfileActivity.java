package com.movetoplay.screens;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.movetoplay.R;
import com.movetoplay.pref.Pref;

public class SetupProfileActivity extends AppCompatActivity {

    private Button cont;
    private CheckBox sport;
    private TextView limitations;
    private final String[] statusArr = {"Создать новый", "Импортировать старый"};
    private final String[] genderArr = {"Мужской", "Женский"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        Log.e("Pref", Pref.INSTANCE.getUserLogin());
        initViews();
        initListeners();
    }

    private void initListeners() {
        cont.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
    }

    private void initViews() {
        cont = findViewById(R.id.btn_continue);
        Spinner status = findViewById(R.id.sp_status);
        Spinner gender = findViewById(R.id.sp_child_gender);

        ArrayAdapter<String> createSpinAdapter = new ArrayAdapter<>(this, R.layout.spin_status_item, statusArr);
        ArrayAdapter<String> genderSpinAdapter = new ArrayAdapter<>(this, R.layout.spin_gender_item, genderArr);

        createSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        status.setAdapter(createSpinAdapter);
        gender.setAdapter(genderSpinAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}