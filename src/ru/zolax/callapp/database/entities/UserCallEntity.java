package ru.zolax.callapp.database.entities;

import java.sql.Timestamp;
import java.util.Date;

public class UserCallEntity {
    private int id;
    private int userIdFK;
    private int callIdFK;
    private Timestamp payDate;
    private boolean isPaid;

    public UserCallEntity(int id, int userIdFK, int callIdFK, Timestamp payDate, boolean isPaid) {
        this.id = id;
        this.userIdFK = userIdFK;
        this.callIdFK = callIdFK;
        this.payDate = payDate;
        this.isPaid = isPaid;
    }



    public UserCallEntity(int userIdFK, int callIdFK, boolean isPaid) {
        this.id = -1;
        this.userIdFK = userIdFK;
        this.callIdFK = callIdFK;
        this.payDate = new Timestamp(new Date().getTime());
        this.isPaid = isPaid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserIdFK() {
        return userIdFK;
    }

    public void setUserIdFK(int userIdFK) {
        this.userIdFK = userIdFK;
    }

    public int getCallIdFK() {
        return callIdFK;
    }

    public void setCallIdFK(int callIdFK) {
        this.callIdFK = callIdFK;
    }

    public Timestamp getPayDate() {
        return payDate;
    }

    public void setPayDateInMillis(long millis) {
        this.payDate = new Timestamp(millis);
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    @Override
    public String toString() {
        return "UserCallEntity{" +
                "id=" + id +
                ", userIdFK=" + userIdFK +
                ", callIdFK=" + callIdFK +
                ", payDate=" + payDate +
                ", isPaid=" + isPaid +
                '}';
    }
}
