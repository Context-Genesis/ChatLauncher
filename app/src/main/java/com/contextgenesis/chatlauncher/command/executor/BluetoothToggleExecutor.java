package com.contextgenesis.chatlauncher.command.executor;

import android.bluetooth.BluetoothAdapter;

import javax.inject.Inject;

public class BluetoothToggleExecutor extends CommandExecutor {

    @Inject
    public BluetoothToggleExecutor() {
    }

    @Override
    public String execute() {
        BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bAdapter.isEnabled()) {
            bAdapter.disable();
            return "Bluetooth switched off";
        } else {
            bAdapter.enable();
            return "Bluetooth switched on";
        }
    }
}
