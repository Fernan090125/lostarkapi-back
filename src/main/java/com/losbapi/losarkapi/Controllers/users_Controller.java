package com.losbapi.losarkapi.Controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.losbapi.losarkapi.Models.users;
import com.losbapi.losarkapi.Security.security_config;
import com.losbapi.losarkapi.Services.users_services;

@RestController
@RequestMapping("/api/v1")
public class users_Controller {

    @Autowired
    private users_services userservice;

    @Autowired
    private security_config passwordcoder;

    @PostMapping("/Users")
    public ResponseEntity<Object> save(@Valid @RequestBody users user) {

        try {
            Optional<users> userup = userservice.findbyEmail(user.getEmail());
            Map<String, String> response = new HashMap<>();
            if (!userup.isPresent()) {
                users newUser = new users();
                newUser.setId(user.getId());
                newUser.setEmail(user.getEmail());
                String actualpass = user.getPassword();
                String modepass = passwordcoder.passwordEncoder().encode(actualpass);
                newUser.setPassword(modepass);
                userservice.save(newUser);
                response.put("Response", "USUARIO REGISTRADO CORRECTAMENTE");
                return new ResponseEntity<Object>(response, HttpStatus.CREATED);
            } else {
                response.put("Response", "USUARIO REGISTRADO");
                return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR AL GUARDAR USUARIOS \n" + e);
        }
    }


}
