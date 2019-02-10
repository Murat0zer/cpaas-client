package com.netas.cpaas.user.validation;

import com.google.common.base.Joiner;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 20),
                new UppercaseCharacterRule(1),
                new DigitCharacterRule(1),
                new SpecialCharacterRule(1),
                new WhitespaceRule()));

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                Joiner.on(",").join(validator.getMessages(result)))
                .addConstraintViolation();
        return false;
    }

    @Override
    public void initialize(Password constraintAnnotation) {
        /**
         * Initializes the validator in preparation for
         * {@link #isValid(Object, ConstraintValidatorContext)} calls.
         * The constraint annotation for a given constraint declaration
         * is passed.
         * <p/>
         * This method is guaranteed to be called before any use of this instance for
         * validation.
         *
         * @param constraintAnnotation annotation instance for a given constraint declaration
         */
    }
}
