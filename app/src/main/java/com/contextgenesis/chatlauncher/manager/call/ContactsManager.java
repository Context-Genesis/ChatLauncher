package com.contextgenesis.chatlauncher.manager.call;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;

import com.contextgenesis.chatlauncher.events.PermissionsEvent;
import com.contextgenesis.chatlauncher.rx.RxBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import static com.contextgenesis.chatlauncher.events.PermissionsEvent.Type.REQUEST;

public class ContactsManager {

    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    @Inject
    Context context;
    @Inject
    RxBus rxBus;

    private List<ContactInfo> contacts;

    @Inject
    public ContactsManager() {
    }

    public boolean isContactsPermissionsGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    /**
     * If we don't have the permission to read contacts, it means the contact is valid; we have
     * no way to really know
     */
    public boolean isContactNameValid(String name) {
        if (!isContactsPermissionsGranted()) {
            return true;
        }
        for (ContactInfo contactInfo : getContacts()) {
            if (contactInfo.getContactName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public List<ContactInfo> getContacts() {
        if (!isContactsPermissionsGranted()) {
            return new ArrayList<>();
        }
        if (contacts == null) {
            contacts = getContactsFromCursor();
        }
        return contacts;
    }

    @SuppressWarnings({"PMD.AvoidDeeplyNestedIfStmts", "PMD.ConfusingTernary"})
    private List<ContactInfo> getContactsFromCursor() {
        List<ContactInfo> contacts = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, null);
        if (cursor != null) {
            try {
                final int contactIdIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                final int displayNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                long contactId;
                String displayName, contactNumber;
                while (cursor.moveToNext()) {
                    contactId = cursor.getLong(contactIdIndex);
                    displayName = cursor.getString(displayNameIndex);
                    contactNumber = cursor.getString(numberIndex);
                    contacts.add(new ContactInfo(contactId, displayName, contactNumber));
                }
            } finally {
                cursor.close();
            }
        }
        return contacts;
    }

    @Nullable
    public ContactInfo getContact(String nameOrNumber) {
        if (!isContactsPermissionsGranted()) {
            return null;
        }
        for (ContactInfo contactInfo : getContacts()) {
            if (contactInfo.getContactName().equalsIgnoreCase(nameOrNumber)
                    || contactInfo.getPhoneNumber().equalsIgnoreCase(nameOrNumber)) {
                return contactInfo;
            }
        }
        return null;
    }

    public void fetchContactsPermissions() {
        // send an event and wait until we receive a granted/denied event
        PermissionsEvent permissionsEvent = Observable.create((ObservableOnSubscribe<PermissionsEvent>) emitter -> {
            rxBus.register(PermissionsEvent.class)
                    .filter(event -> event.getPermissions().equals(Manifest.permission.READ_CONTACTS))
                    .filter(event -> event.getType() != REQUEST)
                    .subscribe(emitter::onNext);
            rxBus.post(new PermissionsEvent(Manifest.permission.READ_CONTACTS, REQUEST));
        })
                .blockingFirst();
    }
}
