package com.example.springbootvalidation.by_annotation.constraint;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * AlphaNumeric must be digits or letters only.
 */
public class AlphaNumericConstraint implements ConstraintValidator<AlphaNumeric, String> {

    private int minDigits;
    private int minLetters;

    @Override
    public void initialize(AlphaNumeric field) {
        minDigits = field.minDigits();
        minLetters = field.minLetters();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if ( value == null ) return false;
        Result result = getResult(value);
        return result != null && result.digits >= minDigits && result.letters >= minLetters;
    }

    /**
     * Returns counts as a wrapper class.
     *
     * @param value
     * @return
     */
    private Result getResult(String value){
        Result result = new Result();
        char[] chars = value.toCharArray();
        for(int i=0; i<chars.length; i++){
            if( Character.isDigit(chars[i]) ){
                result.digits++;
            }
            else if ( Character.isLetter(chars[i]) ){
                result.letters++;
            }
            else{
                // only digits and letters allowed, null indicates a failure.
                return null;
            }
        }
        return result;
    }

    class Result{
        int digits = 0;
        int letters = 0;
    }

}
