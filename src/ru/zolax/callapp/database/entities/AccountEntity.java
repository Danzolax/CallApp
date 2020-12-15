package ru.zolax.callapp.database.entities;

public class AccountEntity {
    private int id;
    private int userFK;
    private String login;
    private String password;
    private boolean isAdmin;

    public AccountEntity(int id, int userFK, String login, String password,boolean isAdmin) {
        this.id = id;
        this.userFK = userFK;
        this.login = login;
        this.password = password;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public AccountEntity(int userFK, String login, String password,boolean isAdmin) {
        this.id = -1;
        this.userFK = userFK;
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public int getUserFK() {
        return userFK;
    }

    public void setUserFK(int userFK) {
        this.userFK = userFK;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "id=" + id +
                ", userFK=" + userFK +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
