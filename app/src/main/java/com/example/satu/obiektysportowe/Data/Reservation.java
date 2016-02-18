package com.example.satu.obiektysportowe.Data;

import java.io.Serializable;
import java.util.Date;

public class Reservation implements Serializable {
    private String id;
    private Date start;
    private Date end;
    private Date reservationTime;
    private Date updatedReservationTime;
    private  String spotrObiectId;
    private String objectName;
    private int cost;


    public Reservation(String id, Date start, Date end, Date reservationTime, Date updatedReservationTime, String spotrObiectId, int cost) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.reservationTime = reservationTime;
        this.updatedReservationTime = updatedReservationTime;
        this.spotrObiectId = spotrObiectId;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getSpotrObiectId() {
        return spotrObiectId;
    }

    public void setSpotrObiectId(String spotrObiectId) {
        this.spotrObiectId = spotrObiectId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Reservation(String id) {
        this.id = id;
    }

    public Reservation(String id, Date start, Date end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public Reservation(String id, Date start, Date end, Date reservationTime, Date updatedReservationTime) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.reservationTime = reservationTime;
        this.updatedReservationTime = updatedReservationTime;
    }

    public Date getUpdatedReservationTime() {
        return updatedReservationTime;
    }

    public void setUpdatedReservationTime(Date updatedReservationTime) {
        this.updatedReservationTime = updatedReservationTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(Date reservationTime) {
        this.reservationTime = reservationTime;
    }


}
