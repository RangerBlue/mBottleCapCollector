package com.km.mbottlecapcollector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.km.mbottlecapcollector.api.rest.API;

import java.util.Calendar;

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
    private ProgressDialog progressBar;
    private CharSequence loggingMessage;
    private int attemptCounter = 0;
    private int maxAttemptsCounter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences(WelcomeScreenActivity.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        textViewLogin = findViewById(R.id.textViewUsername);
        editTextLogin = findViewById(R.id.editTextUsername);

        textViewPasswordOrState = findViewById(R.id.textViewPasswordOrState);
        editTextPassword = findViewById(R.id.editTextPassword);

        buttonLoginLogout = findViewById(R.id.buttonLoginLogout);
        buttonLoginLogout.setOnClickListener(view -> {
            progressBar.show();
            SharedPreferences.Editor editor = prefs.edit();
            if (isAuthenticated()) {
                progressBar.dismiss();
                editor.putBoolean(getString(R.string.authenticated_key), false);
                editor.putString(getString(R.string.login_key), "");
                editor.putString(getString(R.string.locked_day_key), "");
                editor.commit();
                switchPage();
            } else {
                if ((editTextPassword.getText().toString().length() != 0) &&
                        (editTextLogin.getText().toString().length() != 0)) {


                    editor.putString(
                            getString(R.string.login_key),
                            editTextLogin.getText().toString().trim());
                    editor.putString(
                            getString(R.string.locked_day_key),
                            editTextPassword.getText().toString().trim());
                    editor.putBoolean(getString(R.string.logging_key), true);
                    editor.commit();

                    API.bottleCaps().info().enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                progressBar.dismiss();
                                Toast.makeText(
                                        getApplicationContext(),
                                        getText(R.string.logged_in),
                                        Toast.LENGTH_SHORT).show();
                                editor.putBoolean(getString(R.string.authenticated_key), true);
                                editor.putBoolean(getString(R.string.logging_key), false);
                                editor.commit();
                                switchPage();
                            }
                            if (response.code() == 401) {
                                progressBar.dismiss();
                                Toast.makeText(
                                        getApplicationContext(),
                                        getText(R.string.wrong_credentials),
                                        Toast.LENGTH_SHORT).show();
                                editor.putBoolean("authenticated", false);
                                editor.putBoolean(getString(R.string.logging_key), false);
                                editor.commit();
                                attemptCounter++;
                                if (attemptCounter == maxAttemptsCounter) {
                                    editor.putInt(
                                            getString(R.string.locked_day_key),
                                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                                    editor.commit();
                                    Toast.makeText(
                                            getApplicationContext(),
                                            getText(R.string.too_many_attempts),
                                            Toast.LENGTH_SHORT).show();
                                    buttonLoginLogout.setEnabled(false);
                                }
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
                } else {
                    progressBar.dismiss();
                    Toast.makeText(getApplicationContext(), getText(R.string.inputs_empty),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        switchPage();
        if (isLocked()) {
            buttonLoginLogout.setEnabled(false);
            Toast.makeText(
                    getApplicationContext(),
                    getText(R.string.your_app_is_currently_blocked),
                    Toast.LENGTH_SHORT).show();
        }
        progressBar = new ProgressDialog(this);
        progressBar.setTitle(R.string.loading);
        progressBar.setMessage(loggingMessage);
        progressBar.setCancelable(false);
        progressBar.dismiss();
    }

    private void switchPage() {
        if (isAuthenticated()) {
            textViewLogin.setText(prefs.getString(getString(R.string.login_key), "user"));
            editTextLogin.setVisibility(View.INVISIBLE);
            textViewPasswordOrState.setText(R.string.logged);
            editTextPassword.setVisibility(View.INVISIBLE);
            buttonLoginLogout.setText(R.string.logout);
            loggingMessage = getText(R.string.logging_out);
        } else {
            textViewLogin.setText(R.string.login);
            editTextLogin.setVisibility(View.VISIBLE);
            textViewPasswordOrState.setText(R.string.password);
            editTextPassword.setVisibility(View.VISIBLE);
            buttonLoginLogout.setText(R.string.login);
            loggingMessage = getText(R.string.logging_in);
        }

    }

    private boolean isAuthenticated() {
        return prefs.getBoolean(getString(R.string.authenticated_key), false);
    }

    private boolean isLocked() {
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int lockedDay = prefs.getInt(getString(R.string.locked_day_key), -1);
        return lockedDay == currentDay;
    }
}