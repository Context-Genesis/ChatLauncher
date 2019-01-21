package com.contextgenesis.chatlauncher.models.chat;

import com.stfalcon.chatkit.commons.models.IUser;

public class ChatUser implements IUser {
    private final String id;
    private final String name;
    private final String avatar;
    private final boolean online;

    public ChatUser(String id, String name, String avatar, boolean online) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.online = online;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public boolean isOnline() {
        return online;
    }
}
