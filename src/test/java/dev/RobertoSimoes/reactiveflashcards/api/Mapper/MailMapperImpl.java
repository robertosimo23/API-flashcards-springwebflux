package dev.RobertoSimoes.reactiveflashcards.api.Mapper;

import dev.RobertoSimoes.reactiveflashcards.domain.DTO.MailMessageDTO;
import dev.RobertoSimoes.reactiveflashcards.domain.DTO.StudyDTO;
import dev.RobertoSimoes.reactiveflashcards.domain.Mapper.MailMapperDecorator;
import dev.RobertoSimoes.reactiveflashcards.domain.document.DeckDocument;
import dev.RobertoSimoes.reactiveflashcards.domain.document.UserDocument;

public class MailMapperImpl extends MailMapperDecorator {
    public MailMapperImpl(MailMapperImpl_ mailMapperImpl) {
        super();
    }

    @Override
    public MailMessageDTO toDTO(StudyDTO study, DeckDocument deck, UserDocument user) {
        return null;
    }
}
