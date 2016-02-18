package com.example.satu.obiektysportowe.Data;

import java.io.Serializable;

/**
 * Created by Bartek on 2015-06-03.
 */
public class Adress implements Serializable {
    String City;
    String Street;
    Double longitude;
    Double latitude;

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
