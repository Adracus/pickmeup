package com.pickmeupscotty.android.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.pickmeupscotty.android.amqp.RabbitService;
import com.pickmeupscotty.android.amqp.Subscriber;
import com.pickmeupscotty.android.messages.PickUpRequest;

/**
 * Created by jannis on 07/03/15.
 */
public class NotificationService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        RabbitService.subscribe(PickUpRequest.class, new Subscriber<PickUpRequest>() {
            @Override
            public void on(PickUpRequest request) {


//                new NotificationCompat.Builder(getApplicationContext()).set
                Log.d("test", request.toString());
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(), request.toString(), duration);
                toast.show();
            }
        });

        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
