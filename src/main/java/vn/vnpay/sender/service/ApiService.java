package vn.vnpay.sender.service;

import com.google.gson.Gson;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vnpay.sender.connect.kafka.KafkaConnectionPool;
import vn.vnpay.sender.connect.kafka.KafkaConsumerConnectionPool;
import vn.vnpay.sender.connect.kafka.KafkaProducerConnectionPool;
import vn.vnpay.sender.connect.kafka.runnable.KafkaConsumerRunner;
import vn.vnpay.sender.connect.kafka.runnable.KafkaProducerRunner;
import vn.vnpay.sender.connect.kafka.runnable.KafkaSendAndReceiveCallable;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionCell;
import vn.vnpay.sender.connect.rabbit.RabbitConnectionPool;
import vn.vnpay.sender.models.ApiRequest;
import vn.vnpay.sender.util.ExecutorSingleton;
import vn.vnpay.sender.util.GsonSingleton;
import vn.vnpay.sender.util.TokenUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class ApiService {
    private static final Logger log = LoggerFactory.getLogger(RabbitConnectionCell.class);

//    OracleConnectionPool oracleConnectionPool = OracleConnectionPool.getInstancePool();
    RabbitConnectionPool rabbitConnectionPool = RabbitConnectionPool.getInstancePool();
    KafkaConnectionPool kafkaConnectionPool = KafkaConnectionPool.getInstancePool();
    KafkaProducerConnectionPool producerPool = KafkaProducerConnectionPool.getInstancePool();
    KafkaConsumerConnectionPool consumerPool = KafkaConsumerConnectionPool.getInstancePool();
    private Gson gson = GsonSingleton.getInstance().getGson();

    public String sendToCore(String data)  {

        String message = createRequest(data);

//        RabbitConnectionCell conn = rabbitConnectionPool.getConnection();
//        String response = conn.sendAndReceive(message);
//        rabbitConnectionPool.releaseConnection(conn);
        
        ExecutorSingleton.getInstance().getExecutorService().submit(new KafkaConsumerRunner());
        ExecutorSingleton.getInstance().getExecutorService().submit(new KafkaConsumerRunner());
        ExecutorSingleton.getInstance().getExecutorService().submit(new KafkaConsumerRunner());

        Future future = ExecutorSingleton.getInstance().getExecutorService().submit(new KafkaProducerRunner(message));
        String response = null;
        try {
            response = (String) future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
//        try {
//            response = future.get();
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
        return response;
    }

    public String createRequest(String data) {
        String token = TokenUtils.generateNewToken();
        ApiRequest customerRequest = new ApiRequest(token, data);
        log.info("create request {}", customerRequest.toString());
        return gson.toJson(customerRequest);
    }


}
