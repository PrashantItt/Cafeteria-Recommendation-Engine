package model;

public class Menu {
    private long menuId;
    private String menuName;
    private long menuTypeId;

    public Menu(long menuId, String menuName, long menuTypeId) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuTypeId = menuTypeId;
    }

    // Getters and Setters
    public long getMenuId() {
        return menuId;
    }

    public void setMenuId(long menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public long getMenuTypeId() {
        return menuTypeId;
    }

    public void setMenuTypeId(long menuTypeId) {
        this.menuTypeId = menuTypeId;
    }
}
