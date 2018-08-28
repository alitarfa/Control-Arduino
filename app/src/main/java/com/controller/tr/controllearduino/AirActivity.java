package com.controller.tr.controllearduino;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class AirActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    public static int value = 0;
    ImageView imageView;
    BluetoothAdapter mBluetoothAdapter;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    connectThread connect;
    SecondConnection secondConnection;

    private ProgressDialog dialog;
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    ImageView lamb;
    private AdView mAdView;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("");
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // to display the arrow of back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // get the light in the top
        lamb= (ImageView) findViewById(R.id.imageView);


        NativeExpressAdView adView = (NativeExpressAdView)findViewById(R.id.adView);

        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);


        // other ads for large display

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6273099645029758/3361944337");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        // for the bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }

        try {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

             if (pairedDevices.isEmpty()){
                 Toast.makeText(this, "No Device yet", Toast.LENGTH_SHORT).show();
             }else {
                 new ProgressTask(this,getPairedDevices()).execute();
             }



        } catch (IOException e) {
            e.printStackTrace();
        }


        // inialize
        imageView = (ImageView) findViewById(R.id.btn_off_on);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                if (pairedDevices.isEmpty()){
                    Toast.makeText(getBaseContext(), "No Device yet", Toast.LENGTH_SHORT).show();
                }else {
                    offOnBtn();
                }


            }
        });


        // for the seekBar

        SeekBar seekBar= (SeekBar) findViewById(R.id.seekBar2);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {




            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                if (pairedDevices.isEmpty()){
                    Toast.makeText(getBaseContext(), "No Device yet", Toast.LENGTH_SHORT).show();
                }else {

                    int i= seekBar.getProgress();

                    int value=((i*255)/100);

                    Toast.makeText(AirActivity.this, ""+value, Toast.LENGTH_SHORT).show();

                    Integer integer=new Integer(value);

                    secondConnection.write(new byte[]{integer.byteValue()});
                    // TODO: 7/22/17 the same with all other act 
                }



            }
        });


    }



    public BluetoothDevice getPairedDevices() throws IOException {
        // get all paired device and display it into the Recycle View
        // id the user press serche btn then indicate to get the other device
        // and add it to the list od the recycle view
        //for the recycle if the device is paired show the other btn of unpaired
        //

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.

            ArrayList<BluetoothDevice> listDevice=new ArrayList<>();

            // we must chose the right device for make the connection ^_^

            if (listDevice.addAll(pairedDevices)){

                Log.e("the device is :",listDevice.get(0).getName());

                //return createBluetoothSocket(listDevice.get(0));
                return listDevice.get(0);

            }

        }else {
            Toast.makeText(this, "No Paired device", Toast.LENGTH_SHORT).show();
        }

        return null;
    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_for_light, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startActivity(new Intent(getApplication(),SettingSwitchModeOffOnActivity.class));

            // do some this for set the value for the off and on
            // the seek bar have value between 0-255
            // ^_^

            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    //todo add action of off and on btn

    public void offOnBtn(){



        // when value = true (on)
        if (value==1){
            // change the image
            imageView.setImageResource(R.drawable.ic_power_button_on);
            lamb.setImageResource(R.drawable.ic_propeller_no_run);
            // change the value to true
            value=0;
            // and do what yoy want to do
            String s="b";
            int a=0;
            Integer integer=new Integer(0);

            secondConnection.write(new byte[]{integer.byteValue()});

        }else
        {
            // if the value==false
            // change the image
            imageView.setImageResource(R.drawable.ic_power_off);
            lamb.setImageResource(R.drawable.ic_propeller);
            // change the value
            value=1;
            // do what you want
            String s="a";

            Integer integer=new Integer(1);

            secondConnection.write(new byte[]{integer.byteValue()});

        }



    }








    // for show the dialogue


    public class ProgressTask extends AsyncTask<Void, Void, Void> {

        /** progress dialog to show user that the backup is processing. */


        public ProgressTask(AirActivity activity,BluetoothDevice device) {
            dialog = new ProgressDialog(activity);
            dialog.setCancelable(false);

            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;

        }




        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("Connecting ...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            con();

            return null;
        }


        // methode

        public  void  con(){



            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            secondConnection=new SecondConnection(mmSocket);
            secondConnection.start();

            dialog.dismiss();

        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mmSocket!=null){
                mmSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
