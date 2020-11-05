package com.group.cs6650.lab10;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class BullyAlgoRabbit implements Runnable {
  private final static String QUEUE_NAME = "message_queue";
  private final static String EXCHANGE_NAME = "LEADER";
  private final static String I_AM_LEADER = "LEADER";
  
  private boolean isElectionPhase;
  private int numNodes;
  private int id;
  private Channel channel;



  public BullyAlgoRabbit(int numNodes, int id) {
    this.numNodes = numNodes;
    this.id = id;
    this.isElectionPhase = false;

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("local host");

    try (Connection connection = factory.newConnection()) {
      this.channel = connection.createChannel();
      this.channel.queueDeclare(QUEUE_NAME, true, false, false, null);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setNumNodes(int numNodes) {
    this.numNodes = numNodes;
  }

  @Override
  public void run() {
  }

  // notify everyone that you are leader
  public void notifyOthers() {
    try {
      channel.exchangeDeclare(EXCHANGE_NAME, "direct");
      channel.basicPublish(EXCHANGE_NAME, "black", null, I_AM_LEADER.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  
}
