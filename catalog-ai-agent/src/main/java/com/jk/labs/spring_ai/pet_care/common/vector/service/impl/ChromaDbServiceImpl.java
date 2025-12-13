package com.jk.labs.spring_ai.pet_care.common.vector.service.impl;

import com.jk.labs.spring_ai.pet_care.common.vector.model.Embedding;
import com.jk.labs.spring_ai.pet_care.common.vector.model.VectorSearchResult;
import com.jk.labs.spring_ai.pet_care.common.vector.service.VectorStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class ChromaDbServiceImpl implements VectorStoreService {

    @Override
    public void store(Embedding embedding) {
        // TODO: Implement ChromaDB storage
    }

    @Override
    public List<VectorSearchResult> search(List<Float> queryVector, int topK) {
        // TODO: Implement vector search
        return null;
    }

    @Override
    public void delete(String id) {
        // TODO: Implement deletion
    }
}
