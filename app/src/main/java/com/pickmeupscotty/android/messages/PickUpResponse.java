package com.pickmeupscotty.android.messages;

import com.pickmeupscotty.android.amqp.Message;

public class PickUpResponse implements Message {
    private String driverFacebookId;
    private String driverName;
    private String eta;

    public PickUpResponse(String driverFacebookId, String driverName, String eta) {
        this.driverFacebookId = driverFacebookId;
        this.driverName = driverName;
        this.eta = eta;
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
}
