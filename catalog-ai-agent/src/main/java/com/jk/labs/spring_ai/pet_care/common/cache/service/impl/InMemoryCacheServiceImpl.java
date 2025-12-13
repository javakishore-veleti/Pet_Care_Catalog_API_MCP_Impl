package com.jk.labs.spring_ai.pet_care.common.cache.service.impl;

import com.jk.labs.spring_ai.pet_care.common.cache.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InMemoryCacheServiceImpl implements CacheService {

    @Override
    public void put(String key, Object value) {
        // TODO: Implement in-memory cache
    }

    @Override
    public void put(String key, Object value, long ttlSeconds) {
        // TODO: Implement in-memory cache with TTL
    }

    @Override
    public Object get(String key) {
        // TODO: Implement cache retrieval
        return null;
    }

    @Override
    public void delete(String key) {
        // TODO: Implement cache deletion
    }

    @Override
    public void clear() {
        // TODO: Implement cache clear
    }
}
