package com.devops.devops_gateway.service.impl;

import java.util.List;

import com.devops.devops_gateway.dto.UpdateUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devops.devops_gateway.dto.UserRequest;
import com.devops.devops_gateway.model.Role;
import com.devops.devops_gateway.model.User;
import com.devops.devops_gateway.repository.UserRepository;
import com.devops.devops_gateway.service.RoleService;
import com.devops.devops_gateway.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleService roleService;

	@Override
	public User findByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username);
	}

	public User findById(Integer id) throws AccessDeniedException {
		return userRepository.findById(id).orElseGet(null);
	}

	public List<User> findAll() throws AccessDeniedException {
		return userRepository.findAll();
	}

	@Override
	public User save(UserRequest userRequest) {
		User u = new User();
		u.setUsername(userRequest.getUsername());
		
		// pre nego sto postavimo lozinku u atribut hesiramo je kako bi se u bazi nalazila hesirana lozinka
		// treba voditi racuna da se koristi isi password encoder bean koji je postavljen u AUthenticationManager-u kako bi koristili isti algoritam
		u.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		
		u.setFirstName(userRequest.getFirstname());
		u.setLastName(userRequest.getLastname());
		u.setEnabled(true);
		u.setEmail(userRequest.getEmail());

		// u primeru se registruju samo obicni korisnici i u skladu sa tim im se i dodeljuje samo rola USER
		List<Role> roles = roleService.findByName("ROLE_USER");
		u.setRoles(roles);
		
		return this.userRepository.save(u);
	}

	@Override
	public User save(User user){
		return userRepository.save(user);
	}

	@Override
	public boolean update(Integer id, UpdateUserDTO updateUserDTO) {
		User user = userRepository.findById(id).orElse(null);

		if(user == null){
			return false;
		}

		user.setFirstName(updateUserDTO.getFirstname());
		user.setLastName(updateUserDTO.getLastname());
		user.setUsername(updateUserDTO.getUsername());
		user.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
		user.setEmail(updateUserDTO.getEmail());

		userRepository.save(user);
		return true;

	}

	@Override
	public User findByEmail(String email){
		return userRepository.findByEmail(email);
	}

	@Override
	public void disableUser(Integer id){
		User user = userRepository.findById(id).orElse(null);

		if(user != null){
			user.setEnabled(false);
			userRepository.save(user);
		}
	}
}
