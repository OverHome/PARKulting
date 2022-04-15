package com.over.parkulting.tools;

import com.over.parkulting.pojo.VersionPojo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class ApiTool {
    private static ApiTool instance;
    private static final String BASE_URL = "http://192.168.8.100:5000";
    private Retrofit mRetrofit;

    public interface PlaceHolderApi {
        @GET("/get/version.json")
        Call<VersionPojo> getVersion();

        @GET("/get/model.tflite")
        Call<ResponseBody> downloadModelFile();

        @GET("/get/ParKulting.db")
        Call<ResponseBody> downloadParKultingFile();

        @POST("/")
        Call<ResponseBody> sendInfo();
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

    public class RegistrationBody{
        public String login;
        public String password;
    }

    public class RegistrationResponse {
        public String token;
    }
}


