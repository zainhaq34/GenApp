package com.example.generationapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class WebActivity extends AppCompatActivity {

    private static final String TAG = "MyApp";

    private static String BASE_URL = "https://www.generation.com.pk/";
    WebView webView;
    SwipeRefreshLayout refreshLayout;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        relativeLayout = findViewById(R.id.relative_layout);
        refreshLayout = findViewById(R.id.refresh_layout);
        webView = findViewById(R.id.web_view);

        webView.loadUrl(BASE_URL);
        webView.setWebViewClient(new MyWebViewClient());
        //Web Setting
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Swipe Refresh
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(BASE_URL);
            }
        });

        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
               startTimer();
                return false;
            }
        });
    }

    private void startTimer(){

        new CountDownTimer(30000, 1000) {



            public void onTick(long millisUntilFinished) {

                long sec = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));


                Log.e(TAG,"onTick: "+sec );

                if(sec == 1)
                {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(getApplicationContext(), "Finish Time in 1 sec", Toast.LENGTH_SHORT).show();
                        }
                    }, 1000);
                }


            }

            public void onFinish() {
               // tv_timer.setText("Timer finish");
               // Toast.makeText(getApplicationContext(), "Finish Time", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(WebActivity.this, SliderActivity.class));
            }
        }.start();
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {

            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_warning_icon)
                    .setTitle("Exit App")
                    .setMessage("Are you sure you want to close this App?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }


    }

    public class MyWebViewClient extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            refreshLayout.setRefreshing(false);
            BASE_URL = url;
            super.onPageFinished(view, url);
        }
    }
}
