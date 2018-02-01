package com.jahnelgroup.rest.auction.links;

import com.jahnelgroup.rest.auction.Person;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonLinks {

    private final @NonNull EntityLinks entityLinks;

    /**
     * This serves no purpose other than demonstrating how you can add any
     * custom hyper-link to a resource.
     *
     * @param person
     * @return
     */
    Link getWeatherLink(Person person){
        return entityLinks.linkForSingleResource(person)
                .slash("https://www.google.com/search?q=weather+"+person.getState())
                .withRel("weather");
    }
}
