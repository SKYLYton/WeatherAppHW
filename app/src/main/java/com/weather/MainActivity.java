package com.weather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView textViewCountry;
    private TextView textViewInf;
    private boolean isAllInf = false;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        isAllInf = sharedPreferences.getBoolean(Constants.ALL_INFORMATION, false);

        if (sharedPreferences.getBoolean(Constants.SHARED_IS_COUNTRY_EMPTY, true)) {
            startActivityForResult(new Intent(this, ChangeCountryActivity.class), Constants.CHANGE_COUNTRY_REQUEST_CODE);
        }


        textViewCountry = findViewById(R.id.textViewCountry);
        textViewInf = findViewById(R.id.textView2);
        ImageView imageViewEdit = findViewById(R.id.imageView);
        Button buttonUPD = findViewById(R.id.button);

        if (!sharedPreferences.getString(Constants.COUNTRY_NAME, Constants.EMPTY_STRING).isEmpty()) {
            textViewCountry.setText(sharedPreferences.getString(Constants.COUNTRY_NAME, Constants.EMPTY_STRING));
        }

        if(isAllInf){
            Random random = new Random();
            textViewInf.setText(String.valueOf(random.nextInt(60) - 30));
        }

        imageViewEdit.setOnClickListener(view -> {
            startActivityForResult(new Intent(MainActivity.this, ChangeCountryActivity.class), Constants.CHANGE_COUNTRY_REQUEST_CODE);
        });

        buttonUPD.setOnClickListener(view -> {
            if(isAllInf){
                Random random = new Random();
                textViewInf.setText(String.valueOf(random.nextInt(60) - 30));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != Constants.CHANGE_COUNTRY_REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (resultCode == RESULT_OK) {
            textViewCountry.setText(data.getStringExtra(Constants.PUT_EXTRA_CITY));
            isAllInf = data.getBooleanExtra(Constants.PUT_EXTRA_ADD_INFORMATION, false);
            editor.putString(Constants.COUNTRY_NAME, data.getStringExtra(Constants.PUT_EXTRA_CITY));
            editor.putBoolean(Constants.ALL_INFORMATION, isAllInf);
            editor.apply();

            if(isAllInf){
                Random random = new Random();
                textViewInf.setText(String.valueOf(random.nextInt(60) - 30));
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(!savedInstanceState.getString(Constants.COUNTRY_NAME).isEmpty()) {
            textViewCountry.setText(savedInstanceState.getString(Constants.COUNTRY_NAME));
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(Constants.DEBUG) {
            Log.d(TAG, "onSaveInstanceState()");
        }

        outState.putString(Constants.COUNTRY_NAME, textViewCountry.getText().toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Constants.DEBUG) {
            Log.d(TAG, "onStart()");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constants.DEBUG) {
            Log.d(TAG, "onResume()");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(Constants.DEBUG) {
            Log.d(TAG, "onPause()");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(Constants.DEBUG) {
            Log.d(TAG, "onStop()");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(Constants.DEBUG) {
            Log.d(TAG, "onRestart()");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Constants.DEBUG) {
            Log.d(TAG, "onDestroy()");
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(Constants.DEBUG) {
            Log.d(TAG, "onConfigurationChanged()");
        }
    }

}