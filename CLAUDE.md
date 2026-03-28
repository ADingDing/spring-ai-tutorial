# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a comprehensive Spring AI tutorial project demonstrating various AI capabilities including chat models, vector stores, RAG (Retrieval-Augmented Generation), MCP (Model Context Protocol), and graph processing workflows. The project uses Spring Boot 3.4.5, Spring AI 1.0.2, and Spring AI Alibaba 1.0.0.4 with JDK 21.

## Common Development Commands

### Build Commands
```bash
# Build the entire project
mvn clean compile

# Build a specific module
mvn clean compile -pl <module-name>

# Run a specific module
mvn spring-boot:run -pl <module-name>

# Run tests
mvn test

# Package the project
mvn clean package
```

### Module Examples
```bash
# Run OpenAI chat module
mvn spring-boot:run -pl chat/openai-chat

# Run vector store with Neo4j
mvn spring-boot:run -pl vector/vector-neo4j

# Run RAG with Elasticsearch
mvn spring-boot:run -pl rag/rag-elasticsearch

# Run MCP server
mvn spring-boot:run -pl mcp/server/mcp-webflux-server
```

## Architecture Overview

### Core Module Structure
- **chat/** - Chat model implementations (OpenAI, DeepSeek, Alibaba)
- **vector/** - Vector store implementations (Neo4j, PGVector, Elasticsearch, Redis, Simple)
- **rag/** - Retrieval-Augmented Generation examples
- **advisor/** - Chat advisors and memory management
- **graph/** - Graph processing and workflow nodes with parallel execution
- **mcp/** - Model Context Protocol servers and clients
- **tool-calling/** - Function calling implementations
- **structured-output/** - Structured output handling
- **observation/** - Observability and monitoring

### Key Configuration Patterns

#### Chat Model Configuration (Alibaba DashScope)
```yaml
spring:
  ai:
    openai:
      api-key: ${DASHSCOPE_API_KEY}
      base-url: https://dashscope.aliyuncs.com/compatible-mode
      chat:
        options:
          model: qwen-max
      embedding:
        options:
          model: text-embedding-v1
```

#### Vector Store Configuration (Neo4j)
```yaml
spring:
  ai:
    vectorstore:
      neo4j:
        initialize-schema: true
        database-name: neo4j
        index-name: yingzi_index
        embedding-dimension: 1536
        distance-type: cosine
  neo4j:
    uri: bolt://localhost:7687
    authentication:
      username: neo4j
      password: yingzi_password
```

#### MCP Server Configuration
```yaml
spring:
  ai:
    mcp:
      server:
        name: webflux-mcp-server
        version: 1.0.0
        type: ASYNC
        sse-message-endpoint: /mcp/messages
        capabilities:
          tool: true
          resource: true
          prompt: true
          completion: true
```

## Common Code Patterns

### Chat Controller Pattern
```java
@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultAdvisors(new MessageChatMemoryAdvisor(...))
                .build();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestParam String query) {
        return chatClient.prompt(query).stream().content();
    }
}
```

### Vector Store Controller Pattern
```java
@RestController
@RequestMapping("/vector")
public class VectorController {
    private final VectorStore vectorStore;

    public VectorController(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @GetMapping("/add")
    public void addDocuments() {
        List<Document> documents = List.of(
            new Document("Content", Map.of("meta", "data"))
        );
        vectorStore.add(documents);
    }

    @GetMapping("/search")
    public List<Document> search() {
        return vectorStore.similaritySearch(SearchRequest.builder()
                .query("Spring AI")
                .topK(2)
                .build());
    }
}
```

### Function/Tool Implementation
```java
public class TimeService implements Function<TimeService.Request, TimeService.Response> {
    @Override
    public Response apply(Request request) {
        return new Response("Current time: " + Instant.now());
    }

    @JsonClassDescription("Get current time")
    public record Request(@JsonPropertyDescription("Time zone") String timeZone) {}
    public record Response(String description) {}
}
```

### Graph Workflow Pattern
```java
@Configuration
public class GraphConfig {

    @Bean
    public StateGraph<String, String> workflow() {
        return StateGraph.builder()
                .addNode("start", this::startNode)
                .addNode("process", this::processNode)
                .addEdge("start", "process")
                .setStartNode("start")
                .build();
    }

    private Flux<String> startNode(StateContext<String, String> context) {
        return Flux.just("Starting workflow...");
    }
}
```

## Environment Variables

Commonly used environment variables:
- `DASHSCOPE_API_KEY` - Alibaba AI API key
- `OPENAI_API_KEY` - OpenAI API key
- `NEO4J_USERNAME`, `NEO4J_PASSWORD` - Neo4j credentials
- `POSTGRES_USER`, `POSTGRES_PASSWORD` - PostgreSQL credentials

## Testing Strategy

- Each module contains its own test cases
- Tests typically verify controller endpoints and service functionality
- Integration tests check actual AI model responses and vector store operations

## Dependencies

### Core Dependencies
- Spring Boot 3.4.5
- Spring AI 1.0.2
- Spring AI Alibaba 1.0.0.4
- JDK 21

### Key Features
- Reactive programming with WebFlux
- Vector stores (Neo4j, PGVector, Elasticsearch, Redis)
- Chat memory management
- MCP protocol support
- Graph workflow processing
- Function calling capabilities
- Observability integration

## Module-Specific Notes

### MCP Modules
- Use SSE (Server-Sent Events) for real-time communication
- Support both sync and async modes
- Include authentication and recovery mechanisms

### Graph Modules
- Support parallel node execution
- Include human-in-the-loop workflows
- Stream processing capabilities

### Vector Store Modules
- Multiple backend support (in-memory, Redis, Neo4j, PGVector, Elasticsearch)
- Schema initialization options
- Configurable distance metrics and indexing strategies