package com.devops.devops_gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devops.devops_gateway.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}

