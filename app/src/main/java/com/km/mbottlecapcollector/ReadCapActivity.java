package com.km.mbottlecapcollector;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.km.mbottlecapcollector.api.rest.API;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadCapActivity extends CapActivity {
    private static final String TAG = ReadCapActivity.class.getSimpleName();
    private AlertDialog.Builder builder;
    private AlertDialog alert;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_cap);
        initializeViews();
        initializeData();
        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage(R.string.do_you_really_want_to_delete_this_cap);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(TAG, "Removing cap with ID " + capID);
                progressBar.show();
                API.bottleCaps().deleteCap(capID).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        progressBar.dismiss();
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    getText(R.string.successfully_deleted_cap),
                                    Toast.LENGTH_SHORT).show();
                            goToMenuActivity();
                        } else if (responseCode == 404) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    getString(R.string.cap_with_id_was_not_found, capID),
                                    Toast.LENGTH_SHORT).show();
                        } else if (responseCode == 400) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    getText(R.string.exception_disc),
                                    Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 401) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    getText(R.string.action_not_allowed),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Unexpected exception: " + response,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        progressBar.dismiss();
                        Toast.makeText(
                                getApplicationContext(),
                                getText(R.string.failure) + " " + t,
                                Toast.LENGTH_SHORT).show();
                    }
                });
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        buttonRight.setOnClickListener(view -> {
            alert = builder.create();
            alert.show();
        });

        buttonLeft.setOnClickListener(view -> {
            goToEditActivity();
        });

        progressBar = new ProgressDialog(this);
        progressBar.setTitle(R.string.loading);
        progressBar.setMessage(getString(R.string.deleting_cap));
        progressBar.setCancelable(false);
        progressBar.dismiss();
    }

    @Override
    public void initializeLeftButton() {
        buttonLeft = findViewById(R.id.buttonEdit);
    }

    @Override
    public void initializeRightButton() {
        buttonRight = findViewById(R.id.buttonDelete);
    }

    @Override
    public void initializeEditableFields() {
        textViewEditCapName = findViewById(R.id.textViewCapName);
        textViewEditDescription = findViewById(R.id.textViewDescription);
    }

    private void goToEditActivity() {
        Intent intent = new Intent(this, EditActivity.class);
        putValuesForCapIntent(intent, capID, url, textViewEditCapName.getText().toString(),
                textViewEditDescription.getText().toString(), dateText);
        this.finish();
        startActivity(intent);
    }
}