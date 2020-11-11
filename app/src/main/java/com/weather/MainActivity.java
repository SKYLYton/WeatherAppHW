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
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textViewCountry;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean(Constants.SHARED_IS_COUNTRY_EMPTY, true)) {
            startActivityForResult(new Intent(this, ChangeCountryActivity.class), Constants.CHANGE_COUNTRY_REQUEST_CODE);
        }


        textViewCountry = findViewById(R.id.textViewCountry);
        ImageView imageViewEdit = findViewById(R.id.imageView);

        if (!sharedPreferences.getString(Constants.COUNTRY_NAME, Constants.EMPTY_STRING).isEmpty()) {
            textViewCountry.setText(sharedPreferences.getString(Constants.COUNTRY_NAME, Constants.EMPTY_STRING));
        }

        imageViewEdit.setOnClickListener(view -> {
            startActivityForResult(new Intent(MainActivity.this, ChangeCountryActivity.class), Constants.CHANGE_COUNTRY_REQUEST_CODE);
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
            editor.putString(Constants.COUNTRY_NAME, data.getStringExtra(Constants.PUT_EXTRA_CITY));
            editor.apply();
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        textViewCountry.setText(savedInstanceState.getString(Constants.COUNTRY_NAME));

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