package com.movetoplay.screens;

import android.app.ActivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.movetoplay.R;

import java.util.List;

public class GetListAllApps extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_list_all_apps);

        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();
        if (runningProcesses != null && runningProcesses.size() > 0) {
            // Set data adapter
            Log.e("Apps", String.valueOf(runningProcesses));
        } else {
            //error
            Toast.makeText(getApplicationContext(), "No application is running", Toast.LENGTH_LONG).show();
        }
    }
}