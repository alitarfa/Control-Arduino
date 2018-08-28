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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class DoorActivity extends AppCompatActivity {

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


    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door);
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

        displayCodePin= (TextView) findViewById(R.id.code_door_txt);
        deleteFromTheDisplay= (ImageView) findViewById(R.id.deleteCodePin);
        // for delete the code from the display

        deleteFromTheDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // put all your code here for delete the  one by one ^_^

                if (!displayCodePin.equals("")){
                    if ((displayCodePin.getText().toString().length()>0)){
                       displayCodePin.setText(displayCodePin.getText().toString().substring(0,displayCodePin.getText().toString().length()-1));
                       doorCode=displayCodePin.getText().toString();
                    }
                }

            }
        });





        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // delete for the code for one press

        deleteFromTheDisplay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                // put all you code here

                displayCodePin.setText("");
                doorCode="";

                return true;
            }
        });

        // get the light in the top
        lamb= (ImageView) findViewById(R.id.imageView);

        preferences=getPreferences(MODE_PRIVATE);


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

                new  ProgressTask(this,getPairedDevices()).execute();
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

                    one();

                }


            }
        });




    }

    // for the codePin in the  play ^^
     public void writeInPlay(String value){
         displayCodePin.setText(displayCodePin.getText()+value);
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



// TODO: 7/23/17 don't forget to add the slid for the application in the first page when it luanche for the fist time


    public void offOnBtn(int s){


        // when value = true (on)
        if (value==1){
            // change the image
            imageView.setImageResource(R.drawable.ic_power_button_on);
            lamb.setImageResource(R.drawable.ic_closed_door);
            // change the value to true
            value=0;
            // and do what yoy want to do

            // first of all get the value from the prefrences
            Integer integer=new Integer(0);

            secondConnection.write(new byte[]{integer.byteValue()});

            // make the value of the doorvalue null to use it for the second step ^_^


        }else
        {
            // if the value==false
            // change the image
            imageView.setImageResource(R.drawable.ic_power_off);
            lamb.setImageResource(R.drawable.ic_open_door_entrance);
            // change the value
            value=1;
            // do what you want

            // first of all get the value from the prefrences
            Integer integer=new Integer(s);

            secondConnection.write(new byte[]{integer.byteValue()});
            // make the value of the door is null for the second use ;
            doorCode="";

        }



    }



// todo first add the fuction for the all btn when the user compler his code push the btn for open else message appear for infor the user that no code yet


    public void creatCodeBtn(View view){

        Button button=(Button)view;
        String code=button.getText().toString();
        doorCode=doorCode+code;
        writeInPlay(button.getText().toString());


    }

    public void one(){

        Toast.makeText(this, doorCode, Toast.LENGTH_SHORT).show();

        if (!doorCode.equals("")){

            // first convert the string to integer
            int s = Integer.parseInt(doorCode);
            // call the method of the send data
            // for send data to this method we use array to con

              offOnBtn(s);

        }



    }






    // for show the dialogue


    public class ProgressTask extends AsyncTask<Void, Void, Void> {

        /** progress dialog to show user that the backup is processing. */


        public ProgressTask(DoorActivity activity,BluetoothDevice device) {
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
            if (mmSocket!=null) {
                mmSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

