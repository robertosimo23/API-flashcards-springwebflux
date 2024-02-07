package dev.RobertoSimoes.reactiveflashcards.core;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import dev.RobertoSimoes.reactiveflashcards.core.mongo.converter.DateToOffsetDateTimeConverter;
import dev.RobertoSimoes.reactiveflashcards.core.mongo.converter.OffsetDateTimeToDateConverter;
import dev.RobertoSimoes.reactiveflashcards.core.mongo.provider.OffsetDateTimeProvider;
import dev.RobertoSimoes.reactiveflashcards.domain.repository.DeckRepository;
import org.bson.json.Converter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@TestConfiguration
@EnableReactiveMongoRepositories(basePackageClasses = DeckRepository.class)
public class EmbeddedMongoDBConfig extends AbstractReactiveMongoConfiguration {

    @Bean
    MongoClient mongoClient() {
        return MongoClients.create();
    }

    @NotNull
    @Override
    protected String getDatabaseName() {
        return "reactive-flashcards";
    }

    @Bean
    ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(mongoClient(), getDatabaseName());
    }

    @NotNull
    @Override
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new DateToOffsetDateTimeConverter());
        converters.add(new OffsetDateTimeToDateConverter());
        return new MongoCustomConversions(converters);
    }

    @Bean
    DateTimeProvider dateTimeProvider() {
        return new OffsetDateTimeProvider();
    }

}
