package com.km.mbottlecapcollector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.km.mbottlecapcollector.api.model.Cap;
import com.km.mbottlecapcollector.api.rest.API;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SaveActivity extends Activity {
    private ImageView imageViewCap;
    private EditText editTextCapName;
    private Button buttonSave;
    private String imageURI;
    private File image;
    private ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        imageViewCap = findViewById(R.id.savedCapImage);
        editTextCapName = findViewById(R.id.editTextCapName);
        buttonSave = findViewById(R.id.buttonSaveCap);
        buttonSave.setOnClickListener(view -> {
            progressBar.show();
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), image);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", image.getName(), requestFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"),
                    editTextCapName.getText().toString());
            API.bottleCaps().addCap(name, body).enqueue(new Callback<Long>() {
                @Override
                public void onResponse(Call<Long> call, Response<Long> response) {
                    if(response.code() == 201){
                        goToCapActivity(response.body().longValue());
                    }else if(response.code() == 401){
                        Toast.makeText(
                                getApplicationContext(),
                                getText(R.string.action_not_allowed),
                                Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(
                                getApplicationContext(), getText(R.string.try_again) + " " +response,
                                Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Long> call, Throwable t) {
                    progressBar.dismiss();
                    Toast.makeText(
                            getApplicationContext(),
                            getText(R.string.failure) +" "+ t,
                            Toast.LENGTH_SHORT).show();
                }
            });

        });
        buttonSave.setEnabled(false);

        imageURI = getIntent().getStringExtra("uri");
        image = new File(imageURI);
        imageViewCap.setImageDrawable(Drawable.createFromPath(imageURI));

        editTextCapName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length() == 0){
                    buttonSave.setEnabled(false);
                } else {
                    buttonSave.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        progressBar = new ProgressDialog(this);
        progressBar.setTitle(R.string.loading);
        progressBar.setMessage(getString(R.string.saving_cap));
        progressBar.setCancelable(false);
        progressBar.dismiss();
    }

    private void goToCapActivity(long id) {
        API.bottleCaps().cap(id).enqueue(new Callback<Cap>() {
            @Override
            public void onResponse(Call<Cap> call, Response<Cap> response) {
                Cap cap = response.body();
                Intent intent = new Intent(getApplicationContext(), ReadCapActivity.class);
                CapActivity.putValuesForCapIntent(intent, cap);
                progressBar.dismiss();
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Cap> call, Throwable t) {
                Toast.makeText(
                        getApplicationContext(),
                        getText(R.string.failure) +" "+ t,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}