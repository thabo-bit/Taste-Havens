package com.example.tastehavens;

public class Reservation_Details {
    String time ;
    String date;
    String quest;
    String Phone_number;
    String reserve_Name;
    String Table_Number;
    String Accpetance;
    String Reason;

    public Reservation_Details(String date, String time, String phone_number, String quest, String reserve_Name, String table_Number, String accpetance, String reason) {
        this.date = date;
        this.time = time;
        Phone_number = phone_number;
        this.quest = quest;
        this.reserve_Name = reserve_Name;
        Table_Number = table_Number;
        Accpetance = accpetance;
        Reason = reason;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuest() {
        return quest;
    }

    public void setQuest(String quest) {
        this.quest = quest;
    }

    public String getPhone_number() {
        return Phone_number;
    }

    public void setPhone_number(String phone_number) {
        Phone_number = phone_number;
    }

    public String getReserve_Name() {
        return reserve_Name;
    }

    public String getTable_Number() {
        return Table_Number;
    }

    public void setTable_Number(String table_Number) {
        Table_Number = table_Number;
    }

    public String getAccpetance() {
        return Accpetance;
    }

    public void setAccpetance(String accpetance) {
        Accpetance = accpetance;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public void setReserve_Name(String reserve_Name) {
        this.reserve_Name = reserve_Name;
    }
}
