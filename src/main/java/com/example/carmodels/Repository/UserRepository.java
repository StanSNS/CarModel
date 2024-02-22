package com.example.carmodels.Repository;

import com.example.carmodels.Models.Entity.UserModels;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserModels, Integer> {

    boolean existsByEmail(String email);

    Optional<UserModels> findByEmail(String email);

}

