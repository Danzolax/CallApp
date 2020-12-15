package ru.zolax.callapp.database.entities;


import java.sql.Timestamp;
import java.util.Date;

public class CashInformationEntity {
    private int id;
    private double balance;
    private Timestamp additionDate;
    public CashInformationEntity(int id, double balance) {
        this.id = id;
        this.balance = balance;
        this.additionDate = new Timestamp(new Date().getTime());
    }

    public CashInformationEntity(double balance) {
        this.id = -1;
        this.balance = balance;
        this.additionDate = new Timestamp(new Date().getTime());
    }

    public CashInformationEntity(int id, double balance, Timestamp additionDate) {
        this.id = id;
        this.balance = balance;
        this.additionDate = additionDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Timestamp getAdditionDate() {
        return additionDate;
    }

    public void setAdditionDateInMillis(long millis) {
        this.additionDate = new Timestamp(millis);
    }

    @Override
    public String toString() {
        return "CashInformation{" +
                "id=" + id +
                ", balance=" + balance +
                ", additionDate=" + additionDate +
                '}';
    }
}
