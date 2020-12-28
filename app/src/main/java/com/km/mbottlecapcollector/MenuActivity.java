package com.km.mbottlecapcollector;

import android.app.Activity;
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
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        spinner = findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(view -> {
            goToCameraActivity();
        });

        viewButton = findViewById(R.id.viewButton);
        viewButton.setOnClickListener(view -> {
            spinner.setVisibility(View.VISIBLE);
            checkButton.setVisibility(View.INVISIBLE);
            viewButton.setVisibility(View.INVISIBLE);
            adminButton.setVisibility(View.INVISIBLE);
            API.bottleCaps().links().enqueue(new Callback<List<PictureWrapper>>() {
                @Override
                public void onResponse(Call<List<PictureWrapper>> call, Response<List<PictureWrapper>> response) {
                    if (response.code() == 401) {
                        Toast.makeText(getApplicationContext(), "You are not allowed to perform" +
                                "this action ", Toast.LENGTH_SHORT).show();
                    } else {
                        spinner.setVisibility(View.GONE);
                        goToGalleryActivity(response);
                    }

                }

                @Override
                public void onFailure(Call<List<PictureWrapper>> call, Throwable t) {

                }
            });
        });

        adminButton = findViewById(R.id.adminButton);
        adminButton.setOnClickListener(view -> {
            goToLoginActivity();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkButton.setVisibility(View.VISIBLE);
        viewButton.setVisibility(View.VISIBLE);
        adminButton.setVisibility(View.VISIBLE);
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