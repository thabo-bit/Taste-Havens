package com.example.tastehavens;

public class ReservationDetails {
    private String reserveDate;
    private String reserveTime;
    private String reserveNumber;
    private String reserveGuests;
    private String reserveCusName;
    private String reserveTableNumber;
    private String documentId;

    public ReservationDetails(String reserveDate, String reserveTime, String reserveNumber,
                              String reserveGuests, String reserveCusName, String reserveTableNumber,
                              String documentId) {
        this.reserveDate = reserveDate;
        this.reserveTime = reserveTime;
        this.reserveNumber = reserveNumber;
        this.reserveGuests = reserveGuests;
        this.reserveCusName = reserveCusName;
        this.reserveTableNumber = reserveTableNumber;
        this.documentId = documentId;
    }

    // Getter methods
    public String getReserveDate() { return reserveDate; }
    public String getReserveTime() { return reserveTime; }
    public String getReserveNumber() { return reserveNumber; }
    public String getReserveGuests() { return reserveGuests; }
    public String getReserveCusName() { return reserveCusName; }
    public String getReserveTableNumber() { return reserveTableNumber; }
    public String getDocumentId() { return documentId; }
}