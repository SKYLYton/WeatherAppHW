package com.weather.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weather.Constants;
import com.weather.MainActivity;
import com.weather.R;
import com.weather.adapters.DayItem;
import com.weather.adapters.DaysWeatherAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CountryFragment extends Fragment {

    private TextView textViewCountry;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Parcel currentParcel;
    private MainActivity mainActivity;
    private List<DayItem> dayList = new ArrayList<>();
    private DaysWeatherAdapter daysWeatherAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country, container, false);

        mainActivity = (MainActivity) getActivity();
        sharedPreferences = getContext().getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (savedInstanceState != null) {
            currentParcel = (Parcel) savedInstanceState.getParcelable(Constants.CURRENT_PARCEL);
        } else {
            String nameCity = sharedPreferences.getString(Constants.SHARED_COUNTRY_NAME, getResources().getStringArray(R.array.cities)[0]);
            currentParcel = new Parcel(nameCity);
        }

        init(view);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(Constants.CURRENT_PARCEL, currentParcel);
        super.onSaveInstanceState(outState);
    }

    private void init(View view) {

        daysWeatherAdapter = initDaysWeatherList(view);

        textViewCountry = view.findViewById(R.id.textViewCountry);

        ImageView imageViewEdit = view.findViewById(R.id.imageView);

        imageViewEdit.setOnClickListener(v -> mainActivity.pushFragments(new SearchFragment(), true, null));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private DaysWeatherAdapter initDaysWeatherList(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewDays);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(view.getContext().getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);

        DaysWeatherAdapter daysWeatherAdapter = new DaysWeatherAdapter(dayList);

        recyclerView.setAdapter(daysWeatherAdapter);

        return daysWeatherAdapter;
    }

    public Parcel getCurrentParcel() {
        return currentParcel;
    }

    @Override
    public void onResume() {
        super.onResume();

        textViewCountry.setText(currentParcel.getCityName());

        final int MAX_DAYS = getResources().getStringArray(R.array.days).length;

        //Временно!
        // Нормально сделаю, когда будем получение данных из интернета проходить:)
        //------------------
        Random random = new Random();
        if(dayList.size() < MAX_DAYS) {
            for (int i = dayList.size(); i < MAX_DAYS; i++) {

                //Лень дату прикручивать :)
                String date = (i + 5) + "/08/2020";

                String dayName = getResources().getStringArray(R.array.days)[i];

                dayList.add(new DayItem(dayName, date, String.valueOf(random.nextInt(60) - 30)));
                daysWeatherAdapter.notifyItemInserted(dayList.size() - 1);
            }
        } else {
            for (int i = 0; i < MAX_DAYS; i++) {
                dayList.get(i).setTemperature(String.valueOf(random.nextInt(60) - 30));
                daysWeatherAdapter.notifyItemChanged(i);
            }
        }

        //-------------------
    }
}