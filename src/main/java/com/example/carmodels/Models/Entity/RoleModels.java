package com.example.carmodels.Models.Entity;

import com.example.carmodels.Models.Base.BaseModel;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;

@Entity
public class RoleModels extends BaseModel {

    @Size(min = 4, max = 6)
    private String name;

    public RoleModels() {
    }

    public RoleModels(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}