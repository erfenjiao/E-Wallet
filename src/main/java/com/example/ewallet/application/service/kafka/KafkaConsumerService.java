package com.example.ewallet.application.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "ewallet_topic", groupId = "ewallet-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}
