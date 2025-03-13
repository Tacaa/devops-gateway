package com.devops.devops_gateway.unit;

import com.devops.devops_gateway.controller.UserController;
import com.devops.devops_gateway.dto.UpdateUserDTO;
import com.devops.devops_gateway.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UpdateUserDTO updateUserDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test data
        updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstname("John");
        updateUserDTO.setLastname("Doe");
        updateUserDTO.setUsername("johndoe");
        updateUserDTO.setEmail("john@example.com");
        updateUserDTO.setPassword("password123");
    }

    @Test
    void updateUser_WhenUserExists_ReturnsTrue() {
        // Arrange
        Integer userId = 1;
        when(userService.update(userId, updateUserDTO)).thenReturn(true);

        // Act
        boolean result = userController.updateUser(userId, updateUserDTO);

        // Assert
        assertTrue(result);
        verify(userService, times(1)).update(userId, updateUserDTO);
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ReturnsFalse() {
        // Arrange
        Integer userId = 999;
        when(userService.update(userId, updateUserDTO)).thenReturn(false);

        // Act
        boolean result = userController.updateUser(userId, updateUserDTO);

        // Assert
        assertFalse(result);
        verify(userService, times(1)).update(userId, updateUserDTO);
    }

    @Test
    void disableUser_CallsServiceMethod() {
        // Arrange
        Integer userId = 1;
        doNothing().when(userService).disableUser(userId);

        // Act
        userController.disableUser(userId);

        // Assert
        verify(userService, times(1)).disableUser(userId);
    }
}