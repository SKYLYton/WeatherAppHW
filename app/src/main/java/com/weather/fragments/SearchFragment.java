package com.weather.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.weather.R;
import com.weather.adapters.CitiesWeatherAdapter;
import com.weather.adapters.CityItem;
import com.weather.model.city.CitiesModel;
import com.weather.model.city.CityModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private List<CityItem> cityItems = new ArrayList<>();
    private MainActivity mainActivity;
    private CitiesWeatherAdapter citiesWeatherAdapter;
    private List<CityModel> cityModelList;
    private SharedPreferences.Editor editor;

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


        editTextCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cityItems.clear();
                for (int i = 0; i < cityModelList.size(); i++) {
                    if(cityModelList.get(i).getCountry().equals("RU") && cityModelList.get(i).getName().contains(s)) {
                        cityItems.add(new CityItem(cityModelList.get(i).getName(), cityModelList.get(i).getId()));
                    }
                }
                citiesWeatherAdapter.notifyDataSetChanged();
            }
        });

        floatingActionButton.setOnClickListener(v -> mainActivity.onBackPressed());
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private CitiesWeatherAdapter initDaysWeatherList(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(view.getContext().getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);

        Gson gson = new Gson();

        cityModelList = gson.fromJson(getCities(), CitiesModel.class).getList();

        for (int i = 0; i < cityModelList.size(); i++) {
            if(cityModelList.get(i).getCountry().equals("RU")) {
                cityItems.add(new CityItem(cityModelList.get(i).getName(), cityModelList.get(i).getId()));
            }
        }

        CitiesWeatherAdapter citiesWeatherAdapter = new CitiesWeatherAdapter(cityItems);

        recyclerView.setAdapter(citiesWeatherAdapter);

        citiesWeatherAdapter.setOnItemSelect((cityId, cityName) -> {
            editor.putBoolean(Constants.SHARED_IS_COUNTRY_EMPTY, false);
            editor.putString(Constants.SHARED_COUNTRY_NAME, cityName);
            editor.putInt(Constants.SHARED_COUNTRY_ID, cityId);
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
            json = getContext().getAssets().open(Constants.FILE_CITIES_NAME);
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
}