package dev.RobertoSimoes.reactiveflashcards.domain.service;

import com.github.javafaker.Faker;
import com.icegreen.greenmail.util.GreenMail;
import dev.RobertoSimoes.reactiveflashcards.api.Mapper.MailMapperImpl;
import dev.RobertoSimoes.reactiveflashcards.api.Mapper.MailMapperImpl_;
import dev.RobertoSimoes.reactiveflashcards.core.RetryConfig;
import dev.RobertoSimoes.reactiveflashcards.core.TemplateMailConfigStub;
import dev.RobertoSimoes.reactiveflashcards.core.extension.mail.MailSender;
import dev.RobertoSimoes.reactiveflashcards.core.extension.mail.MailServer;
import dev.RobertoSimoes.reactiveflashcards.core.extension.mail.MailServerExtension;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.document.DeckDocumentFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.document.StudyDocumentFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.core.factorybot.dto.MailMessageDTOFactoryBot;
import dev.RobertoSimoes.reactiveflashcards.domain.Mapper.MailMapper;
import dev.RobertoSimoes.reactiveflashcards.domain.helper.RetryHelper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thymeleaf.TemplateEngine;
import reactor.test.StepVerifier;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.util.Arrays;

import static dev.RobertoSimoes.reactiveflashcards.core.factorybot.RandomData.getFaker;
import static org.assertj.core.api.Assertions.assertThat;
@ContextConfiguration(classes = {MailMapperImpl.class,MailMapperImpl_.class} )
@ExtendWith({SpringExtension.class, MailServerExtension.class})
public class MailServiceTest {

    private final int port=1234;
    @Autowired
    private ApplicationContext applicationContext;
    private final RetryHelper retryHelper = new RetryHelper(new RetryConfig(1L, 1L));
    private JavaMailSender javaMailSender;
    private MailService mailService;
    private final Faker faker = getFaker();
    @Autowired
    private  MailMapper mailMapper;
    private GreenMail smtpServer;
    private final String sender = faker.internet().emailAddress();

    @BeforeEach
    void setup(@MailServer final GreenMail smtpServer, @MailSender final JavaMailSender mailSender) {
        this.smtpServer = smtpServer;
        TemplateEngine templateEngine = new TemplateMailConfigStub.templateEngine(applicationContext);
        mailService = new MailService(retryHelper, mailSender, templateEngine, mailMapper, sender);
    }

    @Test
    void sendTest() throws MessagingException {
        var deck = DeckDocumentFactoryBot.builder().build();
        var questions = StudyDocumentFactoryBot.builder(ObjectId.get().toString(), deck)
                .finishedStudy()
                .build()
                .questions();
        var mailMessage = MailMessageDTOFactoryBot.builder(deck, questions).build();
        StepVerifier.create(mailService.send(mailMessage)).verifyComplete();
        assertThat(smtpServer.getReceivedMessages().length).isOne();
        var message = Arrays.stream(smtpServer.getReceivedMessages()).findFirst().orElseThrow();
        assertThat(message.getSubject()).isEqualTo(mailMessage.subject());
        assertThat(message.getRecipients(Message.RecipientType.TO)).contains(new InternetAddress(mailMessage.destination()));
        assertThat(message.getHeader("FROM")).contains(sender);
    }
}
