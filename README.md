# Student Management System

A REST API built with Spring Boot for managing student records. Started this as a mini project to get hands-on with Spring Boot, JPA, and related stuff.

## Tech

- Java 17, Spring Boot 3.3.0
- PostgreSQL
- Maven
- Spring Data JPA + Hibernate
- Lombok (saves a lot of boilerplate)
- SpringDoc OpenAPI / Swagger
- JUnit 5 + Mockito for tests
- Docker (multi-stage build)

## What it does

CRUD operations for students + search functionality. Each student has a roll number, name, email, phone, department, semester, and CGPA.

### Endpoints

| Method | Endpoint | What it does |
|--------|----------|-------------|
| POST | `/api/students` | Add a student |
| GET | `/api/students` | List all students |
| GET | `/api/students/{id}` | Get student by ID |
| PUT | `/api/students/{id}` | Update a student |
| DELETE | `/api/students/{id}` | Delete a student |
| GET | `/api/students/search/name?name=` | Search by name (partial) |
| GET | `/api/students/search/department?department=` | Search by department |
| GET | `/api/students/search/roll-number?rollNumber=` | Search by roll number (exact) |

## How to run

1. Make sure you have Java 17+, Maven 3.8+, and PostgreSQL running.
2. Create the database:
   ```
   CREATE DATABASE student_management_db;
   ```
3. Update `application.yml` if your Postgres credentials are different (defaults are postgres/postgres).
4. Run:
   ```
   cd backend
   mvn spring-boot:run
   ```
5. Swagger UI at `http://localhost:8080/swagger-ui.html`

### Docker

```
cd backend
docker build -t student-management -f DockerFile .
docker run -p 8080:8080 student-management
```

You'll need to pass the DB connection vars or link to a Postgres container though.

## Tests

28 tests across mapper, service, and controller layers.

```
cd backend
mvn test
```

## Project structure

```
backend/
├── src/main/java/com/studentmanagement/
│   ├── controller/    # REST endpoints
│   ├── service/       # business logic
│   ├── repository/    # data access (Spring Data JPA)
│   ├── entity/        # JPA entity
│   ├── dto/           # request/response objects
│   ├── mapper/        # entity <-> DTO conversion
│   └── exception/     # custom exceptions + global handler
└── src/test/java/com/studentmanagement/
    ├── controller/
    ├── service/
    └── mapper/
```

## Notes

- Uses `ddl-auto: update` for Hibernate — fine for dev, don't use this in production
- CORS is wide open (`*`) since there's no frontend tied to it yet
- Swagger docs at `/api-docs` and UI at `/swagger-ui.html`
- All config values can be overridden with env vars (`PORT`, `SPRING_DATASOURCE_URL`, etc.)
