package com.example.generationapp.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import com.example.generationapp.R;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 10000;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progress_circular);

        SplashStartUp();

    }

    private void SplashStartUp() {

        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                startActivity(new Intent(SplashActivity.this, ScreenSaverActivity.class));
                progressBar.setVisibility(View.GONE);
                // close this activity
                finish();

            }

        }, SPLASH_TIME_OUT);

    }


}
