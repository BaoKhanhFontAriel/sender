package vn.vnpay.sender.init;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.slf4j.bridge.SLF4JBridgeHandler;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionCell;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionPool;
import vn.vnpay.sender.util.WebConfigSingleton;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;


@WebListener
@Slf4j
public class StartStopListener implements ServletContextListener {
    //    private static final Logger log = LoggerFactory.getLogger(RabbitConnectionCell.class);
//    private static final WebConfigSingleton web = WebConfigSingleton.getInstance();


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("Servlet has been started.");
    }


    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("Servlet has been stopped.");
    }
}
