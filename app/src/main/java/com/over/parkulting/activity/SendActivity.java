package com.over.parkulting.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.over.parkulting.R;

import java.util.ArrayList;
import java.util.List;

public class SendActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Parkulting);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        Button gyu = findViewById(R.id.buttontyut);

        Spinner spinner = findViewById(R.id.spinner);
        List<String> ll = new ArrayList<>();
        ll.add("hui");
        ll.add("her");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, ll);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);



        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, ll);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner2.setAdapter(spinnerArrayAdapter2);

        gyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}