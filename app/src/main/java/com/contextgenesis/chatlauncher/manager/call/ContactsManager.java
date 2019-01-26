package com.contextgenesis.chatlauncher.manager.call;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;

public class ContactsManager {

    @Inject
    Context context;

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

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if ((cursor != null ? cursor.getCount() : 0) > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCursor != null && pCursor.moveToNext()) {
                        String phoneNo = pCursor.getString(pCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contacts.add(new ContactInfo(name, phoneNo));
                    }
                    if (pCursor != null) {
                        pCursor.close();
                    }
                }
            }
        }
        if (cursor != null) {
            cursor.close();
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
}
