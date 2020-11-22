package com.km.mbottlecapcollector.api.rest;

import com.km.mbottlecapcollector.api.model.Cap;
import com.km.mbottlecapcollector.api.model.Caps;
import com.km.mbottlecapcollector.api.model.ValidateCapResponse;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface BottleCapInterface {
    @GET("/caps")
    Call<Caps> caps();

    @GET("/caps/{id}")
    Observable<Cap> cap(@Path("id") long id);

    @Multipart
    @POST("/validateCap")
    Call<ValidateCapResponse> validateCap(@Part("name") RequestBody name, @Part MultipartBody.Part image);
}
