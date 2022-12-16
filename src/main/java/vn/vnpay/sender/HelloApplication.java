package vn.vnpay.sender;

import vn.vnpay.sender.connect.rabbit.RabbitConnectionPool;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class HelloApplication extends Application {
    RabbitConnectionPool rabbitConnectionPool = RabbitConnectionPool.getInstancePool();

    {
        rabbitConnectionPool.start();
    }

}