package dev.RobertoSimoes.reactiveflashcards.api.controller;

import com.github.javafaker.Faker;
import dev.RobertoSimoes.reactiveflashcards.API.exceptionHandler.*;
import dev.RobertoSimoes.reactiveflashcards.ReactiveFlashcardsApplication;
import dev.RobertoSimoes.reactiveflashcards.core.mongo.provider.OffsetDateTimeProvider;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.DeckInStudyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static dev.RobertoSimoes.reactiveflashcards.core.factorybot.RandomData.getFaker;

@ActiveProfiles("test")
@ContextConfiguration(classes = {OffsetDateTimeProvider.class, ApiExceptionHandler.class,
        DeckInStudyException.class, EmailAlreadyUsedHandler.class, MethodNotAllowedHandler.class, NotFoundHandler.class,
        ConstraintViolationHandler.class, WebExchangeBindHandler.class, ResponseStatusHandler.class,
        ReactiveFlashCardsHandler.class, Exception_Generic_Handler.class, JsonProcessingHandler.class, ReactiveFlashcardsApplication.class})
public abstract class AbstractControllerTest {
    @MockBean
    protected MappingMongoConverter mappingMongoConverter;

    @Autowired
    protected ApplicationContext applicationContext;

    protected final static Faker faker = getFaker();
}
