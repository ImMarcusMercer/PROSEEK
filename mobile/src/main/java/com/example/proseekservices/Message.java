package com.example.proseekservices;
public class Message
{
    public String recipientuserID;
    public String senderId;
    public String text;
    public long timestamp;
    public String senderName;
    public Message() {}
    public Message(String recipientuserID, String senderId, String text, long timestamp)
    {
        this.recipientuserID = recipientuserID;
        this.senderId = senderId;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;

    }

}



