package com.devops.devops_gateway.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTokenState {
    private String accessToken;
    private Integer expiresIn;
}