package com.gp.smart.wear;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = "Login";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2500;

    //Intent
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
        checkConnection();
        FirebaseApp.initializeApp(getApplicationContext());
        initializeAuthListener ();
        mAuth.addAuthStateListener(mAuthListener);
        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                checkConnection();
                if (mAuthListener != null) {
                    mAuth.removeAuthStateListener(mAuthListener);
                }
                startActivity(intent);
                finish();
                // close this activity
            }
        }, SPLASH_TIME_OUT);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Android Insomnia Regular.ttf");
        TextView textView = (TextView) findViewById(R.id.splach_text_view);
        textView.setTypeface(typeface);
    }

    private void initializeAuthListener() {
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    intent = new Intent(getBaseContext(), HomeActivity.class);
                    intent.putExtra("user_name", user.getDisplayName());
                    intent.putExtra("user_email", user.getEmail());
                    SharedPreferences.Editor preferences = getSharedPreferences("UserFile" , MODE_PRIVATE).edit();
                    preferences.putString("user_id",user.getUid());
                    preferences.apply();

                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    intent = new Intent(getBaseContext(), LoginActivity.class);
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    private void checkConnection() {
        // first, check connectivity
        if (Utilities
                .checkInternetConnection(SplashScreenActivity.this)) {
//  Declare a new thread to do a preference check
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    //  Initialize MySharedPreferences
                    SharedPreferences getPrefs = PreferenceManager
                            .getDefaultSharedPreferences(getBaseContext());

/*
                    //  Create a new boolean and preference and set it to true
                    boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                    //  If the activity has never started before...
                    if (isFirstStart) {
                        //  Launch app intro
                        Intent i = new Intent(SplashScreenActivity.this, IntroActivity.class);
                        startActivity(i);

                        //  Make a new preferences editor
                        SharedPreferences.Editor e = getPrefs.edit();

                        //  Edit preference to make it false because we don't want this to run again
                        e.putBoolean("firstStart", false);

                        //  Apply changes
                        e.apply();
                    } else {
                        //  Launch Main Activity
                        Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(i);
                    }

                    Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
*/
                }
            });
            // Start the thread
            t.start();

            // do things if it there's network connection
        } else {
            // as it seems there's no Internet connection
            // ask the user to activate it
            new AlertDialog.Builder(SplashScreenActivity.this)
                    .setTitle("Connection failed")
                    .setMessage("This application requires network access. Please, enable " +
                            "mobile network or Wi-Fi.")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // THIS IS WHAT YOU ARE DOING, Jul
                            SplashScreenActivity.this.startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SplashScreenActivity.this.finish();
                        }
                    })
                    .show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            checkConnection();
        }
    }
}
