package com.km.mbottlecapcollector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.km.mbottlecapcollector.api.model.Cap;
import com.km.mbottlecapcollector.api.rest.API;
import com.squareup.picasso.Picasso;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function4;

public class ValidateActivity extends AppCompatActivity {

    private ImageView validateCapImage0;
    private ImageView validateCapImage1;
    private ImageView validateCapImage2;
    private ImageView validateCapImage3;
    private ImageView validateCapImage4;
    private ImageView validateCapImage5;
    String[] capURLs = new String[6];

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

        initializeCapsURLs();
        initializePictures();
    }

    private void initializeCapsURLs(){
        capURLs[0] = getIntent().getStringExtra("cap0");
        capURLs[1] = getIntent().getStringExtra("cap1");
        capURLs[2] = getIntent().getStringExtra("cap2");
        capURLs[3] = getIntent().getStringExtra("cap3");
        capURLs[4] = getIntent().getStringExtra("cap4");
        capURLs[5] = getIntent().getStringExtra("cap5");

    }

    private void initializePictures() {
        Picasso.get().load(capURLs[0]).into(validateCapImage0);
        Picasso.get().load(capURLs[1]).into(validateCapImage1);
        Picasso.get().load(capURLs[2]).into(validateCapImage2);
        Picasso.get().load(capURLs[3]).into(validateCapImage3);
        Picasso.get().load(capURLs[4]).into(validateCapImage4);
        Picasso.get().load(capURLs[5]).into(validateCapImage5);
    }

}