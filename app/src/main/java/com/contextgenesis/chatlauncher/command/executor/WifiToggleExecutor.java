package com.contextgenesis.chatlauncher.command.executor;

import android.content.Context;
import android.net.wifi.WifiManager;

import javax.inject.Inject;

public class WifiToggleExecutor extends CommandExecutor {

    @Inject
    Context context;

    @Inject
    public WifiToggleExecutor() {
    }

    @Override
    public void execute() {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled()) {
            wifi.setWifiEnabled(false);
            postOutput("Wifi switched off");
        } else {
            wifi.setWifiEnabled(true);
            postOutput("Wifi switched on");
        }
    }
}
