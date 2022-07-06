package com.example.chat_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                Manifest.permission.READ_CONTACTS
        },200);

        sp= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sp.getString("chat_app_user_id","").length()>0)
                {
                    startActivity(new Intent(MainActivity.this,chat_list.class).putExtra("uid",sp.getString("chat_app_user_id","")));
                }
                else {
                    startActivity(new Intent(MainActivity.this,login.class));
                }
                finish();
            }
        },1500);
    }
}
