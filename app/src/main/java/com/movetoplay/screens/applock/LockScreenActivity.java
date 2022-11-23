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
import com.movetoplay.domain.model.ChildInfo;
import com.movetoplay.pref.AccessibilityPrefs;

public class LockScreenActivity extends AppCompatActivity {

    private PinLockView mPinLockView;
    private String correctPin = "";
    private Boolean isPinConfirm = false;
    private TextView textView;
    public LockScreenViewModel lockScreenViewModel;
    private String tag;
    String setPin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        lockScreenViewModel = new ViewModelProvider(LockScreenActivity.this).get(LockScreenViewModel.class);

        tag = getIntent().getStringExtra("tag");

        textView = findViewById(R.id.tv_alert);
        Button button = findViewById(R.id.btn_cancel);
        mPinLockView = findViewById(R.id.pin_lock_view);
        IndicatorDots mIndicatorDots = findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);

        if (tag.equals("confirm"))
            textView.setText("Подтвердите пин код");


        button.setOnClickListener(view -> {
            saveBeforeFinish(false);
        });

        lockScreenViewModel.getPinResult().observe(this, result -> {
            if (result) {
                ChildInfo child = AccessibilityPrefs.INSTANCE.getChildInfo();
                child.setPinCode(setPin);
                AccessibilityPrefs.INSTANCE.setChildInfo(child);
                saveBeforeFinish(true);
            } else {
                Toast.makeText(LockScreenActivity.this, "Ошибка отправки. Попробуйте ещё раз", Toast.LENGTH_SHORT).show();
                saveBeforeFinish(false);
            }
        });

    }

    private void saveBeforeFinish(Boolean isCorrect) {
        Intent intent = new Intent();
        if (tag.equals("confirm")) {
            intent.putExtra("confirm", isCorrect.toString());
        } else intent.putExtra("edit", isCorrect.toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    private final PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            if (isPinConfirm) {
                isPinConfirm = false;
                if (correctPin.equalsIgnoreCase(pin)) {
                    setResult(RESULT_OK);
                  setPin = correctPin;

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
                if (tag.equals("confirm")) {
                    if (AccessibilityPrefs.INSTANCE.getChildInfo().getPinCode().equals(pin)) {
                        isPinConfirm = false;
                        saveBeforeFinish(true);
                        Log.e("pin", "PIN confirm: ");
                    } else {
                        mPinLockView.resetPinLockView();
                        Toast.makeText(LockScreenActivity.this,"Неправильный пин код",Toast.LENGTH_SHORT).show();
                        isPinConfirm = false;
                    }
                } else {
                    correctPin = pin;
                    mPinLockView.resetPinLockView();
                    isPinConfirm = true;
                    textView.setText("Подтвердите пин");
                }
            }
        }

        @Override
        public void onEmpty() {
           // if (tag.equals("confirm"))
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d("TAG", "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };
}