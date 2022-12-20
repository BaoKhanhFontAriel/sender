package vn.vnpay.sender;

import vn.vnpay.sender.connect.rabbit.RabbitConnectionPool;
import vn.vnpay.sender.util.ClientPropertiesSingleton;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class HelloApplication extends Application {
    RabbitConnectionPool rabbitConnectionPool = RabbitConnectionPool.getInstancePool();
    ClientPropertiesSingleton clientPropertiesSingleton = ClientPropertiesSingleton.getInstance();

    {
        rabbitConnectionPool.start();
    }

}