Spring Boot provides `@DataJpaTest` annotation to test the persistence layer components. It will autoconfigure an in-memory embedded database for testing purposes.
The `@DataJpaTest` annotation will only scan the `@Entity` classes and the `@Repository` classes. It will not scan the `@Service`, `@Controller` or `@Component` classes 
or annotated beans. 

- Tests annotated with `@DataJpaTest` will not start the full application context. It will only start the required part of the Spring context needed for JPA testing.
- Tests annotated with `@DataJpaTest` are transactional and roll back at the end of each test by default. This means that the tests will not change the database state.

# Mocking Dependencies using Mockito
- **Mockito mock() method**: We can use the `Mockito.mock()` method to create a mock object of a class or an interface.
- **Mockito @Mock annotation**: We can mock an object using `@Mock` annotation. It's useful when we want to use the mocked object at multiples places because we avoid calling the `Mockito.mock()` method multiple times.
- **Mockito.when() method**: We can use the `Mockito.when()` method to define the behavior of a mock object.
- **Mockito.verify() method**: We can use the `Mockito.verify()` method to verify that a method of a mock object has been called.
- **Mockito @InjectMocks annotation**:
  We can use the `@InjectMocks` annotation to inject a mocked object into another mocked object.
  creates the mock object of the class and
  injects the mocks that are marked with the annotations `@Mock` into it.
- 
