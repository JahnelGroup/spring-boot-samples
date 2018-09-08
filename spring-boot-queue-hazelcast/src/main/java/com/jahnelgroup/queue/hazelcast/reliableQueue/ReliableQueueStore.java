package com.jahnelgroup.queue.hazelcast.reliableQueue;

import com.hazelcast.core.QueueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ReliableQueueStore implements QueueStore<ReliableMessage> {

    @Autowired
    ReliableQueueRepo repo;

    @Override
    public void store(Long key, ReliableMessage value) {
        repo.save(value);
    }

    @Override
    public void storeAll(Map<Long, ReliableMessage> map) {
        repo.save(map.values());
    }

    @Override
    public void delete(Long key) {
        repo.delete(key);
    }

    @Override
    public void deleteAll(Collection<Long> keys) {
        for (Long key : keys) {
            repo.delete(key);
        }
    }

    @Override
    public ReliableMessage load(Long key) {
        return repo.findOne(key);
    }

    @Override
    public Map<Long, ReliableMessage> loadAll(Collection<Long> keys) {
        Map<Long, ReliableMessage> map = new HashMap<>();
        for(ReliableMessage msg : repo.findAll(keys)){
            map.put(msg.getId(), msg);
        }
        return map;
    }

    @Override
    public Set<Long> loadAllKeys() {
        Set<Long> keys = new HashSet<Long>();
        for(ReliableMessage m : repo.findAll()){
            keys.add(m.getId());
        }
        return keys;
    }
}
