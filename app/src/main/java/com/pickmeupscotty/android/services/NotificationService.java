package com.pickmeupscotty.android.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.facebook.Session;
import com.pickmeupscotty.android.R;
import com.pickmeupscotty.android.activities.ResponseActivity;
import com.pickmeupscotty.android.amqp.RabbitService;
import com.pickmeupscotty.android.amqp.Subscriber;
import com.pickmeupscotty.android.login.FBWrapper;
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
                final PickUpRequest req = request;

                Context context = getApplicationContext();

                FBWrapper.INSTANCE.getUserId(new FBWrapper.UserIdCallback() {
                    @Override
                    public void onCompleted(String fbid) {
                        //Do not show request send by oneself
                        if (req.getFacebookId() == fbid) return;

                        Intent notificationIntent = new Intent(context, ResponseActivity.class);
                        notificationIntent.putExtra(PickUpRequest.PICK_UP_REQUEST, request);
                        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                                .setContentTitle("Pick Up Request")
                                .setContentText("by " + request.getFacebookName())
                                .setSmallIcon(R.drawable.mister_mustache);
                        notificationBuilder = notificationBuilder.setContentIntent(contentIntent);
                        Notification notification = notificationBuilder.build();
                        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(12, notification);
                    }
                });
            }
        });

        while (true) {
            Thread.yield();
        }
    }
}
