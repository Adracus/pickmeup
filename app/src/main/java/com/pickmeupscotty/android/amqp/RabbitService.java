package com.pickmeupscotty.android.amqp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.pickmeupscotty.android.messages.PickUpRequest;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RabbitService {

    private static RabbitService instance;
    private Context context;
    private Map<Class<?>, List<WeakReference<Subscriber<Message>>>> subscribers = new HashMap<>();

    private RabbitService(){

    }

    private MessageConsumer mConsumer;

    public static RabbitService getInstance() {
        if(instance == null) {
            instance = new RabbitService();
        }
        return instance;
    }

    public static <T extends Message> void subscribe(Class<T> requestType, Subscriber<T> sub) {
        getInstance().addSubscriber(requestType, sub);
    }

    private <T extends Message> void addSubscriber(Class<T> requestType, Subscriber<T> sub) {
        List<WeakReference<Subscriber<Message>>> list = subscribers.get(requestType);
        if(list == null) {
            subscribers.put(requestType, new ArrayList<WeakReference<Subscriber<Message>>>());
            list = subscribers.get(requestType);
        }
        list.add(new WeakReference<Subscriber<Message>>((Subscriber<Message>) sub));
    }

    public void sendToSubscribers(final Message message) {
        List<WeakReference<Subscriber<Message>>> list = subscribers.get(message.getClass());

        if(list != null) {
            for (WeakReference<Subscriber<Message>> sub : list) {
                if (sub.get() != null) {
                    new Handler(Looper.getMainLooper()).post(new SubscribeRunner(sub.get(), message));
                }
            }
        }
    }


    private class SubscribeRunner implements Runnable {

        private Subscriber<Message> sub;
        private Message message;

        public SubscribeRunner(Subscriber<Message> sub, Message message) {
            this.sub = sub;
            this.message = message;
        }

        @Override
        public void run() {
            sub.on(message);
        }
    }
    public void connect(final String facebookID) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                mConsumer = new MessageConsumer("178.62.55.27");

                mConsumer.connectToRabbitMQ(facebookID);
            }
        }).start();
        // The service is starting, due to a call to startService()
    }

    public static void create(Context context, String facebookID) {
        getInstance().setContext(context);
        getInstance().connect(facebookID);
    }


    public void setContext(Context context) {
        this.context = context;
    }

    public static void send(Message pickUpMessage) {
        getInstance().sendRequest(pickUpMessage);
    }

    public static void send(Message message, String myFacebookID) {
        getInstance().sendRequest(message, myFacebookID);
    }

    private boolean sendRequest(Message pickUpMessage, String myFacebookID) {
        mConsumer.send(pickUpMessage, myFacebookID);
        return false;
    }

    private boolean sendRequest(Message pickUpMessage) {
        mConsumer.send(pickUpMessage);
        return false;
    }
}
