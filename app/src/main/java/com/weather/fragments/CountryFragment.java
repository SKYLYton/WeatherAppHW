package com.weather.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.weather.Constants;
import com.weather.MainActivity;
import com.weather.R;
import com.weather.bottomsheet.BottomSheetCreator;
import com.weather.model.RequestApi;
import com.weather.model.service.RequestIntentService;
import com.weather.model.weather.RetrofitRequest;
import com.weather.model.weather.WeatherRequest;

import java.net.HttpURLConnection;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CountryFragment extends Fragment {

    private TextView textViewCountry;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Parcel currentParcel;
    private MainActivity mainActivity;
    private RequestApi requestApi;
    private TextView textViewType;
    private TextView textViewTemp;
    private TextView textViewPressure;
    private TextView textViewWind;

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
            currentParcel = new Parcel(nameCity, mainActivity.getCurrentCityId());
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

        textViewCountry = view.findViewById(R.id.textViewCountry);
        textViewType = view.findViewById(R.id.textViewType);
        textViewTemp = view.findViewById(R.id.textViewTemp);
        textViewPressure = view.findViewById(R.id.textViewPress);
        textViewWind = view.findViewById(R.id.textViewWind);


        ImageButton imageViewEdit = view.findViewById(R.id.imageButton);

        imageViewEdit.setOnClickListener(v -> mainActivity.pushFragments(new SearchFragment(), true, null));


        RetrofitRequest.getOpenWeather().getWeather(Constants.API_UNITS_METRIC, Locale.getDefault().getCountry(),
                currentParcel.getCityId(), Constants.API_KEY).enqueue(new Callback<WeatherRequest>() {
            @Override
            public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                WeatherRequest weatherRequest = response.body();
                if(response.isSuccessful()){
                    currentParcel.setCityName(weatherRequest.getName());

                    textViewCountry.setText(weatherRequest.getName());
                    mainActivity.setCountryText(weatherRequest.getName());

                    textViewType.setText(firstUpperCase(weatherRequest.getWeather()[0].getDescription()));

                    textViewTemp.setText(String.valueOf(weatherRequest.getMain().getTemp()));

                    textViewPressure.setText(String.valueOf(weatherRequest.getMain().getPressure()));

                    textViewWind.setText(String.valueOf(weatherRequest.getWind().getSpeed()));
                } else {
                    BottomSheetCreator.show(getContext(), getString(R.string.toast_request_error));
                }
            }

            @Override
            public void onFailure(Call<WeatherRequest> call, Throwable t) {
                BottomSheetCreator.show(getContext(), getString(R.string.toast_request_no_data));
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mainActivity.registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION_REQUEST));
    }

    @Override
    public void onStop() {
        super.onStop();
        mainActivity.unregisterReceiver(broadcastReceiver);
    }


    public String firstUpperCase(String word){
        if(word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public Parcel getCurrentParcel() {
        return currentParcel;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int code = intent.getIntExtra(Constants.SERVICE_REQUEST_CODE, 0);

            if(code == HttpURLConnection.HTTP_OK) {

                String name = intent.getStringExtra(Constants.BROADCAST_EXTRA_CITY_NAME);
                String type = intent.getStringExtra(Constants.BROADCAST_EXTRA_CITY_TYPE);
                float temp = intent.getFloatExtra(Constants.BROADCAST_EXTRA_CITY_TEMP, 0f);
                int pressure = intent.getIntExtra(Constants.BROADCAST_EXTRA_CITY_PRESSURE, 0);
                float wind = intent.getFloatExtra(Constants.BROADCAST_EXTRA_CITY_WIND, 0f);

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentParcel.setCityName(name);

                        textViewCountry.setText(name);
                        mainActivity.setCountryText(name);

                        textViewType.setText(firstUpperCase(type));

                        textViewTemp.setText(String.valueOf(temp));

                        textViewPressure.setText(String.valueOf(pressure));

                        textViewWind.setText(String.valueOf(wind));
                    }
                });
            }
        }
    };


}