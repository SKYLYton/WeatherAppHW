package com.weather.fragments;

import android.content.Context;
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
import com.weather.model.weather.RetrofitRequest;
import com.weather.model.weather.WeatherRequest;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private boolean isCord = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country, container, false);

        mainActivity = (MainActivity) getActivity();
        sharedPreferences = getContext().getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (mainActivity.getParcel() != null) {
            currentParcel = mainActivity.getParcel();
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

        if(currentParcel.isCord()){
            RetrofitRequest.getOpenWeather().getWeather(Constants.API_UNITS_METRIC, Locale.getDefault().getCountry(),
                    currentParcel.getLat(), currentParcel.getLng(), Constants.API_KEY).enqueue(callback);
        } else {
            RetrofitRequest.getOpenWeather().getWeather(Constants.API_UNITS_METRIC, Locale.getDefault().getCountry(),
                    currentParcel.getCityId(), Constants.API_KEY).enqueue(callback);
        }

    }

    Callback<WeatherRequest> callback = new Callback<WeatherRequest>() {
        @Override
        public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
            WeatherRequest weatherRequest = response.body();
            if (response.isSuccessful()) {
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
    };

    public String firstUpperCase(String word){
        if(word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public Parcel getCurrentParcel() {
        return currentParcel;
    }

}