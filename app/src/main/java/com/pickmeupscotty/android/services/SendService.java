package com.pickmeupscotty.android.services;

import android.app.IntentService;
import android.content.Intent;

import com.pickmeupscotty.android.amqp.RabbitService;
import com.pickmeupscotty.android.messages.PickUpRequest;
import com.pickmeupscotty.android.messages.PickUpResponse;

/**
 * Created by jannis on 08/03/15.
 */
public class SendService extends IntentService{


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public SendService() {
        super("SendService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        PickUpResponse response = intent.getParcelableExtra(PickUpResponse.PICK_UP_RESPONSE);
        String facebookid = intent.getStringExtra(PickUpRequest.FACEBOOK_ID);

        RabbitService.send(response, facebookid);

    }

}
