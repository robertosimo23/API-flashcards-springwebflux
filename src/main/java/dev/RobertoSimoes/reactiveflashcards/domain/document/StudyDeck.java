package dev.RobertoSimoes.reactiveflashcards.domain.document;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class StudyDeck {
    @Field("deck_id")
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
        private Set<StudyCard> cards = new HashSet<>();

        public StudyDeckBuilder deckId(final String deckId) {
            this.deckId = deckId;
            return this;
        }

        public StudyDeckBuilder cards(final Set<StudyCard> cards) {
            this.cards = cards;
            return this;
        }

        public StudyDeck build() {
            return new StudyDeck(deckId, cards);
        }
    }
}

