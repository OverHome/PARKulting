package com.over.parkulting.tools.permission;

import android.content.Context;

public interface PermissionCallback {
    void onPermissionGranted();
    void onPermissionDenied();
    void onPermissionDeniedByTheUser();
}
