package model;

import java.util.Date;

public class Notification {
    private long notificationId;
    private String message;
    private Date date;

    public Notification(long notificationId, String message, Date date) {
        this.notificationId = notificationId;
        this.message = message;
        this.date = date;
    }

    public Notification(String message) {
        this.message = message;

    }

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

