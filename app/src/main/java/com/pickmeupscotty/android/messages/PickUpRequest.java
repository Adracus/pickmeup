package com.pickmeupscotty.android.messages;

import com.pickmeupscotty.android.amqp.Message;

public class PickUpRequest implements Message {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name = "test";

}
