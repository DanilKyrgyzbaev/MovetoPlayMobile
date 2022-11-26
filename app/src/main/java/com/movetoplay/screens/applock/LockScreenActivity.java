package com.movetoplay.screens.applock;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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
    private ProgressBar progress;
    String setPin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        lockScreenViewModel = new ViewModelProvider(LockScreenActivity.this).get(LockScreenViewModel.class);

        tag = getIntent().getStringExtra("tag");

        textView = findViewById(R.id.tv_alert);
        progress = findViewById(R.id.pb_lock);
        Button button = findViewById(R.id.btn_cancel);
        mPinLockView = findViewById(R.id.pin_lock_view);
        IndicatorDots mIndicatorDots = findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);

        if (tag.equals("confirm") || tag.equals("check"))
            textView.setText("Подтвердите пин");


        button.setOnClickListener(view -> {
            if (!tag.equals("check"))
                saveBeforeFinish(false);
            else finishAffinity();
        });

        lockScreenViewModel.getPinResult().observe(this, result -> {
            if (result.containsKey("success")) {
                ChildInfo child = AccessibilityPrefs.INSTANCE.getChildInfo();
                child.setPinCode(correctPin);
                AccessibilityPrefs.INSTANCE.setChildInfo(child);
                saveBeforeFinish(true);
            } else if (result.containsKey("error")) {
                Toast.makeText(
                        this,
                        result.get("error"),
                        Toast.LENGTH_SHORT
                ).show();
                saveBeforeFinish(false);
            } else {
                progress.setVisibility(View.VISIBLE);
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
            if (tag.equals("edit")) {
                if (isPinConfirm) {
                    isPinConfirm = false;
                    if (correctPin.equalsIgnoreCase(pin)) {
                        if (!correctPin.isEmpty()) {
                            lockScreenViewModel.setPinCode(new PinBody(correctPin));
                        }
                    } else {
                        mPinLockView.resetPinLockView();
                        textView.setText("Устонавите пин");
                        isPinConfirm = false;
                        Toast.makeText(LockScreenActivity.this, "Пин не совпадает!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    correctPin = pin;
                    mPinLockView.resetPinLockView();
                    isPinConfirm = true;
                    textView.setText("Подтвердите пин");
                }
            }

            if (tag.equals("confirm") || tag.equals("check")) {
                ChildInfo confirmPin = AccessibilityPrefs.INSTANCE.getChildInfo();
                Log.e("pin", "onComplete: " + confirmPin);
                if (confirmPin.getPinCode() != null) {
                    Log.e("pin", "onComplete: confirm pin=" + confirmPin + ", entered pin=" + pin);
                    if (confirmPin.getPinCode().equals(pin)) {
                        isPinConfirm = false;
                        if (tag.equals("confirm")) saveBeforeFinish(true);
                        else {
                            AccessibilityPrefs.INSTANCE.setPinConfirmed(true);
                            finishAffinity();
                        }
                    } else {
                        mPinLockView.resetPinLockView();
                        Toast.makeText(LockScreenActivity.this, "Неправильный пин", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LockScreenActivity.this, "Установите пин с родительского устройства", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onEmpty() {
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d("TAG", "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };

    @Override
    public void onBackPressed() {
        if (!tag.equals("check"))
            super.onBackPressed();
    }
}