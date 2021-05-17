package com.just.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.unity3d.player.UnityPlayerActivity;

import java.util.Hashtable;

public class JustPlayerActivity extends UnityPlayerActivity {
    private static final String TAG = "Just";

    private JustBluetooth _bluetoothPlugin;
    public JustBluetooth getBluetoothPlugin(){
        return _bluetoothPlugin = new JustBluetooth(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Activity created");
        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "Received intent " + action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                Log.i(TAG, "Add intent " + action);
                String key = String.format("%s [%s]", deviceHardwareAddress, deviceName);
                discoveredDevices.put(key, device);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }

    private Hashtable<String, BluetoothDevice> discoveredDevices = new Hashtable<>();

    public void requestDiscoverable() {
//        Manifest.permission.ACCESS_FINE_LOCATION
        discoveredDevices.clear();
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(intent);
    }

    public String[] getDeviceList() {
        return discoveredDevices.keySet().toArray(new String[0]);
    }
}
