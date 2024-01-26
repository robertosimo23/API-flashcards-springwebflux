package dev.RobertoSimoes.reactiveflashcards.domain.exception;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.text.MessageFormat;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class BasedErrorMessage {
    private final String DEFAULT_RESOURCE = "messages";

    public static final BasedErrorMessage GENERIC_EXCEPTION = new BasedErrorMessage(" generic");
    public static final BasedErrorMessage GENERIC_NOT_FOUND = new BasedErrorMessage(" generic.notFound");
    public static final BasedErrorMessage GENERIC_METHOD_NOT_ALLOW = new BasedErrorMessage(" generic.methodNotAllow");

    public static final BasedErrorMessage GENERIC_BAD_REQUEST = new BasedErrorMessage("generic.badRequest");

    public static final BasedErrorMessage USER_NOT_FOUND= new BasedErrorMessage("user.NotFound");
    public static final BasedErrorMessage DECK_NOT_FOUND= new BasedErrorMessage("deck.NotFound");
    public static final BasedErrorMessage EMAIL_ALREADY_USED = new BasedErrorMessage("user.EmailAlreadyUsed");
    public static final BasedErrorMessage STUDY_DECK_NOT_FOUND = new BasedErrorMessage("studyDeck.NotFound");
    public static final BasedErrorMessage DECK_IN_STUDY = new BasedErrorMessage("study.DeckInStudy");
    public static final BasedErrorMessage STUDY_NOT_FOUND = new BasedErrorMessage("study.NotFound");
    public static final BasedErrorMessage STUDY_QUESTION_NOT_FOUND = new BasedErrorMessage("studyQuestion.NotFound");



   private final String key;
   private String[] params;


   public  BasedErrorMessage params(final String... params){
       this.params = ArrayUtils.clone(params);
       return this;
   }
   public String getMessage(){
       String message;
       message = tryGetMessageFromBundle();
       if (ArrayUtils.isNotEmpty(params)){
           final var fmt = new MessageFormat(message);
           message= fmt.format(params);
       }
       return message;
   }

   private String tryGetMessageFromBundle(){
        return getResource().getString(key);
    }
    public ResourceBundle getResource (){
        return ResourceBundle.getBundle((DEFAULT_RESOURCE));
    }
}
