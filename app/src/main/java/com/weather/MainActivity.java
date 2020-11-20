package com.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.weather.fragments.CountryFragment;

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