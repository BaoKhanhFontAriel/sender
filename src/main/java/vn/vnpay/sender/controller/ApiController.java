package vn.vnpay.sender.controller;

import com.sun.jersey.api.core.InjectParam;
import lombok.extern.slf4j.Slf4j;
import vn.vnpay.sender.service.ApiService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


@Slf4j
@Path("/api")
public class ApiController {
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
//
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