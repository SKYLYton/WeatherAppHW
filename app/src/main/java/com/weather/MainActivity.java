package com.weather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FragmentManager frgManager;
    private CountryFragment countryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frgManager = getSupportFragmentManager();

        countryFragment = new CountryFragment();

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);

        //boolean isCountryEmpty = sharedPreferences.getBoolean(Constants.SHARED_IS_COUNTRY_EMPTY, true);

        //pushFragments(isCountryEmpty ? new SearchFragment() : countryFragment);

        pushFragments(countryFragment, false, null);

    }

    public void pushFragments(Fragment fragment, boolean addToBS, Bundle bundle) {

        if(bundle != null){
            fragment.setArguments(bundle);
        }

        FragmentTransaction ft = frgManager.beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        ft.replace(R.id.container, fragment);
        if(addToBS) {
            ft.addToBackStack(Constants.EMPTY_STRING);
        }
        ft.commit();
    }

    public void changeCountry(String country){
        countryFragment.getCurrentParcel().setCityName(country);
    }
}