package vn.vnpay.sender;

import vn.vnpay.sender.connect.rabbit.RabbitConnectionPool;
import vn.vnpay.sender.util.WebConfigSingleton;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class HelloApplication extends Application {
    RabbitConnectionPool rabbitConnectionPool = RabbitConnectionPool.getInstancePool();
    WebConfigSingleton webConfigSingleton = WebConfigSingleton.getInstance();

    {
        rabbitConnectionPool.start();
    }

}