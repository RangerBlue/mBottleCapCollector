package com.km.mbottlecapcollector;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.km.mbottlecapcollector.api.model.ValidateCapResponse;
import com.km.mbottlecapcollector.api.rest.API;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class ChoiceActivity extends AppCompatActivity {
    private ImageView imageView;
    private String imageURI;
    private Button retryButton;
    private Button yesButton;
    private File image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        retryButton = (Button) findViewById(R.id.buttonRetry);
        retryButton.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "Retry button", Toast.LENGTH_SHORT).show());

        yesButton = (Button) findViewById(R.id.buttonYes);
        yesButton.setOnClickListener(view -> {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), image);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", image.getName(), requestFile);

            RequestBody name = RequestBody.create(MediaType.parse("text/plain"),
                    image.getName());

            API.bottleCaps().validateCap(name, body).enqueue(new retrofit2.Callback<ValidateCapResponse>() {

                @Override
                public void onResponse(Call<ValidateCapResponse> call, Response<ValidateCapResponse> response) {
                    Toast.makeText(getApplicationContext(), "Downloading results... ", Toast.LENGTH_SHORT).show();
                    goToValidateActivity(response.body());
                }

                @Override
                public void onFailure(Call<ValidateCapResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Try again! " + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        imageView = findViewById(R.id.capImage);
        imageURI = getIntent().getStringExtra("URI");
        image = new File(imageURI);
        imageView.setImageDrawable(Drawable.createFromPath(image.toString()));
    }

    private void goToValidateActivity(ValidateCapResponse response) {
        Intent intent = new Intent(this, ValidateActivity.class);
        intent.putExtra("cap0", response.getSimilarCapsURLss().get(0));
        intent.putExtra("cap1", response.getSimilarCapsURLss().get(1));
        intent.putExtra("cap2", response.getSimilarCapsURLss().get(2));
        intent.putExtra("cap3", response.getSimilarCapsURLss().get(3));
        intent.putExtra("cap4", response.getSimilarCapsURLss().get(4));
        intent.putExtra("cap5", response.getSimilarCapsURLss().get(5));
        intent.putExtra("duplicate", response.isDuplicate());
        startActivity(intent);
    }
}