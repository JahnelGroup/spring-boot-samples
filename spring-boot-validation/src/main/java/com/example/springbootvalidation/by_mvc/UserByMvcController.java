package com.example.springbootvalidation.by_mvc;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserByMvcController {

    /**
     * Display the user form.
     *
     * @return
     */
    @GetMapping("/user")
    public String showForm(com.example.springbootvalidation.by_mvc.User user) {
        return "user";
    }

    /**
     * Validating the user form.
     *
     * @param user
     * @return
     */
    @PostMapping("/user")
    public String byInterface(@Valid com.example.springbootvalidation.by_mvc.User user, BindingResult bindingResult){
        return "user";
    }
}
