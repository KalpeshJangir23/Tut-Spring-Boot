# Department Api Project
 - Layers 
   - Controllers (handle all our request)
   - Service (business logic)
   - Data Access/Repository Layer (interact with the database)
   - Database (H2 , SQL)

   
# Properties for H2 Database

--- 
```properties
spring.application.name=learningSpringBoot
spring.h2.console.enabled=true         # enable H2 web console
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:dcbapp      # in-memory database
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```
# Repository
Think of this as a helper that talks to the database.
You don’t write SQL, Spring does it for you.
You just make an interface that extends JpaRepository.
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
// Spring will automatically create this query
Optional<User> findByEmail(String email);
}
```
---

### Dependency Injection in Spring can be done in three ways: property-based, constructor-based, and setter-based.

---

```text
Request = what the user wants (client → server)
Response = what the user gets back (server → client)
Payload → actual data inside a request or response
FormData is used when sending files + text data
CRUD = Create, Read, Update, Delete
```

### Interface and Implementation
```text 
1. Interface
public interface DepartmentService {
    void addDepartment(String name);
}


An interface is like a contract — it only defines what methods should exist, not how they work.

2. Implementation
public class DepartmentServiceImpl implements DepartmentService {
    @Override
    public void addDepartment(String name) {
        System.out.println("Department added: " + name);
    }
}


Here, DepartmentServiceImpl gives the actual code (implementation) for the methods defined in the interface.
```

what have been done :
 - folder creation of service , controller , entity , repo
 - in entity new class Name Department will different private field (name , address etc)
 - in same file create getter , setter , Constructor ,  toString method and default construtor
 - now to make this department class entity we annotate it with @Entity so that it can interact with the database
 - making department-id has the primary key using `@Id` and `@GeneratedValue(strategy = GenerationType.AUTO)`
 - Now we create a department controller in controller folder
 - and we would annotate that class with `@RestController`
 - now we create interface in service folder `DepartmentService`
 - then we create implement class in the same folder `DepartmentServiceImpl`
 - we extend Department impl with it interface `DepartmentServiceImpl implements DepartmentService` and annotate it with `@service`
 - now we also create interface in `Repository folder` name `DepartmentRepo`
 - in departmentrepo we extend it with jpaRepo which take 2 parameter `JpaRepository<Entity,Primary key type examplee: Long>`
 - `JpaRepository<Department,Long>` => this as a helper that talks to the database.
   - You just make an interface that extends JpaRepository.
   - You don’t write SQL, Spring does it for you.
 - When a Spring Boot application starts, all the classes annotated with Spring stereotypes (like @Component, @Service, @Repository, @Controller) are automatically created as beans and added to the Spring Container, making them available for dependency injection whenever needed
 - Now we create a method in Department Controller
 - POST is an HTTP request method. When the client wants to send data to the server
 - Post Method `/departments`  in a POST API, the JSON sent in the request body can be directly converted into an Entity/Model object using @RequestBody.
 - When you send a JSON object in the request body of a POST API call, Spring Boot will automatically take that JSON and convert (deserialize) it into your Java Entity/Model class (like User, Employee, etc.) using Jackson (a JSON parser library).
 - now we want to save the data so we call service layer and then call the repository layer
 -  we can't use ` DepartmentService service = new DepartmentServiceImpl();`
 - sing new DepartmentServiceImpl() means we are manually creating the object, which breaks Spring’s Inversion of Control (IoC).
 - With @Autowired, Spring injects the already created bean from its container into our class, so we don’t create objects manually.
 - Now we have to create method in service
 - so we create `savedepartment` in both interface and then implemented
 - we would also create an Autowire for repo and use JPA property like(save , fineAll etc) and interact with database
 - add image for post api call
 - New request: Get
 - repeat the same method and then create the get api
 - add image for get api call
 - New Request get department by id 
 - `@PathVariable` is used to capture values from the URL path and bind them to method parameters in your controller.
 - `localhost:8080/users/5`
 - In Spring Boot, we have two main ways to send values in an HTTP request `@PathVariable` and `@RequestParam`
```java
   @GetMapping("/users")
    public String getUserByFilter(@RequestParam String name,
    @RequestParam(required = false, defaultValue = "0") int age) {
    return "User Name: " + name + ", Age: " + age;
    }
```
 - In Spring Data JPA, findById(id) returns Optional<T>, not the entity directly. 
 - Use .orElse(null) if you want null when not found. 
 - Use .orElseThrow() if you want to throw an exception when not found.
 - Delete Request
 - Update Request



