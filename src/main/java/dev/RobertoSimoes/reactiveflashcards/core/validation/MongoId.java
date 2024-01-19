package dev.RobertoSimoes.reactiveflashcards.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {MonoIdValidator.class})
public @interface MongoId {

    String message() default "{br.com.devRobertoSimões.reactiveflahcards.MongoId.message}";

    Class<?>[] group() default {};

    Class<? extends Payload>[] payload() default {};
}
