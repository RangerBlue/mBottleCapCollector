package com.km.mbottlecapcollector;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class ChoiceActivity extends AppCompatActivity {
    private ImageView imageView;
    private String imageURI;
    private Button retryButton;
    private Button yesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        retryButton = (Button) findViewById(R.id.buttonRetry);
        retryButton.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "Retry button", Toast.LENGTH_SHORT).show());

        yesButton = (Button) findViewById(R.id.buttonYes);
        yesButton.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "Yes button", Toast.LENGTH_SHORT).show());

        imageView = findViewById(R.id.capImage);
        imageURI = getIntent().getStringExtra("URI");
        File image = new File(imageURI);
        imageView.setImageDrawable(Drawable.createFromPath(image.toString()));
    }

}