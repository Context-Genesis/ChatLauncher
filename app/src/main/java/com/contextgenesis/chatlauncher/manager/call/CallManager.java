package com.contextgenesis.chatlauncher.manager.call;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import javax.inject.Inject;

public class CallManager {

    @Inject
    Context context;

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
        callIntent.setData(Uri.parse("tel:" + number));
        if (isCallPermissionGranted()) {
            context.startActivity(callIntent);
            return true;
        } else {
            return false;
        }
    }

    private String getNumberFromContact(String input) {
        return input;
    }

}
