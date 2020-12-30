package com.km.mbottlecapcollector;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.km.mbottlecapcollector.util.FileHelper;
import com.km.mbottlecapcollector.util.ScreenRatioHelper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class WhatCapAreYouActivity extends Activity {
    private static final String TAG = WhatCapAreYouActivity.class.getSimpleName();
    private TextView textViewYou;
    private TextView textViewYourCap;
    private ImageView imageViewYou;
    private ImageView imageViewYourCap;
    private Button buttonShare;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_cap_are_you);
        textViewYou = findViewById(R.id.textViewYou);
        textViewYourCap = findViewById(R.id.textViewYourCap);
        imageViewYou = findViewById(R.id.youImage);
        imageViewYourCap = findViewById(R.id.yourCapImage);
        buttonShare = findViewById(R.id.shareButton);
        buttonShare.setOnClickListener(view -> {
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                    .findViewById(android.R.id.content)).getChildAt(0);
            file = file != null ? file : takeScreenshot(createBitmap(viewGroup));
            FileHelper.shareImage(file, getApplicationContext());
        });

        String imageURI = getIntent().getStringExtra(CameraActivity.EXTRA_CAPTURED_IMAGE_URI);
        String imageURL = getIntent().getStringExtra(ChoiceActivity.EXTRA_CAP_URL);
        File image = new File(imageURI);
        Picasso.get().load(image).resize(ScreenRatioHelper.getWhatCapAreYouCapWidth(),
                ScreenRatioHelper.getWhatCapAreYouCapWidth()).into(imageViewYou);
        Picasso.get().load(imageURL).into(imageViewYourCap);
    }

    public Bitmap createBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public File takeScreenshot(Bitmap bitmap) {
        Log.i(TAG, "Saving screenshot");
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                FileHelper.SCREENSHOT_PREFIX + System.currentTimeMillis() + ".jpg");
        ByteArrayOutputStream streamBitmap = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, streamBitmap);
        byte[] outputArray = streamBitmap.toByteArray();
        try {
            FileHelper.save(outputArray, file, getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}