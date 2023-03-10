package vn.vnpay.sender.main;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import vn.vnpay.sender.connect.kafka.KafkaConsumerConnectionPool;
import vn.vnpay.sender.connect.kafka.KafkaProducerConnectionPool;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionPool;
import vn.vnpay.sender.controller.ApiController;
import vn.vnpay.sender.thread.ShutdownThread;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


@ApplicationPath("app")
@Slf4j
public class MainApp extends Application {
    private Set<Object> singleton = new HashSet<>();
    private static final RabbitConnectionPool rabbitConnectionPool = RabbitConnectionPool.getInstancePool();

    public MainApp() {
        singleton.add(new ApiController());
    }

    @Override
    public Set<Object> getSingletons() {
        return singleton;
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

    public static void main(String[] args) {
        try {
            initLoggerConfig();
        } catch (IOException | JoranException e) {
            log.error("fail to load logger config: ", e);
        }

//        rabbitConnectionPool.start();
        KafkaConsumerConnectionPool.getInstancePool().start();
        KafkaProducerConnectionPool.getInstancePool().start();

        Runtime.getRuntime().addShutdownHook(new ShutdownThread());
    }

}
