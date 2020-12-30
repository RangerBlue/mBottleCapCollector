package com.km.mbottlecapcollector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ValidateActivity extends Activity {
    private ImageView validateCapImage0;
    private ImageView validateCapImage1;
    private ImageView validateCapImage2;
    private ImageView validateCapImage3;
    private ImageView validateCapImage4;
    private ImageView validateCapImage5;
    private ImageView validateCapImage6;
    private ImageView validateCapImage7;
    private ImageView validateCapImage8;
    private String[] capURLs = new String[9];
    private ImageView capturedImage;
    private String imageURI;
    private TextView textViewValue0_10;
    private TextView textViewValue10_20;
    private TextView textViewValue20_30;
    private TextView textViewValue30_40;
    private TextView textViewValue40_50;
    private TextView textViewValue50_60;
    private TextView textViewValue60_70;
    private TextView textViewValue70_80;
    private TextView textViewValue80_90;
    private TextView textViewValue90_100;

    private Button yesButton;
    private Button noButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);
        validateCapImage0 = findViewById(R.id.similarCap0);
        validateCapImage1 = findViewById(R.id.similarCap1);
        validateCapImage2 = findViewById(R.id.similarCap2);
        validateCapImage3 = findViewById(R.id.similarCap3);
        validateCapImage4 = findViewById(R.id.similarCap4);
        validateCapImage5 = findViewById(R.id.similarCap5);
        validateCapImage6 = findViewById(R.id.similarCap6);
        validateCapImage7 = findViewById(R.id.similarCap7);
        validateCapImage8 = findViewById(R.id.similarCap8);

        capturedImage = findViewById(R.id.capturedPicture);

        textViewValue0_10 = findViewById(R.id.textViewValue0_10);
        textViewValue10_20 = findViewById(R.id.textViewValue10_20);
        textViewValue20_30 = findViewById(R.id.textViewValue20_30);
        textViewValue30_40 = findViewById(R.id.textViewValue30_40);
        textViewValue40_50 = findViewById(R.id.textViewValue40_50);
        textViewValue50_60 = findViewById(R.id.textViewValue50_60);
        textViewValue60_70 = findViewById(R.id.textViewValue60_70);
        textViewValue70_80 = findViewById(R.id.textViewValue70_80);
        textViewValue80_90 = findViewById(R.id.textViewValue80_90);
        textViewValue90_100 = findViewById(R.id.textViewValue90_100);

        initializeData();
        initializePictures();

        yesButton = findViewById(R.id.buttonYesSave);
        yesButton.setOnClickListener(view -> {
            goToSaveActivity();
        });

        noButton = findViewById(R.id.buttonNoSave);
        noButton.setOnClickListener(view -> {
            goToMenuActivity();
        });
    }

    private void initializeData() {
        Intent intent = getIntent();
        capURLs[0] = intent.getStringExtra(ChoiceActivity.EXTRA_CAP_0);
        capURLs[1] = intent.getStringExtra(ChoiceActivity.EXTRA_CAP_1);
        capURLs[2] = intent.getStringExtra(ChoiceActivity.EXTRA_CAP_2);
        capURLs[3] = intent.getStringExtra(ChoiceActivity.EXTRA_CAP_3);
        capURLs[4] = intent.getStringExtra(ChoiceActivity.EXTRA_CAP_4);
        capURLs[5] = intent.getStringExtra(ChoiceActivity.EXTRA_CAP_5);
        capURLs[6] = intent.getStringExtra(ChoiceActivity.EXTRA_CAP_6);
        capURLs[7] = intent.getStringExtra(ChoiceActivity.EXTRA_CAP_7);
        capURLs[8] = intent.getStringExtra(ChoiceActivity.EXTRA_CAP_8);

        imageURI = intent.getStringExtra(CameraActivity.EXTRA_CAPTURED_IMAGE_URI);
        int[] distribution = intent.getIntArrayExtra(ChoiceActivity.EXTRA_DISTRIBUTION);
        textViewValue0_10.setText(String.valueOf(distribution[0]));
        textViewValue10_20.setText(String.valueOf(distribution[1]));
        textViewValue20_30.setText(String.valueOf(distribution[2]));
        textViewValue30_40.setText(String.valueOf(distribution[3]));
        textViewValue40_50.setText(String.valueOf(distribution[4]));
        textViewValue50_60.setText(String.valueOf(distribution[5]));
        textViewValue60_70.setText(String.valueOf(distribution[6]));
        textViewValue70_80.setText(String.valueOf(distribution[7]));
        textViewValue80_90.setText(String.valueOf(distribution[8]));
        textViewValue90_100.setText(String.valueOf(distribution[9]));
    }

    private void initializePictures() {
        Picasso.get().load(capURLs[0]).into(validateCapImage0);
        Picasso.get().load(capURLs[1]).into(validateCapImage1);
        Picasso.get().load(capURLs[2]).into(validateCapImage2);
        Picasso.get().load(capURLs[3]).into(validateCapImage3);
        Picasso.get().load(capURLs[4]).into(validateCapImage4);
        Picasso.get().load(capURLs[5]).into(validateCapImage5);
        Picasso.get().load(capURLs[6]).into(validateCapImage6);
        Picasso.get().load(capURLs[7]).into(validateCapImage7);
        Picasso.get().load(capURLs[8]).into(validateCapImage8);
        capturedImage.setImageDrawable(Drawable.createFromPath(imageURI));
    }

    private void goToSaveActivity() {
        Intent intent = new Intent(this, SaveActivity.class);
        intent.putExtra(CameraActivity.EXTRA_CAPTURED_IMAGE_URI, imageURI);
        startActivity(intent);
    }

    private void goToMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

}