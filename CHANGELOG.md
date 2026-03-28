# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.0] - 2025-09-27

### Added
- **Chat Modules**: OpenAI, DeepSeek, and Alibaba DashScope chat model implementations
- **Vector Stores**: Neo4j, PGVector, Elasticsearch, Redis, and simple in-memory vector stores
- **RAG Systems**: Retrieval-Augmented Generation examples with various data sources
- **Graph Processing**: Workflow nodes with parallel execution and human-in-the-loop capabilities
- **MCP Integration**: Model Context Protocol servers and clients with SSE support
- **Function Calling**: Tool calling implementations for various AI models
- **Structured Output**: Enhanced output formatting and validation
- **Observability**: Langfuse integration for monitoring and tracing
- **Memory Management**: Chat memory advisors and context management
- **Authentication**: MCP web server with authentication and recovery mechanisms

### Changed
- **Spring AI Alibaba**: Updated to version 1.0.0.4 with enhanced compatibility
- **Dependencies**: Upgraded to Spring Boot 3.4.5 and Spring AI 1.0.2
- **Build System**: Maven-based multi-module project structure
- **Configuration**: Centralized configuration patterns for AI services

### Fixed
- **Authentication**: Resolved auth issues in MCP web server
- **Memory**: Fixed Redis memory management and Neo4j memory waiting
- **Elasticsearch**: Adjusted version compatibility with Spring AI 1.0.0
- **DeepThink**: Enhanced deep thinking capabilities
- **Gateway**: Fixed MCP gateway integration issues

## [0.9.0] - 2025-08-31

### Added
- **Deep Think**: Enhanced reasoning capabilities for AI models
- **React Agent**: Reactive agent implementation for real-time processing
- **Reflection Agent**: Self-reflection capabilities for AI agents
- **Chat Settings**: Configuration examples for chat model customization
- **Parallel Stream Node**: Enhanced parallel processing capabilities

### Fixed
- **MCP Streamable**: Removed deprecated streaming functionality
- **Version Conflicts**: Resolved dependency version issues

## [0.8.0] - 2025-07-29

### Added
- **MCP Gateway**: Gateway integration for MCP services
- **Dynamic Tool Client**: Dynamic tool calling capabilities
- **MCP Direct Client**: Direct client implementation for MCP
- **MCP Recovery**: Recovery mechanisms for MCP connections

### Fixed
- **Dependency Issues**: Resolved artifact resolution problems with Spring AI Alibaba MCP gateway

## [0.7.0] - 2025-07-15

### Added
- **MCP Auth**: Authentication support for MCP servers
- **MCP Recovery**: Connection recovery examples
- **Thread Management**: Enhanced thread handling for async operations
- **Tool Optimization**: Performance improvements for tool calling

### Changed
- **Configuration**: Updated MCP server configurations

## [0.6.0] - 2025-06-27

### Added
- **MCP Integration**: Model Context Protocol support
- **Nacos Integration**: Service discovery and configuration management
- **Observability**: Monitoring and tracing capabilities
- **Graph Nodes**: Enhanced graph processing with parallel and human nodes

### Fixed
- **Memory Management**: Improved chat memory handling
- **SSE Updates**: Enhanced Server-Sent Events functionality

## [0.5.0] - 2025-06-13

### Added
- **Graph Processing**: Workflow and graph processing capabilities
- **Parallel Nodes**: Parallel execution support for graph workflows
- **MCP Nodes**: MCP protocol integration in graph workflows
- **Evaluation**: Model evaluation and benchmarking tools

### Changed
- **Project Structure**: Reorganized modules for better maintainability

## [0.4.0] - 2025-05-31

### Added
- **ETL Pipeline**: Data processing pipeline examples
- **Memory Advisors**: Enhanced chat memory management
- **Vector Redis**: Redis-based vector store implementation
- **RAG Modules**: Comprehensive RAG implementation examples

### Fixed
- **Configuration**: Resolved namespace and configuration issues

## [0.3.0] - 2025-05-28

### Added
- **Advisor Memory**: Memory management for chat advisors
- **MCP Examples**: Model Context Protocol implementation examples
- **Elasticsearch**: Elasticsearch integration for vector storage

### Fixed
- **Vector/RAG**: Adjusted vector store and RAG implementations

## [0.2.0] - 2025-05-26

### Added
- **Vector Stores**: Multiple vector store implementations
- **RAG Systems**: Retrieval-Augmented Generation examples
- **Tool Calling**: Function calling capabilities for AI models
- **Output Parsing**: Enhanced response parsing and formatting

### Changed
- **Documentation**: Updated README files with comprehensive guides

## [0.1.0] - 2025-05-24

### Added
- **Chat Modules**: Basic chat model implementations
- **Project Structure**: Initial Maven multi-module setup
- **Documentation**: Basic project documentation

## [0.0.1] - 2025-05-23

### Added
- **Initial Project**: Basic Spring AI tutorial project structure
- **Core Dependencies**: Spring Boot 3.4.5 and Spring AI 1.0.2 setup