package com.losbapi.losarkapi.Controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.losbapi.losarkapi.Models.users;
import com.losbapi.losarkapi.Security.security_config;
import com.losbapi.losarkapi.Security.jwt.JwtTokenUtil;
import com.losbapi.losarkapi.Services.users_services;

@RestController
@RequestMapping("/api/v1/auth")
public class auth_controller {

    @Autowired
    private users_services userservice;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private security_config passwordcoder;

    @PostMapping("/login")
    public ResponseEntity<Object> Login(@RequestBody users user, @RequestHeader(value = "Authorization") String token) {

        try {
            Map<String, String> response = new HashMap<>();
            Optional<users> finded = userservice.findbyEmail(user.getEmail());

            if (finded.isPresent()) {
                users userfound = finded.get();
                String userid = userfound.getId();
                String userEmail = userfound.getEmail();
                boolean credentials = passwordcoder.passwordEncoder().matches(user.getPassword(),
                        userfound.getPassword());
                if (credentials) {
                    try {
                        if (token=="") {
                            response.put("Response", "No se encontro Token");
                            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
                        } else {
                            String tokenid = jwtTokenUtil.getKey(token);
                            String tokenEmail = jwtTokenUtil.getValue(token);
                            if (!userid.equals(tokenid) || !userEmail.equals(tokenEmail)) {
                                response.put("Response", "Token Invalido");
                                return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
                            } else {
                                if (jwtTokenUtil.expiration(token)) {
                                    response.put("Response", "Logged");
                                    return new ResponseEntity<Object>(response, HttpStatus.ACCEPTED);
                                } else {

                                    if (credentials) {
                                        response.put("Response", "Token expired but logged");
                                        response.put("Token", jwtTokenUtil.create(userid, userEmail));
                                        return new ResponseEntity<Object>(response, HttpStatus.ACCEPTED);
                                    }
                                    response.put("Response", "Token expired");
                                    return new ResponseEntity<Object>(response, HttpStatus.UNAUTHORIZED);

                                }
                            }
                        }

                    } catch (Exception e) {
                        response.put("Response", "Token Invalido catch");
                        return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
                    }

                } else {
                    response.put("Response", "Credenciales Erroneas");
                    return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
                }

            } else {
                response.put("Response", "No se encontro el usuario");
                return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR AL INICIAR SESION \n" + e);
        }

    }

}
