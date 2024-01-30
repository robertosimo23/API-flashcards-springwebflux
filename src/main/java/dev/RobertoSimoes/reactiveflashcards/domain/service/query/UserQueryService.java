package dev.RobertoSimoes.reactiveflashcards.domain.service.query;

import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.UserPageRequest;
import dev.RobertoSimoes.reactiveflashcards.domain.DTO.UserPageDocument;
import dev.RobertoSimoes.reactiveflashcards.domain.document.UserDocument;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.EmailAlreadyUsedException;
import dev.RobertoSimoes.reactiveflashcards.domain.exception.NotFoundException;
import dev.RobertoSimoes.reactiveflashcards.domain.repository.UserRepository;
import dev.RobertoSimoes.reactiveflashcards.domain.repository.UserRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static dev.RobertoSimoes.reactiveflashcards.domain.exception.BasedErrorMessage.EMAIL_ALREADY_USED;
import static dev.RobertoSimoes.reactiveflashcards.domain.exception.BasedErrorMessage.USER_NOT_FOUND;

@Service
@Slf4j
@AllArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;
    private final UserRepositoryImpl userRepositoryImpl;

    public Mono<UserDocument> findById(final String id){
        return userRepository.findById(id)
                .doFirst(() -> log.info("==== try to find user with id {}", id))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(USER_NOT_FOUND.params("id", id).getMessage()))));
    }

    public Mono<UserDocument> findByEmail(final String email){
        return userRepository.findByEmail(email)
                .doFirst(() -> log.info("==== try to find user with email {}", email))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(USER_NOT_FOUND.params("email",email).getMessage()))));
    }

    public Mono<UserPageDocument> findOnDemand(final UserPageRequest request){
        return userRepositoryImpl.findOnDemand(request)
                .collectList()
                .zipWhen(documents -> userRepositoryImpl.count(request))
                .map(tuple -> UserPageDocument.builder()
                        .limit(request.limit())
                        .currentPage(request.page())
                        .totalItems(tuple.getT2())
                        .content(tuple.getT1())
                        .build());
    }



}

