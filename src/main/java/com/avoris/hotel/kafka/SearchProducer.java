package com.avoris.hotel.kafka;

import com.avoris.hotel.dto.SearchMessage;
import com.avoris.hotel.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class SearchProducer {
    private static final Logger log = LoggerFactory.getLogger(SearchProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public SearchProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishMessage(SearchMessage searchMessage) {
        Message<SearchMessage> message = MessageBuilder
                .withPayload(searchMessage)
                .setHeader(KafkaHeaders.TOPIC, Constants.HOTEL_SEARCH_TOPIC)
                .build();
        this.kafkaTemplate.send(message);
        log.info("Publishing message in kafka topic: {}", Constants.HOTEL_SEARCH_TOPIC);
    }

}
