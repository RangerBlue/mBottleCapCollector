package com.km.mbottlecapcollector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeScreenActivity extends Activity {
    private Handler handler;
    private int delayInSeconds = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(WelcomeScreenActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }, delayInSeconds * 1000);

    }
}
