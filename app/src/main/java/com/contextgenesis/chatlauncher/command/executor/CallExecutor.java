package com.contextgenesis.chatlauncher.command.executor;

import com.contextgenesis.chatlauncher.manager.call.CallManager;
import com.contextgenesis.chatlauncher.manager.call.ContactsManager;

import javax.inject.Inject;


public class CallExecutor extends CommandExecutor {

    @Inject
    CallManager callManager;
    @Inject
    ContactsManager contactsManager;

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
            callManager.fetchCallingPermissions();
        }
        if (!contactsManager.isContactsPermissionsGranted()) {
            contactsManager.fetchContactsPermissions();
        }

        if (callManager.call(input)) {
            postOutput("Calling " + input);
        } else {
            postOutput("Unable to call");
        }
    }
}
