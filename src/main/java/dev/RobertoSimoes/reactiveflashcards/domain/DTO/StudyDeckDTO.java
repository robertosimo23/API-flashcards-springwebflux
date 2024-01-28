package dev.RobertoSimoes.reactiveflashcards.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class StudyDeckDTO {
    private String Id;
    private Set<StudyCardDTO> cards;

    public Set<StudyCardDTO> cards(){
        return this.cards;
    }

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

