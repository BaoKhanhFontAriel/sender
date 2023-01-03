package vn.vnpay.sender.thread;


import lombok.extern.slf4j.Slf4j;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionCell;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionPool;

@Slf4j
public class ShutdownThread extends Thread{
    RabbitConnectionPool rabbitConnectionPool = RabbitConnectionPool.getInstancePool();
    public void run() {
        rabbitConnectionPool.getPool().forEach(RabbitConnectionCell::close);
        rabbitConnectionPool.getPool().clear();
        System.out.println("shut down hook task completed..");
    }
}
