package com.weather.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.weather.Constants;
import com.weather.MainActivity;
import com.weather.MapsActivity;
import com.weather.R;
import com.weather.adapters.CitiesWeatherAdapter;
import com.weather.adapters.CityItem;
import com.weather.retrofit.model.city.CitiesModel;
import com.weather.retrofit.model.city.CityModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class SearchFragment extends Fragment {

    private static final int RESULT_CHOOSE_LOCATION_CODE = 0;
    private List<CityItem> cityItems = new ArrayList<>();
    private MainActivity mainActivity;
    private CitiesWeatherAdapter citiesWeatherAdapter;
    private List<CityModel> cityModelList;
    private SharedPreferences.Editor editor;
    private Thread thread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mainActivity = (MainActivity) getActivity();

        init(view);

        return view;
    }

    private void init(View view) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        citiesWeatherAdapter = initDaysWeatherList(view);
        EditText editTextCountry = view.findViewById(R.id.editTextCountry);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        FloatingActionButton floatingActionButtonMap = view.findViewById(R.id.floatingActionButtonMap);

        editTextCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(cityModelList == null){
                    return;
                }
                cityItems.clear();
                for (int i = 0; i < cityModelList.size(); i++) {
                    if(cityModelList.get(i).getCountry().equals(Constants.LANGUAGE_RU) &&
                            cityModelList.get(i).getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        cityItems.add(new CityItem(cityModelList.get(i).getName(), cityModelList.get(i).getId()));
                    }
                }
                citiesWeatherAdapter.notifyDataSetChanged();
            }
        });

        floatingActionButton.setOnClickListener(v -> mainActivity.onBackPressed());

        floatingActionButtonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), MapsActivity.class), RESULT_CHOOSE_LOCATION_CODE);
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private CitiesWeatherAdapter initDaysWeatherList(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(view.getContext().getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);
        CitiesWeatherAdapter citiesWeatherAdapter = new CitiesWeatherAdapter(cityItems);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();

                cityModelList = gson.fromJson(getCities(), CitiesModel.class).getList();

                for (int i = 0; i < cityModelList.size(); i++) {
                    if(cityModelList.get(i).getCountry().equals(Constants.LANGUAGE_RU)) {
                        cityItems.add(new CityItem(cityModelList.get(i).getName(), cityModelList.get(i).getId()));
                    }
                }

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(citiesWeatherAdapter);
                    }
                });
            }
        });
        thread.start();

        citiesWeatherAdapter.setOnItemSelect((cityId, cityName) -> {
            editor.putString(Constants.SHARED_COUNTRY_NAME, cityName);
            editor.putInt(Constants.SHARED_COUNTRY_ID, cityId);
            editor.putBoolean(Constants.SHARED_TYPE_CORD, false);
            editor.apply();

            mainActivity.changeCountry(cityId, cityName);
            mainActivity.onBackPressed();
        });

        return citiesWeatherAdapter;
    }

    private String getCities(){

        StringBuilder buf = new StringBuilder();
        InputStream json = null;
        try {
            json = mainActivity.getAssets().open(Constants.FILE_CITIES_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(json, StandardCharsets.UTF_8));
        String str = null;

        while (true) {
            try {
                if ((str = in.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            buf.append(str);
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CHOOSE_LOCATION_CODE) {
            if(resultCode == RESULT_OK){
                double lat = data.getDoubleExtra(Constants.EXTRA_LAT, 0f);
                double lng = data.getDoubleExtra(Constants.EXTRA_LNG, 0f);

                editor.putFloat(Constants.SHARED_LAT, (float) lat);
                editor.putFloat(Constants.SHARED_LNG, (float) lng);
                editor.putBoolean(Constants.SHARED_TYPE_CORD, true);
                editor.commit();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Geocoder gcd = new Geocoder(mainActivity, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = gcd.getFromLocation(lat, lng, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                        if (addresses.size() > 0) {
                            editor.putString(Constants.SHARED_COUNTRY_NAME, addresses.get(0).getLocality());
                            editor.commit();
                        }
                    }
                }).start();

                mainActivity.changeCountry(lat, lng);
                mainActivity.onBackPressed();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}