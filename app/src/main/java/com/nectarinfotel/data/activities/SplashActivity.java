package com.nectarinfotel.data.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;

import com.google.firebase.messaging.FirebaseMessaging;
import com.nectarinfotel.R;
import com.nectarinfotel.ui.login.LoginActivity;
import com.nectarinfotel.utils.Config;


/**
 * Created by Nectar on 23-07-2018.
 */

public class SplashActivity extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences pref;
    String lang;
    private String sharedPrefFile = "kotlinsharedpreference";

    // public static String validurl=null;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public static String validurl = "http://wfms.timesheet.nectarinfotel.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Fabric.with(this, new Crashlytics());
        setContentView(R.layout.splash_layout);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                }
            }
        };

        displayFirebaseRegId();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
               /* Bundle b=getIntent().getExtras();
                if(b!=null)
                {
                    String aa=b.getString("click_action");
                    if (getIntent().hasExtra("click_action"))
                    {
                        Intent resultIntent = new Intent(getApplicationContext(), TicketDetailsActivity.class);
                        startActivity(resultIntent);
                    }
                }
                else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                }*/
                SharedPreferences sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
                String sharedNameValue = sharedPreferences.getString("username", null);
                String sharedPasswordValue = sharedPreferences.getString("password", null);

                if (sharedNameValue != null && sharedPasswordValue != null) {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this, ConfigActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        //   Log.e( "Firebase reg id: " ,""+ regId);
        // PrefUtils.storeKey(SplashActivity.this, AppConstants.To,regId);
    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }

}