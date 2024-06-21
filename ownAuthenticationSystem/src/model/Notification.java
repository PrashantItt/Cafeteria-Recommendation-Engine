package model;

public class Notification {
    private long notificationId;
    private String message;
    private String date;

    public Notification(long notificationId, String message, String date) {
        this.notificationId = notificationId;
        this.message = message;
        this.date = date;
    }

    // Getters and Setters
    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

