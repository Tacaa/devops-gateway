package com.devops.devops_gateway.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.devops.devops_gateway.dto.JwtAuthenticationRequest;
import com.devops.devops_gateway.dto.UserRequest;
import com.devops.devops_gateway.dto.UserTokenState;
import com.devops.devops_gateway.exception.ResourceConflictException;
import com.devops.devops_gateway.model.User;
import com.devops.devops_gateway.service.UserService;
import com.devops.devops_gateway.util.TokenUtils;


//Kontroler zaduzen za autentifikaciju korisnika
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
//@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<UserTokenState> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		User user = (User) authentication.getPrincipal();
		String jwt = tokenUtils.generateToken(user);
		int expiresIn = tokenUtils.getExpiredIn();

		return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
	}

	@PostMapping("/signup")
	public ResponseEntity<User> addUser(@RequestBody UserRequest userRequest, UriComponentsBuilder ucBuilder) {
		User existUser = this.userService.findByUsername(userRequest.getUsername());

		if (existUser != null) {
			throw new ResourceConflictException(userRequest.getId(), "Username already exists");
		}

		//TODO: promijeniti UserRequest dodati polja da se mozeregistrovati i na user service
		//TODO: povezati sa userservisom i sacuvati u njegovoj bazi
		User user = this.userService.save(userRequest);

		//TODO: diskutovati da li odmah i ulogovati korisnika pri registraciji? Ja sam za
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
}