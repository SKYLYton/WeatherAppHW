package com.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        EditText editTextCountry = findViewById(R.id.editTextCountry);
        Button buttonChange = findViewById(R.id.button);
        CheckBox checkBox = findViewById(R.id.checkBox);

        buttonChange.setOnClickListener(view -> {
            if(editTextCountry.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(), getString(R.string.toast_inf_empty_field), Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(getString(R.string.put_extra_city), editTextCountry.getText());
            intent.putExtra(getString(R.string.put_extra_add_inf), checkBox.isChecked());
            startActivity(intent);
            finish();
        });
    }
}