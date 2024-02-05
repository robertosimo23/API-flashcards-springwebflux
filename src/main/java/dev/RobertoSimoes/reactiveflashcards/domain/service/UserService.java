package dev.RobertoSimoes.reactiveflashcards.domain.service;

import dev.RobertoSimoes.reactiveflashcards.domain.document.UserDocument;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.EmailAlreadyUsedException;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.NotFoundException;
import dev.RobertoSimoes.reactiveflashcards.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static dev.RobertoSimoes.reactiveflashcards.domain.exception.BasedErrorMessage.EMAIL_ALREADY_USED;

@AllArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserQueryService userQueryService;

    public Mono<UserDocument> save(final UserDocument document){
        return userQueryService.findByEmail(document.email())
                .doFirst(() -> log.info("==== Try to save a follow user {}", document))
                .filter(Objects::isNull)
                .switchIfEmpty(Mono.defer(() ->Mono.error(new EmailAlreadyUsedException(EMAIL_ALREADY_USED
                        .params(document.email()).getMessage()))))
                .onErrorResume(NotFoundException.class, e -> userRepository.save(document));
    }

    private Mono<Void> verifyEmail(final UserDocument document){
        return userQueryService.findByEmail(document.email())
                .filter(stored -> stored.id().equals(document.id()))
                .switchIfEmpty(Mono.defer(() ->Mono.error(new EmailAlreadyUsedException(EMAIL_ALREADY_USED
                        .params(document.email()).getMessage()))))
                .onErrorResume(NotFoundException.class, e -> Mono.empty())
                .then();
    }

    public Mono<UserDocument> update(final UserDocument document){
        return verifyEmail(document)
                .then(Mono.defer(() -> userQueryService.findById(document.id())
                        .map(user -> document.toBuilder()
                                .createdAt(user.createdAt())
                                .updatedAt(user.updatedAt())
                                .build())
                        .flatMap(userRepository::save)
                        .doFirst(() -> log.info("==== Try to update a user with follow info {}", document))));
    }

    public Mono<Void> delete(final String id){
        return userQueryService.findById(id)
                .flatMap(userRepository::delete)
                .doFirst(() -> log.info("==== Try to delete a user with follow id {}", id));
    }

}
