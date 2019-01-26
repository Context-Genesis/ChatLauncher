package com.contextgenesis.chatlauncher.manager.call;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ContactsManager {

    @Inject
    Context context;

    private List<ContactInfo> contacts;

    @Inject
    public ContactsManager() {
    }

    public boolean isContactNameValid(String name) {
        for (ContactInfo contactInfo : getContacts()) {
            if (contactInfo.getContactName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public List<ContactInfo> getContacts() {
        if (contacts == null) {
            contacts = getContactsFromCursor();
        }
        return contacts;
    }

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

}