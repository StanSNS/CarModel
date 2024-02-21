package com.example.carmodels.Models.Entity;

import com.example.carmodels.Models.Base.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class UserModels extends BaseModel {
    @Column
    String name;
    @Column(unique = true)
    String email;
    @Column
    String password;
    @Column
    String jwtToken;
    @ManyToOne
    RoleModels role;

    public UserModels() {
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

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public RoleModels getRole() {
        return role;
    }

    public void setRole(RoleModels role) {
        this.role = role;
    }
}
