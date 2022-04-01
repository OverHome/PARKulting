package com.over.parkulting.tools.permission;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

public class PermissionTool {

    public PermissionTool(Context context, PermissionCallback test, String[] permis) {
        boolean rest = true;
        for (String perm : permis) {
            if (ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_DENIED){
                rest = false;
                break;
            }
        }

        if (rest)
            test.onPermissionGranted();
        else
            test.onPermissionDenied();
    }
}
