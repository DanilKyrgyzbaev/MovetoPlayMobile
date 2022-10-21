package com.movetoplay.screens.applock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.movetoplay.R;

public class LockScreenActivity extends AppCompatActivity {

    private PinLockView mPinLockView;
    private String correctPin = "";
    private Boolean isPinConfirm = false;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        textView = findViewById(R.id.tv_alert);
        Button button = findViewById(R.id.btn_cancel);
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(mPinLockListener);

        button.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
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

                    SharedPreferences sharedPreferences = getSharedPreferences("pin_code", MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("PIN", setPin);
                    edit.apply();

                    Log.e("PIN", "onComplete: " + pin);

                    finish();
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
}