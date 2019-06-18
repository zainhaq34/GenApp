package com.example.generationapp.Controller;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

import com.example.generationapp.R;
import com.karan.churi.PermissionManager.PermissionManager;

public class ScreenSaverActivity extends AppCompatActivity {

    private static final String TAG = "";
    VideoView videoView;
    private static final long ALERT_DIALOG_TIME_OUT = 3000;
    String folderName = "/Generation Video";

    PermissionManager permissionManager;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_saver);

//        File file = new File(Environment.getExternalStorageDirectory() + folderName);
//        if (!file.exists()) {
//
//            file.mkdirs();
//            Log.e(TAG, "Folder Name " + file.getName());
//        }
//        String myFolder = Environment.getExternalStorageDirectory() + "/" + folderName;
//        File file = new File(myFolder);
//        if (!file.exists()) {
//            file.mkdir();
//            if (!file.mkdir()) {
//                Toast.makeText(this, myFolder + " can't be created.", Toast.LENGTH_SHORT).show();
//
//            } else
//                Toast.makeText(this, myFolder + " can be created.", Toast.LENGTH_SHORT).show();
//        }
//        permissionManager = new PermissionManager() {
//            @Override
//            public void ifCancelledAndCanRequest(Activity activity) {
//                // Do Customized operation if permission is cancelled without checking "Don't ask again"
//                // Use super.ifCancelledAndCanRequest(activity); or Don't override this method if not in use
//            }
//
//            @Override
//            public void ifCancelledAndCannotRequest(Activity activity) {
//                // Do Customized operation if permission is cancelled with checking "Don't ask again"
//                // Use super.ifCancelledAndCannotRequest(activity); or Don't override this method if not in use
//            }
//
//            @Override
//            public List<String> setPermission() {
//                // If You Don't want to check permission automatically and check your own custom permission
//                // Use super.setPermission(); or Don't override this method if not in use
//                List<String> customPermission=new ArrayList<>();
//                customPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//                customPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                return customPermission;
//            }
//        };
//
//        if (permissionManager.checkAndRequestPermissions(this)){
//
//            File folder = getFilesDir();
//            File f = new File(folder, folderName);
//
//            if (!f.exists()) {
//                f.mkdir();
//                Toast.makeText(this, f + " can't be created.", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, f + " can be created.", Toast.LENGTH_SHORT).show();
//            }
//
//            VideoStreamer();
//
//            videoView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                    startActivity(new Intent(ScreenSaverActivity.this, WebActivity.class));
//                    finish();
//                    return false;
//                }
//            });
//        }

//
//        File folder = getFilesDir();
//        File f = new File(folder, folderName);
//
//        if (!f.exists()) {
//            f.mkdir();
//            Toast.makeText(this, f + " can't be created.", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, f + " can be created.", Toast.LENGTH_SHORT).show();
//        }


//
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode,permissions, grantResults);
    }
}
