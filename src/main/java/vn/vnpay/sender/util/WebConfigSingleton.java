package vn.vnpay.sender.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

@Slf4j
@Setter
@Getter
public class WebConfigSingleton {
    private String URL = AppConfigSingleton.getInstance().getStringProperty("app.url");
    private int CONNECT_TIMEOUT = AppConfigSingleton.getInstance().getIntProperty("jersey.config.client.connection_timeout");
    private int READ_TIMEOUT = AppConfigSingleton.getInstance().getIntProperty("jersey.config.client.read_timeout");
    private static WebConfigSingleton instance;
    private Client client;
    private WebTarget webTarget;

    public WebConfigSingleton() {
        log.info("set time out for all request...");

        // set timeout for all request
        this.client = ClientBuilder.newClient();
        client.property(ClientProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT );
        client.property(ClientProperties.READ_TIMEOUT,    READ_TIMEOUT);
        webTarget = client.target(URL);

//        try {
//            String responseMsg = webTarget.request().get(String.class);
//            log.info("responseMsg: {}", responseMsg);
//        } catch (ProcessingException pe) {
//            log.error("request timeout: ", pe);
//        }
    }

    public static WebConfigSingleton getInstance() {
    if (instance == null){
        instance = new WebConfigSingleton();

    }
        return instance;
    }
}
