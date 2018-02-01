package com.jahnelgroup.rest.auction.links;

import com.jahnelgroup.rest.auction.Person;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonResourceProcessor implements ResourceProcessor<Resource<Person>> {

    @NonNull
    private final PersonLinks personLinks;

    /**
     * Here you can place conditional statements to determine what links
     * should be shown in the API response.
     *
     * It's important to note that these links DO NOT prevent the client from
     * directly calling them, it's purely to help drive a consumer experience.
     *
     * For example client-side code could detect if this link is present to
     * conditionally display a weather widget.
     *
     * @param p
     * @return
     */
    @Override
    public Resource<Person> process(Resource<Person> p) {
        if( p.getContent().getState() != null ){
            p.add(personLinks.getWeatherLink(p.getContent()));
        }
        return p;
    }

}
