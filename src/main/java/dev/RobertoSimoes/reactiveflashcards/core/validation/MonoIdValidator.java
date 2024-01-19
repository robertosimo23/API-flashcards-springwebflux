package dev.RobertoSimoes.reactiveflashcards.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

@Slf4j
public class MonoIdValidator implements ConstraintValidator<MongoId, String> {

    @Override
    public void initialize(MongoId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        log.info("=== checking if {} is a valid mongoDB id",value);
        return StringUtils.isNotBlank(value)&& ObjectId.isValid(value);

    }
}
