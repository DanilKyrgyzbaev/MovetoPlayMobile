package com.movetoplay.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.movetoplay.R;

import java.util.Timer;
import java.util.TimerTask;

public class Register extends AppCompatActivity {

    Button btn;
    EditText email, pass, age;

    // Идентификатор уведомления
    private static final int NOTIFY_ID = 101;

    // Идентификатор канала
    private static String CHANNEL_ID = "MoveToPlay";


    public void startTimer(){
        Timer timer = new Timer();
        timer.schedule(new UpdateTimeTask(), 0, 1000); //установленно в данный момент на 1 секунду

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

        btn = findViewById(R.id.btn_enter);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        age = findViewById(R.id.password2);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get data

                String email_data = email.getText().toString();
                String pass_data = pass.getText().toString();
                String age_data = age.getText().toString();


                if (email_data.isEmpty() || pass_data.isEmpty() || age_data.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Поля пусты", Toast.LENGTH_LONG).show();
                }else{
                    //process to backend
                    startActivity(new Intent(getApplicationContext(), SetupProfileActivity.class));
                    finish();
                }

            }
        });
    }

    class UpdateTimeTask extends TimerTask {
        public void run() {
            //создание уведомления
            create_notification();
        }
    }
}