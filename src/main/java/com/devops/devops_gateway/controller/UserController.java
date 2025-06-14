package com.devops.devops_gateway.controller;



//    TODO: ovaj kontroler nece biti pozivan od strane fronta, nego od strane user servisa
//    sluzice za sinhronizaciju za update i brisanje naloga

import com.devops.devops_gateway.dto.UpdateUserDTO;
import com.devops.devops_gateway.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/gateway/")
public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping(value = "update-user/{id}")
    public boolean updateUser(@PathVariable("id") Integer id, @RequestBody UpdateUserDTO updateUserDTO){
        log.info("Received request to update user with ID: {}", id);

        boolean success = userService.update(id, updateUserDTO);
        if (success) {
            log.info("User with ID {} successfully updated.", id);
        } else {
            log.warn("Failed to update user with ID {}", id);
        }
        return success;
    }

    @PutMapping(value = "disable-user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void disableUser(@PathVariable("id") Integer id){
        log.info("Received request to disable user with ID: {}", id);
        userService.disableUser(id);
        log.info("User with ID {} has been disabled.", id);
    }
}
