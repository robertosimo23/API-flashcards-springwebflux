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
