package com.over.parkulting.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    private String SavePicture(Bitmap img, String folderToSave, String name) throws IOException {
        File pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File dir = new File(pictures+"/"+folderToSave);
        dir.mkdir();
        name = name+"_"+System.currentTimeMillis()+".jpg";
        File file = new File(dir,name);
        FileOutputStream fos = new FileOutputStream(file);
        img.compress(Bitmap.CompressFormat.JPEG, 85, fos);
        fos.flush();
        fos.close();
        MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), file.getAbsolutePath(), file.getName(),  file.getName());
        return pictures+"/"+folderToSave+"/"+name;
    }
    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmap = RotateBitmap(bitmap, 90);
            try {
                SavePicture(bitmap,"Park", "img");
                Toast.makeText(mContext, "Save file: ", Toast.LENGTH_LONG).show();
            }
            catch (Exception e) {
                Toast.makeText(mContext, "Error: can't save file", Toast.LENGTH_LONG).show();
            }
            mCamera.startPreview(); //3
        }
    };
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
        capture = (ImageButton) root.findViewById(R.id.capture);
        preview = new CameraPreview (mContext, mCamera);
        mFrame = (FrameLayout) root.findViewById(R.id.layoutframe); //4
        mFrame.addView(preview, 0);

        capture.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCamera.takePicture(null, null, null, mPictureCallback);
                    }
                }
        );

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