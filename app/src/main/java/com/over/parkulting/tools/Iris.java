package com.over.parkulting.tools;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class Iris {
    private static int imageSize = 200;
    public static final String MODEL_NAME = "model.tflite";

    public static String getModelPath(@Nullable Context context){
        if (android.os.Build.VERSION.SDK_INT >= 17)
            return context.getApplicationInfo().dataDir + "/";
        else
            return  "/data/data/" + context.getPackageName() + "/";
    }

    private static boolean checkModel(Context mContext) {
        File dbFile = new File(getModelPath(mContext) + MODEL_NAME);
        return dbFile.exists();
    }

    private static void copyDBFile(Context mContext) throws IOException {
        InputStream mInput = mContext.getAssets().open(MODEL_NAME);
        File dd = new File(getModelPath(mContext) + MODEL_NAME);
        dd.createNewFile();
        OutputStream mOutput = new FileOutputStream(dd);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    private static void copyDataBase(Context context){
        if (!checkModel(context)) {
            try {
                copyDBFile(context);
            } catch (IOException mIOException) {

                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                mIOException.printStackTrace(pw);
                String sStackTrace = sw.toString(); // stack trace as a string


                Log.i("TAG", "copyDataBase:" +sStackTrace);
            }
        }
    }

    public static String classifyImage(Bitmap img, Context context) {
        int des = Math.min(img.getWidth(), img.getHeight());
        img = ThumbnailUtils.extractThumbnail(img, des , des);
        img = Bitmap.createScaledBitmap(img, imageSize, imageSize, false);
        try {

            copyDataBase(context);
            ByteBuffer modelf = loadModelFile(MODEL_NAME, getModelPath(context));







            Interpreter interpreter = new Interpreter(modelf);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());
            int[] intVal = new int[imageSize * imageSize];
            img.getPixels(intVal, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
            int pix = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intVal[pix++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }
            SQLiteDatabase db = DBHelper.connectDB(context);
            List<String> classest = new ArrayList<>();
            Cursor cursor = db.rawQuery("SELECT * FROM points_in_park WHERE iris_id > 0 ORDER BY iris_id", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int ir_id = cursor.getInt(5);
                if (ir_id>0){
                    classest.add(cursor.getString(2));
                }
                cursor.moveToNext();
            }
            String[] classes = classest.toArray(new String[0]);
            float[][] result = new float[1][classes.length];
            interpreter.run(byteBuffer, result);
            int index = -1;
            float mx = 0;
            for (int i = 0; i<classes.length; i++){
                if (result[0][i]>mx){
                    index = i;
                    mx = result[0][i];
                }
            }
            if (mx > 0.95) {
                return classes[index];
            } else {
                return "Не распознано";
            }

        } catch (IOException e) {
            return "Не распознано";
        }
    }
    private static ByteBuffer loadModelFile(String modelPath, String mPath) throws IOException {

        File fpm = new File(mPath, modelPath);
        FileInputStream inputStream = new FileInputStream(fpm);
        FileChannel fileChannel = inputStream.getChannel();
        MappedByteBuffer bb = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        return bb;
    }

}
