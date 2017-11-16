package com.jahnelgroup.elasticsearch.injestor.person

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/person")
class PersonController {

    @RequestMapping("/{id}")
    fun findById(@PathVariable("id") id : Int) : Person {
        return Person(firstName = "Steven", lastName = "Zgaljic")
    }

    fun findByLastName(@RequestParam("lastName") lastName : String) : Person{
        return Person(firstName = "Steven", lastName = "Zgaljic")
    }

}