# Producer
This sends one message to the queue
```Java
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {
  private final static String QUEUE_NAME = "hello";
  
  public static void main(String[] args) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("local host");

    try (
      Connection connection = factory.newConnection();
      Channel channel = connection.createChannel()) {
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent " + message + "");
      }
  }
}
```

- create a factory then connect to local host
- using a try-block (which automatically closes items that implement "closable"), we:
   1. create a channel - a connection between the client and the message broker
   2. declare a queue: `channel.queueDeclare()`
      - asking the broker to create a queue called `QUEUE_NAME`
   3. publish a message ("hello world") to the queue
      - empty string - identifies the exchange type
      - this exchange "" is the anonymous exchange
      - this means it will send the exchange to *any* queues that are connected to it

# Consumer
```Java
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Recv {
  private final static String QUEUE_NAME = "hello";

  public static void main(String[] args) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      System.out.println(" [x] Received " + message + "");
    };
    channel.basicConsume(QUEUE_NAME, true , deliverCallback , consumerTag -> {});
  }
}
```

# Consumer - Manual Acknowledge
```Java
channel.basicQos(1); // accept only one unack-ed message at a time

DeliverCallback deliverCallback = (consumerTag, delivery) -> {
  String message = new String(delivery.getBody(), "UTF-8");
  
  System.out.println(" [x] Received " + message + "");
  try {
    ProcessMessage(message);
  } finally {
    System.out.println(" [x] Done");
    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
  }
};

boolean autoAck = false;
channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {});
```
- the `channel.basicQos(1)` is used by consumers to state that it ONLY wants 1 un-acknowledged message sent to it at a given time

## Stating that you want things to be persisted
```Java
// in producer and consumer - make the queue DURABLE
boolean durable = true;
channel.queueDeclare("task_queue", durable, false, false, null);
```
```Java
import com.rabbitmq.client.MessageProperties;

// make sent messages PERSISTENT - persists the messages in a message log
channel.basicPublish ("", "task_queue", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
```