package com.devops.devops_gateway.dto;

import com.devops.devops_gateway.enumeration.Role;
import com.devops.devops_gateway.model.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private boolean enabled;
    private Role role;

    public static UserDTO from(User user) {
        UserDTO userDTO =  UserDTO.builder()
                .id(user.getId())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .build();

        if(user.getRoles().getFirst().getName().equals("ROLE_GUEST")){
            userDTO.setRole(Role.GUEST);
        }else{
            userDTO.setRole(Role.HOST);
        }
        return userDTO;
    }
}
