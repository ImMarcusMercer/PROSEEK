package com.example.proseekservices;
public class Review {
    private String senderId;
    private String senderName;
    private String comment;
    private long timestamp;
    private float rating;

    // Add constructors, getters and setters

    public Review() {}

    public Review(String senderId, String senderName, String comment, long timestamp, float rating) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.comment = comment;
        this.timestamp = timestamp;
        this.rating = rating;
    }

    public String getSenderName(){
        return this.senderName;
    }

    public Object getRating() {
        return this.rating;
    }

    public String getComment() {
        return this.comment;
    }
}
