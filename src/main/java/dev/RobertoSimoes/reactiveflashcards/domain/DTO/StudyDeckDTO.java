package dev.RobertoSimoes.reactiveflashcards.domain.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder(toBuilder = true)
public class StudyDeckDTO{
    public StudyDeckDTO(String deckId, Set<StudyCardDTO> cards) {
        this.deckId = deckId;
        this.cards = cards;
    }

    private String deckId;
    private Set <StudyCardDTO> cards;
}




