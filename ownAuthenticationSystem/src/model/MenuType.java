package model;

public class MenuType {
    private long menuTypeId;
    private String menuType;

    public MenuType(long menuTypeId, String menuType) {
        this.menuTypeId = menuTypeId;
        this.menuType = menuType;
    }

    // Getters and Setters
    public long getMenuTypeId() {
        return menuTypeId;
    }

    public void setMenuTypeId(long menuTypeId) {
        this.menuTypeId = menuTypeId;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }
}
