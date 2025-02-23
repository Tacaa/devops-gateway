package com.devops.devops_gateway.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
	private Long id;
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String email;
}
