package com.devops.devops_gateway.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDTO {
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String email;
}
