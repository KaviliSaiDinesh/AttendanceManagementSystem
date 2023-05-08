package com.example.attendance;

public class NotificationModel {

    private String title;
    private String message;
    private int regno;
    private String timestamp;

    public NotificationModel(int regno, String title, String message) {
        // Required empty public constructor for Firestore
    }

    public NotificationModel(String title, String message, String timestamp) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
    }

    public NotificationModel(String title, String message, int regno){
        this.title = title;
        this.message = message;
        this.regno = regno;
    }
    public NotificationModel(String title, String message){
        this.title = title;
        this.message = message;

    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}