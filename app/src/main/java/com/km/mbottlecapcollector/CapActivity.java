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
        url = getIntent().getStringExtra("url");
        Picasso.get().load(url).into(imageViewCapPicture);
        textViewEditCapName.setText(getIntent().getStringExtra("capName"));
        textViewGoogleDriveName.setText(getIntent().getStringExtra("googleDriveName"));
        textViewCreationDate.setText(getIntent().getStringExtra("creationDate"));
        capID = getIntent().getLongExtra("id", 0);
    }

    public final void goToMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public abstract void initializeLeftButton();

    public abstract void initializeRightButton();

    public abstract void initializeCapNameField();

    public static void putValuesForCapIntent(Intent intent, Cap cap) {
        intent.putExtra("id", cap.getId());
        intent.putExtra("url", cap.getFileLocation(ScreenRatioHelper.getStandaloneCapWidth()));
        intent.putExtra("capName", cap.getCapName());
        intent.putExtra("googleDriveName", cap.getGoogleDriveID());
        intent.putExtra("creationDate", cap.getCreationDate());
    }

    public static void putValuesForCapIntent(Intent intent, long id, String url, String capName,
                                             String googleDriveName, String creationDate) {
        putValuesForCapIntent(intent, new Cap(id, url, googleDriveName, capName, creationDate));
        intent.putExtra("url", url);
    }
}
