package ru.zolax.callapp.database.entities;


import java.sql.Timestamp;
import java.util.Date;

public class CallInformationEntity {
    private int id;
    private int rateIdFK;
    private int duration;
    private Timestamp date;
    private double price;

    public CallInformationEntity(int id, int rateIdFK, int duration, Timestamp date, double price) {
        this.id = id;
        this.rateIdFK = rateIdFK;
        this.duration = duration;
        this.date = date;
        this.price = price;
    }

    public CallInformationEntity(int rateIdFK, int duration, double price) {
        this.id = -1;
        this.rateIdFK = rateIdFK;
        this.duration = duration;
        this.date = new Timestamp(new Date().getTime());
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRateIdFK() {
        return rateIdFK;
    }

    public void setRateIdFK(int rateIdFK) {
        this.rateIdFK = rateIdFK;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDateInMillis(long millis) {
        this.date = new Timestamp(millis);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CallInformationEntity{" +
                "id=" + id +
                ", rateID=" + rateIdFK +
                ", duration=" + duration +
                ", date=" + date +
                ", price=" + price +
                '}';
    }
}
