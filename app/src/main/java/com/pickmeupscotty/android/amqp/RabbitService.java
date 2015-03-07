package com.pickmeupscotty.android.amqp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.pickmeupscotty.android.MainActivity;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class RabbitService {

    private static RabbitService instance;
    private Context context;
    private Runner runner;
    private Map<Class<?>, List<WeakReference<Subscriber<Request>>>> subscribers = new HashMap<>();

    private RabbitService(){

    }

    private MessageConsumer mConsumer;

    public static RabbitService getInstance() {
        if(instance == null) {
            instance = new RabbitService();
        }
        return instance;
    }

    public static <T extends Request> void subscribe(Class<T> requestType, Subscriber<T> sub) {

        getInstance().addSubscriber(requestType, sub);

    }

    private <T extends Request> void addSubscriber(Class<T> requestType, Subscriber<T> sub) {
        List<WeakReference<Subscriber<Request>>> list = subscribers.get(requestType);
        if(list == null) {
            subscribers.put(requestType, new ArrayList<WeakReference<Subscriber<Request>>>());
            list = subscribers.get(requestType);
        }
        list.add(new WeakReference<Subscriber<Request>>((Subscriber<Request>) sub));
    }

    private class Runner implements Runnable {

        @Override
        public void run() {

            Log.e("RabbitService", "onStartCommand()");
            mConsumer = new MessageConsumer("178.62.55.27");

            mConsumer.connectToRabbitMQ();

            mConsumer.setOnReceiveMessageHandler(new MessageConsumer.OnReceiveMessageHandler() {

                public void onReceiveMessage(Request request) {

                    List<WeakReference<Subscriber<Request>>> list = subscribers.get(request.getClass());

                    if(list != null) {
                        for (WeakReference<Subscriber<Request>> sub : list) {
                            if (sub.get() != null) {
                                sub.get().on(request);

                            }
                        }
                    }

                }
            });
        }

    }
    public void connect() {
        
        runner = new Runner();
        new Thread(runner).start();
        // The service is starting, due to a call to startService()
    }

    public static void create(Context context) {
        getInstance().setContext(context);
        getInstance().connect();
    }


    public void setContext(Context context) {
        this.context = context;
    }

    public static boolean send(Request pickUpRequest) {
        return getInstance().sendRequest(pickUpRequest);
    }

    private boolean sendRequest(Request pickUpRequest) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mConsumer.send(pickUpRequest);
        return false;
    }
}
//mOutput =  (TextView) findViewById(R.id.rabbitmessage);
//
//        Intent intent = new Intent(this, RabbitService.class);
//        startService(intent);


//
//            }
