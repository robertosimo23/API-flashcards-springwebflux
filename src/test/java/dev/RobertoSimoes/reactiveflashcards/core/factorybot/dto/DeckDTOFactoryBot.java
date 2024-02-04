package dev.RobertoSimoes.reactiveflashcards.core.factorybot.dto;

import com.github.javafaker.Faker;
import dev.RobertoSimoes.reactiveflashcards.domain.DTO.CardDTO;
import dev.RobertoSimoes.reactiveflashcards.domain.DTO.DeckDTO;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.RobertoSimoes.reactiveflashcards.core.factorybot.RandomData.getFaker;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class DeckDTOFactoryBot {

    public static DeckDTOFactoryBotBuilder builder() {
        return new DeckDTOFactoryBotBuilder();
    }

    public static class DeckDTOFactoryBotBuilder {
        private final String name;
        private final String info;
        private final String author;
        private final List<CardDTO> cards = new ArrayList<>();
        private final Faker faker = getFaker();

        public DeckDTOFactoryBotBuilder() {
            this.name = faker.name().name();
            this.info = faker.gameOfThrones().dragon();
            this.author = faker.name().fullName();
            generateCards();

        }

        private void generateCards() {
            int limit = faker.number().numberBetween(3, 8);
            cards.addAll(Stream.generate(() -> new CardDTO(faker.cat().name(), faker.color().name()))
                    .limit(faker.number().numberBetween(3, 8))
                    .collect(Collectors.toSet())

            );
        }

        public DeckDTO build() {
            return new DeckDTO(name, info, author, cards);
        }
    }
}