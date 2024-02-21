package com.example.carmodels.Models.Base;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Base class for entities with common fields.

 * This class is marked as MappedSuperclass, indicating that it is not an entity to be
 * persisted on its own, but provides common fields that can be inherited by other
 * entity classes.
 */
@MappedSuperclass
public abstract class BaseModel {

    /**
     * The unique identifier for an entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;


    public Integer getId() {
        return id;
    }
}