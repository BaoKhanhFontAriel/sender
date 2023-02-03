package vn.vnpay.sender.init;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import vn.vnpay.sender.connect.kafka.KafkaConsumerConnectionPool;
import vn.vnpay.sender.connect.kafka.KafkaProducerConnectionPool;
import vn.vnpay.sender.thread.ShutdownThread;
//import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.IOException;


@WebListener
@Slf4j
public class ApplicationServletContextListener implements ServletContextListener {
    //    private static final Logger log = LoggerFactory.getLogger(RabbitConnectionCell.class);
//    private static final WebConfigSingleton web = WebConfigSingleton.getInstance();


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("Servlet has been started.");
//        try {
//            initLoggerConfig();
//        } catch (IOException | JoranException e) {
//            log.error("fail to load logger config: ", e);
//        }

//        rabbitConnectionPool.start();
        KafkaConsumerConnectionPool.getInstancePool().start();
        KafkaProducerConnectionPool.getInstancePool().start();

        Runtime.getRuntime().addShutdownHook(new ShutdownThread());
    }


    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("Servlet has been stopped.");

    }

    private static void initLoggerConfig() throws IOException, JoranException {
        log.info("start loading logger config......");
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();
        JoranConfigurator configurator = new JoranConfigurator();
        String location = new File(".").getCanonicalPath() + "/config/logback.xml";
        configurator.setContext(loggerContext);
        configurator.doConfigure(location);
    }
}
