package com.example.generationapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    CountDownTimer timer;

    private static String BASE_URL = "https://www.generation.com.pk/";
    private  long TIMER_START = 30000;
    private  long TIMER_END = 1000;
    WebView webView;
    SwipeRefreshLayout refreshLayout;
    RelativeLayout relativeLayout;
    
    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
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


        /***
         * Touch Functionality of web View
         */
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                //press touch - timer is cancel
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    Log.e(TAG, "Press Touch");
                    return false;
                    // release touch - timer is start
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    startTimer();

                    Log.e(TAG, "Release Touch");
                    return false;
                }
                return false;
            }
        });

        /***
         * Every time start this activity call startTimer method
         * Mean time count down start to initial
         */
       startTimer();

    }

    private void startTimer() {
            timer = new CountDownTimer(TIMER_START, TIMER_END) {

                public void onTick(long millisUntilFinished) {
                    long sec = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                    Log.e(TAG, "onTick: " + sec);
                }
                // Stop Timer
                public void onFinish() {
                    startActivity(new Intent(WebActivity.this, SliderActivity.class));
                    finish();
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
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }
    /***
     * When Pause WebActivity CountTimerDown must be Cancel mode
     * otherwise CountTimerDown create count time duplication
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null){
            timer.cancel();
        }
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            refreshLayout.setRefreshing(false);
            BASE_URL = url;
            super.onPageFinished(view, url);
        }
    }
}
