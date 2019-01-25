package com.contextgenesis.chatlauncher.manager.input;

import com.contextgenesis.chatlauncher.command.executor.AppLaunchExecutor;
import com.contextgenesis.chatlauncher.command.executor.BluetoothToggleExecutor;
import com.contextgenesis.chatlauncher.command.executor.CallExecutor;
import com.contextgenesis.chatlauncher.command.executor.WifiToggleExecutor;
import com.contextgenesis.chatlauncher.events.OutputMessageEvent;
import com.contextgenesis.chatlauncher.rx.RxBus;

import javax.inject.Inject;

/**
 * Parses the input typed in the by the user and further processes it and send it to the various
 * managers
 */
public class InputManager {

    @Inject
    RxBus rxBus;
    @Inject
    AppLaunchExecutor appLaunchExecutor;
    @Inject
    CallExecutor callExecutor;
    @Inject
    WifiToggleExecutor wifiToggleExecutor;
    @Inject
    BluetoothToggleExecutor bluetoothToggleExecutor;

    @Inject
    public InputManager() {
    }

    public void executeInput(String input) {
        InputMessage inputMessage = new InputMessage(input);
        switch (inputMessage.getCommandType()) {
            case LAUNCH_APP:
                appLaunchExecutor.setInputMessage(inputMessage);
                appLaunchExecutor.execute();
                break;
            case WIFI_TOGGLE:
                wifiToggleExecutor.execute();
                break;
            case BLUETOOTH_TOGGLE:
                bluetoothToggleExecutor.execute();
                break;
            case CALL:
                callExecutor.setInputMessage(inputMessage);
                callExecutor.execute();
                break;
            default:
                rxBus.post(new OutputMessageEvent("So, here's the thing. I'm sort of dumb right now."));
                break;
        }
    }

}
