package vn.vnpay.sender.init;

import com.sun.jersey.api.core.InjectParam;
import lombok.extern.slf4j.Slf4j;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionPool;
import vn.vnpay.sender.service.ApiService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
@Slf4j
public class StartStopListener implements ServletContextListener {

    private final RabbitConnectionPool rabbitConnectionPool = RabbitConnectionPool.getInstancePool();
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("Servlet has been started.");
        rabbitConnectionPool.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("Servlet has been stopped.");
    }
}
