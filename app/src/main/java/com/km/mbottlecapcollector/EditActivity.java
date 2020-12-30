package com.km.mbottlecapcollector;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.km.mbottlecapcollector.api.model.Cap;
import com.km.mbottlecapcollector.api.rest.API;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends CapActivity {
    private static final String TAG = EditActivity.class.getSimpleName();
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initializeViews();
        initializeData();
        buttonRight.setOnClickListener(view -> {
            progressBar.show();
            API.bottleCaps().updateCap(capID, textViewEditCapName.getText().
                    toString()).enqueue(new Callback<Cap>() {
                @Override
                public void onResponse(Call<Cap> call, Response<Cap> response) {
                    progressBar.dismiss();
                    int responseCode = response.code();
                    if (responseCode == 200) {
                        Toast.makeText(
                                getApplicationContext(),
                                getText(R.string.successfully_updated_cap),
                                Toast.LENGTH_SHORT).show();
                        Cap cap = response.body();
                        Intent intent = new Intent(getApplicationContext(), ReadCapActivity.class);
                        CapActivity.putValuesForCapIntent(intent, cap);
                        startActivity(intent);
                    } else if (responseCode == 404) {
                        Toast.makeText(
                                getApplicationContext(),
                                getString(R.string.cap_with_id_was_not_found, capID),
                                Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 401) {
                        Toast.makeText(
                                getApplicationContext(),
                                getText(R.string.action_not_allowed),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                getText(R.string.unexpected_exception) + " " + response,
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Cap> call, Throwable t) {
                    progressBar.dismiss();
                    Toast.makeText(
                            getApplicationContext(),
                            getText(R.string.failure) + " " + t,
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
        buttonLeft.setOnClickListener(view -> {
            this.onBackPressed();
        });

        progressBar = new ProgressDialog(this);
        progressBar.setTitle(R.string.loading);
        progressBar.setMessage(getString(R.string.saving_cap));
        progressBar.setCancelable(false);
        progressBar.dismiss();
    }

    @Override
    public void initializeLeftButton() {
        buttonLeft = findViewById(R.id.buttonCancel);
    }

    @Override
    public void initializeRightButton() {
        buttonRight = findViewById(R.id.buttonUpdate);
    }

    @Override
    public void initializeCapNameField() {
        textViewEditCapName = findViewById(R.id.editTextCapName);
    }
}