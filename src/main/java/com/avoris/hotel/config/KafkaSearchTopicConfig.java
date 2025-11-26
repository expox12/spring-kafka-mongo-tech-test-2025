package com.avoris.hotel.config;

import com.avoris.hotel.util.Constants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaSearchTopicConfig {

    @Bean
    public NewTopic hotelSearchTopic() {
        return TopicBuilder.name(Constants.HOTEL_SEARCH_TOPIC).build();
    }

}
