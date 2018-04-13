package com.example.springbootvalidation.by_annotation;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class User {

    @NotBlank
    private String username;

    @NotBlank
    private String firstName;

    @NotBlank
    @Size(min = 2, max=10)
    private String lastName;

}
