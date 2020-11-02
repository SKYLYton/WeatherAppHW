package com.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle arg = getIntent().getExtras();

        TextView textViewCountry = findViewById(R.id.textViewCountry);
        ImageView imageViewEdit = findViewById(R.id.imageView);

        if(arg != null) {
            textViewCountry.setText(arg.get(getString(R.string.put_extra_city)).toString());
        }

        imageViewEdit.setOnClickListener(view -> {
            startActivity(new Intent(this, ChangeCountryActivity.class));
            finish();
        });


    }
}