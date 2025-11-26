package com.avoris.hotel.kafka;

import com.avoris.hotel.dto.SearchMessage;
import com.avoris.hotel.models.SearchEntity;
import com.avoris.hotel.util.Constants;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class SearchProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public SearchProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishSearchEntity(SearchEntity searchEntity) {
        System.out.println("producer - send SearchEntity");
        Message<SearchEntity> message = getSearchEntityMessage(searchEntity);
        this.kafkaTemplate.send(message);
    }

    public void publishMessage(SearchMessage searchMessage) {
        System.out.println("producer - send SearchMessage");
        Message<SearchMessage> message = MessageBuilder
                .withPayload(searchMessage)
                .setHeader(KafkaHeaders.TOPIC, Constants.HOTEL_SEARCH_TOPIC)
                .build();
        this.kafkaTemplate.send(message);
    }

    private Message<SearchEntity> getSearchEntityMessage(SearchEntity searchEntity) {
        return MessageBuilder
                .withPayload(searchEntity)
                .setHeader(KafkaHeaders.TOPIC, Constants.HOTEL_SEARCH_TOPIC)
                .build();
    }

}
