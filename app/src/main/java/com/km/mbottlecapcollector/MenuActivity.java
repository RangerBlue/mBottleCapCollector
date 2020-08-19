package com.km.mbottlecapcollector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    private Button checkButton;
    private Button viewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        checkButton = (Button) findViewById(R.id.checkButton);
        checkButton.setOnClickListener(view -> {
            goToCameraActivity();
        });

        viewButton = (Button) findViewById(R.id.viewButton);
        viewButton.setOnClickListener(view -> {
            goToGalleryActivity();
        });
    }

    private void goToCameraActivity() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    private void goToGalleryActivity() {
        Toast.makeText(getApplicationContext(), "Activity view", Toast.LENGTH_SHORT).show();
    }
}