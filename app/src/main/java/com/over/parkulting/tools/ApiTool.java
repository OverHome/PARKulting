package com.over.parkulting.tools;

import com.over.parkulting.pojo.VersionPojo;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class ApiTool {
    private static ApiTool instance;
    private static final String BASE_URL = "http://193.178.172.217";
    private Retrofit mRetrofit;

    public interface PlaceHolderApi {
        @GET("/park/version.json")
        Call<VersionPojo> getVersion();
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
