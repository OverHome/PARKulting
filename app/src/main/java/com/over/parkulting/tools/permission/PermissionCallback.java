package com.over.parkulting.tools.permission;

import android.content.Context;

public interface PermissionCallback {
    public void onPermissionGranted();
    public void onPermissionDenied();
}
