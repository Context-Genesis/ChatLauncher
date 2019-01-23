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

    public boolean call(String numberOrContact) {
        String number = getNumberFromContact(numberOrContact);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //   Activity#requestPermissions
                //   here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                //   to handle the case where the user grants the permission. See the documentation
                //   for Activity#requestPermissions for more details.
                return false;
            }
        }
        context.startActivity(callIntent);
        return true;
    }

    private String getNumberFromContact(String input) {
        return input;
    }

}
