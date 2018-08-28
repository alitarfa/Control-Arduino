package com.controller.tr.controllearduino;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingSwitchModeOffOnActivity extends AppCompatActivity {

    EditText onValue;
    EditText offValue;
    Button saveBtn;
    SharedPreferences  sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // to display the arrow of back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        // get instance for all object

        saveBtn= (Button) findViewById(R.id.save_btn);
        onValue= (EditText) findViewById(R.id.on_value);
        offValue= (EditText) findViewById(R.id.off_value);


        // get the Shared Prefre
        sharedPreferences=getPreferences(MODE_PRIVATE);

        // call the method
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo();
            }
        });


    }
    public void saveInfo(){

        if (!onValue.getText().toString().equals("") && !offValue.getText().toString().equals("")){
            String on=onValue.getText().toString();
            String off=offValue.getText().toString();

            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("value_on",on);
            editor.putString("value_off",off);
            editor.apply();
            Toast.makeText(this, "Modification saved", Toast.LENGTH_SHORT).show();
        }

    }


}
