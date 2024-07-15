package model;



public class Feedback {
    private long id;
    private long menuItemId;
    private String comment;
    private int rating;
    private String date;
    private long userId;


    public Feedback( long menuItemId,long userId, String comment, int rating, String date) {
        this.menuItemId = menuItemId;
        this.comment = comment;
        this.rating = rating;
        this.userId = userId;
        this.date = date;
    }
    public Feedback( long id, long menuItemId,long userId, String comment, int rating, String date) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.comment = comment;
        this.rating = rating;
        this.userId = userId;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserIdId(long id) {
        this.userId = userId;
    }

    public long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", menuItemId=" + menuItemId +
                ", comment='" + comment + '\'' +
                ", rating=" + rating +
                ", date=" + date +
                '}';
    }


}

