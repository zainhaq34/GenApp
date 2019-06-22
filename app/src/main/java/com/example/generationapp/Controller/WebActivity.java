package com.example.generationapp.Controller;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.generationapp.R;

import java.util.concurrent.TimeUnit;

public class WebActivity extends AppCompatActivity {

    private static final String TAG = "MyApp";
    CountDownTimer timer;

    private static String BASE_URL = "https://www.generation.com.pk/";
    private long TIMER_START = 30000;
    private long TIMER_END = 1000;
    private static final long ALERT_DIALOG_TIME_OUT = 3000;
    WebView webView;
    SwipeRefreshLayout refreshLayout;
    RelativeLayout relativeLayout;
    BroadcastReceiver broadcastReceiver;

    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        checkInternetConnection();
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
                startActivity(new Intent(WebActivity.this, ScreenSaverActivity.class));
                finish();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {

            final AlertDialog alertDialog = new AlertDialog.Builder(this)
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
            // Auto Close Dialog
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    alertDialog.cancel();

                }
            }, ALERT_DIALOG_TIME_OUT);
        }
    }

    /***
     * When Pause WebActivity CountTimerDown must be Cancel mode
     * otherwise CountTimerDown create count time duplication
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

    public void checkInternetConnection() {

            if (broadcastReceiver == null) {

                broadcastReceiver = new BroadcastReceiver() {

                    @Override
                    public void onReceive(final Context context, Intent intent) {

                        Bundle extras = intent.getExtras();

                        NetworkInfo info = (NetworkInfo) extras.getParcelable("networkInfo");
                        NetworkInfo.State state = info.getState();

                        final AlertDialog.Builder alertDialog;

                        if (state == NetworkInfo.State.CONNECTED) {
                            //Toast.makeText(getApplicationContext(), "Internet connection is on", Toast.LENGTH_LONG).show();
                            webView.loadUrl(BASE_URL);

                        } else {
                            //Toast.makeText(getApplicationContext(), "Internet connection is Off", Toast.LENGTH_LONG).show();
                            alertDialog = new AlertDialog.Builder(context);

                            alertDialog.setTitle("Internet not available");
                            alertDialog.setMessage("Please Check your internet connection.");
                            alertDialog.setIcon(R.drawable.ic_warning_icon);
                            alertDialog.setCancelable(false);


                            alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                    startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                                     // startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
                                    //finish();
                                  //  refreshDialog();

                                }
                            });
                            alertDialog.show();
                        }
                    }
                };

                final IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                registerReceiver(broadcastReceiver, intentFilter);
            }
        }

    public void refreshDialog(){
        //Toast.makeText(getApplicationContext(), "Internet connection is Off", Toast.LENGTH_LONG).show();
      AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Refresh Page");
        alertDialog.setMessage("Check internet connection & Refresh Page");
        alertDialog.setIcon(R.drawable.ic_refresh);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("REFRESH", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                webView.loadUrl(BASE_URL);
            }
        });

        alertDialog.show();
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
