package com.pickmeupscotty.android.amqp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.HashMap;

/**
 * Consumes messages from a RabbitMQ broker
 */
public class MessageProcessor extends IConnectToRabbitMQ {

    private static final String EXCHANGE = "pickmeup";

    public MessageProcessor(String server) {
        super(server, EXCHANGE, "fanout");
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
                        mQueue = mModel.queueDeclare().getQueue();
                        MySubscription = new QueueingConsumer(mModel);
                        mModel.basicConsume(mQueue, false, MySubscription);
                        mModel.exchangeDeclare(facebookID, "fanout");
                        mModel.queueBind(mQueue, facebookID, "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (MyExchangeType == "fanout")
                        AddBinding("");//fanout has default binding

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
                    QueueingConsumer.Delivery delivery;
                    try {
                        delivery = MySubscription.nextDelivery();
                        mLastMessage = delivery.getBody();
                        mLastType = delivery.getProperties().getHeaders().get("CLASS").toString();
                        try {
                            Class<Message> clazz = (Class<Message>) Class.forName(mLastType);
                            ObjectMapper mapper = new ObjectMapper();
                            Message message = mapper.readValue(mLastMessage, clazz);
                            RabbitService.getInstance().notifySubscribers(message);
                        } catch (ClassNotFoundException | ClassCastException e) {
                            e.printStackTrace();
                        } catch (JsonMappingException e) {
                            e.printStackTrace();
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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

    public void dispose() {
        Running = false;
    }

    public void send(final Message message) {
        send(message, EXCHANGE);
    }

    public void send(final Message message, final String exchange) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, Object> headers = new HashMap<>();

                    headers.put("CLASS", message.getClass().getName());

                    ObjectMapper mapper = new ObjectMapper();
                    byte[] messageBodyBytes = mapper.writeValueAsBytes(message);
                    mModel.basicPublish(exchange, "", new AMQP.BasicProperties.Builder()
                            .headers(headers)
                            .build(), messageBodyBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
