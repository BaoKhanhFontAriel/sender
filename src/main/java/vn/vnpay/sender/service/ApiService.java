package vn.vnpay.sender.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionCell;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionPool;
import vn.vnpay.sender.models.ApiRequest;
import vn.vnpay.sender.util.GsonSingleton;

import java.security.SecureRandom;
import java.util.Base64;


@Slf4j
public class ApiService {

//    OracleConnectionPool oracleConnectionPool = OracleConnectionPool.getInstancePool();


    RabbitConnectionPool rabbitConnectionPool = RabbitConnectionPool.getInstancePool();

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    private Gson gson = GsonSingleton.getInstance().getGson();

    public String sendToCore(String data) {

        String message = createRequest(data);

        RabbitConnectionCell conn = rabbitConnectionPool.getConnection();
        String response = conn.sendAndReceive(message);
        rabbitConnectionPool.releaseConnection(conn);
        return response;
    }

    public String createRequest(String data) {
        String token = generateNewToken();
        ApiRequest customerRequest = new ApiRequest(token, data);
        log.info("create request {}", customerRequest.toString());
        return gson.toJson(customerRequest);
    }

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        String token = base64Encoder.encodeToString(randomBytes);
        log.info("generate token: {}", token);
        return token;
    }
}
