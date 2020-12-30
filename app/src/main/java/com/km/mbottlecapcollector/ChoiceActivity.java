package com.km.mbottlecapcollector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.km.mbottlecapcollector.api.model.Cap;
import com.km.mbottlecapcollector.api.model.ValidateCapResponse;
import com.km.mbottlecapcollector.api.rest.API;
import com.km.mbottlecapcollector.util.ScreenRatioHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoiceActivity extends Activity {
    public static final String EXTRA_CAP_URL = "bcc.CAP_URL";
    public static final String EXTRA_DISTRIBUTION = "bcc.DISTRIBUTION";
    public static final String EXTRA_CAP_0 = "bcc.CAP_0";
    public static final String EXTRA_CAP_1 = "bcc.CAP_1";
    public static final String EXTRA_CAP_2 = "bcc.CAP_2";
    public static final String EXTRA_CAP_3 = "bcc.CAP_3";
    public static final String EXTRA_CAP_4 = "bcc.CAP_4";
    public static final String EXTRA_CAP_5 = "bcc.CAP_5";
    public static final String EXTRA_CAP_6 = "bcc.CAP_6";
    public static final String EXTRA_CAP_7 = "bcc.CAP_7";
    public static final String EXTRA_CAP_8 = "bcc.CAP_8";
    private ImageView imageView;
    private String imageURI;
    private Button retryButton;
    private Button yesButton;
    private File image = null;
    private ProgressDialog progressBar;
    private boolean goToValidateScreen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        String className = getIntent().getStringExtra(CameraActivity.EXTRA_CLASS_NAME);
        goToValidateScreen = allowToGoToValidateScreen(className);
        retryButton = findViewById(R.id.buttonRetry);
        retryButton.setOnClickListener(view -> {
            try {
                Class<?> returnActivity = Class.forName(className);
                Intent intent = new Intent(this, returnActivity);
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        });

        yesButton = findViewById(R.id.buttonYes);
        yesButton.setOnClickListener(view -> {
            progressBar.show();
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), image);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", image.getName(), requestFile);

            RequestBody name = RequestBody.create(MediaType.parse("text/plain"),
                    image.getName());


            if (goToValidateScreen) {
                API.bottleCaps().validateCap(name, body).enqueue(new retrofit2.Callback<ValidateCapResponse>() {

                    @Override
                    public void onResponse(Call<ValidateCapResponse> call, Response<ValidateCapResponse> response) {
                        progressBar.dismiss();
                        goToValidateActivity(response.body());
                    }

                    @Override
                    public void onFailure(Call<ValidateCapResponse> call, Throwable t) {
                        progressBar.dismiss();
                        Toast.makeText(getApplicationContext(), getText(R.string.try_again) +
                                " " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                API.bottleCaps().whatCapAreYou(name, body).enqueue(new Callback<Cap>() {
                    @Override
                    public void onResponse(Call<Cap> call, Response<Cap> response) {
                        progressBar.dismiss();
                        goToWhatCapYouAreActivity(response.body());
                    }

                    @Override
                    public void onFailure(Call<Cap> call, Throwable t) {
                        progressBar.dismiss();
                        Toast.makeText(getApplicationContext(), getText(R.string.try_again) +
                                " " + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

        imageView = findViewById(R.id.capImage);
        imageURI = getIntent().getStringExtra(CameraActivity.EXTRA_CAPTURED_IMAGE_URI);
        image = new File(imageURI);
        Picasso.get().load(image).into(imageView);

        progressBar = new ProgressDialog(this);
        progressBar.setTitle(R.string.loading);
        progressBar.setMessage(getString(R.string.validating_picture));
        progressBar.setCancelable(false);
        progressBar.dismiss();
    }

    private void goToValidateActivity(ValidateCapResponse response) {
        Intent intent = new Intent(this, ValidateActivity.class);
        ArrayList<String> urls = response.getSimilarCapsURLss();
        int pixels = ScreenRatioHelper.getValidateCapWidth();
        intent.putExtra(EXTRA_CAP_0, ValidateCapResponse.getLinkWithPixels(urls.get(0), pixels));
        intent.putExtra(EXTRA_CAP_1, ValidateCapResponse.getLinkWithPixels(urls.get(1), pixels));
        intent.putExtra(EXTRA_CAP_2, ValidateCapResponse.getLinkWithPixels(urls.get(2), pixels));
        intent.putExtra(EXTRA_CAP_3, ValidateCapResponse.getLinkWithPixels(urls.get(3), pixels));
        intent.putExtra(EXTRA_CAP_4, ValidateCapResponse.getLinkWithPixels(urls.get(4), pixels));
        intent.putExtra(EXTRA_CAP_5, ValidateCapResponse.getLinkWithPixels(urls.get(5), pixels));
        intent.putExtra(EXTRA_CAP_6, ValidateCapResponse.getLinkWithPixels(urls.get(6), pixels));
        intent.putExtra(EXTRA_CAP_7, ValidateCapResponse.getLinkWithPixels(urls.get(7), pixels));
        intent.putExtra(EXTRA_CAP_8, ValidateCapResponse.getLinkWithPixels(urls.get(8), pixels));
        intent.putExtra(CameraActivity.EXTRA_CAPTURED_IMAGE_URI, imageURI);
        intent.putExtra(EXTRA_DISTRIBUTION, response.getSimilarityDistribution());
        startActivity(intent);
    }

    private void goToWhatCapYouAreActivity(Cap response) {
        Intent intent = new Intent(this, WhatCapAreYouActivity.class);
        intent.putExtra(CameraActivity.EXTRA_CAPTURED_IMAGE_URI, imageURI);
        intent.putExtra(EXTRA_CAP_URL,
                response.getFileLocation(ScreenRatioHelper.getWhatCapAreYouCapWidth()));
        startActivity(intent);
    }

    private boolean allowToGoToValidateScreen(String className) {
        if (className.equals(FrontCameraActivity.class.getName())) {
            return false;
        }
        return true;
    }
}