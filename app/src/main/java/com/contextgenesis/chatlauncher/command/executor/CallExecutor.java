package com.contextgenesis.chatlauncher.command.executor;

import android.Manifest;

import com.contextgenesis.chatlauncher.events.PermissionsEvent;
import com.contextgenesis.chatlauncher.manager.call.CallManager;
import com.contextgenesis.chatlauncher.rx.RxBus;

import javax.inject.Inject;

import static com.contextgenesis.chatlauncher.events.PermissionsEvent.Type.REQUEST;

public class CallExecutor extends CommandExecutor {

    @Inject
    CallManager callManager;
    @Inject
    RxBus rxBus;

    @Inject
    public CallExecutor() {
    }

    @Override
    public String execute() {
        if (inputMessage.getArgs().length <= 0) {
            return "Tell me who you want to call!";
        }

        String input = (String) inputMessage.getArgs()[0];

        if (!callManager.isCallPermissionGranted()) {
            rxBus.post(new PermissionsEvent(Manifest.permission.CALL_PHONE, REQUEST));
        }

        if (callManager.call(input)) {
            return "Calling " + input;
        } else {
            return "Unable to call";
        }
    }
}
