package com.jk.labs.spring_ai.pet_care.common.cache.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheEntry {
    private String key;
    private Object value;
    private Long ttl;
    private Long timestamp;
}
