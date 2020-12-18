package com.km.mbottlecapcollector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.km.mbottlecapcollector.api.rest.API;
import com.squareup.picasso.Picasso;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CapActivity extends Activity {
    private static final String TAG = CapActivity.class.getSimpleName();
    private ImageView imageViewCapPicture;
    private TextView textViewCapName;
    private TextView textViewGoogleDriveName;
    private TextView textViewCreationDate;
    private Button buttonEdit;
    private Button buttonDelete;
    private long capID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap);

        imageViewCapPicture = findViewById(R.id.capPicture);
        textViewCapName = findViewById(R.id.textViewCapName);
        textViewGoogleDriveName = findViewById(R.id.textViewGoogleDriveName);
        textViewCreationDate = findViewById(R.id.textViewCreationDate);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonEdit = findViewById(R.id.buttonEdit);

        Picasso.get().load(getIntent().getStringExtra("url")).into(imageViewCapPicture);
        textViewCapName.setText(getIntent().getStringExtra("capName"));
        textViewGoogleDriveName.setText(getIntent().getStringExtra("googleDriveName"));
        textViewCreationDate.setText(getIntent().getStringExtra("creationDate"));
        capID = getIntent().getLongExtra("id", 0);

        buttonDelete.setOnClickListener(view -> {
            Log.i(TAG, "Removing cap with ID " + capID);
            API.bottleCaps().deleteCap(capID).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int responseCode = response.code();
                    if (responseCode == 200) {
                        Toast.makeText(getApplicationContext(), "Successfully deleted cap ",
                                Toast.LENGTH_SHORT).show();
                        goToMenuActivity();
                    } else if (responseCode == 404) {
                        Toast.makeText(getApplicationContext(), "Cap with " + capID + "was not found",
                                Toast.LENGTH_SHORT).show();
                    } else if (responseCode == 400) {
                        Toast.makeText(getApplicationContext(), "Exception during removing from disc",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Unexpected exception: " + response,
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Failure! " + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void goToMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}