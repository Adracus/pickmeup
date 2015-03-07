package com.pickmeupscotty.android.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.pickmeupscotty.android.MainActivity;
import com.pickmeupscotty.android.R;
import com.pickmeupscotty.android.activities.DriverActivity;
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

                Intent notificationIntent = new Intent(context, DriverActivity.class);

                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

                NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext()).setContentTitle("Test").setContentText("Test").setSmallIcon(R.drawable.notification);
                notification = notification
                        .setContentIntent(contentIntent);


                Log.d("test", request.toString());

                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(12, notification.build());

                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(), request.toString(), duration);
                toast.show();
            }
        });

        while (true) {
//            try true{
                Thread.yield();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

    }
}
