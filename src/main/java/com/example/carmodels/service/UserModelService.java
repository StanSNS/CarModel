package com.example.carmodels.service;

import com.example.carmodels.Models.UserModels;
import com.example.carmodels.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserModelService {

    @Autowired
    UserRepository repository;

    public UserModels addUserModel(UserModels userModels) {
        return repository.save(userModels);
    }
}
