package com.movetoplay.screens.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.movetoplay.R;
import com.movetoplay.screens.create_child_profile.SetupProfileActivity;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import java.util.Timer;
import java.util.TimerTask;

public class Register extends AppCompatActivity {

    private  RegisterViewModel viewModel;
    Button btn;
    EditText email, pass, age;

    // Идентификатор уведомления
    private static final int NOTIFY_ID = 101;

    // Идентификатор канала
    private static final String CHANNEL_ID = "MoveToPlay";


    public void startTimer() {
        Timer timer = new Timer();
        timer.schedule(new UpdateTimeTask(), 0, 1000); //установленно в данный момент на 1 секунду

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void create_notification(){

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_noti)
                        .setContentTitle("Время кончилось")
                        .setContentText("Нужно пройти испытания, чтобы добавить времени")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(NOTIFY_ID, builder.build());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //yandex

        // Creating an extended library configuration.
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("e5a90bfc-abfd-4c35-a47b-308effb3c011").build();
        // Initializing the AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
        // Automatic tracking of user activity.
        YandexMetrica.enableActivityAutoTracking(getApplication());

        btn = findViewById(R.id.google);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        age = findViewById(R.id.child_age);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);


       initListeners();
    }

    private void initListeners() {

    }

    private void goTo() {
        startActivity(new Intent(getApplicationContext(), SetupProfileActivity.class));
        finish();
    }

    class UpdateTimeTask extends TimerTask {
        public void run() {
            //создание уведомления
            create_notification();
        }
    }
}