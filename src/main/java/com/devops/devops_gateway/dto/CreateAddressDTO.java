package com.devops.devops_gateway.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAddressDTO {
    private String street;
    private Integer number;
    private String city;
    private String country;
}