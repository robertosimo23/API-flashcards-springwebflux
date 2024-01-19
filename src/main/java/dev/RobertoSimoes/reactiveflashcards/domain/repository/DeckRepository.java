package dev.RobertoSimoes.reactiveflashcards.domain.repository;

import dev.RobertoSimoes.reactiveflashcards.domain.document.DeckDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeckRepository extends ReactiveMongoRepository<DeckDocument, String> {

}
