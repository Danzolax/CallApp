package ru.zolax.callapp.database.entities;

public class RateEntity {
    private int id;
    private int userZoneCodeFK;
    private int callZoneCodeFK;
    private double pricePerMinute;

    public RateEntity(int id, int userZoneCodeFK, int callZoneCodeFK, double pricePerMinute) {
        this.id = id;
        this.userZoneCodeFK = userZoneCodeFK;
        this.callZoneCodeFK = callZoneCodeFK;
        this.pricePerMinute = pricePerMinute;
    }

    public RateEntity(int userZoneCodeFK, int callZoneCodeFK, double pricePerMinute) {
        this.id = -1;
        this.userZoneCodeFK = userZoneCodeFK;
        this.callZoneCodeFK = callZoneCodeFK;
        this.pricePerMinute = pricePerMinute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserZoneCodeFK() {
        return userZoneCodeFK;
    }

    public void setUserZoneCodeFK(int userZoneCodeFK) {
        this.userZoneCodeFK = userZoneCodeFK;
    }

    public int getCallZoneCodeFK() {
        return callZoneCodeFK;
    }

    public void setCallZoneCodeFK(int callZoneCodeFK) {
        this.callZoneCodeFK = callZoneCodeFK;
    }

    public double getPricePerMinute() {
        return pricePerMinute;
    }

    public void setPricePerMinute(double pricePerMinute) {
        this.pricePerMinute = pricePerMinute;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "id=" + id +
                ", userZoneCode=" + userZoneCodeFK +
                ", callZoneCode=" + callZoneCodeFK +
                ", pricePerMinute=" + pricePerMinute +
                '}';
    }
}
