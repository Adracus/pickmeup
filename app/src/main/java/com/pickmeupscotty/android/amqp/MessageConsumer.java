package com.pickmeupscotty.android.amqp;

import android.os.Handler;
import android.os.Looper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.HashMap;

/**
 *Consumes messages from a RabbitMQ broker
 *
 */
public class MessageConsumer extends  IConnectToRabbitMQ{

    private static final String EXCHANGE = "pickmeup";

    public MessageConsumer(String server) {
        super(server, EXCHANGE, "fanout");
    }

    //The Queue name for this consumer
    private String mQueue;
    private QueueingConsumer MySubscription;

    //last message to post back
    private byte[] mLastMessage;
    private String mLastType;

    public void send(Request request) {
        try {
            HashMap<String, Object> headers = new HashMap<>();

            headers.put("CLASS", request.getClass().getName());

            ObjectMapper mapper = new ObjectMapper();
            byte[] messageBodyBytes =  mapper.writeValueAsBytes(request);
            mModel.basicPublish(EXCHANGE, "", new AMQP.BasicProperties.Builder()
                    .headers(headers)
                    .build(), messageBodyBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // An interface to be implemented by an object that is interested in messages(listener)
    public interface OnReceiveMessageHandler{
        public void onReceiveMessage(Request request);
    };

    //A reference to the listener, we can only have one at a time(for now)
    private OnReceiveMessageHandler mOnReceiveMessageHandler;

    /**
     *
     * Set the callback for received messages
     * @param handler The callback
     */
    public void setOnReceiveMessageHandler(OnReceiveMessageHandler handler){
        mOnReceiveMessageHandler = handler;
    };

    private Handler mMessageHandler = new Handler(Looper.getMainLooper());
    private Handler mConsumeHandler = new Handler(Looper.getMainLooper());

    // Create runnable for posting back to main thread
    final Runnable mReturnMessage = new Runnable() {
        public void run() {
            try {
                Class<Request> clazz = (Class<Request>) Class.forName(mLastType);
                ObjectMapper mapper = new ObjectMapper();
                Request request = mapper.readValue(mLastMessage, clazz);
                mOnReceiveMessageHandler.onReceiveMessage(request);
            } catch (ClassNotFoundException | ClassCastException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    final Runnable mConsumeRunner = new Runnable() {
        public void run() {
            Consume();
        }
    };

    /**
     * Create Exchange and then start consuming. A binding needs to be added before any messages will be delivered
     */
    @Override
    public boolean connectToRabbitMQ()
    {
        if(super.connectToRabbitMQ())
        {

            try {
                mQueue = mModel.queueDeclare().getQueue();
                MySubscription = new QueueingConsumer(mModel);
                mModel.basicConsume(mQueue, false, MySubscription);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            if (MyExchangeType == "fanout")
                AddBinding("");//fanout has default binding

            Running = true;
            mConsumeHandler.post(mConsumeRunner);
//            Consume();

            return true;
        }
        return false;
    }

    /**
     * Add a binding between this consumers Queue and the Exchange with routingKey
     * @param routingKey the binding key eg GOOG
     */
    public void AddBinding(String routingKey)
    {
        try {
            mModel.queueBind(mQueue, mExchange, routingKey);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Remove binding between this consumers Queue and the Exchange with routingKey
     * @param routingKey the binding key eg GOOG
     */
    public void RemoveBinding(String routingKey)
    {
        try {
            mModel.queueUnbind(mQueue, mExchange, routingKey);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void Consume()
    {
        Thread thread = new Thread()
        {

            @Override
            public void run() {
                while(Running){
                    QueueingConsumer.Delivery delivery;
                    try {
                        delivery = MySubscription.nextDelivery();
                        mLastMessage = delivery.getBody();
                        mLastType = delivery.getProperties().getHeaders().get("CLASS").toString();
                        mMessageHandler.post(mReturnMessage);
                        try {
                            mModel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        };
        thread.start();

    }

    public void dispose(){
        Running = false;
    }
}
