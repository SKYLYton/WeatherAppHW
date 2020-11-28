package com.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.weather.fragments.AboutFragment;
import com.weather.fragments.CountryFragment;
import com.weather.fragments.SettingsFragment;
import com.weather.model.RequestApi;
import com.weather.model.weather.WeatherRequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private long mLastClickTime;
    private static final String TAB_MAIN = "tab_main";
    private static final String TAB_ABOUT = "tab_about";
    private static final String TAB_SETTINGS = "tab_settings";
    private String mCurrentTab = TAB_MAIN;
    private FragmentManager frgManager;
    private Map<String, Stack<Fragment>> mStacks;
    private BottomNavigationView navView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private MainParcel currentMainParcel;
    private CountryFragment countryFragment;
    private int currentCityId;

    public int getCurrentCityId() {
        return currentCityId;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putParcelable(Constants.CURRENT_PARCEL, currentMainParcel);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (savedInstanceState != null) {
            currentMainParcel = (MainParcel) savedInstanceState.getParcelable(Constants.CURRENT_PARCEL);
            setCurrentTab(currentMainParcel.getCurrentTab());
        } else {
            currentMainParcel = new MainParcel(false, TAB_MAIN);
        }

        if (sharedPreferences.getBoolean(Constants.SHARED_THEME_IS_DARK, false) &&
                AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(true);
            return;
        } else if (!sharedPreferences.getBoolean(Constants.SHARED_THEME_IS_DARK, false) &&
                AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(false);
            return;
        }

        frgManager = getSupportFragmentManager();

        navView = findViewById(R.id.nav_view);

        init();

    }

    private void init() {

        currentCityId = sharedPreferences.getInt(Constants.SHARED_COUNTRY_ID, Constants.DEFAULT_CITY_ID);

        if (mStacks == null) {
            mStacks = new HashMap<String, Stack<Fragment>>();
            mStacks.put(TAB_MAIN, new Stack<Fragment>());
            mStacks.put(TAB_ABOUT, new Stack<Fragment>());
            mStacks.put(TAB_SETTINGS, new Stack<Fragment>());
        }

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return false;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                switch (item.getItemId()) {
                    case R.id.navigation_home:

                        setCurrentTab(TAB_MAIN);

                        break;

                    case R.id.navigation_about:

                        setCurrentTab(TAB_ABOUT);

                        break;

                    case R.id.navigation_settings:

                        setCurrentTab(TAB_SETTINGS);

                        break;
                }

                currentMainParcel.setCurrentTab(getCurrentTab());
                selectedTab();

                return true;
            }
        });

        int id = 0;
        switch (getCurrentTab()) {
            case TAB_MAIN:
                id = R.id.navigation_home;
                break;
            case TAB_ABOUT:
                id = R.id.navigation_about;
                break;
            case TAB_SETTINGS:
                id = R.id.navigation_settings;
                break;
        }
        navView.setSelectedItemId(id);

        navView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (mStacks.get(getCurrentTab()).size() > 1) {
                    mStacks.get(getCurrentTab()).subList(1, mStacks.get(getCurrentTab()).size()).clear();
                    selectedTab();
                }
            }
        });

    }

    private void selectedTab() {
        if (mStacks.get(getCurrentTab()).empty()) {

            Fragment fragment = null;

            switch (getCurrentTab()) {
                case TAB_MAIN:
                    fragment = new CountryFragment();
                    countryFragment = (CountryFragment) fragment;
                    break;
                case TAB_ABOUT:
                    fragment = new AboutFragment();
                    break;
                case TAB_SETTINGS:
                    fragment = new SettingsFragment();
                    break;
            }
            pushFragments(fragment, true, null);
        } else {
            pushFragments(mStacks.get(getCurrentTab()).lastElement(), false, null);
        }
    }

    public void pushFragments(Fragment fragment, boolean addToBS, Bundle bundle) {
        if (addToBS) {
            mStacks.get(getCurrentTab()).push(fragment);
        }

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        FragmentTransaction ft = frgManager.beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    public void changeCountry(int id, String country) {
        countryFragment.getCurrentParcel().setCityName(country);
        countryFragment.getCurrentParcel().setCityId(id);
        currentCityId = id;
    }

    public String getCurrentTab() {
        return mCurrentTab;
    }

    public void setCurrentTab(String mCurrentTab) {
        this.mCurrentTab = mCurrentTab;
    }

    public void popFragments() {
        mStacks.get(getCurrentTab()).pop();

        Fragment fragment = mStacks.get(getCurrentTab()).elementAt(mStacks.get(getCurrentTab()).size() - 1);

        pushFragments(fragment, false, null);
    }

    public void setTheme(boolean isDark) {
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
        editor.putBoolean(Constants.SHARED_THEME_IS_DARK, isDark);
        editor.commit();
    }

    public boolean isDarkTheme() {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
    }

    @Override
    public void onBackPressed() {
        if (mStacks.get(getCurrentTab()).size() <= 1) {
            super.onBackPressed();
            return;
        }

        popFragments();
    }


}