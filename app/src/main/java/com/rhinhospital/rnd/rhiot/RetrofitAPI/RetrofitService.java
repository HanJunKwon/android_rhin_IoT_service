package com.rhinhospital.rnd.rhiot.RetrofitAPI;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Retrofit Singleton pattern
public class RetrofitService {
    private static final String TAG = "RestrfitService";
    private static RetrofitService mRetrofitService = new RetrofitService();
    public static RetrofitService getInstance(){
        Log.d(TAG, "getInstance");
        return mRetrofitService;
    }

    private RetrofitService(){
        Log.d(TAG, "RetrofitService()");
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://106.10.33.63/rhin-iot-service/")
            .addConverterFactory(GsonConverterFactory.create()) // 파싱 등록
            .build();

    IRetrofitService service = retrofit.create(IRetrofitService.class);

    public IRetrofitService getService(){
        Log.d(TAG, "getService()");
        return service;
    }
}
