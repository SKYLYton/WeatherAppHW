package com.weather.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.weather.Constants;
import com.weather.MainActivity;
import com.weather.R;

public class SearchFragment extends Fragment {

    private MainActivity mainActivity;
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
        SharedPreferences.Editor editor = sharedPreferences.edit();

        EditText editTextCountry = view.findViewById(R.id.editTextCountry);
        Button buttonChange = view.findViewById(R.id.button);
        CheckBox checkBox = view.findViewById(R.id.checkBox);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);

        buttonChange.setOnClickListener(v -> {
            if(editTextCountry.getText().toString().isEmpty()){
                Toast.makeText(getContext(), getString(R.string.toast_inf_empty_field), Toast.LENGTH_SHORT).show();
                return;
            }

            editor.putBoolean(Constants.SHARED_IS_COUNTRY_EMPTY, false);
            editor.putString(Constants.SHARED_COUNTRY_NAME, editTextCountry.getText().toString());
            editor.apply();

            mainActivity.changeCountry(editTextCountry.getText().toString());
            getParentFragmentManager().popBackStack();
        });

        floatingActionButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }
}