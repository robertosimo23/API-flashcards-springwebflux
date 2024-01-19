package dev.RobertoSimoes.reactiveflashcards.API.Mapper;

import dev.RobertoSimoes.reactiveflashcards.API.Controller.request.UserRequest;
import dev.RobertoSimoes.reactiveflashcards.API.Controller.response.UserResponse;
import dev.RobertoSimoes.reactiveflashcards.domain.document.UserDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper  {

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    UserDocument toDocument(final UserRequest request);


    UserResponse toResponse(final UserDocument document);

}
