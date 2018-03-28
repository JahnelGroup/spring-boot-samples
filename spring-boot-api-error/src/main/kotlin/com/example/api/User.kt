package com.example.api

import javax.validation.constraints.NotNull

data class User(

    @NotNull
    var username: String,

    @NotNull
    var firstName: String,

    @NotNull
    var lastName: String
)