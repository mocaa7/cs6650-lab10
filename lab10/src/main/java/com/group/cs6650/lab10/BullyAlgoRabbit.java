package com.group.cs6650.lab10;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class BullyAlgoRabbit implements Runnable {
  private final static String QUEUE_NAME = "message_queue";
  private final static String EXCHANGE_NAME = "LEADER";
  private final static String I_AM_LEADER = "LEADER";

  private boolean isElectionPhase;
  private int numNodes;
  private int id;
  private boolean isLeader;
  private int currentLeader;
  private Channel channel;
  private DeliverCallback ack;

  public BullyAlgoRabbit(int numNodes, int id) {
    this.numNodes = numNodes;
    this.id = id;
    this.isLeader = id == numNodes;
    this.currentLeader = numNodes;
    this.isElectionPhase = false;

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("local host");

    try (Connection connection = factory.newConnection()) {
      this.channel = connection.createChannel();
      this.channel.queueDeclare(QUEUE_NAME, true, false, false, null);
    } catch (Exception e) {
      e.printStackTrace();
    }

    this.ack = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      System.out.println(" [x] Received " + message + "");
      System.out.println(" [x] Done");
      this.channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    };
  }

  public void setNumNodes(int numNodes) {
    this.numNodes = numNodes;
  }

  @Override
  public void run() {

    while (this.isLeader) {
      try {
        Thread.sleep(1000);
        System.out.println(this.id + ": I am leader");
        channel.basicConsume(QUEUE_NAME, true, this.ack, consumerTag -> {});
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    while (!isLeader) {
      try {
        Thread.sleep(1000);
        System.out.println(this.id + ": I am a follower, " + this.currentLeader + " is the leader");
        channel.basicPublish("", QUEUE_NAME, null, "hi".getBytes("UTF-8"));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    // try {
    // // Sends a request
    // Thread.sleep(5000); // 5 sec

    // // Check for a response
    // }catch(
    // InterruptedException e)
    // {
    // e.printStackTrace();
    // }

    // }
  }

  // notify everyone that you are leader
  public void notifyOthers() {
    try {
      // channel.exchangeDeclare(EXCHANGE_NAME, "direct");
      channel.basicPublish(EXCHANGE_NAME, "black", null, I_AM_LEADER.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void sendElectionResponse() {
    try {
      String queueName = channel.queueDeclare().getQueue();
      channel.queueBind(queueName, EXCHANGE_NAME, Integer.toString(this.id + 1));
      // channel.queueBind(queueName, EXCHANGE_NAME, "black");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void getElectionResponse() {
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      System.out.println(" [x] Received " + message + "");
    };

    try {
      String queueName = channel.queueDeclare().getQueue();
      channel.queueBind(queueName, EXCHANGE_NAME, Integer.toString(this.id));
      channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
      });
      // channel.queueBind(queueName, EXCHANGE_NAME, "black");
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void startElection() {
    // TODO: start the election
    System.out.println(this.id + "started election");
  }

}
