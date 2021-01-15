package com.km.mbottlecapcollector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WelcomeScreenActivity extends Activity {
    private static final String TAG = WelcomeScreenActivity.class.getSimpleName();
    public static String SHARED_PREFERENCES_NAME = "UserData";
    private Handler handler;
    private final int DELAY_IN_SECONDS_WELCOME = 2;
    private final int DELAY_IN_SECONDS_WORKING_HOURS = 5;
    private final int DELAY_IN_SECONDS_NO_INTERNET = 3;
    public static Context context;
    private final String FROM = "07:00";
    private final String TO = "00:15";
    private AlertDialog.Builder builder;
    private AlertDialog alert;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_welcome);
        welcomeText = findViewById(R.id.textViewWelcome);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        hasDeviceInternetConnection();

        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.hello_admin);
        builder.setMessage(R.string.admin_override);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), getString(R.string.admin_access), Toast.LENGTH_SHORT).show();
                goToMenuActivity();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                closeApplication();
            }
        });

        handler = new Handler();
        if (isCurrentDateBetweenRange(new Date())) {
            if (hasDeviceInternetConnection()) {
                handler.postDelayed(() -> {
                    goToMenuActivity();
                    finish();
                }, DELAY_IN_SECONDS_WELCOME * 1000);
            } else {
                welcomeText.setText(getString(R.string.no_internet_access));
                handler.postDelayed(() -> {
                    this.finishAffinity();
                    finish();
                }, DELAY_IN_SECONDS_NO_INTERNET * 1000);
            }

        } else {
            if (prefs.getBoolean(getString(R.string.authenticated_key), false)) {
                alert = builder.create();
                alert.show();
            } else {
                welcomeText.setText(getString(R.string.outside_of_working_hours, TO, FROM));
                handler.postDelayed(() -> {
                    closeApplication();
                    finish();
                }, DELAY_IN_SECONDS_WORKING_HOURS * 1000);
            }
        }


    }

    public static Context getContext() {
        return context;
    }

    /**
     * Using old way to deal with dates to support devices with android < 8.0
     *
     * @return
     */
    public boolean isCurrentDateBetweenRange(Date now) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date dateFrom;
        Date dateTo;
        Date nowDate;
        try {
            dateFrom = formatter.parse(FROM);
            dateTo = formatter.parse(TO);
            nowDate = formatter.parse(formatter.format(now));
        } catch (ParseException e) {
            return false;
        }

        if (nowDate.after(dateTo) && nowDate.before(dateFrom)) {
            return false;
        } else {
            return true;
        }
    }

    private void goToMenuActivity() {
        Intent intent = new Intent(WelcomeScreenActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    private void closeApplication() {
        this.finishAffinity();
    }

    private boolean hasDeviceInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null) {
                if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                    Log.i(TAG, "Connected to Wi-Fi");
                } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    Log.i(TAG, "Connected to mobile internet");
                }
                return true;
            }
        }
        Log.i(TAG, "Device has no internet access");
        return false;
    }
}
