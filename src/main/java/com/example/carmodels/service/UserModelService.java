package com.example.carmodels.service;

import com.example.carmodels.Models.DTO.RegisterDTO;
import com.example.carmodels.Models.Entity.UserModels;
import com.example.carmodels.repository.UserRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserModelService {


    private final UserRepository repository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor to initialize the UserModelService with necessary dependencies.
     *
     * @param repository      UserRepository for interacting with user data storage
     * @param modelMapper     ModelMapper for mapping DTOs to entity models
     * @param passwordEncoder PasswordEncoder for encoding user passwords
     */
    public UserModelService(UserRepository repository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Adds a new user model based on the provided RegisterDTO.
     *
     * @param registerDTO RegisterDTO containing user data to be added
     * @return UserModels representing the newly added user
     */
    public UserModels addUserModel(@Valid RegisterDTO registerDTO) {

        // Map RegisterDTO to UserModels entity
        UserModels userModel = modelMapper.map(registerDTO, UserModels.class);

        // Encode the user's password before saving
        userModel.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        System.out.println(userModel);

        // Save the user model to the repository and return the saved entity
        return repository.save(userModel);
    }
}
