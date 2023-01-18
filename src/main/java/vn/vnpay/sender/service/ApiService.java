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



//        KafkaConnectionCell conn = kafkaConnectionPool.getConnection();
//        String response = conn.sendAndReceive(message);
//        kafkaConnectionPool.releaseConnection(conn);

        String topic = "test-topic";
        String bootstrapServers="127.0.0.1:9092";
        String grp_id="kafka";

        Properties consumerProps = new Properties();
        consumerProps.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        consumerProps.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,   StringDeserializer.class.getName());
        consumerProps.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        consumerProps.setProperty(ConsumerConfig.GROUP_ID_CONFIG,grp_id);
        consumerProps.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        Properties producerConfig = new Properties();
        producerConfig.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerConfig.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerConfig.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        KafkaConnectionCell kafkaConnectionCell = new KafkaConnectionCell(consumerProps, producerConfig, topic, 1000);
        kafkaConnectionCell.sendAndReceive(message);

        return "ok";
    }

    public String createRequest(String data) {
        String token = TokenUtils.generateNewToken();
        ApiRequest customerRequest = new ApiRequest(token, data);
        log.info("create request {}", customerRequest.toString());
        return gson.toJson(customerRequest);
    }


}
