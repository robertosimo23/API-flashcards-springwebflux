package dev.RobertoSimoes.reactiveflashcards.API.Mapper;

import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.DeckRequest;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.DeckResponse;
import dev.RobertoSimoes.reactiveflashcards.domain.document.DeckDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeckMapper {


    @Mapping(target = "id",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    DeckDocument toDocument (final DeckRequest request);

    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    DeckDocument toDocument (final DeckRequest request, final String id);

    DeckResponse toResponse (final DeckDocument document);
}
