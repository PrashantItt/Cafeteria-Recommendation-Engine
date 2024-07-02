package model;

public class User {
    private long userId;
    private String name;
    private String password;
    private long roleId;

    public User(long userId, String name, String password, long roleId) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.roleId = roleId;
    }

    public User( String name, String password, long roleId) {
        this.name = name;
        this.password = password;
        this.roleId = roleId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}
