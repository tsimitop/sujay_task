# Tag Management API

A Spring Boot Java application that provides CRUD operations for managing **Tags**. The application uses **H2 database** for storage and is packaged with **Maven**. It supports both in-container file-based persistence for deployment and in-memory operations for testing.

---

## Features

- Full CRUD operations for `Tag` entities
- REST API exposed at `/api/tags`
- In-memory H2 database for local testing
- File-based H2 persistence when running in Docker
- Automatic API documentation using **OpenAPI** available at `/docs`
- Unit and integration tests for all CRUD operations

---

## Tag Entity

A `Tag` consists of the following fields:

| Field       | Type      | Description                    |
|------------ |---------- |--------------------------------|
| `id`       | Long      | Unique identifier (auto-generated) |
| `title`    | String    | Tag title                       |
| `description` | String | Tag description                 |
| `createdAt` | LocalDateTime | Timestamp when created       |
| `updatedAt` | LocalDateTime | Timestamp when last updated  |

---

## API Endpoints

The REST API is exposed at `/api/tags`:

- `GET /api/tags` – Get all tags
- `GET /api/tags/{id}` – Get a single tag by ID
- `POST /api/tags` – Create a new tag
- `PUT /api/tags/{id}` – Update an existing tag
- `DELETE /api/tags/{id}` – Delete a tag

---

## API Documentation

OpenAPI documentation is available at `/docs`:

- `openapi.yaml` – OpenAPI specification
- `index.html` – Browse the API documentation in a web browser

---

## Running the Application

### Prerequisites

- Java 21
- Maven
- Docker (optional, for containerized deployment)

### Running Locally

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   make build-run
   ```
   
2. The app is accessible at http://localhost:8080/h2-console/
    In the JDBC URL field write ```jdbc:h2:file:/data/testdb``` and click on connect.

3. Now you can check the contents of the database.
