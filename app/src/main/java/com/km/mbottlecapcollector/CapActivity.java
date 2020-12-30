package com.km.mbottlecapcollector;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.km.mbottlecapcollector.api.model.Cap;
import com.km.mbottlecapcollector.util.ScreenRatioHelper;
import com.squareup.picasso.Picasso;

public abstract class CapActivity extends Activity {
    private static final String TAG = ReadCapActivity.class.getSimpleName();
    public static final String EXTRA_ID = "bcc.ID";
    public static final String EXTRA_URL = "bcc.URL";
    public static final String EXTRA_CAP_NAME = "bcc.CAP_NAME";
    public static final String EXTRA_GOOGLE_DRIVE_NAME = "bcc.GOOGLE_DRIVE_NAME";
    public static final String EXTRA_CREATION_DATE= "bcc.CREATION_DATE";
    public ImageView imageViewCapPicture;
    public TextView textViewEditCapName;
    public TextView textViewGoogleDriveName;
    public TextView textViewCreationDate;
    public Button buttonLeft;
    public Button buttonRight;
    public long capID;
    public String url;

    public final void initializeViews() {
        imageViewCapPicture = findViewById(R.id.capPicture);
        textViewGoogleDriveName = findViewById(R.id.textViewGoogleDriveName);
        textViewCreationDate = findViewById(R.id.textViewCreationDate);
        initializeCapNameField();
        initializeLeftButton();
        initializeRightButton();
    }

    public final void initializeData() {
        url = getIntent().getStringExtra(EXTRA_URL);
        Picasso.get().load(url).into(imageViewCapPicture);
        textViewEditCapName.setText(getIntent().getStringExtra(EXTRA_CAP_NAME));
        textViewGoogleDriveName.setText(getIntent().getStringExtra(EXTRA_GOOGLE_DRIVE_NAME));
        textViewCreationDate.setText(getIntent().getStringExtra(EXTRA_CREATION_DATE));
        capID = getIntent().getLongExtra(EXTRA_ID, 0);
    }

    public final void goToMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public abstract void initializeLeftButton();

    public abstract void initializeRightButton();

    public abstract void initializeCapNameField();

    public static void putValuesForCapIntent(Intent intent, Cap cap) {
        intent.putExtra(EXTRA_ID, cap.getId());
        intent.putExtra(EXTRA_URL, cap.getFileLocation(ScreenRatioHelper.getStandaloneCapWidth()));
        intent.putExtra(EXTRA_CAP_NAME, cap.getCapName());
        intent.putExtra(EXTRA_GOOGLE_DRIVE_NAME, cap.getGoogleDriveID());
        intent.putExtra(EXTRA_CREATION_DATE, cap.getCreationDate());
    }

    public static void putValuesForCapIntent(Intent intent, long id, String url, String capName,
                                             String googleDriveName, String creationDate) {
        putValuesForCapIntent(intent, new Cap(id, url, googleDriveName, capName, creationDate));
        intent.putExtra(EXTRA_URL, url);
    }
}
