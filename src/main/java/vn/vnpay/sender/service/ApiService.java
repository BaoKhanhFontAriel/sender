package vn.vnpay.sender.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vnpay.sender.connect.kafka.KafkaConnectionCell;
import vn.vnpay.sender.connect.kafka.KafkaConnectionPool;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionCell;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionPool;
import vn.vnpay.sender.models.ApiRequest;
import vn.vnpay.sender.util.GsonSingleton;
import vn.vnpay.sender.util.TokenUtils;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;


public class ApiService {
    private static final Logger log = LoggerFactory.getLogger(RabbitConnectionCell.class);

//    OracleConnectionPool oracleConnectionPool = OracleConnectionPool.getInstancePool();
    RabbitConnectionPool rabbitConnectionPool = RabbitConnectionPool.getInstancePool();
    KafkaConnectionPool kafkaConnectionPool = KafkaConnectionPool.getInstancePool();
    private Gson gson = GsonSingleton.getInstance().getGson();

    public String sendToCore(String data) {

        String message = createRequest(data);

//        RabbitConnectionCell conn = rabbitConnectionPool.getConnection();
//        String response = conn.sendAndReceive(message);
//        rabbitConnectionPool.releaseConnection(conn);

        KafkaConnectionCell conn = kafkaConnectionPool.getConnection();
        String response = conn.sendAndReceive(message);
        kafkaConnectionPool.releaseConnection(conn);

        return response;
    }

    public String createRequest(String data) {
        String token = TokenUtils.generateNewToken();
        ApiRequest customerRequest = new ApiRequest(token, data);
        log.info("create request {}", customerRequest.toString());
        return gson.toJson(customerRequest);
    }


}
