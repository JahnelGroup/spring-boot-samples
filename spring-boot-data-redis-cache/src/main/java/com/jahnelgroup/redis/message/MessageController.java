package com.jahnelgroup.redis.message;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/messages")
@RestController
@RequiredArgsConstructor
public class MessageController {

    @NonNull
    private MessageRepo messageRepo;

    /**
     * Create a new message and proactively update the cache with the key
     * as auto-generated id from the database.
     *
     * @param text
     * @return
     */
    @Caching(
        put = {
            @CachePut(value = "messages", key = "#result.id", condition = "#result != null")
        }
    )
    @PostMapping
    public Message create(@RequestParam String text){
        Message m = messageRepo.save(new Message(text));
        log.info("Created {}", m);
        return m;
    }

    /**
     * Get an existing message either from the cache or the database.
     *
     * @param id
     * @return
     */
    @Cacheable(value = "messages", key = "#id", unless="#result == null")
    @GetMapping("/{id}")
    public Message get(@PathVariable Long id){
        log.info("Cache miss for id {} - going to the database.", id);
        return messageRepo.findById(id).orElseThrow(() -> new NotFoundExcepton());
    }

}

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundExcepton extends RuntimeException{}