package com.pickmeupscotty.android.amqp;

import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RabbitService {


    public static final String RABBIT_MQ_IP = "178.62.55.27";




    private static final RabbitService INSTANCE = new RabbitService();

    public static RabbitService getInstance() {
        return INSTANCE;
    }

    private RabbitService(){}

    public static void create(String facebookID) {
        getInstance().connect(facebookID);
    }

    public static <T extends Message> void subscribe(Class<T> requestType, Subscriber<T> sub) {
        getInstance().addSubscriber(requestType, sub);
    }


    public static void send(Message pickUpMessage) {
        getInstance().sendRequest(pickUpMessage);
    }

    public static void send(Message message, String myFacebookID) {
        getInstance().sendRequest(message, myFacebookID);
    }

    private MessageProcessor mConsumer;
    private Map<Class<?>, List<WeakReference<Subscriber<Message>>>> subscribers = new HashMap<>();





    private <T extends Message> void addSubscriber(Class<T> requestType, Subscriber<T> sub) {
        List<WeakReference<Subscriber<Message>>> list = subscribers.get(requestType);
        if(list == null) {
            subscribers.put(requestType, new ArrayList<WeakReference<Subscriber<Message>>>());
            list = subscribers.get(requestType);
        }
        list.add(new WeakReference<>((Subscriber<Message>) sub));
    }

    public void notifySubscribers(final Message message) {
        List<WeakReference<Subscriber<Message>>> list = subscribers.get(message.getClass());

        if(list != null) {
            for (WeakReference<Subscriber<Message>> sub : list) {
                if (sub.get() != null) {
                    new Handler(Looper.getMainLooper()).post(new SubscribeRunner(sub.get()) {

                        @Override
                        public void run() {
                            this.sub.on(message);
                        }
                    });
                }
            }
        }
    }

    private abstract class SubscribeRunner implements Runnable {

        private Subscriber<Message> sub;

        public SubscribeRunner(Subscriber<Message> sub) {
            this.sub = sub;
        }
    }

    public void connect(final String facebookID) {
        mConsumer = new MessageProcessor(RABBIT_MQ_IP);
        mConsumer.connectToRabbitMQ(facebookID);
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
