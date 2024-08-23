package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.request.user.UserRegisterRequestDto;
import com.vladislavlevchik.cloud_file_storage.entity.User;
import com.vladislavlevchik.cloud_file_storage.exception.UserAlreadyExistException;
import com.vladislavlevchik.cloud_file_storage.exception.UserNotFoundException;
import com.vladislavlevchik.cloud_file_storage.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class UserServiceIntegrationTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test")
            .withUsername("user")
            .withPassword("password");

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterUserSuccessfully() {
        UserRegisterRequestDto requestDto = new UserRegisterRequestDto();
        requestDto.setUsername("testuser");
        requestDto.setPassword("password");

        userService.register(requestDto);

        User user = userRepository.findByUsername("testuser").orElse(null);
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
    }

    @Test
    void testRegisterUserAlreadyExists() {
        UserRegisterRequestDto requestDto = new UserRegisterRequestDto();
        requestDto.setUsername("testuser");
        requestDto.setPassword("password");

        userService.register(requestDto);

        assertThrows(UserAlreadyExistException.class, () -> userService.register(requestDto));
    }

    @Test
    void testGetUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUser("nonexistentuser"));
    }

}
