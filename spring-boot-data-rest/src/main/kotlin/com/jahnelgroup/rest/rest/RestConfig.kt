package com.jahnelgroup.rest.rest

import com.jahnelgroup.rest.data.auction.Auction
import com.jahnelgroup.rest.data.auction.bid.Bid
import com.jahnelgroup.rest.data.user.User
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter


@Configuration
class RestConfig(private var applicationContext: ApplicationContext) : RepositoryRestConfigurerAdapter() {

    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration) {
        config.exposeIdsFor(
                Auction::class.java,
                Bid::class.java,
                User::class.java
        )

        config.setBasePath("api")
    }

}