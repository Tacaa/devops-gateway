package com.devops.devops_gateway.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtAuthenticationRequest {
    private String username;
    private String password;
}
