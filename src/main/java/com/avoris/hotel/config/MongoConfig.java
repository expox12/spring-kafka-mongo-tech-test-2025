package com.avoris.hotel.config;

import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of(
                new LocalDateToDateUTCConverter(),
                new DateToLocalDateUTCConverter()
        ));
    }

    static class LocalDateToDateUTCConverter implements Converter<LocalDate, Date> {

        @Override
        public @Nullable Date convert(LocalDate source) {
            return Date.from(source.atStartOfDay().toInstant(ZoneOffset.UTC));
        }
    }

    static class DateToLocalDateUTCConverter implements Converter<Date, LocalDate> {

        @Override
        public @Nullable LocalDate convert(Date source) {
            return source.toInstant().atOffset(ZoneOffset.UTC).toLocalDate();
        }
    }
}
