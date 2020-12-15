package ru.zolax.callapp.database.entities;

public class UserInformationEntity {
    private int id;
    private String name;
    private int telephoneNumber;
    private String address;
    private String city;
    private int cashInformationFK;

    public UserInformationEntity(int id, String name, int telephoneNumber, String address, String city, int cashInformationFK) {
        this.id = id;
        this.name = name;
        this.telephoneNumber = telephoneNumber;
        this.address = address;
        this.city = city;
        this.cashInformationFK = cashInformationFK;
    }

    public UserInformationEntity(String name, int telephoneNumber, String address, String city, int cashInformationFK) {
        this.id = -1;
        this.name = name;
        this.telephoneNumber = telephoneNumber;
        this.address = address;
        this.city = city;
        this.cashInformationFK = cashInformationFK;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(int telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCashInformationFK() {
        return cashInformationFK;
    }

    public void setCashInformationFK(int cashInformationFK) {
        this.cashInformationFK = cashInformationFK;
    }

    @Override
    public String toString() {
        return "UserInformation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", telephoneNumber=" + telephoneNumber +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", cashInformationFK=" + cashInformationFK +
                '}';
    }
}
