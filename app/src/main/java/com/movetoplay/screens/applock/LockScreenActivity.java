package com.movetoplay.screens.applock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.movetoplay.R;
import com.movetoplay.data.model.user_apps.PinBody;

public class LockScreenActivity extends AppCompatActivity {

    private PinLockView mPinLockView;
    private String correctPin = "";
    private Boolean isPinConfirm = false;
    private TextView textView;
    public LockScreenViewModel lockScreenViewModel;
    private String tag;
    //by viewModels()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        lockScreenViewModel = new ViewModelProvider(LockScreenActivity.this).get(LockScreenViewModel.class);

        tag = getIntent().getStringExtra("tag");

        textView = findViewById(R.id.tv_alert);
        Button button = findViewById(R.id.btn_cancel);
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(mPinLockListener);

        button.setOnClickListener(view -> {
            Intent intent = new Intent();
            if (tag.equals("confirm")) {
                intent.putExtra("confirm", "false");
            } else intent.putExtra("edit", "false");
            setResult(RESULT_OK, intent);
            finish();
        });

        lockScreenViewModel.getPinResult().observe(this, result -> {
            if (result) {
                finish();
            } else  Toast.makeText(LockScreenActivity.this, "Ошибка отправки. Попробуйте ещё раз", Toast.LENGTH_SHORT).show();
        });

    }

    private final PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            if (isPinConfirm) {
                isPinConfirm = false;
                if (correctPin.equalsIgnoreCase(pin)) {
                    setResult(RESULT_OK);
                    String setPin = correctPin;

                    Intent intent = new Intent();
                    intent.putExtra("PIN", setPin);
                    setResult(RESULT_OK, intent);

                    if (!setPin.isEmpty()) {
                        lockScreenViewModel.setPinCode(new PinBody(Integer.parseInt(setPin)));
                    }
                    Log.e("PIN", "onComplete: " + pin);

                } else {
                    textView.setText("Введите пин");
                    mPinLockView.resetPinLockView();
                    Toast.makeText(LockScreenActivity.this, "Попробуйте ещё раз", Toast.LENGTH_SHORT).show();
                }
            } else {
                correctPin = pin;
                mPinLockView.resetPinLockView();
                isPinConfirm = true;
                textView.setText("Подтвердите пин");
            }

        }

        @Override
        public void onEmpty() {
            Log.d("TAG", "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d("TAG", "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        lockScreenViewModel = new ViewModelProvider(LockScreenActivity.this).get(LockScreenViewModel.class);

        //
        textView = findViewById(R.id.tv_alert);
        Button button = findViewById(R.id.btn_cancel);
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        IndicatorDots mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);

        button.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        lockScreenViewModel.getPinResult().observe(this,result ->{
            if (result){
                finish();
            }
        });

    }
}