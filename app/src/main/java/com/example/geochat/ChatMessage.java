package com.example.geochat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private String messageTime;

    public ChatMessage(String messageText, String messageUser, String fecha) {
        this.messageText = messageText;
        this.messageUser = messageUser;

        // Inizializar a hora actual
        messageTime = fecha;
        ;
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}