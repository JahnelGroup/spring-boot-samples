package com.example.springbootvalidation.by_annotation;

import com.example.springbootvalidation.by_annotation.constraint.AlphaNumeric;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class User {

    @NotBlank
    @AlphaNumeric(minLetters = 3, minDigits = 2)
    private String username;

    @NotBlank
    private String firstName;

    @NotBlank
    @Size(min = 2, max=10)
    private String lastName;

}
