package com.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class CountryFragment extends Fragment {

    private TextView textViewCountry;
    private TextView textViewInf;
    private boolean isAllInf = false;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Parcel currentParcel;
    private MainActivity mainActivity;

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
            isAllInf = sharedPreferences.getBoolean(Constants.ALL_INFORMATION, false);
            currentParcel = new Parcel(nameCity, isAllInf);
        }

        init(view);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(Constants.CURRENT_PARCEL, currentParcel);
        super.onSaveInstanceState(outState);
    }

    void init(View view) {

        textViewCountry = view.findViewById(R.id.textViewCountry);
        textViewInf = view.findViewById(R.id.textView2);

        isAllInf = currentParcel.isAllInf();

        ImageView imageViewEdit = view.findViewById(R.id.imageView);

        imageViewEdit.setOnClickListener(v -> mainActivity.pushFragments(new SearchFragment(), true, null));
    }

    public Parcel getCurrentParcel() {
        return currentParcel;
    }

    @Override
    public void onStart() {
        super.onStart();

        textViewCountry.setText(currentParcel.getCityName());

        if(isAllInf) {
            Random random = new Random();
            textViewInf.setText(String.valueOf(random.nextInt(60) - 30));
        }
    }
}