package com.example.carmodels.service;

import com.example.carmodels.Models.DTO.RegisterDTO;
import com.example.carmodels.Models.Entity.RoleModels;
import com.example.carmodels.Models.Entity.UserModels;
import com.example.carmodels.repository.RoleRepository;
import com.example.carmodels.repository.UserRepository;
import com.example.carmodels.exception.DataValidationException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.carmodels.constants.RoleConst.BASIC_USER;

@Service
public class UserModelService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    /**
     * Constructor to initialize the UserModelService with necessary dependencies.
     *
     * @param repository      UserRepository for interacting with user data storage
     * @param modelMapper     ModelMapper for mapping DTOs to entity models
     * @param passwordEncoder PasswordEncoder for encoding user passwords
     * @param roleRepository  RoleRepository for the user role
     */
    public UserModelService(UserRepository repository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = repository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    /**
     * Adds a new user model based on the provided RegisterDTO.
     *
     * @param registerDTO RegisterDTO containing user data to be added
     * @return UserModels representing the newly added user
     */
    public UserModels addUserModel(@Valid RegisterDTO registerDTO) {

        if (!userRepository.existsByEmail(registerDTO.getEmail())) {

            // Map RegisterDTO to UserModels entity
            UserModels userModel = modelMapper.map(registerDTO, UserModels.class);

            // Encode the user's password before saving
            userModel.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

            // Create a new RoleModels entity and persist it
            RoleModels roleModel = new RoleModels(BASIC_USER);
            roleRepository.save(roleModel);

            // Set the persisted RoleModels entity on the UserModels entity
            userModel.setRole(roleModel);

            //FIXME DELETE THIS
            System.out.println(userModel);

            // Save the user model to the repository and return the saved entity
            return userRepository.save(userModel);
        }

        // Throw a DataValidationException to indicate that the registration data is invalid
        throw new DataValidationException();
    }
}
