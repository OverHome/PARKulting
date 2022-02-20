package com.over.parkulting;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import com.over.parkulting.ml.*;
import com.over.parkulting.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class ImportIris {
    private static int imageSize = 200;

    public static String classifyImage(Bitmap img, Context context) {
        int des = Math.min(img.getWidth(), img.getHeight());
        img = ThumbnailUtils.extractThumbnail(img, des , des);
        img = Bitmap.createScaledBitmap(img, imageSize, imageSize, false);
        try {
            Model model = Model.newInstance(context);

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, imageSize, imageSize, 3}, DataType.FLOAT32);
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
            inputFeature0.loadBuffer(byteBuffer);
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confid = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confid.length; i++) {
                if (confid[i] > maxConfidence) {
                    maxConfidence = confid[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Аллея арок", "Арт-объект Позвони родителям", "Букводом", "Главный фонтан", "Памятник Мойдодыру", "Сад астрономов", "Уличное пианино", "Храм Святого Тихона Задонского"};
            String a = classes[maxPos];
            String s = "";
            // for(int i = 0; i < classes.length; i++){
            //     s += String.format("%s: %.1f%%\n", classes[i], confid[i] * 100);
            // }
            model.close();
            if (maxConfidence > 0.95) {
                return a;
            } else {
                return "Не распознано";
            }

        } catch (IOException e) {
            return "Не распознано";
        }
    }
}
