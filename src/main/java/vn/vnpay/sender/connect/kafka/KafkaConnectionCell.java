package vn.vnpay.sender.connect.kafka;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Arrays;
import java.util.Properties;

@Slf4j
@Setter
@Getter
public class KafkaConnectionCell {
    private long relaxTime;
    private long timeOut;


    private String topic;

    private boolean isClosed;
    private KafkaConsumer<String, String> consumer;

    private KafkaProducer<String, String> producer;

    public KafkaConnectionCell(Properties consumerProps, Properties producerProps,  String topic, long timeOut) {
        producer = new KafkaProducer<String, String>(producerProps);
        consumer = new KafkaConsumer<>(consumerProps);
        this.topic = topic;
        this.timeOut = timeOut;
        consumer.subscribe(Arrays.asList("reply-topic"));
        log.info("Subscribed to topic " + topic);
    }

    public String sendAndReceive(String message) {

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "Hello world");
        producer.send(record, (recordMetadata, e) -> {
            if (e == null) {
                log.info("Successfully received the details as: \n" +
                        "Topic:" + recordMetadata.topic() + "\n" +
                        "Partition:" + recordMetadata.partition() + "\n" +
                        "Offset" + recordMetadata.offset() + "\n" +
                        "Timestamp" + recordMetadata.timestamp());
            }

            else {
                log.error("Can't produce,getting error",e);
            }
        });

        // polling
//        while (true) {
//            ConsumerRecords<String, String> records = consumer.poll(100);
//            for (ConsumerRecord<String, String> r : records) {
//                log.info("----");
//                log.info("rabbit begin receiving data: offset = %d, key = %s, value = %s\n",
//                        r.offset(), r.key(), r.value());
//            }
//        }

        return "ok";
    }

    public boolean isTimeOut() {
        if (System.currentTimeMillis() - this.relaxTime > this.timeOut) {
            return true;
        }
        return false;
    }

    public void close() {
        try {
            consumer.close();
            isClosed = true;
        } catch (Exception e) {
            log.warn("connection is closed: {0}", e);
        }
    }
}
