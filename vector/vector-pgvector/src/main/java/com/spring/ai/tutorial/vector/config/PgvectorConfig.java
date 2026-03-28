package com.spring.ai.tutorial.vector.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author yingzi
 * @since 2025/9/3
 */
@Configuration
public class PgvectorConfig {

    @Value("${spring.ai.vectorstore.pgvector.index-type}")
    private PgVectorStore.PgIndexType indexType;
    @Value("${spring.ai.vectorstore.pgvector.distance-type}")
    private PgVectorStore.PgDistanceType distanceType;
    @Value("${spring.ai.vectorstore.pgvector.dimensions}")
    private int dimensions;
    @Value("${spring.ai.vectorstore.pgvector.max-document-batch-size}")
    private int maxDocumentBatchSize;

    @Bean("pgVectorStore")
    public PgVectorStore vectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .dimensions(dimensions)                    // Optional: defaults to model dimensions or 1536
                .distanceType(distanceType)       // Optional: defaults to COSINE_DISTANCE
                .indexType(indexType)                     // Optional: defaults to HNSW
                .initializeSchema(true)              // Optional: defaults to false
                .schemaName("public")                // Optional: defaults to "public"
                .vectorTableName("vector_store")     // Optional: defaults to "vector_store"
                .maxDocumentBatchSize(maxDocumentBatchSize)         // Optional: defaults to 10000
                .build();
    }
}
