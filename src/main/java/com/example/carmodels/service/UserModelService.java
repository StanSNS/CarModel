package com.example.carmodels.service;

import com.example.carmodels.Models.DTO.LoginDTO;
import com.example.carmodels.Models.DTO.RegisterDTO;
import com.example.carmodels.Models.Entity.RoleModels;
import com.example.carmodels.Models.Entity.UserModels;
import com.example.carmodels.Security.JwtTokenProvider;
import com.example.carmodels.exception.AccessDeniedException;
import com.example.carmodels.exception.DataValidationException;
import com.example.carmodels.exception.ResourceNotFoundException;
import com.example.carmodels.repository.RoleRepository;
import com.example.carmodels.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.example.carmodels.constants.JWTConst.COOKIE_NAME;
import static com.example.carmodels.constants.JWTConst.TOKEN_TYPE;
import static com.example.carmodels.constants.RoleConst.BASIC_USER;

@Service
public class UserModelService {

    @Value("${app.jwt.cookieExpiry}")
    private int cookieExpiry;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructs a new UserModelService with the provided dependencies.
     *
     * @param userRepository        The repository for managing user data.
     * @param modelMapper           The mapper for object-to-object transformation.
     * @param passwordEncoder       The encoder for encoding passwords.
     * @param roleRepository        The repository for managing role data.
     * @param jwtTokenProvider      The provider for generating and validating JWT tokens.
     * @param authenticationManager The manager for authenticating users.
     */
    public UserModelService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
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

            // Create new HashSet with role.
            Set<RoleModels> roleModels = new HashSet<>();
            roleModels.add(roleModel);

            // Set the persisted RoleModels entity on the UserModels entity
            userModel.setRoles(roleModels);

            // Save the user model to the repository and return the saved entity
            return userRepository.save(userModel);
        }

        // Throw a DataValidationException to indicate that the registration data is invalid
        throw new DataValidationException();
    }


    /**
     * Authenticates a user using the provided login credentials.
     * If authentication is successful, generates a JWT token for the user,
     * saves it in the database, and returns it as a cookie.
     *
     * @param loginDTO The data transfer object containing the user's login credentials.
     * @return The JWT token wrapped in a cookie upon successful authentication.
     * @throws ResourceNotFoundException If the user is not found in the database.
     * @throws AccessDeniedException     If the provided password is incorrect.
     */
    public Cookie authenticateUser(@Valid LoginDTO loginDTO) {
        // Retrieve user information from the database based on the provided email
        Optional<UserModels> userModelsOptional = userRepository.findByEmail(loginDTO.getEmail());

        // Check if user exists, otherwise throw ResourceNotFoundException
        if (userModelsOptional.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        UserModels userModels = userModelsOptional.get();

        // Verify if the provided password matches the user's password, otherwise throw AccessDeniedException
        if (!passwordEncoder.matches(loginDTO.getPassword(), userModels.getPassword())) {
            throw new AccessDeniedException();
        }

        // Authenticate the user using the provided credentials
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = "";

        // Generate a new token if the user doesn't have one or if the existing token is invalid
        if (userModels.getJwtToken() == null || userModels.getJwtToken().isEmpty() || !jwtTokenProvider.validateToken(userModels.getJwtToken())) {
            token = jwtTokenProvider.generateToken(authentication);
        } else {
            token = userModels.getJwtToken();
        }

        // Save the token in the user's record in the database
        if (!token.isEmpty()) {
            userModels.setJwtToken(token);
            userRepository.save(userModels);
        }

        // Prepend token type to the token
        token = TOKEN_TYPE + token;

        // Create a cookie containing the token
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setMaxAge((cookieExpiry));
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }

}

