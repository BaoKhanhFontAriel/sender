package vn.vnpay.sender.init;

import com.sun.jersey.api.core.InjectParam;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionPool;
import vn.vnpay.sender.service.ApiService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class StartStopListener implements ServletContextListener {

    private final RabbitConnectionPool rabbitConnectionPool = RabbitConnectionPool.getInstancePool();
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Servlet has been started.");
        rabbitConnectionPool.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Servlet has been stopped.");
    }
}
