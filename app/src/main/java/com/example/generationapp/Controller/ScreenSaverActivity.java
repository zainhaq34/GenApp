package com.example.generationapp.Controller;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;
import com.example.generationapp.R;


public class ScreenSaverActivity extends AppCompatActivity {

    private static final String TAG = "";
    VideoView videoView;
    private static final long ALERT_DIALOG_TIME_OUT = 3000;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_saver);

        VideoStreamer();

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                startActivity(new Intent(ScreenSaverActivity.this, WebActivity.class));
                finish();
                return false;
            }
        });
    }

    private void VideoStreamer() {
        videoView = findViewById(R.id.video_view);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

            }
        });
        videoView.requestFocus();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                videoView.start();

            }
        });
    }

    @Override
    public void onBackPressed() {
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
