package com.losbapi.losarkapi.Interfaces;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.losbapi.losarkapi.Models.users;

@Repository
public interface users_repository extends MongoRepository<users,String>{

    @Query("{'Email' : ?0 }")
    Optional<users> findByEmail(String Email);

}
