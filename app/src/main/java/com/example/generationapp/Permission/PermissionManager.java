package com.example.generationapp.Permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionManager {
    public PermissionManager() {
    }

    private static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    private static final int REQUEST_CODE = 101;

    public boolean userHasPermission(Context context) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, PERMISSIONS[0]);
        int permissionCheck2 = ContextCompat.checkSelfPermission(context, PERMISSIONS[1]);
        int permissionCheck3 = ContextCompat.checkSelfPermission(context, PERMISSIONS[2]);
//        int permissionCheck4 = ContextCompat.checkSelfPermission(context, PERMISSIONS[3]);
//        int permissionCheck5 = ContextCompat.checkSelfPermission(context, PERMISSIONS[4]);
        return permissionCheck == PackageManager.PERMISSION_GRANTED &&
                permissionCheck2 == PackageManager.PERMISSION_GRANTED &&
                permissionCheck3 == PackageManager.PERMISSION_GRANTED; //&&
//                permissionCheck4 == PackageManager.PERMISSION_GRANTED &&
//                permissionCheck5 == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_CODE);
    }
}