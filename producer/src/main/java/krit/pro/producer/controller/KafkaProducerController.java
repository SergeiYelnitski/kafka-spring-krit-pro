package krit.pro.producer.controller;

import krit.pro.producer.model.Greeting;
import krit.pro.producer.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProducerController {
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public KafkaProducerController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/send-message-simple")
    public void sendMessageSimple(@RequestBody Greeting message) {
        kafkaProducerService.sendMessageSimple(message);
    }

    @PostMapping("/send-message")
    public void sendMessage(@RequestBody Greeting message) {
        kafkaProducerService.sendMessage(message);
    }
}
