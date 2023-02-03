package vn.vnpay.sender.main;


import javafx.stage.Stage;
import org.apache.juli.logging.ch.qos.logback.classic.LoggerContext;
import org.apache.juli.logging.ch.qos.logback.classic.joran.JoranConfigurator;
import org.apache.juli.logging.ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import vn.vnpay.sender.controller.ApiController;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationPath("app")
public class MainApp extends Application {
    private Set<Object> singletons = new HashSet<>();

    public MainApp() {
        singletons.add(new ApiController());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    //    private static void initLoggerConfig() throws IOException, JoranException {
//        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
//        loggerContext.reset();
//        JoranConfigurator configurator = new JoranConfigurator();
//        String location = "src/main/resources/logs";
//        configurator.setContext(loggerContext);
//        configurator.doConfigure(location);
//    }

//    public static void main(String[] args) {
//        try {
//            initLoggerConfig();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (JoranException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
