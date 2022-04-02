package com.over.parkulting.tools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionTool {
    public static interface PermissionCallback {
        void onPermissionGranted();
        void onPermissionDenied();
        void onPermissionDeniedByTheUser();
    }


    private Context mContext;
    private int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private PermissionCallback permissionCallback;
    private String[] permList;

    public PermissionTool(Context context, PermissionCallback permCallback, String[] permis) {
        mContext = context;
        permissionCallback = permCallback;
        permList = permis;
    }

    public void check(){
        boolean rest = true;
        for (String perm : permList) {
            if (ContextCompat.checkSelfPermission(mContext, perm) == PackageManager.PERMISSION_DENIED){
                rest = false;
                break;
            }
        }

        if (rest)
            permissionCallback.onPermissionGranted();
        else
            permissionCallback.onPermissionDenied();
    }

    public void reqPerm(Activity activity, String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, LOCATION_PERMISSION_REQUEST_CODE);
    }

    public void callBackPermissionResult(@NonNull int[] grantResults){

        boolean test = true;

        for (int gres : grantResults) if (gres == -1) {
            test = false;
            break;
        }

        if (test)
            permissionCallback.onPermissionGranted();
        else
            permissionCallback.onPermissionDeniedByTheUser();
    }
}
