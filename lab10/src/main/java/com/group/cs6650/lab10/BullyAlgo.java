package com.group.cs6650.lab10;

import java.util.concurrent.*;

public class BullyAlgo implements Runnable {
  private int numNodes;
  private int id;
  private BlockingQueue<String> requestsReceived;
  private BlockingQueue<String> requestsWaiting;

  // (1) the number of nodes, (2) the id for the local node
  public BullyAlgo(int numNodes, int id, BlockingQueue<String> requestsReceived, BlockingQueue<String> requestsWaiting) {
    this.numNodes = numNodes;
    this.id = id;
    this.requestsReceived = requestsReceived;
    this.requestsWaiting = requestsWaiting;
  }

  public void run() {
    // TODO
    while (true) {
        
    }
  }
  
  public void printState() {
      
  }
}
