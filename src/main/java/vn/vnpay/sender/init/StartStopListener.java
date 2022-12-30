package vn.vnpay.sender.init;

import com.sun.jersey.api.core.InjectParam;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionCell;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionPool;
import vn.vnpay.sender.service.ApiService;
import vn.vnpay.sender.util.WebConfigSingleton;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class StartStopListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(RabbitConnectionCell.class);

//    private final RabbitConnectionPool rabbitConnectionPool = RabbitConnectionPool.getInstancePool();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("Servlet has been started.");
//        rabbitConnectionPool.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("Servlet has been stopped.");
    }
}
