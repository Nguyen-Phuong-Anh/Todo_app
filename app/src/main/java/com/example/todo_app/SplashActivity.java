package com.example.todo_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); //them giao dien

        final Intent i = new Intent(SplashActivity.this, MainActivity.class);
//        start other activity after SplashActivity is done
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i); //start MainActivity after 2s
                finish();
            }
        }, 2000);
    }
}
