package com.manasmalla.ecofun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.card.MaterialCardView;
import com.parse.ParseUser;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView jungleImageView, cityImageView, animalsSafariImageView;
    MaterialCardView appTitleMaterialCardView;
    Pair[] pairs;
    Intent intentToLoginActivity;
    SharedPreferences sharedPreferences;
    String presentUserContinent, userLevel;
    boolean firstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide the status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        PrefManager prefManager = new PrefManager(getApplicationContext());
        firstRun = prefManager.isFirstTimeLaunch();

        jungleImageView = findViewById(R.id.jungleBackground_splashScreen);
        cityImageView = findViewById(R.id.cityBackground_splashScreen);
        animalsSafariImageView = findViewById(R.id.carAnimalsImageView_splashScreen);
        appTitleMaterialCardView = findViewById(R.id.appTitleBoard_splashScreen);

        sharedPreferences = this.getSharedPreferences("com.manasmalla.ecofun", MODE_PRIVATE);

        presentUserContinent = sharedPreferences.getString("presentUserContinent", null);
        userLevel = sharedPreferences.getString("userLevel", null);

        intentToLoginActivity = new Intent(SplashScreenActivity.this, LoginActivity.class);

        pairs = new Pair[2];
        pairs[0] = new Pair<View, String>(jungleImageView, getString(R.string.jungleImageViewTransition));
        pairs[1] = new Pair<View, String>(animalsSafariImageView, getString(R.string.animalsCarImageViewTransition));

    }

    @Override
    protected void onResume() {
        super.onResume();

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if (ParseUser.getCurrentUser() != null) {

                    Intent intentToEarthActivity = new Intent(SplashScreenActivity.this, EarthActivity.class);
                    Intent intentToPermissionActivity = new Intent(SplashScreenActivity.this, PermissionActivity.class);

                    if (firstRun) {
                        intentToPermissionActivity.putExtra("firstRun", true);
                        startActivity(intentToPermissionActivity);
                    } else {
                        //ParseUser exists!
                        intentToPermissionActivity.putExtra("firstRun", false);
                        PermissionDialogRequester permissionDialogRequester = new PermissionDialogRequester();

                        if (presentUserContinent == null || userLevel == null || presentUserContinent.isEmpty() || userLevel.isEmpty()) {
                                //Variables and permissions not available
                                intentToPermissionActivity.putExtra("requireVariables", true);
                                startActivity(intentToPermissionActivity);
                        } else {
                            if (permissionDialogRequester.checkPermissions(SplashScreenActivity.this).size() > 0) {
                                //Variables avaiable but no permissions
                                intentToPermissionActivity.putExtra("requireVariables", false);
                                startActivity(intentToPermissionActivity);
                            } else {
                                //Everything are avaialable
                                intentToEarthActivity.putExtra("presentUserContinent", presentUserContinent);
                                intentToEarthActivity.putExtra("userLevel", userLevel);
                                startActivity(intentToEarthActivity);
                            }
                        }
                    }

                } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this, pairs);
                    startActivity(intentToLoginActivity, activityOptions.toBundle());
                } else {
                    startActivity(intentToLoginActivity);
                }
            }
        };
        handler.postDelayed(runnable, 2000);
    }
}