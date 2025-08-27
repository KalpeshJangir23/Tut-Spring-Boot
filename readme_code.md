# Department API Project

This document outlines the structure and implementation of a Spring Boot Department API project, addressing issues in the provided description and providing a clear, corrected, and comprehensive guide.

## Project Structure

The project follows a layered architecture:

- **Controllers**: Handle HTTP requests and responses.
- **Service**: Contains business logic.
- **Repository (Data Access Layer)**: Interacts with the database.
- **Database**: Uses H2 (in-memory SQL database).

## H2 Database Configuration

The `application.properties` file configures the H2 in-memory database.

```properties
spring.application.name=departmentApi
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:dcbapp
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
```

**Fixes Applied**:

- Changed `spring.application.name` to `departmentApi` for clarity.
- Added `spring.jpa.hibernate.ddl-auto=update` to automatically create/update database schema based on entities.

## Entity Layer

The `Department` entity represents a department in the database.

```java
package com.example.department.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long departmentId;
    private String name;
    private String address;

    // Default constructor
    public Department() {}

    // Parameterized constructor
    public Department(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // Getters and Setters
    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // toString
    @Override
    public String toString() {
        return "Department{" +
               "departmentId=" + departmentId +
               ", name='" + name + '\'' +
               ", address='" + address + '\'' +
               '}';
    }
}
```

**Fixes Applied**:

- Used `jakarta.persistence` instead of `javax.persistence` (modern standard for Jakarta EE).
- Ensured all fields, constructors, getters, setters, and `toString` are properly defined.

## Repository Layer

The `DepartmentRepository` interface extends `JpaRepository` to interact with the database.

```java
package com.example.department.repository;

import com.example.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
```

**Fixes Applied**:

- Corrected the interface name to `DepartmentRepository` (was `DepartmentRepo`).
- Removed unnecessary custom query like `findByEmail` (not relevant for Department).
- Properly imported the `Department` entity.

## Service Layer

### Interface

The `DepartmentService` interface defines the contract for business logic.

```java
package com.example.department.service;

import com.example.department.entity.Department;
import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    Department saveDepartment(Department department);
    List<Department> getAllDepartments();
    Optional<Department> getDepartmentById(Long id);
    Department updateDepartment(Long id, Department department);
    void deleteDepartment(Long id);
}
```

### Implementation

The `DepartmentServiceImpl` class implements the service logic.

```java
package com.example.department.service;

import com.example.department.entity.Department;
import com.example.department.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public Department updateDepartment(Long id, Department department) {
        Optional<Department> existingDepartment = departmentRepository.findById(id);
        if (existingDepartment.isPresent()) {
            Department updatedDepartment = existingDepartment.get();
            updatedDepartment.setName(department.getName());
            updatedDepartment.setAddress(department.getAddress());
            return departmentRepository.save(updatedDepartment);
        } else {
            throw new RuntimeException("Department not found with id: " + id);
        }
    }

    @Override
    public void deleteDepartment(Long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Department not found with id: " + id);
        }
    }
}
```

**Fixes Applied**:

- Corrected method names (e.g., `saveDepartment` instead of `savedepartment`).
- Added proper dependency injection with `@Autowired` for `DepartmentRepository`.
- Implemented all CRUD operations with proper error handling using `Optional` and exceptions.
- Used proper package structure.

## Controller Layer

The `DepartmentController` handles HTTP requests and responses.

```java
package com.example.department.controller;

import com.example.department.entity.Department;
import com.example.department.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // Create a new department
    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        Department savedDepartment = departmentService.saveDepartment(department);
        return ResponseEntity.ok(savedDepartment);
    }

    // Get all departments
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    // Get department by ID
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        Optional<Department> department = departmentService.getDepartmentById(id);
        return department.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update department
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        Department updatedDepartment = departmentService.updateDepartment(id, department);
        return ResponseEntity.ok(updatedDepartment);
    }

    // Delete department
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
```

**Fixes Applied**:

- Added `@RequestMapping("/departments")` to group all endpoints under `/departments`.
- Used `ResponseEntity` for proper HTTP response handling.
- Implemented all CRUD operations (POST, GET, GET by ID, PUT, DELETE).
- Used `@RequestBody` for POST and PUT to deserialize JSON into `Department` objects.
- Used `@PathVariable` for ID-based operations.
- Handled `Optional` properly with `.orElseGet()` for GET by ID.
- Added proper package structure.

## Dependency Injection

Dependency injection is handled using `@Autowired` for `DepartmentService` in the controller and `DepartmentRepository` in the service. This adheres to Spring's Inversion of Control (IoC) principle, avoiding manual object creation (e.g., `new DepartmentServiceImpl()`).

## HTTP Request Handling

- **POST** `/departments`: Creates a new department using JSON payload in the request body.
  - Example payload: `{"name": "IT", "address": "123 Tech St"}`
- **GET** `/departments`: Retrieves all departments.
- **GET** `/departments/{id}`: Retrieves a department by ID using `@PathVariable`.
- **PUT** `/departments/{id}`: Updates a department by ID with a JSON payload.
- **DELETE** `/departments/{id}`: Deletes a department by ID.

## Notes on Request Parameters

- Use `@PathVariable` for values in the URL path (e.g., `/departments/5`).
- Use `@RequestParam` for query parameters (e.g., `/departments?name=IT`).

Example of `@RequestParam`:

```java
@GetMapping("/departments/search")
public ResponseEntity<List<Department>> getDepartmentsByName(@RequestParam String name) {
    // Implementation for searching by name (not implemented in repository for simplicity)
    return ResponseEntity.ok(departmentService.getAllDepartments());
}
```

## Spring Data JPA

- `findById(id)` returns `Optional<Department>`. Use `.orElse(null)` or `.orElseThrow()` based on requirements.
- `save()`, `findAll()`, `deleteById()`, and `existsById()` are provided by `JpaRepository`.

## Spring Boot Application

Ensure the main application class is annotated with `@SpringBootApplication`:

```java
package com.example.department;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DepartmentApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(DepartmentApiApplication.class, args);
    }
}
```

## Dependencies

Add the following dependencies to `pom.xml` for a Maven project:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

## Testing the API

- Use tools like Postman or curl to test the API.
- **POST** example:

  ```bash
  curl -X POST http://localhost:8080/departments -H "Content-Type: application/json" -d '{"name":"IT","address":"123 Tech St"}'
  ```
- **GET** example:

  ```bash
  curl http://localhost:8080/departments
  ```
- **GET by ID** example:

  ```bash
  curl http://localhost:8080/departments/1
  ```
- **PUT** example:

  ```bash
  curl -X PUT http://localhost:8080/departments/1 -H "Content-Type: application/json" -d '{"name":"HR","address":"456 Main St"}'
  ```
- **DELETE** example:

  ```bash
  curl -X DELETE http://localhost:8080/departments/1
  ```

## Additional Notes

- The H2 console is accessible at `http://localhost:8080/h2-console`.
- Ensure JSON payloads match the `Department` entity fields.
- The project uses Jackson for JSON serialization/deserialization.
- Images for API calls (POST, GET) are not included as they are not directly generatable in this context. Use Postman or similar tools to visualize requests.

# This setup provides a fully functional Spring Boot REST API for managing departments with proper layering, dependency injection, and error handling.
