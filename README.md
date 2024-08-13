# Backend

# Technical Stack

The project uses the following technologies and dependencies:

## Core Technologies
- **Java 17**: The project is built using Java 17, ensuring access to the latest language features and performance improvements.
- **Spring Boot 3.3.1**: A robust framework for building Java applications, providing a streamlined development experience with embedded servers, autoconfiguration, and more.
- **Spring Data JPA**: For working with relational databases using the Java Persistence API (JPA).
- **Spring Security**: Handles authentication and authorization within the application.
- **Spring Web**: Used for building web applications and RESTful APIs.
- **Spring Kafka**: Integration with Apache Kafka for event-driven architecture.
- **Spring Validation**: Ensures data integrity and validation across the application.
- **Spring Mail**: Provides email sending capabilities.

## OpenAPI and Documentation
- **SpringDoc OpenAPI**: Generates OpenAPI documentation for the RESTful web services.

## Utility Libraries
- **Guava**: A Google library that provides immutable collections and additional utilities.
- **ExpiringMap**: Provides a map implementation with expiration capabilities, useful for caching.

## Lombok
- **Lombok**: Reduces boilerplate code in Java, such as getters, setters, and constructors, through annotations.

## Databases
- **H2 Database**: An in-memory database useful for development and testing.
- **MySQL**: A relational database used for production.

## Testing
- **Spring Boot Test**: Provides testing support for Spring Boot applications.
- **Spring Security Test**: Offers utilities for testing Spring Security configurations.
- **JUnit Platform Launcher**: A test engine for running JUnit tests.

## JWT Authentication
- **JJWT**: A library for creating and verifying JSON Web Tokens (JWT).
- **Bouncy Castle**: A cryptography library, used in conjunction with JJWT for secure token handling.

## QueryDSL
- **QueryDSL JPA**: A framework for constructing type-safe SQL-like queries in JPA.
- **Jakarta Annotations and Persistence**: Provides annotations and API specifications for building applications compatible with Jakarta EE.

## WebClient and Logging
- **Spring WebFlux**: Includes WebClient, a non-blocking, reactive HTTP client for accessing APIs.
- **Spring Boot Logging**: Default logging framework integrated into the application.
- **Netty Resolver for macOS**: A native DNS resolver for macOS, resolving potential DNS issues on the platform.

## AWS Integration
- **Spring Cloud AWS**: Provides integration with Amazon Web Services, particularly for handling S3 storage for images.

## XML Handling
- **Jackson XML**: A Jackson module for working with XML data format.

This comprehensive stack provides robust support for building secure, scalable, and high-performance applications with Spring Boot, leveraging modern Java features and a wide range of libraries for various functionalities.
