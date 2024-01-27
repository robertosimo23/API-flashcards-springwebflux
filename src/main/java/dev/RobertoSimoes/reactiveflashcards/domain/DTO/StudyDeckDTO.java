package dev.RobertoSimoes.reactiveflashcards.domain.DTO;

import dev.RobertoSimoes.reactiveflashcards.domain.document.StudyCard;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class StudyDeckDTO {
    private String Id;
    private Set<StudyCard> cards;

    public static StudyDeckBuilder builder() {
        return new StudyDeckBuilder();
    }

    public StudyDeckBuilder toBuilder() {
        return new StudyDeckBuilder(Id, cards);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudyDeckBuilder {
        private String deckId;
        private Set<StudyCardDTO> cards = new HashSet<>();

        public StudyDeckBuilder deckId(final String deckId) {
            this.deckId = deckId;
            return this;
        }

        public StudyDeckBuilder cards(final Set<StudyCardDTO> cards) {
            this.cards = cards;
            return this;
        }

        public StudyDeckDTO build() {
            return new StudyDeckDTO(deckId, cards);
        }
    }
}

