package com.jahnelgroup.rest.data.user.links

import com.jahnelgroup.rest.common.context.UserContextService
import com.jahnelgroup.rest.data.user.User
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.webmvc.RepositoryLinksResource
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks
import org.springframework.hateoas.Link
import org.springframework.hateoas.Resource
import org.springframework.hateoas.ResourceProcessor
import org.springframework.hateoas.Resources

/**
 * Add custom links to Resource's with a ResourceProcessor
 */
@Configuration
class UserLinksConfig (
    private val entityLinks: RepositoryEntityLinks,
    private val userContextService: UserContextService
) {

    /**
     * Add a link to be displayed when listing a collection of User's
     *
     *  Example: /api/users
     */
    @Bean
    fun userCollectionLinks() = ResourceProcessor<Resources<Resource<User>>> { res ->
        res.add(entityLinks.linkToSingleResource(userContextService.getCurrentUser()).withRel("me"))
        res
    }

    /**
     * Add a link to be displayed when listing a single User
     *
     *  Example: /api/users/1
     */
    @Bean
    fun userSingleLinks() = ResourceProcessor<Resource<User>> { res ->
        res.add(Link("https://www.google.com/search?q="+userContextService.getCurrentUsername()).withRel("google"))
        res
    }

    /**
     * Add a link to the very top level API.
     *
     *  Example: /api
     */
    @Bean
    fun apiLink() = ResourceProcessor<RepositoryLinksResource> { res ->
        res.add(Link("http://top-level-link").withRel("example"))
        res
    }

}