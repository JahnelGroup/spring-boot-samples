package com.example.springbootvalidation.by_annotation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserAnnotationController {

    /**
     * Validating using JSR-303 annotations.
     * @param user
     * @return
     */
    @PostMapping("/users/annotation")
    public String by_annotation(@Valid @RequestBody com.example.springbootvalidation.by_annotation.User user){
        return "PASSED";
    }

}
