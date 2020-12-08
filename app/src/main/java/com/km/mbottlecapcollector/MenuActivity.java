package com.km.mbottlecapcollector;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
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

public class MenuActivity extends AppCompatActivity {

    private Button checkButton;
    private Button viewButton;
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
            Toast.makeText(getApplicationContext(), "Performing call... ", Toast.LENGTH_SHORT).show();
            spinner.setVisibility(View.VISIBLE);
            checkButton.setVisibility(View.INVISIBLE);
            viewButton.setVisibility(View.INVISIBLE);
            API.bottleCaps().links().enqueue(new Callback<List<PictureWrapper>>() {
                @Override
                public void onResponse(Call<List<PictureWrapper>> call, Response<List<PictureWrapper>> response) {
                    spinner.setVisibility(View.GONE);
                    goToGalleryActivity(response);
                }

                @Override
                public void onFailure(Call<List<PictureWrapper>> call, Throwable t) {

                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkButton.setVisibility(View.VISIBLE);
        viewButton.setVisibility(View.VISIBLE);
    }

    private void goToCameraActivity() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    private void goToGalleryActivity(Response<List<PictureWrapper>> response) {
        Toast.makeText(getApplicationContext(), "Opening gallery... ", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putParcelableArrayListExtra("caps", (ArrayList<? extends Parcelable>) response.body());
        startActivity(intent);
    }
}