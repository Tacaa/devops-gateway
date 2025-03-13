package com.devops.devops_gateway.integration;

import com.devops.devops_gateway.dto.UpdateUserDTO;
import com.devops.devops_gateway.dto.UserRequest;
import com.devops.devops_gateway.model.Role;
import com.devops.devops_gateway.model.User;
import com.devops.devops_gateway.repository.RoleRepository;
import com.devops.devops_gateway.repository.UserRepository;
import com.devops.devops_gateway.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Role userRole;
    private List<Role> roles;
    private UserRequest userRequest;
    private UpdateUserDTO updateUserDTO;

    @BeforeEach
    void setUp() {
        // Create and save a test role if it doesn't exist
        List<Role> existingRoles = roleRepository.findByName("ROLE_GUEST");
        if (existingRoles.isEmpty()) {
            userRole = new Role();
            userRole.setName("ROLE_GUEST");
            userRole = roleRepository.save(userRole);
        } else {
            userRole = existingRoles.get(0);
        }

        // Create test user request
        userRequest = new UserRequest();
        userRequest.setUsername("testuser");
        userRequest.setPassword("password123");
        userRequest.setFirstname("Test");
        userRequest.setLastname("User");
        userRequest.setEmail("test.user@example.com");
        userRequest.setRole(com.devops.devops_gateway.enumeration.Role.GUEST);

        // Create update user DTO
        updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("updateduser");
        updateUserDTO.setPassword("newpassword");
        updateUserDTO.setFirstname("Updated");
        updateUserDTO.setLastname("User");
        updateUserDTO.setEmail("updated.user@example.com");
    }

    @Test
    void update_WhenUserDoesNotExist_ShouldReturnFalse() {
        // Act
        boolean updated = userService.update(999, updateUserDTO);

        // Assert
        assertFalse(updated);
    }


}