package com.example.springbootvalidation.by_interface;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserByInterfaceController {

    @InitBinder
    public void init(WebDataBinder webDataBinder){
        webDataBinder.addValidators(new UserValidator());
    }

    /**
     * Validating with Spring Validator interface directly.
     * @param user
     * @return
     */
    @PostMapping("/users/interface")
    public String byInterface(@Valid @RequestBody com.example.springbootvalidation.by_interface.User user){
        return "PASSED";
    }
}
