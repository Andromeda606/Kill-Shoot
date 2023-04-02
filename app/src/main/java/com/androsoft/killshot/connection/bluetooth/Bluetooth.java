package com.androsoft.killshot.connection.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.androsoft.killshot.connection.ConnectionHelper;
import com.androsoft.killshot.connection.StreamInterface;

import java.util.Set;
import java.util.UUID;

public class Bluetooth implements ConnectionHelper {
    Context context;
    BluetoothAdapter bluetoothAdapter;
    StreamInterface streamInterface;
    private static final UUID MY_UUID_SECURE =
            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    public Bluetooth(Context context){
        this.context = context;
        BluetoothManager bluetoothManager = context.getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //context.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        }
    }


    @Override
    public StreamInterface createConnectedThread() throws Exception {
        try{
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            UUID uuid = UUID.randomUUID();
            bluetoothAdapter.cancelDiscovery();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    UUID deviceUuid = device.getUuids()[0].getUuid(); //device.getUuids().length - 1
                    try{
                        //return new ConnectedThread(device.createRfcommSocketToServiceRecord(deviceUuid));
                    }catch (Exception e){
                        //return new ConnectedThread(device.createInsecureRfcommSocketToServiceRecord(deviceUuid));
                    }

                }
            }
        }catch (Exception e){
            Log.wtf("dasdsa",e.toString());
            throw new Exception(e);
        }


        return null;
    }

    @Override
    public String getDevice() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        //if (pairedDevices.size() > 0) {
        //    Log.wtf("csadas",pairedDevices.toString());
        //    for (BluetoothDevice device : pairedDevices) {
        //        return device.getAddress(); // MAC address
        //    }
        //}
        return null;
    }
}
