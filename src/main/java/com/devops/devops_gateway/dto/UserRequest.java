package com.devops.devops_gateway.dto;

import com.devops.devops_gateway.enumeration.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String email;
	private Role role; //enumeracija, ne klasa
	private CreateAddressDTO address;
}
