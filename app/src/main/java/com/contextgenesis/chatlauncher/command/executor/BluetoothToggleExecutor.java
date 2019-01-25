package com.contextgenesis.chatlauncher.command.executor;

import android.bluetooth.BluetoothAdapter;

import javax.inject.Inject;

public class BluetoothToggleExecutor extends CommandExecutor {

    @Inject
    public BluetoothToggleExecutor() {
    }

    @Override
    public void execute() {
        BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bAdapter.isEnabled()) {
            bAdapter.disable();
            postOutput("Bluetooth switched off");
        } else {
            bAdapter.enable();
            postOutput("Bluetooth switched on");
        }
    }
}
