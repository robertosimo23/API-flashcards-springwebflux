package dev.RobertoSimoes.reactiveflashcards.domain.service;

import dev.RobertoSimoes.reactiveflashcards.domain.document.UserDocument;
import dev.RobertoSimoes.reactiveflashcards.domain.repository.UserRepository;
import dev.RobertoSimoes.reactiveflashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserQueryService userQueryService;

    public Mono<UserDocument> save(final UserDocument document) {
        return userRepository.save(document)
                .doFirst(() -> log.info("=== try to save a follow document{}", document));

    }

    public Mono<UserDocument> update(final UserDocument document) {
        return userQueryService.findbyId(document.id())
                .map(user -> document.toBuilder().createdAt(user.createdAt())
                        .updatedAt(user.updatedAt())
                        .build())
                .flatMap(userRepository::save)
                .doFirst(()-> log.info("==== Try do update a user with follow info {}", document));

    }
}
