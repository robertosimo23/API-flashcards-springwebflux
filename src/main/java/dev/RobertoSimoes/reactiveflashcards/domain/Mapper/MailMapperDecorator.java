package dev.RobertoSimoes.reactiveflashcards.domain.Mapper;

import dev.RobertoSimoes.reactiveflashcards.domain.DTO.MailMessageDTO;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.MimeMessageHelper;

public abstract class MailMapperDecorator implements MailMapper {

    @Qualifier("delegate")
    @Autowired
    private MailMapper mailMapper;

    @Override
    public MimeMessageHelper toMimeMessageHelper(final MimeMessageHelper helper, final MailMessageDTO mailMessageDTO,
                                                 final String sender, final String body) throws MessagingException {
        mailMapper.toMimeMessageHelper(helper, mailMessageDTO, sender, body);
        helper.setText(body, true);
        return helper;
    }
}
