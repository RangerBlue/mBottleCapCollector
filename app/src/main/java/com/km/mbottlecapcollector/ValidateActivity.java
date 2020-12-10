package com.km.mbottlecapcollector;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

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
    String[] capURLs = new String[9];

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

        initializeCapsURLs();
        initializePictures();
    }

    private void initializeCapsURLs() {
        capURLs[0] = getIntent().getStringExtra("cap0");
        capURLs[1] = getIntent().getStringExtra("cap1");
        capURLs[2] = getIntent().getStringExtra("cap2");
        capURLs[3] = getIntent().getStringExtra("cap3");
        capURLs[4] = getIntent().getStringExtra("cap4");
        capURLs[5] = getIntent().getStringExtra("cap5");
        capURLs[6] = getIntent().getStringExtra("cap6");
        capURLs[7] = getIntent().getStringExtra("cap7");
        capURLs[8] = getIntent().getStringExtra("cap8");

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
    }

}