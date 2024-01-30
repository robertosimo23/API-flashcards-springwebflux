package dev.RobertoSimoes.reactiveflashcards.domain.Mapper;

import dev.RobertoSimoes.reactiveflashcards.domain.DTO.CardDTO;
import dev.RobertoSimoes.reactiveflashcards.domain.DTO.DeckDTO;
import dev.RobertoSimoes.reactiveflashcards.domain.document.Card;
import dev.RobertoSimoes.reactiveflashcards.domain.document.DeckDocument;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR,componentModel = "spring")
public interface DeckDoMainMapper {

    @Mapping(target = "description",source = "info")
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "createdAt" , ignore = true)
    @Mapping(target = "updatedAt" , ignore = true)
    DeckDocument toDocument(final DeckDTO dto);
    @Mapping(target = "back",source = "answer")
    @Mapping(target = "front",source = "ask")
    Card toDocument(final CardDTO dto);
}
