# AI Agent Guidelines for InventoryService

## Architecture Overview
This is a Spring Boot 3.5.13 microservice handling inventory management in an eCommerce platform. It uses JPA for MySQL persistence, REST APIs for external access, and Kafka for event-driven communication with other services.

Key components (to be implemented):
- **Controllers**: REST endpoints under `com.ecommerce.InventoryService.controller`
- **Services**: Business logic in `com.ecommerce.InventoryService.service`
- **Entities**: JPA models in `com.ecommerce.InventoryService.entity`
- **Repositories**: Data access in `com.ecommerce.InventoryService.repository`

Data flow: HTTP requests → Controllers → Services → Repositories → MySQL DB. Events published to Kafka topics for order processing, etc.

## Development Workflows
- **Build**: Use `mvnw.cmd clean compile` for incremental builds
- **Test**: Run `mvnw.cmd test` for unit/integration tests (JUnit 5)
- **Run**: Execute `mvnw.cmd spring-boot:run` to start the service on port 8080 (default)
- **Debug**: Add `--debug` to spring-boot:run for verbose logging

Lombok annotation processing is configured in `pom.xml` - ensure IDE plugins are enabled for @Data, @Entity, etc.

## Project Conventions
- **Package Structure**: Follow `com.ecommerce.InventoryService.{layer}` (e.g., controller, service)
- **Validation**: Use Bean Validation annotations (@NotNull, @Size) on DTOs and entities
- **Error Handling**: Implement @ControllerAdvice for global exception handling
- **Kafka Integration**: Use @KafkaListener for consuming events, KafkaTemplate for publishing
- **Database**: MySQL with JPA - create entities extending BaseEntity if needed for auditing

## Integration Points
- **Database**: Configure connection in `application.properties` (currently minimal)
- **Kafka**: Topics for inventory updates (e.g., "inventory.updated") - producer/consumer configs in properties
- **Other Services**: REST calls to OrderService, ProductService via WebClient or RestTemplate

## Examples
- Entity: `@Entity @Data public class Product { @Id private Long id; private String name; }`
- Controller: `@RestController @RequestMapping("/api/inventory") public class InventoryController { ... }`
- Service: `@Service public class InventoryService { @Autowired private InventoryRepository repo; }`

Reference: `pom.xml` for dependencies, `InventoryServiceApplication.java` for main class.
