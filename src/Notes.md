Spring Boot provides `@DataJpaTest` annotation to test the persistence layer components. It will autoconfigure an in-memory embedded database for testing purposes.
The `@DataJpaTest` annotation will only scan the `@Entity` classes and the `@Repository` classes. It will not scan the `@Service`, `@Controller` or `@Component` classes 
or annotated beans. 

- Tests annotated with `@DataJpaTest` will not start the full application context. It will only start the required part of the Spring context needed for JPA testing.
- Tests annotated with `@DataJpaTest` are transactional and roll back at the end of each test by default. This means that the tests will not change the database state.