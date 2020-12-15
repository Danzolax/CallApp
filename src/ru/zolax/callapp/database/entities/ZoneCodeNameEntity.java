package ru.zolax.callapp.database.entities;

public class ZoneCodeNameEntity {
    private int zoneCode;
    private String zoneName;

    public ZoneCodeNameEntity(int zoneCode, String zoneName) {
        this.zoneCode = zoneCode;
        this.zoneName = zoneName;
    }

    public int getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(int zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    @Override
    public String toString() {
        return "ZoneCodeName{" +
                "zoneCode=" + zoneCode +
                ", zoneName='" + zoneName + '\'' +
                '}';
    }
}
