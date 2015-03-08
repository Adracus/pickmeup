package com.pickmeupscotty.android.messages;

import android.os.Parcel;
import android.os.Parcelable;
import com.pickmeupscotty.android.amqp.Message;

public class PickUpResponse implements Message, Parcelable {
    public static final String PICK_UP_RESPONSE = "PICK_UP_RESPONSE";
    private String driverFacebookId;
    private String driverName;
    private String eta;
    private double friendLatitude;
    private double friendLongitude;
    private double destinationLatitude;
    private double destinationLongitude;
    private double driverLatitude;
    private double driverLongitude;

    public PickUpResponse() {}

    public PickUpResponse(String driverFacebookId, String driverName, String eta, double friendLatitude, double friendLongitude, double destinationLatitude, double destinationLongitude, double driverLatitude, double driverLongitude) {
        this.driverFacebookId = driverFacebookId;
        this.driverName = driverName;
        this.eta = eta;
        this.friendLatitude = friendLatitude;
        this.friendLongitude = friendLongitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
        this.driverLatitude = driverLatitude;
        this.driverLongitude = driverLongitude;
    }

    public String getDriverFacebookId() {
        return driverFacebookId;
    }

    public void setDriverFacebookId(String driverFacebookId) {
        this.driverFacebookId = driverFacebookId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public double getFriendLatitude() {
        return friendLatitude;
    }

    public void setFriendLatitude(double friendLatitude) {
        this.friendLatitude = friendLatitude;
    }

    public double getFriendLongitude() {
        return friendLongitude;
    }

    public void setFriendLongitude(double friendLongitude) {
        this.friendLongitude = friendLongitude;
    }

    public double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public double getDriverLatitude() {
        return driverLatitude;
    }

    public void setDriverLatitude(double driverLatitude) {
        this.driverLatitude = driverLatitude;
    }

    public double getDriverLongitude() {
        return driverLongitude;
    }

    public void setDriverLongitude(double driverLongitude) {
        this.driverLongitude = driverLongitude;
    }

    protected PickUpResponse(Parcel in) {
        driverFacebookId = in.readString();
        driverName = in.readString();
        eta = in.readString();
        friendLatitude = in.readDouble();
        friendLongitude = in.readDouble();
        destinationLatitude = in.readDouble();
        destinationLongitude = in.readDouble();
        driverLatitude = in.readDouble();
        driverLongitude = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(driverFacebookId);
        dest.writeString(driverName);
        dest.writeString(eta);
        dest.writeDouble(friendLatitude);
        dest.writeDouble(friendLongitude);
        dest.writeDouble(destinationLatitude);
        dest.writeDouble(destinationLongitude);
        dest.writeDouble(driverLatitude);
        dest.writeDouble(driverLongitude);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PickUpResponse> CREATOR = new Parcelable.Creator<PickUpResponse>() {
        @Override
        public PickUpResponse createFromParcel(Parcel in) {
            return new PickUpResponse(in);
        }

        @Override
        public PickUpResponse[] newArray(int size) {
            return new PickUpResponse[size];
        }
    };
}