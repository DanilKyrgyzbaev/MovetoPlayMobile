package com.movetoplay.screens.applock;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.movetoplay.R;

public class LockScreenChildActivity extends AppCompatActivity {

    private PinLockView mPinLockView;
    private TextView textView;
    private String correctPin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        textView = findViewById(R.id.tv_alert);
        Button button = findViewById(R.id.btn_cancel);
        mPinLockView = findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(mPinLockListener);

        getPinPrefs();

        button.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private final PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
                if (correctPin.equalsIgnoreCase(pin)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    textView.setText("Попробуйте еще раз");
                    mPinLockView.resetPinLockView();
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

    public void getPinPrefs(){
        SharedPreferences prefs = getSharedPreferences("pin_code", MODE_PRIVATE);
        correctPin = prefs.getString("PIN", "Default");
    }
}