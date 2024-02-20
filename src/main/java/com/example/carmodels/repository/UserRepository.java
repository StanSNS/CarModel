package com.example.carmodels.repository;

import com.example.carmodels.Models.UserModels;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserModels, Integer> {
}

