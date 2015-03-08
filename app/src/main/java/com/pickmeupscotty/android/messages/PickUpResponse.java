package com.pickmeupscotty.android.messages;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.pickmeupscotty.android.amqp.Message;

import java.util.List;

public class PickUpResponse implements Message, Parcelable {
    public static final String PICK_UP_RESPONSE = "PICK_UP_RESPONSE";
    private String driverFacebookId;
    private String driverName;
    private String eta;
    private List<LatLng> route;

    public PickUpResponse(String driverFacebookId, String driverName, String eta, List<LatLng> route) {
        this.driverFacebookId = driverFacebookId;
        this.driverName = driverName;
        this.eta = eta;
        this.route = route;
    }

    public PickUpResponse() {}

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

    public List<LatLng> getRoute() {
        return route;
    }

    public void setRoute(List<LatLng> route) {
        this.route = route;
    }

    protected PickUpResponse(Parcel in) {
        driverFacebookId = in.readString();
        driverName = in.readString();
        eta = in.readString();
        route = in.readArrayList(LatLng.class.getClassLoader());
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
        dest.writeList(route);
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