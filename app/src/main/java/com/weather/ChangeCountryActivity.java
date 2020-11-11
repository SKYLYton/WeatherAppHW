package com.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeCountryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_country);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        EditText editTextCountry = findViewById(R.id.editTextCountry);
        Button buttonChange = findViewById(R.id.button);
        CheckBox checkBox = findViewById(R.id.checkBox);

        buttonChange.setOnClickListener(view -> {
            if(editTextCountry.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(), getString(R.string.toast_inf_empty_field), Toast.LENGTH_SHORT).show();
                return;
            }

            editor.putBoolean(Constants.SHARED_IS_COUNTRY_EMPTY, false);
            editor.apply();

            Intent intent = new Intent(ChangeCountryActivity.this, MainActivity.class);
            intent.putExtra(Constants.PUT_EXTRA_CITY, editTextCountry.getText().toString());
            intent.putExtra(Constants.PUT_EXTRA_ADD_INFORMATION, checkBox.isChecked());
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}