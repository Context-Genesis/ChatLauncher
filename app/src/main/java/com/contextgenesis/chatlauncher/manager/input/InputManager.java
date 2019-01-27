package com.contextgenesis.chatlauncher.manager.input;

import com.contextgenesis.chatlauncher.command.executor.AliasAddExecutor;
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
    AliasAddExecutor aliasAddExecutor;
    @Inject
    InputParser inputParser;

    @Inject
    public InputManager() {
    }

    public void executeInput(String input) {
        InputMessage inputMessage = inputParser.parse(input);
        if (inputMessage.isValid()) {
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
                case ALIAS_ADD:
                    aliasAddExecutor.setInputMessage(inputMessage);
                    aliasAddExecutor.execute();
                    break;
                default:
                    rxBus.post(new OutputMessageEvent("So, here's the thing. I'm sort of dumb right now."));
                    break;
            }
        } else {
            rxBus.post(new OutputMessageEvent("Bad input message. I'm supposed to have info on why this is dumb, but my makers were lazy."));
        }
    }

}
