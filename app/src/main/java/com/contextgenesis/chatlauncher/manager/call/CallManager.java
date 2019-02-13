package com.contextgenesis.chatlauncher.manager.call;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import com.contextgenesis.chatlauncher.events.PermissionsEvent;
import com.contextgenesis.chatlauncher.rx.RxBus;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import static com.contextgenesis.chatlauncher.events.PermissionsEvent.Type.REQUEST;

public class CallManager {

    @Inject
    Context context;
    @Inject
    ContactsManager contactsManager;
    @Inject
    RxBus rxBus;

    @Inject
    public CallManager() {
    }

    public boolean isCallPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public boolean call(String numberOrContact) {
        String number = getNumberFromContact(numberOrContact);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setData(Uri.parse("tel:" + number));
        if (isCallPermissionGranted()) {
            context.startActivity(callIntent);
            return true;
        } else {
            return false;
        }
    }

    private String getNumberFromContact(String input) {
        if (StringUtils.isNumeric(input)) {
            return input;
        }
        ContactInfo contactInfo = contactsManager.getContact(input);
        if (contactInfo == null) {
            return input;
        }
        return contactInfo.getPhoneNumber();
    }


    public void fetchCallingPermissions() {
        // send an event and wait until we receive a granted/denied event
        PermissionsEvent permissionsEvent = Observable.create((ObservableOnSubscribe<PermissionsEvent>) emitter -> {
            rxBus.register(PermissionsEvent.class)
                    .filter(event -> event.getPermissions().equals(Manifest.permission.CALL_PHONE))
                    .filter(event -> event.getType() != REQUEST)
                    .subscribe(emitter::onNext);
            rxBus.post(new PermissionsEvent(Manifest.permission.CALL_PHONE, REQUEST));
        })
                .blockingFirst();
    }

}
