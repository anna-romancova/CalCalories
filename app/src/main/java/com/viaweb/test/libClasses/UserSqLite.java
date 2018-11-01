package com.viaweb.test.libClasses;

import edu.itstap.calculator.User;

public class UserSqLite {
    private User user;
    private boolean hasMenu;
    private boolean hasHistory;


    public UserSqLite(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isHasMenu() {
        return hasMenu;
    }

    public void setHasMenu(boolean hasMenu) {
        this.hasMenu = hasMenu;
    }

    public boolean isHasHistory() {
        return hasHistory;
    }

    public void setHasHistory(boolean hasHistory) {
        this.hasHistory = hasHistory;
    }

    @Override
    public String toString() {
        return "UserSqLite" +
                "user=" + user +
                ", hasMenu=" + hasMenu +
                ", hasHistory=" + hasHistory ;
    }
}
