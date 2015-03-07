package com.pickmeupscotty.android.messages;

import android.location.Location;

import com.pickmeupscotty.android.amqp.Message;

public class PickUpRequest implements Message {
    private double latitude;
    private double longitude;

    public PickUpRequest(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PickUpRequest(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return latitude + ":" + longitude;
    }
}
