package dev.RobertoSimoes.reactiveflashcards.core.mongo;

import dev.RobertoSimoes.reactiveflashcards.core.mongo.converter.DateToOffsetDateTimeConverter;
import dev.RobertoSimoes.reactiveflashcards.core.mongo.converter.OffsetDateTimeToDateConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.core.convert.converter.Converter;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class MongoConfig {

    @Bean
    MongoCustomConversions mongoCustomConversions(){
        final List<Converter<?,?>> converters = new ArrayList<>();
        converters.add(new OffsetDateTimeToDateConverter());
        converters.add(new DateToOffsetDateTimeConverter());
        return new MongoCustomConversions(converters);

    }
}
