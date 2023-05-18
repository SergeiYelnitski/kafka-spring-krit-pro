package krit.pro.producer.service;

import krit.pro.producer.model.Greeting;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, Greeting> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Greeting> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessageSimple(Greeting msg) {
        kafkaTemplate.send("topic-1", msg);
    }

    public void sendMessage(Greeting message) {
        CompletableFuture<SendResult<String, Greeting>> future = kafkaTemplate.send("topic-1", message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        message + "] due to : " + ex.getMessage());
            }
        });
    }
}
