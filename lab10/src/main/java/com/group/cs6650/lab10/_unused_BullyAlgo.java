// package com.group.cs6650.lab10;

// import java.util.concurrent.*;

// public class BullyAlgo implements Runnable {
//   private int numNodes;
//   private int id;
//   private ConcurrentHashMap<String, BlockingQueue<String>> manager; 
//   private BlockingQueue<String> requestsReceived;
//   private BlockingQueue<String> requestsWaiting;

//   // (1) the number of nodes, (2) the id for the local node
//   public BullyAlgo(int numNodes, int id, ConcurrentHashMap<Integer, BlockingQueue<String>> manager) {
//     this.numNodes = numNodes;
//     this.id = id;
//     this.requestsReceived = new LinkedBlockingQueue<String>();
//     this.requestsWaiting = new LinkedBlockingQueue<String>();
//     this.manager = manager;
//   }

//   public void run() {
//     while (true) {
//         if (!requestsReceived.isEmpty()) {
//             String req = requestsReceived.poll();
//             if (req.equals("election")) {
//                 boolean master = true;
//                 for (Integer i : manager.keySet()) {
//                     if (i > this.id) {
//                         this.manager.get(i).offer("election");
//                         master = false;
//                     }
//                 } 
//                 if (master) {
//                     System.out.println(this.id + ": I am elected with");
//                 }
//             }
//         }
//     }
//   }
  
//   public void printState() {
      
//   }
// }
