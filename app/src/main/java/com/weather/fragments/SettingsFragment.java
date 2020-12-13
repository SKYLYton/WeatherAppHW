package com.weather.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.weather.Constants;
import com.weather.MainActivity;
import com.weather.R;

public class SettingsFragment extends Fragment {
    private MainActivity mainActivity;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);

        mainActivity = (MainActivity) getActivity();

        sharedPreferences = getContext().getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        init(view);

        return view;
    }

    private void init(View view) {
        RadioGroup radioGroupTheme = view.findViewById(R.id.radioGroupTheme);
        RadioButton radioButtonDark = view.findViewById(R.id.radioButtonDark);
        RadioButton radioButtonLight = view.findViewById(R.id.radioButtonLight);

        radioButtonDark.setChecked(mainActivity.isDarkTheme());

        radioGroupTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButtonDark:
                        mainActivity.setTheme(true);
                        break;
                    case R.id.radioButtonLight:
                        mainActivity.setTheme(false);
                        break;
                }
            }
        });

        RadioGroup radioGroupTypeMenu = view.findViewById(R.id.radioGroupTypeMenu);
        RadioButton radioButtonSide = view.findViewById(R.id.radioButtonSide);
        RadioButton radioButtonBottom = view.findViewById(R.id.radioButtonBottom);

        radioButtonBottom.setChecked(!mainActivity.isSideMenu());

        radioGroupTypeMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButtonSide:
                        mainActivity.setTypeMenu(true);
                        break;
                    case R.id.radioButtonBottom:
                        mainActivity.setTypeMenu(false);
                        break;
                }
            }
        });

    }
}