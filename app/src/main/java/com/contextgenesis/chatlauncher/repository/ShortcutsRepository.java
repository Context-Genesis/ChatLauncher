package com.contextgenesis.chatlauncher.repository;

import android.annotation.SuppressLint;

import com.contextgenesis.chatlauncher.models.attachments.Attachment;
import com.orhanobut.hawk.Hawk;

import javax.inject.Inject;

/**
 * A wrapper above Hawk following repository pattern.
 * This allows us to later change the underlying implementation of the database if we'd so like.
 * For example, say we have to shift from Hawk to Room, we can do so.
 */
public class ShortcutsRepository {

    @Inject
    public ShortcutsRepository() {
    }

    @SuppressLint("DefaultLocale")
    private String toKey(int id) {
        return String.format("attachment%d", id);
    }

    public void setOption(int key, Attachment attachment) {
        Hawk.put(toKey(key), attachment);
    }

    public boolean isOptionSet(int key) {
        String id = toKey(key);
        return Hawk.contains(id);
    }

    public Attachment getOption(int key) {
        return Hawk.get(toKey(key));
    }

}
