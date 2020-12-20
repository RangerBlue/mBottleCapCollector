package com.km.mbottlecapcollector.api.rest;

import com.km.mbottlecapcollector.api.model.Cap;
import com.km.mbottlecapcollector.api.model.PictureWrapper;
import com.km.mbottlecapcollector.api.model.ValidateCapResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface BottleCapInterface {
    @GET("/caps")
    Call<List<Cap>> caps();

    @Multipart
    @POST("/caps/")
    Call<Long> addCap(@Part("name") RequestBody name, @Part MultipartBody.Part image);

    @DELETE("/caps/{id}")
    Call<ResponseBody> deleteCap(@Path("id") long id);

    @FormUrlEncoded
    @PUT("/caps/{id}")
    Call<Cap> updateCap(@Path("id") long id, @Field("newName") String newName);

    @GET("/links")
    Call<List<PictureWrapper>> links();

    @GET("/caps/{id}")
    Call<Cap> cap(@Path("id") long id);

    @GET("/management/info")
    Call<ResponseBody> info();

    @Multipart
    @POST("/validateCap")
    Call<ValidateCapResponse> validateCap(@Part("name") RequestBody name, @Part MultipartBody.Part image);
}
