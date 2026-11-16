# OrderFlow — Inventory & Order Management API

OrderFlow is a Java Spring Boot REST API connected to a PostgreSQL database. I am developing this project to strengthen my skills with backend development, REST APIs, databases, and automated testing.

The current version supports product management, with inventory tracking and order processing planned for future development.

## Technologies
- Java 21
- Spring Boot
- PostgreSQL
- Spring Data JPA
- Maven
- JUnit, Mockito, and MockMvc
- Postman

## Features
- Create, retrieve, and update products
- Store product information in PostgreSQL
- Validate product data
- Handle invalid requests and duplicate SKUs
- Automated API testing

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/products | Get all products |
| GET | /api/products/{id} | Get product by ID |
| POST | /api/products | Create product |
| PUT | /api/products/{id} | Update product |

## Running the Project

Requirements: Java 21, PostgreSQL, and Maven.

1. Create and configure the PostgreSQL database.
2. Configure the database connection in `application.properties`.
3. Set the `ORDERFLOW_DB_PASSWORD` environment variable.
4. Run the application:

   ./mvnw spring-boot:run

The API currently runs at `http://localhost:8080`.

## Testing
OrderFlow uses JUnit, Mockito, and MockMvc to test product operations, validation, and error handling.

Run automated tests using:

    ./mvnw clean test

The project currently has 8 passing automated tests covering product operations and error handling with 0 failed tests.

## Future Improvements
- Inventory tracking
- Customer orders and order fulfillment
- Inventory reservations
- React dashboard

## Status
OrderFlow is currently in development. The product management API is implemented and tested, with additional features actively being developed. 