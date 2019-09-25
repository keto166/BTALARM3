package com.example.tom.btalarm;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    public static MainActivity instance = null;
    public BluetoothAdapter myBluetooth = null;
    public HashMap<String,String> deviceMap; //<Device Name,Address>
    public ArrayList<String> deviceNames;

    Button buttonDeviceList;
    Button buttonSoundList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniVars();

        if (BTDCListenerService.instance == null) {
            Intent i = new Intent(this.getApplicationContext(), BTDCListenerService.class);
            startService(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void iniVars() {
        if (instance == null) {
            instance = this;
        } else {
            //TODO ERROR, not singleton
        }
        buttonDeviceList = (Button) findViewById(R.id.d_bDeviceList);
        buttonDeviceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBTDevices();
                try {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    DeviceListFragment frag = new DeviceListFragment();
                    ft.replace(R.id.d_listFragment, frag);
                    ft.addToBackStack(null);
                    ft.commit();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonSoundList = (Button) findViewById(R.id.d_bSoundList);
        buttonSoundList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    SoundMenuFragment frag = new SoundMenuFragment();
                    ft.replace(R.id.d_listFragment, frag);
                    ft.addToBackStack(null);
                    ft.commit();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        deviceMap = new HashMap<>();
        deviceNames = new ArrayList<>();
    }

    public void updateBTDevices() {

        if (myBluetooth == null) {
            //TODO bluetooth not found
            Toast.makeText(instance, "No BlueTooth found on device", Toast.LENGTH_LONG).show();
            return;
        }

        if (!myBluetooth.isEnabled()) {
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(i,1);
        }
        try {
            Set<BluetoothDevice> deviceSet = myBluetooth.getBondedDevices();
            String s;
            int x;
            deviceMap.clear();
            for (BluetoothDevice device : deviceSet) {
                if (deviceMap.containsKey(device.getName())) {
                    s = device.getName();
                    x = 2;

                    while (deviceMap.containsKey(s + " - " + x)) {
                        x++;
                    }
                    deviceMap.put(s + " - " + x, device.getAddress());
                } else {
                    deviceMap.put(device.getName(), device.getAddress());
                }
            }
            deviceNames.clear();
            deviceNames.addAll(deviceMap.values());
        } catch (Exception e) {
            Toast.makeText(instance, e.getMessage(), Toast.LENGTH_LONG).show();
        }



    }

}
