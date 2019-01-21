package com.contextgenesis.chatlauncher.models.chat;

import com.stfalcon.chatkit.commons.models.IMessage;

import java.util.Date;

public class ChatMessage implements IMessage {

    private String id;
    private String text;
    private Date createdAt;
    private ChatUser chatUser;

    public ChatMessage(String id, ChatUser chatUser, String text) {
        this(id, chatUser, text, new Date());
    }

    public ChatMessage(String id, ChatUser chatUser, String text, Date createdAt) {
        this.id = id;
        this.text = text;
        this.chatUser = chatUser;
        this.createdAt = new Date(createdAt.getTime());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Date getCreatedAt() {
        return new Date(createdAt.getTime());
    }

    @Override
    public ChatUser getUser() {
        return this.chatUser;
    }

}
