package com.devops.devops_gateway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devops.devops_gateway.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	List<Role> findByName(String name);
}
