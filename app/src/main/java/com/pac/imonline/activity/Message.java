package com.pac.imonline.activity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Message {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String text;
    private String avatar;
    private String sender;
    private String reply;
    private String timestamp;
    private boolean isReply;
    private long chatId;


    public Message(long id, String text, String avatar, String sender,String reply, String timestamp, boolean isReply, long chatId) {
        this.id = id;
        this.text = text;
        this.avatar = avatar;
        this.sender = sender;
        this.timestamp = timestamp;
        this.isReply = isReply;
        this.reply = reply;
        this.chatId = chatId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReply(){return reply;}
    public void setReply(String reply){this.reply= reply;}

    public String getTimestamp() {return timestamp;}

    public String setTimestamp(String timestamp) {this.timestamp = timestamp;
        return timestamp;
    }

    public boolean isReply() {
        return isReply;
    }
    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }
}
