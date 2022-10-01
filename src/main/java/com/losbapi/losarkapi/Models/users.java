package com.losbapi.losarkapi.Models;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(value = "users")
public class users implements Serializable{

    @Id
    @Indexed(unique = true)
    private String id;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @Size(min = 8, message = "password should have at least 8 characters")
    private String Password;

    public String getId() {
        return this.id;
    }

    public void setId(String Id) {
        this.id = Id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String Email) {
        this.email = Email;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    
}
