package model;

import java.sql.Timestamp;

public class UserActivity {
    private int id;
    private long userId;
    private Timestamp loginTime;
    private Timestamp logOutTime;
    public UserActivity(Long userId) {
        this.userId = userId;
    }
    public UserActivity(int id, long userId, Timestamp loginTime, Timestamp logOutTime) {
        this.id = id;
        this.userId = userId;
        this.loginTime = loginTime;
        this.logOutTime = logOutTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }

    public Timestamp getLogOutTime() {
        return logOutTime;
    }

    public void setLogOutTime(Timestamp logOutTime) {
        this.logOutTime = logOutTime;
    }

}
