package com.losbapi.losarkapi.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.losbapi.losarkapi.Interfaces.users_repository;
import com.losbapi.losarkapi.Models.users;

@Service
public class users_services {

    @Autowired
    private users_repository userrepo;
    
    public void save(users user){
        userrepo.save(user);
    }

    public Optional<users> findbyEmail(String email){
        return userrepo.findByEmail(email);

    }

    
}
