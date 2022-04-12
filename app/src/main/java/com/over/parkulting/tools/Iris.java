package com.over.parkulting.tools;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;


public class Iris {
    private static int imageSize = 200;

    public static String classifyImage(Bitmap img, Context context) {
        int des = Math.min(img.getWidth(), img.getHeight());
        img = ThumbnailUtils.extractThumbnail(img, des , des);
        img = Bitmap.createScaledBitmap(img, imageSize, imageSize, false);
        try {
            ByteBuffer modelf = loadModelFile(context.getAssets(), "model.tflite");
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
            String[] classes = {"Аллея арок", "Арт-объект Позвони родителям", "Букводом", "Главный фонтан", "Памятник Мойдодыру", "Сад астрономов", "Уличное пианино", "Храм Святого Тихона Задонского"};
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
    private static ByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

}
