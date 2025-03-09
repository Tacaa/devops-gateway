package com.devops.devops_gateway.unit;

import com.devops.devops_gateway.dto.UpdateUserDTO;
import com.devops.devops_gateway.dto.UserRequest;
import com.devops.devops_gateway.model.Role;
import com.devops.devops_gateway.model.User;
import com.devops.devops_gateway.repository.UserRepository;
import com.devops.devops_gateway.service.RoleService;
import com.devops.devops_gateway.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserRequest userRequest;
    private UpdateUserDTO updateUserDTO;
    private List<Role> roles;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test data
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("johndoe");
        testUser.setPassword("encodedPassword");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john@example.com");
        testUser.setEnabled(true);

        userRequest = new UserRequest();
        userRequest.setUsername("johndoe");
        userRequest.setPassword("password123");
        userRequest.setFirstname("John");
        userRequest.setLastname("Doe");
        userRequest.setEmail("john@example.com");

        updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("johndoe_updated");
        updateUserDTO.setPassword("newpassword");
        updateUserDTO.setFirstname("John");
        updateUserDTO.setLastname("Smith");
        updateUserDTO.setEmail("john.smith@example.com");

        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
        roles = new ArrayList<>();
        roles.add(role);
    }

    @Test
    void findByUsername_ShouldReturnUser() {
        // Arrange
        when(userRepository.findByUsername("johndoe")).thenReturn(testUser);

        // Act
        User result = userService.findByUsername("johndoe");

        // Assert
        assertEquals(testUser, result);
        verify(userRepository, times(1)).findByUsername("johndoe");
    }

    @Test
    void findById_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.findById(1);

        // Assert
        assertEquals(testUser, result);
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void findAll_ShouldReturnUserList() {
        // Arrange
        List<User> userList = new ArrayList<>();
        userList.add(testUser);
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<User> result = userService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void save_WithUserRequest_ShouldReturnSavedUser() {
        // Arrange
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(roleService.findByName("ROLE_USER")).thenReturn(roles);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1);
            return savedUser;
        });

        // Act
        User result = userService.save(userRequest);

        // Assert
        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john@example.com", result.getEmail());
        assertTrue(result.isEnabled());
        assertEquals(1, result.getRoles().size());
        assertEquals("ROLE_USER", result.getRoles().get(0).getName());

        verify(passwordEncoder, times(1)).encode("password123");
        verify(roleService, times(1)).findByName("ROLE_USER");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void save_WithUser_ShouldReturnSavedUser() {
        // Arrange
        when(userRepository.save(testUser)).thenReturn(testUser);

        // Act
        User result = userService.save(testUser);

        // Assert
        assertEquals(testUser, result);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void update_WhenUserExists_ShouldReturnTrue() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");

        // Act
        boolean result = userService.update(1, updateUserDTO);

        // Assert
        assertTrue(result);
        assertEquals("johndoe_updated", testUser.getUsername());
        assertEquals("encodedNewPassword", testUser.getPassword());
        assertEquals("John", testUser.getFirstName());
        assertEquals("Smith", testUser.getLastName());
        assertEquals("john.smith@example.com", testUser.getEmail());

        verify(userRepository, times(1)).findById(1);
        verify(passwordEncoder, times(1)).encode("newpassword");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void update_WhenUserDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        boolean result = userService.update(999, updateUserDTO);

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findById(999);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findByEmail_ShouldReturnUser() {
        // Arrange
        when(userRepository.findByEmail("john@example.com")).thenReturn(testUser);

        // Act
        User result = userService.findByEmail("john@example.com");

        // Assert
        assertEquals(testUser, result);
        verify(userRepository, times(1)).findByEmail("john@example.com");
    }

    @Test
    void disableUser_WhenUserExists_ShouldDisableUser() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        // Act
        userService.disableUser(1);

        // Assert
        assertFalse(testUser.isEnabled());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void disableUser_WhenUserDoesNotExist_ShouldDoNothing() {
        // Arrange
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        userService.disableUser(999);

        // Assert
        verify(userRepository, times(1)).findById(999);
        verify(userRepository, never()).save(any(User.class));
    }
}