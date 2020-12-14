package com.km.mbottlecapcollector;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.km.mbottlecapcollector.api.rest.API;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {
    private TextView textViewLogin;
    private EditText editTextLogin;
    private TextView textViewPasswordOrState;
    private EditText editTextPassword;
    private Button buttonLoginLogout;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        textViewLogin = findViewById(R.id.textViewUsername);

        editTextLogin = findViewById(R.id.editTextUsername);

        textViewPasswordOrState = findViewById(R.id.textViewPasswordOrState);
        editTextPassword = findViewById(R.id.editTextPassword);

        buttonLoginLogout = findViewById(R.id.buttonLoginLogout);
        buttonLoginLogout.setOnClickListener(view -> {
            SharedPreferences.Editor editor = prefs.edit();
            if (isAuthenticated()) {
                editor.putBoolean("authenticated", false);
                editor.putString("login", "");
                editor.putString("password", "");
                editor.commit();
                switchPage();
            } else {
                if ((editTextPassword.getText().toString().length() != 0) &&
                        (editTextLogin.getText().toString().length() != 0)) {


                    editor.putString("login", editTextLogin.getText().toString().trim());
                    editor.putString("password", editTextPassword.getText().toString().trim());
                    editor.putBoolean("logging", true);
                    editor.commit();

                    API.bottleCaps().info().enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                Toast.makeText(getApplicationContext(), "Logged in... ", Toast.LENGTH_SHORT).show();
                                editor.putBoolean("authenticated", true);
                                editor.putBoolean("logging", false);
                                editor.commit();
                                switchPage();
                            }
                            if (response.code() == 401) {
                                Toast.makeText(getApplicationContext(), "Wrong credentials... ", Toast.LENGTH_SHORT).show();
                                editor.putBoolean("authenticated", false);
                                editor.putBoolean("logging", false);
                                editor.commit();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Failure... " + t, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Inputs must not be empty",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        switchPage();
    }

    private void switchPage() {
        if (isAuthenticated()) {
            textViewLogin.setText(prefs.getString("login", "user"));
            editTextLogin.setVisibility(View.INVISIBLE);
            textViewPasswordOrState.setText(R.string.logged);
            editTextPassword.setVisibility(View.INVISIBLE);
            buttonLoginLogout.setText(R.string.logout);
        } else {
            textViewLogin.setText(R.string.login);
            editTextLogin.setVisibility(View.VISIBLE);
            textViewPasswordOrState.setText(R.string.password);
            editTextPassword.setVisibility(View.VISIBLE);
            buttonLoginLogout.setText(R.string.login);
        }

    }

    private boolean isAuthenticated() {
        return prefs.getBoolean("authenticated", false);
    }

}