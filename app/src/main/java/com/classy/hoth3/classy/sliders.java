package com.classy.hoth3.classy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.*;
import android.widget.AdapterView;
import android.widget.Toast;

import static android.widget.Toast.*;

public class sliders extends AppCompatActivity {

    private Spinner spinner;
    private static final String[]subjects = {"Computer Science", "Electrical Engineering", "Physics", "Scandinavian"};

    private String subject_to_pass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = (Spinner)findViewById(R.id.spinner_subject);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(sliders.this, android.R.layout.simple_spinner_dropdown_item, subjects);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject_to_pass = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ToastIfNothingSelected();
            }
        });

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //   }
        //});
    }


    void ToastIfNothingSelected(){
        makeText(getApplicationContext(), "Please select a subject.",
                LENGTH_SHORT).show();
    }

}
