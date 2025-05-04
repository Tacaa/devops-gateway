package com.devops.devops_gateway.controller;

import com.devops.devops_gateway.client.UserClient;
import com.devops.devops_gateway.dto.*;
import com.devops.devops_gateway.model.Role;
import com.devops.devops_gateway.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.devops.devops_gateway.exception.ResourceConflictException;
import com.devops.devops_gateway.model.User;
import com.devops.devops_gateway.service.UserService;
import com.devops.devops_gateway.util.TokenUtils;

import java.sql.Timestamp;
import java.util.List;


//Kontroler zaduzen za autentifikaciju korisnika
@Slf4j
@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
//@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserClient userClient;

	//metoda koja sluzi za logovanje, izdvojena kako se ne bi duplirao kod i u registraciji
	private UserTokenState login(JwtAuthenticationRequest authenticationRequest){
		log.info("Attempting login for user: {}", authenticationRequest.getUsername());

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		User user = (User) authentication.getPrincipal();
		String jwt = tokenUtils.generateToken(user);
		int expiresIn = tokenUtils.getExpiredIn();

		// LOGGING FOR DEBUGGING
		log.info("User {} successfully authenticated. Token generated, expires in {} seconds.", user.getUsername(), expiresIn);
		log.debug("Security context authentication set: {}", SecurityContextHolder.getContext().getAuthentication());

		return new UserTokenState(jwt, expiresIn);
	}


	@PostMapping("/login")
	public ResponseEntity<UserTokenState> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
		log.info("Received login request for username: {}", authenticationRequest.getUsername());

		UserTokenState token = this.login(authenticationRequest);
		return ResponseEntity.ok(token);
	}


	@GetMapping("/current-user")
	public ResponseEntity<UserDTO> getCurrentUser() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDTO userDTO = UserDTO.from(user);
		log.info("Fetching current authenticated user: {}", user.getUsername());
		return ResponseEntity.ok(userDTO);
	}



	@PostMapping("/register")
	public ResponseEntity<UserTokenState> addUser(@RequestBody UserRequest userRequest, UriComponentsBuilder ucBuilder) {
		log.info("Received registration request for username: {}", userRequest.getUsername());

		if(userRequest.getFirstname() == null || userRequest.getLastname() == null || userRequest.getUsername() == null
				|| userRequest.getPassword() == null || userRequest.getEmail() == null || userRequest.getRole() == null
				|| userRequest.getAddress() == null || userRequest.getAddress().getStreet() == null ||  userRequest.getAddress().getCity() == null
				||  userRequest.getAddress().getCountry() == null){
			log.warn("Registration failed: Missing required fields.");
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		User existUser = this.userService.findByUsername(userRequest.getUsername());

		if (existUser != null) {
			log.warn("Registration failed: Username {} already exists.", userRequest.getUsername());
			throw new ResourceConflictException(existUser.getId(), "Username already exists");
		}

		existUser = this.userService.findByEmail(userRequest.getEmail());

		if (existUser != null) {
			log.warn("Registration failed: Email {} already exists.", userRequest.getEmail());
			throw new ResourceConflictException(existUser.getId(), "Email already exists");
		}

		//pronadji rolu

		List<Role> roles;
		if(userRequest.getRole() == com.devops.devops_gateway.enumeration.Role.GUEST){
			roles = roleService.findByName("ROLE_GUEST");
		}else{
			roles = roleService.findByName("ROLE_HOST");
		}

		User user = User.builder()
				.firstName(userRequest.getFirstname())
				.lastName(userRequest.getLastname())
				.email(userRequest.getEmail())
				.username(userRequest.getUsername())
				.password(passwordEncoder.encode(userRequest.getPassword()))
				.roles(roles)
				.enabled(true)
				.lastPasswordResetDate(new Timestamp(System.currentTimeMillis()))
				.build();



		user = this.userService.save(user);
		log.info("User {} registered successfully.", user.getUsername());

		boolean saved = userClient.createUserInUserService(userRequest);
		if(!saved){
			log.error("Failed to create user in external user service for username: {}", userRequest.getUsername());
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}

		//kada se registrovao korisnik neka se odmah i loguje (tj. dobije svoj token i postavi u kontekst)
		JwtAuthenticationRequest jwtAuthenticationRequest = new JwtAuthenticationRequest(userRequest.getUsername(), userRequest.getPassword());
		UserTokenState token = this.login(jwtAuthenticationRequest);

		return new ResponseEntity<>(token, HttpStatus.CREATED);
	}


	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request) {
		String authToken = tokenUtils.getToken(request);

		if (authToken != null) {
			log.info("Logging out token: {}", authToken);
			tokenUtils.blacklistToken(authToken);
		}

		SecurityContextHolder.clearContext();
		log.info("Security context cleared. User logged out.");
		return new ResponseEntity<>(HttpStatus.OK);
	}
}