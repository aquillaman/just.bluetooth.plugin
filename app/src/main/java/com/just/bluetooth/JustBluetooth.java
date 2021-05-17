package com.just.bluetooth;

import android.bluetooth.BluetoothAdapter;

public class JustBluetooth {

    public boolean hasAdapter(){
        return  BluetoothAdapter.getDefaultAdapter() != null;
    }

    public boolean isAdapterEnabled(){
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter != null && adapter.isEnabled();
    }

    public void enableAdapter(){
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && !adapter.isEnabled()){
            adapter.enable();
        }
    }

    public void disableAdapter(){
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()){
            adapter.disable();
        }
    }

    public void startDiscovery(){
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()){
            adapter.startDiscovery();
        }
    }
}

