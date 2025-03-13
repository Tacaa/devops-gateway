package com.devops.devops_gateway.integration;

import com.devops.devops_gateway.model.Role;
import com.devops.devops_gateway.repository.RoleRepository;
import com.devops.devops_gateway.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RoleServiceIntegrationTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findById_ShouldReturnRole() {
        // Act
        Role foundRole = roleService.findById(1);

        // Assert
        assertNotNull(foundRole);
        assertEquals(foundRole.getId(), 1);
        assertEquals("ROLE_GUEST", foundRole.getName());
    }


    @Test
    public void findByName_ShouldReturnRoleList() {
        List<Role> roles = roleService.findByName("ROLE_GUEST");
        assertEquals(1, roles.size());
        assertEquals("ROLE_GUEST", roles.get(0).getName());
    }

    @Test
    void findByName_WhenNoRoleFound_ShouldReturnEmptyList() {
        // Act
        List<Role> foundRoles = roleService.findByName("ROLE_NONEXISTENT");

        // Assert
        assertNotNull(foundRoles);
        assertEquals(0, foundRoles.size());
    }
}