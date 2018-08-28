package com.controller.tr.controllearduino;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.controller.tr.controllearduino.youtube.YouTubeActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;

public class MainActivity extends AppCompatActivity {
    // attr of the ads
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 7/14/17 make the connection with bluetooth
               startActivity(new Intent(getBaseContext(),BluetoothMainList.class));

            }
        });

        // add for admob

        NativeExpressAdView adView = (NativeExpressAdView)findViewById(R.id.adView);

        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);

    }



    // menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.youtube) {
            startActivity(new Intent(this, YouTubeActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // make all what you want her ^_^

    public void goToAir(View view){
        startActivity(new Intent(this,AirActivity.class));

    }


    public void goToLight(View view){
            // go to light activity
           startActivity(new Intent(this,LightActivity.class));

    }


    public void goToDoor(View view){

        startActivity(new Intent(this,DoorActivity.class));

    }

    public void goToAlarm(View view){

        startActivity(new Intent(this,AlarmActivity.class));

    }

    public void goToLcd(View view){

        startActivity(new Intent(this,LcdActivity.class));

    }

    public void goToGaz(View view){


    }


}
