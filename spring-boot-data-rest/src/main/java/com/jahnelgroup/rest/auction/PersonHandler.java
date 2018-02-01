package com.jahnelgroup.rest.auction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Spring Data REST has built in events for the Lifecycle of exposed Entity's
 * as shown here: https://docs.spring.io/spring-data/rest/docs/current/reference/html/#events
 *
 * Here is an example of the hierarchy.
 *
 * org.springframework.context.ApplicationEvent
 *      +
 *      | is-a
 *      |
 * org.springframework.data.rest.core.event.RepositoryEvent
 *      +
 *      | is-a
 *      |
 * org.springframework.data.rest.core.event.BeforeCreateEvent
 *
 * It's important to note that these event handler's are synchronous and any work performed
 * in them will hold the request until it's completed.
 *
 */
@Component
@RepositoryEventHandler
public class PersonHandler {

    private Logger log = LoggerFactory.getLogger(PersonHandler.class);

    @HandleBeforeCreate
    public void beforeCreate(Person p){
        log.info("beforeCreate[thread={}] {}", thread(), p);
    }

    @HandleAfterCreate
    public void afterCreate(Person p){
        log.info("afterCreate[thread={}]  {}", thread(), p);
    }

    @HandleBeforeSave
    public void beforeSave(Person p){
        log.info("beforeSave[thread={}]  {}", thread(), p);
    }

    @HandleAfterSave
    public void afterSave(Person p){
        log.info("afterSave[thread={}]  {}", thread(), p);
    }

    @HandleBeforeDelete
    public void beforeDelete(Person p){
        log.info("beforeLinkSave[thread={}]  {}", thread(), p);
    }

    @HandleAfterDelete
    public void afterDelete(Person p){
        log.info("afterLinkSave[thread={}]  {}", thread(), p);
    }

    @HandleBeforeLinkSave
    public void beforeLinkSave(Person p){
        log.info("beforeLinkSave[thread={}]  {}", thread(), p);
    }

    @HandleAfterLinkSave
    public void afterLinkSave(Person p){
        log.info("afterLinkSave[thread={}]  {}", thread(), p);
    }

    private String thread(){
        return Thread.currentThread().getName();
    }

}