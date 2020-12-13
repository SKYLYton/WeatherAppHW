package com.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);

        if(isChangedTheme()){
            return;
        }

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();

    }

    private boolean isChangedTheme(){
        if (sharedPreferences.getBoolean(Constants.SHARED_THEME_IS_DARK, false) &&
                AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(true);
            return true;
        } else if (!sharedPreferences.getBoolean(Constants.SHARED_THEME_IS_DARK, false) &&
                AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(false);
            return true;
        }
        return false;
    }

    public void setTheme(boolean isDark) {
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}