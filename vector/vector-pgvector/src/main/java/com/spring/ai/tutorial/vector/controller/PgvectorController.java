package com.spring.ai.tutorial.vector.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yingzi
 * @since 2025/9/3
 */
@RestController
@RequestMapping("/vector/pgvector")
public class PgvectorController {

    private static final Logger logger = LoggerFactory.getLogger(PgvectorController.class);

    private final PgVectorStore pgVectorStore;

    @Autowired
    public PgvectorController(@Qualifier("pgVectorStore") PgVectorStore vectorStore) {
        this.pgVectorStore = vectorStore;
    }

    @GetMapping("/add")
    public void add() {
        logger.info("start add data");

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", "12345");
        map.put("year", 2025);
        map.put("name", "yingzi");
        List<Document> documents = List.of(
                new Document("The World is Big and Salvation Lurks Around the Corner"),
                new Document("You walk forward facing the past and you turn back toward the future.", Map.of("year", 2024)),
                new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", map));
        pgVectorStore.add(documents);
    }

    @GetMapping("/search")
    public List<Document> search() {
        logger.info("start search data");
        return pgVectorStore.similaritySearch(SearchRequest
                .builder()
                .query("Spring")
                .topK(2)
                .build());
    }

}
