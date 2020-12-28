package com.km.mbottlecapcollector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.km.mbottlecapcollector.api.model.PictureWrapper;
import com.km.mbottlecapcollector.api.rest.API;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends Activity {

    private Button checkButton;
    private Button viewButton;
    private Button adminButton;
    private Button whatCapAreYouButton;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(view -> {
            goToCameraActivity();
        });

        viewButton = findViewById(R.id.viewButton);
        viewButton.setOnClickListener(view -> {
            progressBar.show();
            API.bottleCaps().links().enqueue(new Callback<List<PictureWrapper>>() {
                @Override
                public void onResponse(Call<List<PictureWrapper>> call, Response<List<PictureWrapper>> response) {
                    progressBar.dismiss();
                    if (response.code() == 401) {
                        Toast.makeText(getApplicationContext(), "You are not allowed to perform" +
                                "this action ", Toast.LENGTH_SHORT).show();
                    } else {
                        goToGalleryActivity(response);
                    }

                }

                @Override
                public void onFailure(Call<List<PictureWrapper>> call, Throwable t) {
                    progressBar.dismiss();
                    Toast.makeText(getApplicationContext(), getText(R.string.failure) +" "+ t, Toast.LENGTH_SHORT).show();
                }
            });
        });

        adminButton = findViewById(R.id.adminButton);
        adminButton.setOnClickListener(view -> {
            goToLoginActivity();
        });

        whatCapAreYouButton = findViewById(R.id.whatCapAreYouButton);
        whatCapAreYouButton.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "What cap are you ", Toast.LENGTH_SHORT).show();
        });

        progressBar = new ProgressDialog(this);
        progressBar.setTitle(R.string.loading);
        progressBar.setMessage(getString(R.string.loading_gallery));
        progressBar.setCancelable(false);
        progressBar.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void goToCameraActivity() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    private void goToGalleryActivity(Response<List<PictureWrapper>> response) {
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putParcelableArrayListExtra("caps", (ArrayList<? extends Parcelable>) response.body());
        startActivity(intent);
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}