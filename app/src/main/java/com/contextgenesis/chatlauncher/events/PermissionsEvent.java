package com.contextgenesis.chatlauncher.events;

import android.content.pm.PackageManager;

import androidx.annotation.Nullable;
import lombok.Getter;

@Getter
public class PermissionsEvent extends Event {

    private final String permissions;
    private final Type type;

    public PermissionsEvent(String permission, Type type) {
        this.permissions = permission;
        this.type = type;
    }

    public enum Type {
        REQUEST,
        GRANTED,
        DENIED;

        @Nullable
        public static Type get(int grantResult) {
            switch (grantResult) {
                case PackageManager.PERMISSION_GRANTED:
                    return GRANTED;
                case PackageManager.PERMISSION_DENIED:
                    return DENIED;
            }
            return null;
        }
    }

}
