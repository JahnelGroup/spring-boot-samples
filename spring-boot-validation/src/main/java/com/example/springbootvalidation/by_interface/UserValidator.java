package com.example.springbootvalidation.by_interface;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        rejectIfEmptyOrWhitespace(errors, "username", "firstName", "lastName");
    }

    private void rejectIfEmptyOrWhitespace(Errors errors, String...fields){
        for(String field : fields){
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, field, "FieldIsEmpty");
        }
    }
}
