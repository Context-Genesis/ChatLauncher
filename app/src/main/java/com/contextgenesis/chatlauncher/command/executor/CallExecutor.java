package com.contextgenesis.chatlauncher.command.executor;

import android.Manifest;

import com.contextgenesis.chatlauncher.events.PermissionsEvent;
import com.contextgenesis.chatlauncher.manager.call.CallManager;
import com.contextgenesis.chatlauncher.rx.RxBus;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

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
    public void execute() {
        if (inputMessage.getArgs().length <= 0) {
            postOutput("Tell me who you want to call!");
        }

        String input = (String) inputMessage.getArgs()[0];

        if (!callManager.isCallPermissionGranted()) {
            // send an event and wait until we receive a granted/denied event
            PermissionsEvent permissionsEvent = Observable.create((ObservableOnSubscribe<PermissionsEvent>) emitter -> {
                rxBus.register(PermissionsEvent.class)
                        .filter(event -> event.getPermissions().equals(Manifest.permission.CALL_PHONE))
                        .filter(event -> event.getType() != REQUEST)
                        .subscribe(emitter::onNext);
                rxBus.post(new PermissionsEvent(Manifest.permission.CALL_PHONE, REQUEST));
            })
                    .blockingFirst();
            postOutput("Permission Status: " + permissionsEvent.getType());
        }

        if (callManager.call(input)) {
            postOutput("Calling " + input);
        } else {
            postOutput("Unable to call");
        }
    }
}
