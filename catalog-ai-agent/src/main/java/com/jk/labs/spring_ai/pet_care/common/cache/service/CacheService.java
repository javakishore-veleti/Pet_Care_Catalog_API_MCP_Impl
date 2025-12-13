package com.jk.labs.spring_ai.pet_care.common.cache.service;

public interface CacheService {
    void put(String key, Object value);
    void put(String key, Object value, long ttlSeconds);
    Object get(String key);
    void delete(String key);
    void clear();
}
