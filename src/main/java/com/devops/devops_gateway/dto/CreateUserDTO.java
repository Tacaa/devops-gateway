package com.devops.devops_gateway.dto;

import com.devops.devops_gateway.enumeration.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDTO {
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String email;
    private Role role;
    private CreateAddressDTO address;
}
