package com.devops.devops_gateway.service;

import java.util.List;

import com.devops.devops_gateway.model.Role;

public interface RoleService {
	Role findById(Integer id);
	List<Role> findByName(String name);
}
