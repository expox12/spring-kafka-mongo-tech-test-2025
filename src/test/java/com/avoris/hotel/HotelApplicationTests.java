package com.avoris.hotel;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.kafka.autoconfigure.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude = KafkaAutoConfiguration.class)
class HotelApplicationTests {

	@Test
	void contextLoads() {
	}

}
