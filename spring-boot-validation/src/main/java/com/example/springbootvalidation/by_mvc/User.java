package com.example.springbootvalidation.by_mvc;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class User {

    @NotBlank
    private String username;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

}
