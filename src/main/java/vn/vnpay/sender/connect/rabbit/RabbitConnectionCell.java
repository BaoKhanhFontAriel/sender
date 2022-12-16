package vn.vnpay.sender.connect.rabbit;

import com.rabbitmq.client.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@Slf4j
@Setter
@Getter
@ToString
public class RabbitConnectionCell {
    private String exchangeName;

    private String exchangeType;

    private String routingKey;
    private long relaxTime;
    private long timeOut;
    private Connection conn;

    private Channel channel;

    public RabbitConnectionCell(ConnectionFactory factory, String exchangeName, String exchangeType, String routingKey, long relaxTime) {
//        super();
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.relaxTime = relaxTime;

        try {
            this.conn = factory.newConnection();
            this.channel = conn.createChannel();
            this.channel.exchangeDeclare(exchangeName, exchangeType);
//            channel.queuePurge(exchangeName);

        } catch (IOException | TimeoutException e) {
            log.info("fail connecting to rabbit : {0}", e);
        }
    }

    public String sendAndReceive(String message) {
        log.info("begin sending and receiving message: {}", message);
        String result = null;
        final String corrId = UUID.randomUUID().toString();

        try {

            String replyQueueName = this.channel.queueDeclare().getQueue();
            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(corrId)
                    .replyTo(replyQueueName)
                    .build();

            // send message
            this.channel.basicPublish(exchangeName, routingKey, props, message.getBytes("UTF-8"));

            // receive message
            final CompletableFuture<String> response = new CompletableFuture<>();

            String ctag = this.channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
                if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                    response.complete(new String(delivery.getBody(), "UTF-8"));
                }
            }, consumerTag -> {
            });

            result = response.get();
            this.channel.basicCancel(ctag);
            log.info("successful send and receive");

        } catch (IOException | InterruptedException | ExecutionException e) {
            log.info("fail to send and receive message: {0}", e);
        }
        return result;
    }

    public boolean isTimeOut() {
        if (System.currentTimeMillis() - this.relaxTime > this.timeOut) {
            return true;
        }
        return false;
    }

    public void close() {
        try {
            this.conn.close();
        } catch (Exception e) {
            log.warn("connection is closed: {0}", e);
        }
    }

    public boolean isClosed() throws Exception {
        return !conn.isOpen();
    }

}
