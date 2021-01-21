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
    public static final String EXTRA_DESCRIPTION = "bcc.DESCRIPTION";
    public static final String EXTRA_CREATION_DATE= "bcc.CREATION_DATE";
    public ImageView imageViewCapPicture;
    public TextView textViewEditCapName;
    public TextView textViewEditDescription;
    public TextView textViewCreationDate;
    public String dateText;
    public Button buttonLeft;
    public Button buttonRight;
    public long capID;
    public String url;

    public final void initializeViews() {
        imageViewCapPicture = findViewById(R.id.capPicture);
        textViewEditDescription = findViewById(R.id.textViewDescription);
        textViewCreationDate = findViewById(R.id.textViewCreationDate);
        initializeEditableFields();
        initializeLeftButton();
        initializeRightButton();
    }

    public final void initializeData() {
        url = getIntent().getStringExtra(EXTRA_URL);
        Picasso.get().load(url).into(imageViewCapPicture);
        textViewEditCapName.setText(getIntent().getStringExtra(EXTRA_CAP_NAME));
        textViewEditDescription.setText(getIntent().getStringExtra(EXTRA_DESCRIPTION));
        dateText = getIntent().getStringExtra(EXTRA_CREATION_DATE);
        textViewCreationDate.setText(trimDate(dateText));
        capID = getIntent().getLongExtra(EXTRA_ID, 0);
    }

    public final void goToMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public abstract void initializeLeftButton();

    public abstract void initializeRightButton();

    public abstract void initializeEditableFields();

    public static void putValuesForCapIntent(Intent intent, Cap cap) {
        intent.putExtra(EXTRA_ID, cap.getId());
        intent.putExtra(EXTRA_URL, cap.getFileLocation(ScreenRatioHelper.getStandaloneCapWidth()));
        intent.putExtra(EXTRA_CAP_NAME, cap.getCapName());
        intent.putExtra(EXTRA_DESCRIPTION, cap.getDescription());
        intent.putExtra(EXTRA_CREATION_DATE, cap.getCreationDate());
    }

    public static void putValuesForCapIntent(Intent intent, long id, String url, String capName,
                                             String googleDriveName, String creationDate) {
        putValuesForCapIntent(intent, new Cap(id, url, googleDriveName, capName, creationDate));
        intent.putExtra(EXTRA_URL, url);
    }

    private String trimDate(String date){
        return date.replace("T", " ").substring(0, date.length()-4);
    }
}
