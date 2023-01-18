package vn.vnpay.sender.thread;


import lombok.extern.slf4j.Slf4j;
import vn.vnpay.sender.connect.kafka.KafkaConnectionCell;
import vn.vnpay.sender.connect.kafka.KafkaConnectionPool;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionCell;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionPool;

@Slf4j
public class ShutdownThread extends Thread{
    RabbitConnectionPool rabbitConnectionPool = RabbitConnectionPool.getInstancePool();
    private static final KafkaConnectionPool kafkaConnectionPool = KafkaConnectionPool.getInstancePool();
    public void run() {
        kafkaConnectionPool.getPool().forEach(KafkaConnectionCell::close);
        rabbitConnectionPool.getPool().forEach(RabbitConnectionCell::close);
        rabbitConnectionPool.getPool().clear();
        kafkaConnectionPool.getPool().clear();
        System.out.println("shut down hook task completed..");
    }
}
