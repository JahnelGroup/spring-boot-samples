package com.example.springbootvalidation;

import com.example.springbootvalidation.by_interface.UserValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    private UserValidator userValidator = new UserValidator();

    /**
     * Validating using JSR-303 annotations.
     * @param user
     * @return
     */
    @PostMapping("/users/annotation")
    public String by_annotation(@Valid @RequestBody com.example.springbootvalidation.by_annotation.User user){
        return "PASSED";
    }


    /**
     * Validating with Spring Validator interface directly.
     * @param user
     * @return
     */
    @PostMapping("/users/interface")
    public List<ObjectError> byInterface(@RequestBody  com.example.springbootvalidation.by_interface.User user){
        Errors errors = new BeanPropertyBindingResult(user, "user");
        userValidator.validate(user, errors);
        return errors.getAllErrors();
    }
}
