package dev.RobertoSimoes.reactiveflashcards.domain.repository;

import com.github.javafaker.Faker;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.UserPageRequest;
import dev.RobertoSimoes.reactiveflashcards.core.EmbeddedMongoDBConfig;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.document.UserDocumentFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.request.UserPageRequestFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.domain.document.UserDocument;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static dev.RobertoSimoes.reactiveflashcards.API.Controller.request.UserSortBy.EMAIL;
import static dev.RobertoSimoes.reactiveflashcards.API.Controller.request.UserSortBy.NAME;
import static dev.RobertoSimoes.reactiveflashcards.API.Controller.request.UserSortDirection.ASC;
import static dev.RobertoSimoes.reactiveflashcards.API.Controller.request.UserSortDirection.DESC;
import static dev.RobertoSimoes.reactiveflashcards.core.factorybot.RandomData.getFaker;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {EmbeddedMongoDBConfig.class})

public class UserRepositoryImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRepositoryImpl userRepositoryImpl;
    private final Faker faker = getFaker();
    private final List<UserDocument> storedDocuments = new ArrayList<>();

    @BeforeEach
    void setup() {
        var users = Stream.generate(() -> UserDocumentFactoryBot.builder().build()).limit(15).toList();
        storedDocuments.addAll(Objects.requireNonNull(userRepository.saveAll(users).collectList().block()));

    }

    @Test
    void combineAllOptions() {
        var selectUser = storedDocuments.get(faker.number().numberBetween(0, storedDocuments.size()));
        var sentence = faker.bool().bool() ? selectUser.name().substring(1, 2) : selectUser.email().substring(1, 2);
        var pageRequest = UserPageRequestFactoryBot.builder().build()
                .toBuilder()
                .sentence(sentence)
                .limit(20)
                .page(0L)
                .build();
        StepVerifier.create(userRepositoryImpl.findOnDemand(pageRequest))
                .recordWith(ArrayList::new)
                .thenConsumeWhile(actual -> true)
                .consumeRecordedWith(actual -> assertThat(actual).isNotEmpty()).verifyComplete();
    }

    @Test
    void checkFindOnDemandFilterBySentenceTest() {
        var selectUser = storedDocuments.get(faker.number().numberBetween(0, storedDocuments.size()));
        var sentence = faker.bool().bool() ? selectUser.name().substring(1, 2) : selectUser.email().substring(1, 2);
        var pageRequest = UserPageRequest.builder().sentence(sentence).build();
        StepVerifier.create(userRepositoryImpl.findOnDemand(pageRequest))
                .recordWith(ArrayList::new)
                .thenConsumeWhile(actual -> true)
                .consumeRecordedWith(actual -> {
                    var actualList = new ArrayList<>(actual);
                    var expectSize = storedDocuments.stream().filter(u -> u.name().contains(sentence) || u.email().contains(sentence))
                            .count();
                    assertThat(actual.size()).isEqualTo(expectSize);
                    assertThat(actualList).isSortedAccordingTo(Comparator.comparing(UserDocument::name));

                })
                .verifyComplete();
    }

    @Test
    void checkCountFilterBySentenceTest() {
        var selectUser = storedDocuments.get(faker.number().numberBetween(0, storedDocuments.size()));
        var sentence = faker.bool().bool() ? selectUser.name().substring(1, 2) : selectUser.email().substring(1, 2);
        var pageRequest = UserPageRequest.builder().sentence(sentence).build();
        StepVerifier.create(userRepositoryImpl.count(pageRequest))
                .assertNext(actual -> {
                    var expectSize = storedDocuments.stream().filter(u -> u.name().contains(sentence) || u.email().contains(sentence))
                            .count();
                    assertThat(actual).isEqualTo(expectSize);
                })
                .verifyComplete();
    }

    private static Stream<Arguments> verifySort() {
        return Stream.of(
                Arguments.of(
                        UserPageRequest.builder().sortBy(NAME).sortDirection(ASC).build(),
                        Comparator.comparing(UserDocument::name)
                ),
                Arguments.of(
                        UserPageRequest.builder().sortBy(NAME).sortDirection(DESC).build(),
                        Comparator.comparing(UserDocument::name).reversed()
                ),
                Arguments.of(
                        UserPageRequest.builder().sortBy(EMAIL).sortDirection(ASC).build(),
                        Comparator.comparing(UserDocument::email)
                ),
                Arguments.of(
                        UserPageRequest.builder().sortBy(EMAIL).sortDirection(DESC).build(),
                        Comparator.comparing(UserDocument::email).reversed()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("verifySort")
    void verifySort(final UserPageRequest pageRequest, final Comparator<UserDocument> comparator) {
        StepVerifier.create(userRepositoryImpl.findOnDemand(pageRequest))
                .recordWith(ArrayList::new)
                .thenConsumeWhile(actual -> true)
                .consumeRecordedWith(actual -> {
                    var actualList = new ArrayList<>(actual);
                    assertThat(actualList).isSortedAccordingTo(comparator);
                })
                .verifyComplete();
    }

    @AfterEach
    void teardown() {
        userRepository.deleteAll().block();
        storedDocuments.clear();
    }


}
