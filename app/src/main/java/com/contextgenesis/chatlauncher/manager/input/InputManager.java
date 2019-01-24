package com.contextgenesis.chatlauncher.manager.input;

import com.contextgenesis.chatlauncher.command.executor.AppLaunchExecutor;
import com.contextgenesis.chatlauncher.command.executor.BluetoothToggleExecutor;
import com.contextgenesis.chatlauncher.command.executor.WifiToggleExecutor;

import javax.inject.Inject;

/**
 * Parses the input typed in the by the user and further processes it and send it to the various
 * managers
 */
public class InputManager {

    @Inject
    AppLaunchExecutor appLaunchExecutor;

    @Inject
    WifiToggleExecutor wifiToggleExecutor;

    @Inject
    BluetoothToggleExecutor bluetoothToggleExecutor;

    @Inject
    public InputManager() {
    }

    public String executeInput(String input) {
        InputMessage inputMessage = new InputMessage(input);
        switch (inputMessage.getCommandType()) {
            case LAUNCH_APP:
                appLaunchExecutor.setInputMessage(inputMessage);
                return appLaunchExecutor.execute();
            case WIFI_TOGGLE:
                return wifiToggleExecutor.execute();
            case BLUETOOTH_TOGGLE:
                return bluetoothToggleExecutor.execute();
            default:
                return "So, here's the thing. I'm sort of dumb right now.";
        }
    }

}
