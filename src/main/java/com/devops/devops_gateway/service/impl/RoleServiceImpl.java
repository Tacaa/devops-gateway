package com.devops.devops_gateway.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devops.devops_gateway.model.Role;
import com.devops.devops_gateway.repository.RoleRepository;
import com.devops.devops_gateway.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public Role findById(Integer id) {
    Role auth = this.roleRepository.getReferenceById(id);
    return auth;
  }

  @Override
  public List<Role> findByName(String name) {
	List<Role> roles = this.roleRepository.findByName(name);
    return roles;
  }
}
