package com.over.parkulting.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.over.parkulting.R;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment {

    public class CameraPreview extends SurfaceView implements Callback {

        SurfaceHolder mHolder;
        Camera mCamera;
        Context mContext;

        public CameraPreview(Context context, Camera camera) { //1
            super(context);
            mContext = context;
            mCamera = camera;
            mHolder = getHolder(); //2
            mHolder.addCallback(this); //3

            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //4
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) { //5
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
            catch (IOException e) {
                Toast.makeText(mContext, "Camera preview failed", Toast.LENGTH_LONG).show();
            }
        }

        // 6
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (mHolder.getSurface() == null)
                return;

            mCamera.stopPreview();

            setCameraDisplayOrientation();

            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (IOException e) {
                Toast.makeText(mContext, "Camera preview failed", Toast.LENGTH_LONG).show();
            }
        }

        public void setCameraDisplayOrientation()
        {
            if (mCamera == null)
                return;

            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(0, info);
            Camera.Parameters parameters = mCamera.getParameters();

            WindowManager winManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            int rotation = winManager.getDefaultDisplay().getRotation();

            int degrees = 0;

            switch (rotation) {
                case Surface.ROTATION_0: degrees = 0; break;
                case Surface.ROTATION_90: degrees = 90; break;
                case Surface.ROTATION_180: degrees = 180; break;
                case Surface.ROTATION_270: degrees = 270; break;
            }

            int result;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
            {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;  // + зеркало
            } else {
                result = (info.orientation - degrees + 360) % 360;
            }

            mCamera.setDisplayOrientation(result);

            int rotate = (degrees + 270) % 360;
            parameters.setRotation(rotate);
            mCamera.setParameters(parameters);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    ImageButton capture;
    CameraPreview preview;
    Camera mCamera;
    FrameLayout mFrame;
    Context mContext;


    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_camera, container, false);
        mContext = getContext();

        mCamera = openCamera (); //1
        if (mCamera == null) { //2
            Toast.makeText(mContext, "Opening camera failed", Toast.LENGTH_LONG).show();
            return root;
        }
        preview = new CameraPreview (mContext, mCamera);
        mFrame = (FrameLayout) root.findViewById(R.id.layoutframe); //4
        mFrame.addView(preview, 0);



        return root;
    }

    private Camera openCamera() {
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
            return null;

        Camera cam = null;
        if (Camera.getNumberOfCameras() > 0) {
            try {
                cam = Camera.open(0);
            }
            catch (Exception exc) {
                //
            }
        }
        return cam;
    }
}