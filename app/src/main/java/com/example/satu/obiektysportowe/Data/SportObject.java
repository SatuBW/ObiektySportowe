package com.example.satu.obiektysportowe.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Satu on 2015-05-03.
 */
public class SportObject implements Serializable {
    private String id;
    private String nazwa;
    private String opis;
    private String type;
    private Adress adress;
    private String url;
    private Date startDate ;
    private Date endDate ;

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public SportObject(String id, String nazwa) {
        this.id = id;
        this.nazwa = nazwa;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    private int cena;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Adress getAdres() {
        return adress;
    }

    public void setAdres(Adress adres) {
        adress = adres;
    }

    public SportObject() {
    }


}
