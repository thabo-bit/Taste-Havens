package com.example.tastehavens;

public class Reserve_Admin {
    String reserveDate;
    String reserveTime;
    String reserveNumber;
    String reserveCusName;
    String reserveGuests;
    String reserveTableNumber;

    public Reserve_Admin() {
    }


    public String getReserveDate() {
        return reserveDate;
    }

    public Reserve_Admin(String reserveDate, String reserveTime, String reserveNumber, String reserveCusName, String reserveGuests, String reserveTableNumber) {
        this.reserveDate = reserveDate;
        this.reserveTime = reserveTime;
        this.reserveNumber = reserveNumber;
        this.reserveCusName = reserveCusName;
        this.reserveGuests = reserveGuests;
        this.reserveTableNumber = reserveTableNumber;
    }

    public void setReserveDate(String reserveDate) {
        this.reserveDate = reserveDate;
    }

    public String getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }

    public String getReserveNumber() {
        return reserveNumber;
    }

    public void setReserveNumber(String reserveNumber) {
        this.reserveNumber = reserveNumber;
    }

    public String getReserveCusName() {
        return reserveCusName;
    }

    public void setReserveCusName(String reserveCusName) {
        this.reserveCusName = reserveCusName;
    }

    public String getReserveTableNumber() {
        return reserveTableNumber;
    }

    public void setReserveTableNumber(String reserveTableNumber) {
        this.reserveTableNumber = reserveTableNumber;
    }

    public String getReserveGuests() {
        return reserveGuests;
    }

    public void setReserveGuests(String reserveGuests) {
        this.reserveGuests = reserveGuests;
    }
}
