package com.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.weather.bottomsheet.BottomSheetCreator;
import com.weather.fragments.AboutFragment;
import com.weather.fragments.CountryFragment;
import com.weather.fragments.Parcel;
import com.weather.fragments.SettingsFragment;
import com.weather.receivers.NetworkReceiver;
import com.weather.receivers.PowerConnectionReceiver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private NetworkReceiver networkReceiver;
    private PowerConnectionReceiver powerConnectionReceiver;
    private long mLastClickTime;
    private static final String TAB_MAIN = "tab_main";
    private static final String TAB_ABOUT = "tab_about";
    private static final String TAB_SETTINGS = "tab_settings";
    private static final int TAB_MAIN_ID = 0;
    private static final int TAB_ABOUT_ID = 1;
    private static final int TAB_SETTINGS_ID = 2;
    private String mCurrentTab = TAB_MAIN;
    private FragmentManager frgManager;
    private Map<String, Stack<Fragment>> mStacks;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private MainParcel currentMainParcel;
    private CountryFragment countryFragment;
    private boolean isSideMenu = false;
    private int currentCityId;
    private String currentCountryText;
    private TextView textViewCountry;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Parcel parcel;

    public int getCurrentCityId() {
        return currentCityId;
    }

    public void setCountryText(String countryText){
        currentMainParcel.setCurrentCountry(countryText);
        if(isSideMenu) {
            textViewCountry.setText(countryText);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putParcelable(Constants.CURRENT_PARCEL, currentMainParcel);

        super.onSaveInstanceState(outState);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String currentCountry = sharedPreferences.getString(Constants.SHARED_COUNTRY_NAME, getResources().getStringArray(R.array.cities)[0]);

        parcel = new Parcel(currentCountry, getCurrentCityId(), false);

        isSideMenu = sharedPreferences.getString(Constants.SHARED_MENU_TYPE, Constants.SHARED_SIDE_MENU).equals(Constants.SHARED_SIDE_MENU);

        if (isSideMenu) {
            setContentView(R.layout.activity_side_menu);
        } else {
            setContentView(R.layout.activity_main);
        }

        if (savedInstanceState != null) {
            currentMainParcel = (MainParcel) savedInstanceState.getParcelable(Constants.CURRENT_PARCEL);
            setCurrentTab(currentMainParcel.getCurrentTab());
            currentCountryText = currentMainParcel.getCurrentCountry();
        } else {
            currentMainParcel = new MainParcel(false, TAB_MAIN, currentCountryText);
        }

        frgManager = getSupportFragmentManager();
        currentCityId = sharedPreferences.getInt(Constants.SHARED_COUNTRY_ID, Constants.DEFAULT_CITY_ID);

        initStack();
        if(!isSideMenu) {
            initBottomNavView();
        } else {
            Toolbar toolbar = initToolbar();
            initNavView(toolbar);
        }

        initBroadcastReceiverNetwork();
        initBroadcastReceiverPower();
    }

    private void initBroadcastReceiverNetwork(){
        ImageView imageViewNetwork = findViewById(R.id.imageViewNetwork);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.CONNECTIVITY_ACTION);
        networkReceiver = new NetworkReceiver();
        registerReceiver(networkReceiver, intentFilter);
        networkReceiver.setOnNetworkStateListener(isConnected ->
                imageViewNetwork.setImageResource(isConnected ? R.drawable.ic_signal : R.drawable.ic_no_signal));
    }

    private void initBroadcastReceiverPower(){
        ImageView imageViewBattery = findViewById(R.id.imageViewBattery);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        powerConnectionReceiver = new PowerConnectionReceiver();
        registerReceiver(powerConnectionReceiver, intentFilter);
        powerConnectionReceiver.setOnPowerStateListener(new PowerConnectionReceiver.OnPowerStateListener() {
            @Override
            public void onCharging() {
                imageViewBattery.setImageResource(R.drawable.ic_battery_charging);
            }

            @Override
            public void onNormalLevel() {
                imageViewBattery.setImageResource(R.drawable.ic_battery_full);
            }

            @Override
            public void onLowLevel() {
                imageViewBattery.setImageResource(R.drawable.ic_battery_alert);
            }

            @Override
            public void onCriticalLowLevel() {
                imageViewBattery.setImageResource(R.drawable.ic_battery_low);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem settings = menu.findItem(R.id.action_settings);

        settings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(isSideMenu){
                    setCurrentTab(TAB_SETTINGS);
                    setSelectedItemNavView(TAB_SETTINGS_ID);
                    selectedTab();
                }
                return true;
            }
        });

        return true;
    }

    private void initStack(){
        if (mStacks == null) {
            mStacks = new HashMap<String, Stack<Fragment>>();
            mStacks.put(TAB_MAIN, new Stack<Fragment>());
            mStacks.put(TAB_ABOUT, new Stack<Fragment>());
            mStacks.put(TAB_SETTINGS, new Stack<Fragment>());
        }
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initNavView(Toolbar toolbar) {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View view = navigationView.getHeaderView(0);
        textViewCountry = view.findViewById(R.id.textViewCountry);
        textViewCountry.setText(currentMainParcel.getCurrentCountry());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(selectedItem(item)){
                    getSupportActionBar().setTitle(item.getTitle());
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
                return false;
            }
        });

        int id = 0;
        switch (getCurrentTab()) {
            case TAB_MAIN:
                id = TAB_MAIN_ID;
                break;
            case TAB_ABOUT:
                id = TAB_ABOUT_ID;
                break;
            case TAB_SETTINGS:
                id = TAB_SETTINGS_ID;
                break;
        }

        setSelectedItemNavView(id);

        selectedTab();
    }

    private void setSelectedItemNavView(int id){
        getSupportActionBar().setTitle(navigationView.getMenu().getItem(id).getTitle());
        navigationView.setCheckedItem(navigationView.getMenu().getItem(id));
    }

    private void initBottomNavView() {
        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return selectedItem(item);
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

    @SuppressLint("NonConstantResourceId")
    private boolean selectedItem(MenuItem item){
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
        if(!isSideMenu) {
            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        }
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    public void changeCountry(int id, String country) {
        parcel.setCityName(country);
        parcel.setCityId(id);
        parcel.setLat(0f);
        parcel.setLng(0f);
        currentCityId = id;
        parcel.setCord(false);
    }

    public void changeCountry(double lat, double lng) {
        parcel.setCityName("");
        parcel.setCityId(0);
        parcel.setLat(lat);
        parcel.setLng(lng);
        parcel.setCord(true);
    }

    public Parcel getParcel() {
        return parcel;
    }

    public void popFragments() {
        mStacks.get(getCurrentTab()).pop();

        Fragment fragment = mStacks.get(getCurrentTab()).elementAt(mStacks.get(getCurrentTab()).size() - 1);

        pushFragments(fragment, false, null);
    }

    public String getCurrentTab() {
        return mCurrentTab;
    }

    public void setCurrentTab(String mCurrentTab) {
        this.mCurrentTab = mCurrentTab;
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

    public void setTypeMenu(boolean isSide) {
        editor.putString(Constants.SHARED_MENU_TYPE,
                isSide ? Constants.SHARED_SIDE_MENU : Constants.SHARED_BOTTOM_MENU);
        editor.commit();
        recreate();
    }

    public boolean isSideMenu(){
        return isSideMenu;
    }

    public boolean isDarkTheme() {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (mStacks.get(getCurrentTab()).size() <= 1) {
            super.onBackPressed();
            return;
        }

        popFragments();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkReceiver);
        unregisterReceiver(powerConnectionReceiver);
    }
}