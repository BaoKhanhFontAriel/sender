package vn.vnpay.sender.init;

import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.ch.qos.logback.classic.LoggerContext;
import org.apache.juli.logging.ch.qos.logback.classic.joran.JoranConfigurator;
import org.apache.juli.logging.ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionCell;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionPool;

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

    private final RabbitConnectionPool rabbitConnectionPool = RabbitConnectionPool.getInstancePool();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("Servlet has been started.");

        // Get Jersey to use SLF4J instead of JUL
//        java.util.logging.LogManager.getLogManager().reset();
//        java.util.logging.Logger.getLogger("global").setLevel(Level.FINEST);
//        SLF4JBridgeHandler.removeHandlersForRootLogger();
//        SLF4JBridgeHandler.install();

//        try {
//            initLogger();
//        } catch (IOException | JoranException e) {
//            throw new RuntimeException(e);
//        }

        rabbitConnectionPool.start();
    }

    private static void initLogger() throws IOException, JoranException {
        LoggerContext loggerContext = (LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();
        loggerContext.reset();
        JoranConfigurator configurator = new JoranConfigurator();
        String location = new File(".").getCanonicalPath() + "\\logback.xml";
        System.out.println("location " + location);
        configurator.setContext(loggerContext);
        configurator.doConfigure(location);
    }


    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("Servlet has been stopped.");
    }
}
