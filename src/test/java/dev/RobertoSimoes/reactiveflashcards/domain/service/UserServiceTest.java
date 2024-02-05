package dev.RobertoSimoes.reactiveflashcards.domain.service;

import dev.RobertoSimoes.reactiveflashcards.core.factorybot.document.UserDocumentFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.domain.document.UserDocument;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.EmailAlreadyUsedException;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.NotFoundException;
import dev.RobertoSimoes.reactiveflashcards.domain.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.OffsetDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserQueryService userQueryService;
    private UserService userService;

    @BeforeEach
    void setup() {
        userService = new UserService(userRepository, userQueryService);
    }

    @Test
    void saveTest() {
        var document = UserDocumentFactoryBot.builder().build();
        when(userQueryService.findByEmail(any(String.class))).thenReturn(Mono.error(new NotFoundException("")));
        when(userRepository.save(any(UserDocument.class))).thenAnswer(invocation -> {
            var user = invocation.getArgument(0,UserDocument.class);
            return Mono.just(user.toBuilder()
                    .id(ObjectId.get().toString())
                    .createdAt(OffsetDateTime.now())
                    .updatedAt(OffsetDateTime.now())
                    .build());
        });
        StepVerifier.create(userService.save(document))
                .assertNext(actual ->{
                    assertThat(actual).isNotNull();
                    assertThat(actual).usingRecursiveComparison()
                            .ignoringFields("id","createdAt","updateAt")
                            .isEqualTo(document);
                    assertThat(actual).isNotNull();
                    assertThat(actual.createdAt()).isNotNull();
                    assertThat(actual.updatedAt()).isNotNull();
                })
                .verifyComplete();
        verify(userRepository).save(any(UserDocument.class));
        verify(userQueryService).findByEmail(anyString());
    }
    @Test
    void whenTryToSaveUserWithExistingEmailThenThrowError(){
        var document = UserDocumentFactoryBot.builder().build();
        when(userQueryService.findByEmail(anyString())).thenReturn(Mono.just(UserDocumentFactoryBot.builder().build()));

        StepVerifier.create(userService.save(document))
                .verifyError(NotFoundException.class);
        verify(userRepository, times(0)).save(any(UserDocument.class));
        verify(userQueryService).findByEmail(anyString());
    }

    private static Stream<Arguments> updateTest(){
        var document = UserDocumentFactoryBot.builder().build();
        var storedDocument = UserDocumentFactoryBot.builder().preUpdate(document.id()).build();
        return Stream.of(
                Arguments.of(document, Mono.error(new NotFoundException("")), storedDocument),
                Arguments.of(document, Mono.just(storedDocument), storedDocument)
        );
    }
    @MethodSource
    @ParameterizedTest
    void updateTest(final UserDocument toUpdate, final Mono<UserDocument> mockFindByEmail, final UserDocument mockFindById) {
        when(userQueryService.findByEmail(anyString())).thenReturn(mockFindByEmail);
        when(userQueryService.findById(anyString())).thenReturn(Mono.just(mockFindById));
        when(userRepository.save(any(UserDocument.class))).thenAnswer(invocation -> {
            var user = invocation.getArgument(0, UserDocument.class);
            return Mono.just(user.toBuilder()
                    .updatedAt(OffsetDateTime.now())
                    .build());
        });
        StepVerifier.create(userService.update(toUpdate))
                .assertNext(actual -> {
                    assertThat(actual).usingRecursiveComparison()
                            .ignoringFields("createdAt", "updatedAt")
                            .isNotEqualTo(mockFindById);
                    assertThat(actual).usingRecursiveComparison()
                            .ignoringFields("createdAt", "updatedAt")
                            .isEqualTo(toUpdate);
                })
                .verifyComplete();
        verify(userQueryService).findByEmail(anyString());
        verify(userRepository).save(any(UserDocument.class));
        verify(userQueryService).findById(anyString());
    }

    @Test
    void whenTryToUpdateUserWithEmailUsedByOtherThenThrowError(){
        var document = UserDocumentFactoryBot.builder().build();
        when(userQueryService.findByEmail(anyString())).thenReturn(Mono.just(UserDocumentFactoryBot.builder().build()));

        StepVerifier.create(userService.update(document))
                .verifyError(EmailAlreadyUsedException.class);
        verify(userQueryService).findByEmail(anyString());
        verify(userQueryService, times(0)).findById(anyString());
        verify(userRepository, times(0)).save(any(UserDocument.class));
    }
    @Test
    void whenTryToUpdateUserNonStoredThenThrowError() {
        var document = UserDocumentFactoryBot.builder().build();
        when(userQueryService.findByEmail(anyString())).thenReturn(Mono.error(new NotFoundException(" ")));
        when(userQueryService.findById(anyString())).thenReturn(Mono.error(new NotFoundException(" ")));

        StepVerifier.create(userService.update(document))
                .verifyError(NotFoundException.class);
        verify(userQueryService).findByEmail(anyString());
        verify(userQueryService).findById(anyString());
        verify(userRepository, times(0)).save(any(UserDocument.class));

    }


    @Test
    void deleteTest() {
        when(userQueryService.findById(any(String.class))).thenReturn(Mono.just(UserDocumentFactoryBot.builder().build()));
        when(userRepository.delete(any(UserDocument.class))).thenReturn(Mono.empty());

        StepVerifier.create((userService.delete(ObjectId.get().toString())))
                .verifyComplete();
        verify(userRepository).delete(any(UserDocument.class));
        verify(userQueryService).findById(any(String.class));
    }

    @Test
    void whenTryToDeleteNonStoredUserThenThrowError() {
        when(userQueryService.findById(any(String.class))).thenReturn(Mono.error(new NotFoundException("")));

        StepVerifier.create((userService.delete(ObjectId.get().toString())))
                .verifyError(NotFoundException.class);
        verify(userQueryService).findById(any(String.class));
        verifyNoInteractions(userRepository);
    }

}