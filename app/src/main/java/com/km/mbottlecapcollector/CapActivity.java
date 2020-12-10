package com.km.mbottlecapcollector;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CapActivity extends Activity {
    private ImageView imageViewCapPicture;
    private TextView textViewCapName;
    private TextView textViewGoogleDriveName;
    private TextView textViewCreationDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap);

        imageViewCapPicture = findViewById(R.id.capPicture);
        textViewCapName = findViewById(R.id.textViewCapName);
        textViewGoogleDriveName = findViewById(R.id.textViewGoogleDriveName);
        textViewCreationDate = findViewById(R.id.textViewCreationDate);

        Picasso.get().load(getIntent().getStringExtra("url")).into(imageViewCapPicture);
        textViewCapName.setText(getIntent().getStringExtra("capName"));
        textViewGoogleDriveName.setText(getIntent().getStringExtra("googleDriveName"));
        textViewCreationDate.setText(getIntent().getStringExtra("creationDate"));
    }
}