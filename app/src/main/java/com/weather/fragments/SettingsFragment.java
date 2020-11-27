package com.weather.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.weather.MainActivity;
import com.weather.R;

public class SettingsFragment extends Fragment {
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);

        mainActivity = (MainActivity) getActivity();

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

    }
}