package com.devops.devops_gateway.unit;

import com.devops.devops_gateway.model.Role;
import com.devops.devops_gateway.repository.RoleRepository;
import com.devops.devops_gateway.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role testRole;
    private List<Role> roleList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test data
        testRole = new Role();
        testRole.setId(1);
        testRole.setName("ROLE_USER");

        roleList = new ArrayList<>();
        roleList.add(testRole);
    }

    @Test
    void findById_ShouldReturnRole() {
        // Arrange
        when(roleRepository.getReferenceById(1)).thenReturn(testRole);

        // Act
        Role result = roleService.findById(1);

        // Assert
        assertEquals(testRole, result);
        verify(roleRepository, times(1)).getReferenceById(1);
    }

    @Test
    void findByName_ShouldReturnRoleList() {
        // Arrange
        String roleName = "ROLE_USER";
        when(roleRepository.findByName(roleName)).thenReturn(roleList);

        // Act
        List<Role> result = roleService.findByName(roleName);

        // Assert
        assertEquals(roleList, result);
        assertEquals(1, result.size());
        assertEquals(roleName, result.get(0).getName());
        verify(roleRepository, times(1)).findByName(roleName);
    }

    @Test
    void findByName_WhenNoRoleFound_ShouldReturnEmptyList() {
        // Arrange
        String roleName = "ROLE_ADMIN";
        when(roleRepository.findByName(roleName)).thenReturn(new ArrayList<>());

        // Act
        List<Role> result = roleService.findByName(roleName);

        // Assert
        assertEquals(0, result.size());
        verify(roleRepository, times(1)).findByName(roleName);
    }
}