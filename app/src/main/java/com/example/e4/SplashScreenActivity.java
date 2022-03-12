package com.example.e4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e4.newtworkmonitor.NetworkChangeListener;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    IntentFilter intentFilter;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sp_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp_login = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (sp_login.getBoolean("logged", false)) {
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        }else{
            startActivity(new Intent(SplashScreenActivity.this, UserLoginActivity.class));
        }

        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }
}
