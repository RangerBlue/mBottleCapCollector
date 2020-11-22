package com.km.mbottlecapcollector.api.rest;

import com.km.mbottlecapcollector.api.config.Config;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {
    private static <T> T builder(Class<T> endpoint) {
        return new Retrofit.Builder()
                .baseUrl(Config.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(endpoint);
    }

    public static BottleCapInterface bottleCaps() {
        return builder(BottleCapInterface.class);
    }
}
