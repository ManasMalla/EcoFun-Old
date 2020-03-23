package com.manasmalla.ecofun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class EarthActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    startActivity(new Intent(EarthActivity.this, LoginActivity.class));
                }else{
                    Toast.makeText(EarthActivity.this, "Error, logging you out! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth);
    }
}