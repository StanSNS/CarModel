package com.example.carmodels.Models.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterDTO {
    @NotNull
    @Size(min = 2)
    String name;

    @Email
    @NotNull
    @Size(min = 2)
    String email;

    @NotNull
    @Size(min = 2)
    String password;

    public RegisterDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
