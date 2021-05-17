package com.just.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import java.util.Hashtable;
import java.util.Set;

public class JustBluetooth {

    private final Hashtable<String, BluetoothDevice> pairedDevices = new Hashtable<>();
    private final JustPlayerActivity _activity;

    public JustBluetooth(JustPlayerActivity activity) {

        _activity = activity;
    }

    public boolean hasAdapter() {
        return BluetoothAdapter.getDefaultAdapter() != null;
    }

    public boolean isAdapterEnabled() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter != null && adapter.isEnabled();
    }

    public void enableAdapter() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && !adapter.isEnabled()) {
            adapter.enable();
        }
    }

    public void disableAdapter() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            adapter.disable();
        }
    }

    public void startDiscovery() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled() && !adapter.isDiscovering()) {
            adapter.startDiscovery();
        }
    }

    public void cancelDiscovery() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && adapter.isDiscovering()) {
            adapter.cancelDiscovery();
        }
    }

    public String[] getPairedDevices() {

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            Set<BluetoothDevice> bondedDevices = adapter.getBondedDevices();

            if (bondedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : bondedDevices) {
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress(); // MAC address

                    String key = String.format("%s [%s]", deviceHardwareAddress, deviceName);
                    pairedDevices.put(key, device);
                }
            }
        }

        return pairedDevices.keySet().toArray(new String[0]);
    }

    public void requestDiscoverable() {
        _activity.requestDiscoverable();
    }

    public String[] getDeviceList() {
        return _activity.getDeviceList();
    }
}

