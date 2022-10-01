package com.movetoplay.screens;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.movetoplay.R;

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

        initViews();
        initListeners();
    }

    private void initListeners() {
        limitations.setOnClickListener(view -> {
            openLimitationAppsActivityForResult();
        });

        cont.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
    }

    public void openLimitationAppsActivityForResult() {
        Intent intent = new Intent(this, LimitationAppActivity.class);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
              
                }
            });


    private void initViews() {
        cont = findViewById(R.id.btn_continue);
        limitations = findViewById(R.id.tv_limitations);
        Spinner status = findViewById(R.id.sp_status);
        Spinner gender = findViewById(R.id.sp_child_gender);

        ArrayAdapter<String> createSpinAdapter = new ArrayAdapter<>(this, R.layout.spin_status_item, statusArr);
        ArrayAdapter<String> genderSpinAdapter = new ArrayAdapter<>(this, R.layout.spin_gender_item, genderArr);

        createSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        status.setAdapter(createSpinAdapter);
        gender.setAdapter(genderSpinAdapter);
    }
}