package com.pickmeupscotty.android.messages;

import android.os.Parcel;
import android.os.Parcelable;

import com.pickmeupscotty.android.amqp.Message;

public class PickUpRequest implements Message, Parcelable {
    public static String CURRENT_LATITUDE = "CURRENT_LATITUDE";
    public static String CURRENT_LONGITUDE = "CURRENT_LONGITUDE";
    public static String DESTINATION_LATITUDE = "DESTINATION_LATITUDE";
    public static String DESTINATION_LONGITUDE = "DESTINATION_LONGITUDE";
    public static String FACEBOOK_ID = "FACEBOOK_ID";
    public static String PICK_UP_REQUEST = "PICKUP_REQUEST";

    private double currentLatitude;
    private double currentLongitude;
    private double destinationLatitude;
    private double destinationLongitude;
    private String facebookId;
    private String facebookName;

    public PickUpRequest(double currentLatitude, double currentLongitude,
                         double destinationLatitude, double destinationLongitude,
                         String facebookId, String facebookName) {
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
        this.facebookId = facebookId;
        this.facebookName = facebookName;
    }

    public PickUpRequest() {
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
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

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    protected PickUpRequest(Parcel in) {
        currentLatitude = in.readDouble();
        currentLongitude = in.readDouble();
        destinationLatitude = in.readDouble();
        destinationLongitude = in.readDouble();
        facebookId = in.readString();
        facebookName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(currentLatitude);
        dest.writeDouble(currentLongitude);
        dest.writeDouble(destinationLatitude);
        dest.writeDouble(destinationLongitude);
        dest.writeString(facebookId);
        dest.writeString(facebookName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PickUpRequest> CREATOR = new Parcelable.Creator<PickUpRequest>() {
        @Override
        public PickUpRequest createFromParcel(Parcel in) {
            return new PickUpRequest(in);
        }

        @Override
        public PickUpRequest[] newArray(int size) {
            return new PickUpRequest[size];
        }
    };

    public String getFacebookName() {
        return facebookName;
    }

    public void setFacebookName(String facebookName) {
        this.facebookName = facebookName;
    }
}
