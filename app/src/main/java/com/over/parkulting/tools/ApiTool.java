package com.over.parkulting.tools;

import com.over.parkulting.pojo.VersionPojo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class ApiTool {
    private static ApiTool instance;
    private static final String BASE_URL = "https://31b5-46-188-125-150.eu.ngrok.io";
    private Retrofit mRetrofit;

    public interface PlaceHolderApi {
        @GET("/get/version.json")
        Call<VersionPojo> getVersion();

        @GET("/get/model.tflite")
        Call<ResponseBody> downloadModelFile();

        @GET("/get/ParKulting.db")
        Call<ResponseBody> downloadParKultingFile();

        @Multipart
        @POST("/")
        Call<ResponseBody> sendInfo(@Part("file\"; filename=\"pp.png\" ") RequestBody file,
                                    @Part("point") RequestBody point,
                                    @Part("info") RequestBody info,
                                    @Part("park") RequestBody park,
                                    @Part("url") RequestBody url,
                                    @Part("location_longitude") RequestBody ll,
                                    @Part("location_width") RequestBody lw);
    }

    private ApiTool(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiTool getInstance() {
        if (instance == null) {
            instance = new ApiTool();
        }
        return instance;
    }

    public PlaceHolderApi getApi() {
        return mRetrofit.create(PlaceHolderApi.class);
    }

}


