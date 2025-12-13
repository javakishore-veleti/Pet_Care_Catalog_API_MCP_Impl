package com.jk.labs.spring_ai.pet_care.common.vector.service;

import com.jk.labs.spring_ai.pet_care.common.vector.model.Embedding;
import com.jk.labs.spring_ai.pet_care.common.vector.model.VectorSearchResult;
import java.util.List;

public interface VectorStoreService {
    void store(Embedding embedding);
    List<VectorSearchResult> search(List<Float> queryVector, int topK);
    void delete(String id);
}
