package com.controller.tr.controllearduino;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class LcdActivity extends AppCompatActivity {

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

    // shared prefrec

    SharedPreferences preferences;

    // thde code for the door
    public static  String doorCode="";


    TextView displayCodePin;
    ImageView deleteFromTheDisplay;

    EditText lcdText;


    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lcd);
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


        preferences=getPreferences(MODE_PRIVATE);
        lcdText= (EditText) findViewById(R.id.lcdTxt);


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // for the bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }

        try {

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.isEmpty()){
                Toast.makeText(getBaseContext(), "No Device yet", Toast.LENGTH_SHORT).show();
            }else {

                new ProgressTask(this,getPairedDevices()).execute();

            }



        } catch (IOException e) {
            e.printStackTrace();
        }


        findViewById(R.id.sendBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                if (pairedDevices.isEmpty()){
                    Toast.makeText(getBaseContext(), "No Device yet", Toast.LENGTH_SHORT).show();
                }else {


                    if (lcdText.getText().toString().equals("")){
                        Toast.makeText(LcdActivity.this, "Write Some Txt", Toast.LENGTH_SHORT).show();
                    }else {

                        sendData(lcdText.getText().toString());

                    }
                }


            }
        });




    }



    //todo add when the activity start other dialogue of after to chose the device of connection


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


    public void sendData(String data){

        secondConnection.write(data.getBytes());

    }

       // for show the dialogue


    public class ProgressTask extends AsyncTask<Void, Void, Void> {

        /** progress dialog to show user that the backup is processing. */


        public ProgressTask(LcdActivity activity, BluetoothDevice device) {
            dialog = new ProgressDialog(activity);
            dialog.setCancelable(true);

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
            if (mmSocket!=null) {
                mmSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
