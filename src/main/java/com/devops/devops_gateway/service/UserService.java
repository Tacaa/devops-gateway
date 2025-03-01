package com.devops.devops_gateway.service;

import java.util.List;

import com.devops.devops_gateway.dto.UpdateUserDTO;
import com.devops.devops_gateway.dto.UserRequest;
import com.devops.devops_gateway.model.User;

public interface UserService {
    User findById(Integer id);
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findAll ();
	User save(UserRequest userRequest);
    User save(User user);
    boolean update(Integer id, UpdateUserDTO updateUserDTO);
}
