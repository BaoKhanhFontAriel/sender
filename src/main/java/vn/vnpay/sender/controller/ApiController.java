package vn.vnpay.sender.controller;

import com.sun.jersey.api.core.InjectParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionCell;
import vn.vnpay.sender.service.ApiService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


@Path("/api")
public class ApiController {
    private static final Logger log = LoggerFactory.getLogger(RabbitConnectionCell.class);

    @Path("/hello-world")
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }

    private final int REQUEST_TIMEOUT_SECONDS = 1;

    @InjectParam
    private ApiService apiService;

    @Path("/sendtocore")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces("text/plain")
    public String sendToCore(String data, @Context HttpServletRequest request) {

        // set time out for this request
//        Invocation.Builder req = WebConfigSingleton.getInstance().getWebTarget().request();
//        req.property(ClientProperties.CONNECT_TIMEOUT, 5);
//        req.property(ClientProperties.READ_TIMEOUT, 5);

        log.info("IP call request is: {}", request.getRemoteAddr());
        log.info("sending data is: {}", data);

        long start = System.currentTimeMillis();
        String message = apiService.sendToCore(data);
        long end = System.currentTimeMillis();

        log.info("end - start: {}", end - start);
        log.info("Time from api request to response is: {} ms", end - start);
        return message;
    }
}