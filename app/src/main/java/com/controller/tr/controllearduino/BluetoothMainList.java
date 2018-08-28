package com.controller.tr.controllearduino;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothMainList extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    ProgressDialog dialog;
    private static final int REQUEST_ENABLE_BT = 0;
    ArrayList<BluetoothDevice> listDevice=new ArrayList<>();
    ListView listView;
    Button pairBtn;
    ListDeviceAdapter listDeviceAdapter;
    RecyclerView recyclerView;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_main_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // for the on

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // to display the arrow of back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 startDescovery();

            }
        });



        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);


           registerReceiver(mPairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));

            recyclerView= (RecyclerView) findViewById(R.id.rec);

            listDeviceAdapter=new  ListDeviceAdapter(this,listDevice);


        // progress

                dialog = new ProgressDialog(this);
                dialog.setMessage("Scanning Device ...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setCancelable(false);


                // for the bluetooth
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth
                }




        // get the all device paired if it's present
        getPairedDevices();


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void getPairedDevices(){
        // get all paired device and display it into the Recycle View
        // id the user press serche btn then indicate to get the other device
        // and add it to the list od the recycle view
        //for the recycle if the device is paired show the other btn of unpaired
        //

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            listDevice.addAll(pairedDevices);
            addAllDevice(listDevice);

        }else {
            Toast.makeText(this, "No Paired device", Toast.LENGTH_SHORT).show();
        }

    }


    public void startDescovery() {

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            mBluetoothAdapter.startDiscovery();

        } else {
            if (mBluetoothAdapter.isDiscovering()) {

                mBluetoothAdapter.cancelDiscovery();

            }

        }

        mBluetoothAdapter.startDiscovery();
        // Register for broadcasts when a device is discovered.

    }


    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
            listDeviceAdapter.notifyDataSetChanged();
            addAllDevice(listDevice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
            listDeviceAdapter.notifyDataSetChanged();
            addAllDevice(listDevice);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                // the descvery start
                dialog.show();

            }else {

                if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){

                    // the descovry finishied

                    dialog.dismiss();
                    addAllDevice(listDevice);
                    Toast.makeText(context, ""+listDevice.size(), Toast.LENGTH_SHORT).show();

                }else {

                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        // Discovery has found a device. Get the BluetoothDevice
                        // object and its info from the Intent.
                        //BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                         mBluetoothAdapter.cancelDiscovery();
                         dialog.dismiss();
                         BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                        listDevice.add(device);


                    }
                }
            }

        }
    };



     public void addAllDevice(ArrayList<BluetoothDevice> listDevice){

             ListDeviceAdapter listDeviceAdapter=new ListDeviceAdapter(this,listDevice);

             recyclerView.setAdapter(listDeviceAdapter);
             recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));

             listDeviceAdapter.notifyDataSetChanged();
     }





    // reciver for The pair Mode

    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state 		= intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    Toast.makeText(context, "Paired", Toast.LENGTH_SHORT).show();
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){

                }


            }
        }
    };



    public class ListDeviceAdapter extends RecyclerView.Adapter<ListDeviceAdapter.Holder>{

        private LayoutInflater mInflater;
        private List<BluetoothDevice> mData;
        Context context;


        public ListDeviceAdapter(Context context, ArrayList<BluetoothDevice> bluetoothDevices) {
            mInflater = LayoutInflater.from(context);
            this.mData=bluetoothDevices;
            this.context=context;
        }


        @Override
        public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View view =mInflater.inflate(R.layout.list_item_device,viewGroup,false);

            return new Holder(view);

        }

        @Override
        public void onBindViewHolder(final Holder holder, final int i) {

            holder.nameTv.setText(mData.get(i).getName());
            holder.addressTv.setText(mData.get(i).getAddress());
            // check if the device is paired or no

            if (mData.get(i).getBondState()==BluetoothDevice.BOND_BONDED){
                   holder.pairBtn.setText("UnPaired");

                holder.pairBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        unpairDevice(mData.get(i));
                        holder.pairBtn.setText("Pair");
                        notifyDataSetChanged();


                    }
                });

            }else {
                   holder.pairBtn.setText("pair");
                holder.pairBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        pairDevice(mData.get(i));
                        holder.pairBtn.setText("UnPair");
                        notifyDataSetChanged();



                    }
                });

            }


        }


        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }



        public class Holder extends RecyclerView.ViewHolder {
            TextView nameTv;
            TextView addressTv;
            Button pairBtn;

            public Holder(View itemView) {
                super(itemView);
                nameTv= (TextView) itemView.findViewById(R.id.tv_name);
                addressTv= (TextView) itemView.findViewById(R.id.tv_address);
                pairBtn= (Button) itemView.findViewById(R.id.btn_pair);


            }
        }

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
        unregisterReceiver(mPairReceiver);
    }



}
