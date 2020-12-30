package com.km.mbottlecapcollector.api.rest;

import android.content.Context;
import android.content.SharedPreferences;

import com.km.mbottlecapcollector.WelcomeScreenActivity;
import com.km.mbottlecapcollector.api.config.Config;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class API {
    private final static int TIMEOUT = 15;

    private static <T> T builder(Class<T> endpoint) {
        Context applicationContext = WelcomeScreenActivity.getContext();
        SharedPreferences prefs = applicationContext.getSharedPreferences("UserData", MODE_PRIVATE);
        BasicAuthInterceptor interceptor = new BasicAuthInterceptor(prefs.getString("login", ""),
                prefs.getString("password", ""));

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();

        if (prefs.getBoolean("authenticated", false) ||
                prefs.getBoolean("logging", false)) {
            return new Retrofit.Builder()
                    .baseUrl(Config.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(client)
                    .build()
                    .create(endpoint);
        } else {
            return new Retrofit.Builder()
                    .baseUrl(Config.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()
                    .create(endpoint);
        }
    }

    public static BottleCapInterface bottleCaps() {
        return builder(BottleCapInterface.class);
    }
}
