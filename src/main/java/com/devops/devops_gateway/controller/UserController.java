package com.devops.devops_gateway.controller;



//    TODO: ovaj kontroler nece biti pozivan od strane fronta, nego od strane user servisa
//    sluzice za sinhronizaciju za update i brisanje naloga

import com.devops.devops_gateway.dto.UpdateUserDTO;
import com.devops.devops_gateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/gateway/")
public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping(value = "update-user/{id}")
    public boolean updateUser(@PathVariable("id") Integer id, @RequestBody UpdateUserDTO updateUserDTO){
        return userService.update(id, updateUserDTO);
    }
}
