package com.just.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.companion.AssociationRequest;
import android.companion.BluetoothDeviceFilter;
import android.companion.CompanionDeviceManager;
import android.content.Context;
import android.content.IntentSender;
import android.util.Log;

import java.util.Hashtable;
import java.util.Set;

public class JustBluetooth {

    private static final String TAG = "JustBluetooth";

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


    private final int SELECT_DEVICE_REQUEST_CODE = 77;
    public void pairWithDevice(String macAddress) {
        BluetoothDeviceFilter deviceFilter = new BluetoothDeviceFilter.Builder()
                // Match only Bluetooth devices whose name matches the pattern.
//                .setNamePattern(Pattern.compile("My device"))
                .setAddress(macAddress)
                // Match only Bluetooth devices whose service UUID matches this pattern.
//                .addServiceUuid(new ParcelUuid(new UUID(0x123abcL, -1L)), null)
                .build();

        AssociationRequest pairingRequest = new AssociationRequest.Builder()
                // Find only devices that match this request filter.
                .addDeviceFilter(deviceFilter)
                // Stop scanning as soon as one device matching the filter is found.
                .setSingleDevice(true)
                .build();

        CompanionDeviceManager deviceManager = (CompanionDeviceManager) _activity.getSystemService(Context.COMPANION_DEVICE_SERVICE);
        deviceManager.associate(pairingRequest, new CompanionDeviceManager.Callback() {
            // Called when a device is found. Launch the IntentSender so the user can
            // select the device they want to pair with.
            @Override
            public void onDeviceFound(IntentSender chooserLauncher) {
                try {
                    _activity.startIntentSenderForResult(
                            chooserLauncher, SELECT_DEVICE_REQUEST_CODE, null, 0, 0, 0
                    );
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Failed to send intent");
                }
            }

            @Override
            public void onFailure(CharSequence error) {
                Log.e(TAG, "Failed to send intent");
                // Handle the failure.
            }
        }, null);
    }
}

