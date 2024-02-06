package dev.RobertoSimoes.reactiveflashcards.domain.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class StudyDeckDTO{
    public StudyDeckDTO(String deckId,
                        Set<StudyCardDTO> cards) {

    }
@Builder(toBuilder = true)
    public StudyDeckDTO() { }

    public Collection<Object> cards() {
        return null;
    }

    public String deckID() {
        return null;
    }
}




