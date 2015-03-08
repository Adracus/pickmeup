package com.pickmeupscotty.android.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.pickmeupscotty.android.R;
import com.pickmeupscotty.android.activities.ResponseActivity;
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
                Context context = getApplicationContext();

                // berechne ob notification angezeigt werden soll.

                Intent notificationIntent = new Intent(context, ResponseActivity.class);
                notificationIntent.putExtra(PickUpRequest.PICK_UP_REQUEST, request);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                        .setContentTitle("Pick Up Request")
                        .setContentText("by " + request.getFacebookId())
                        .setSmallIcon(R.drawable.notification);
                notification = notification
                        .setContentIntent(contentIntent);

                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(12, notification.build());
            }
        });

        while (true) {
            Thread.yield();
        }
    }
}
