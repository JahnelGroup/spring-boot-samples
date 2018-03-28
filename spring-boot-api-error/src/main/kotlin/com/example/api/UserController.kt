package com.example.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class UserController {

    @PostMapping("/users")
    fun create(@Valid @RequestBody user: User) = user

}