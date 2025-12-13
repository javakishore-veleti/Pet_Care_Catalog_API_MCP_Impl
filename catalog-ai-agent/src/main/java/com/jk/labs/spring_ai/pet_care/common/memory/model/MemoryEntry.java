package com.jk.labs.spring_ai.pet_care.common.memory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoryEntry {
    private String role;
    private String content;
    private Long timestamp;
    private Object metadata;
}
