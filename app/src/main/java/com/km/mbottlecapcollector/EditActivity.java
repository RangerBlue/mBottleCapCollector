package com.km.mbottlecapcollector;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initializeViews();
        initializeData();
        buttonRight.setOnClickListener(view -> {
            System.out.println("CLICK");
            API.bottleCaps().updateCap(capID, textViewEditCapName.getText().toString()).enqueue(new Callback<Cap>() {
                @Override
                public void onResponse(Call<Cap> call, Response<Cap> response) {
                    int responseCode = response.code();
                    if (responseCode == 200) {
                        Toast.makeText(getApplicationContext(), "Successfully updated cap ",
                                Toast.LENGTH_SHORT).show();
                        Cap cap = response.body();
                        Intent intent = new Intent(getApplicationContext(), ReadCapActivity.class);
                        CapActivity.putValuesForCapIntent(intent, cap);
                        startActivity(intent);
                    }else if (responseCode == 404) {
                        Toast.makeText(getApplicationContext(), "Cap with " + capID + "was not found",
                                Toast.LENGTH_SHORT).show();
                    }else if(response.code() == 401){
                        Toast.makeText(getApplicationContext(), "You are not allowed to perform" +
                                "this action "+response, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Unexpected exception: " + response,
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Cap> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Failure! " + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        });
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