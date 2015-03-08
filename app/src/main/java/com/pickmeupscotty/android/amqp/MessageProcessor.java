package com.pickmeupscotty.android.amqp;


import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickmeupscotty.android.login.FBWrapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Consumes messages from a RabbitMQ broker
 */
public class MessageProcessor extends IConnectToRabbitMQ {

    private static final String EXCHANGE = "pickmeup";

    public MessageProcessor(String server) {
        super(server, EXCHANGE, "direct");
    }

    //The Queue name for this consumer
    private String mQueue;
    private QueueingConsumer MySubscription;

    //last message to post back
    private byte[] mLastMessage;
    private String mLastType;


    /**
     * Create Exchange and then start consuming. A binding needs to be added before any messages will be delivered
     */
    public void connectToRabbitMQ(final String facebookID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (MessageProcessor.super.connectToRabbitMQ()) {

                    try {
                        Map<String, Object> args = new HashMap<String, Object>();
                        args.put("x-expires", 600000);
                        mQueue = mModel.queueDeclare(facebookID, true, false, false, args).getQueue();
//                        mModel.exchangeDeclare(facebookID, "direct");
//                        mModel.queueBind(mQueue, facebookID, "");
                        MySubscription = new QueueingConsumer(mModel);
                        mModel.basicConsume(mQueue, false, MySubscription);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    AddBinding(facebookID);//fanout has default binding

                    Running = true;
                    Consume();

                }
            }
        }).start();
    }

    /**
     * Add a binding between this consumers Queue and the Exchange with routingKey
     *
     * @param routingKey the binding key eg GOOG
     */
    public void AddBinding(String routingKey) {
        try {
            mModel.queueBind(mQueue, mExchange, routingKey);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Remove binding between this consumers Queue and the Exchange with routingKey
     *
     * @param routingKey the binding key eg GOOG
     */
    public void RemoveBinding(String routingKey) {
        try {
            mModel.queueUnbind(mQueue, mExchange, routingKey);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void Consume() {
        Thread thread = new Thread() {

            @Override
            public void run() {
                while (Running) {
                    QueueingConsumer.Delivery delivery = null;
                        try {
                            delivery = MySubscription.nextDelivery();

                            if( delivery.getProperties().getHeaders().get("CLASS") != null) {
                                mLastMessage = delivery.getBody();
                                mLastType = delivery.getProperties().getHeaders().get("CLASS").toString();

                                Class<Message> clazz = (Class<Message>) Class.forName(mLastType);
                                ObjectMapper mapper = new ObjectMapper();
                                Message message = mapper.readValue(mLastMessage, clazz);
                                RabbitService.getInstance().notifySubscribers(message);
                            }
                        } catch (ClassNotFoundException | ClassCastException e) {
                            e.printStackTrace();
                        } catch (JsonMappingException e) {
                            e.printStackTrace();
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    try {
                            mModel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
        };
        thread.start();

    }

    public void dispose() {
        Running = false;
    }

    public void send(final Message message) {
        FBWrapper.INSTANCE.getFBFriends(new Request.GraphUserListCallback() {
            @Override
            public void onCompleted(List<GraphUser> graphUsers, Response response) {
                List<String> ccList = new ArrayList<String>();

                for(GraphUser user: graphUsers) {
                    ccList.add(user.getId());
                }
                ccList.add("statrt");
                HashMap<String, Object> headers = new HashMap<>();
                headers.put("CC", ccList);
                headers.put("CLASS", message.getClass().getName());
                send(message, "", headers);
            }
        });
    }

    public void send(final Message message, final String routing_key, final HashMap<String, Object> headers) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    ObjectMapper mapper = new ObjectMapper();
                    byte[] messageBodyBytes = mapper.writeValueAsBytes(message);
                    mModel.basicPublish(EXCHANGE, routing_key, new AMQP.BasicProperties.Builder()
                            .headers(headers)
                            .build(), messageBodyBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    public void send(final Message message, final String routing_key) {

        HashMap<String, Object> headers = new HashMap<>();
        headers.put("CLASS", message.getClass().getName());
        send(message, routing_key, headers);

    }
}
