package com.jahnelgroup.rest.rest

import com.jahnelgroup.rest.data.auction.Auction
import com.jahnelgroup.rest.data.bid.Bid
import com.jahnelgroup.rest.data.user.User
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter

@Configuration
class RestConfig : RepositoryRestConfigurerAdapter() {

    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration?) {
        config!!.exposeIdsFor(
            User::class.java,
            Auction::class.java,
            Bid::class.java
        )

        config.setBasePath("api")
    }

}