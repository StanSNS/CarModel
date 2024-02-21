package service;

import static com.example.carmodels.constants.JWTConst.TOKEN_TYPE;
import static com.example.carmodels.constants.RoleConst.BASIC_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.carmodels.Models.DTO.LoginDTO;
import com.example.carmodels.Models.Entity.RoleModels;
import com.example.carmodels.Security.JwtTokenProvider;
import com.example.carmodels.exception.AccessDeniedException;
import com.example.carmodels.exception.ResourceNotFoundException;
import com.example.carmodels.repository.RoleRepository;
import com.example.carmodels.service.UserModelService;
import com.example.carmodels.exception.DataValidationException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.carmodels.Models.DTO.RegisterDTO;
import com.example.carmodels.Models.Entity.UserModels;
import com.example.carmodels.repository.UserRepository;

import java.util.Optional;

public class UserModelServiceTest {

    // Mocks for dependencies
    @Mock
    UserRepository userRepositoryMock;

    @Mock
    RoleRepository roleRepositoryMock;

    @Mock
    ModelMapper modelMapperMock;

    @Mock
    PasswordEncoder passwordEncoderMock;

    // Service to be tested, with dependencies injected
    @InjectMocks
    UserModelService userService;

    @Mock
    JwtTokenProvider jwtTokenProviderMock;

    @Mock
    AuthenticationManager authenticationManagerMock;

    /**
     * Setup method to initialize mocks before each test case.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test method for addUserModel functionality.
     */
    @Test
    void testAddUserModel() {
        // Create a RegisterDTO object with test data
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("test@gmail.com");
        registerDTO.setName("testuser");
        registerDTO.setPassword("testpassword");

        // Create a RoleModels object
        RoleModels roleModels = new RoleModels();
        roleModels.setName(BASIC_USER);

        // Create a UserModels object with test data
        UserModels userModel = new UserModels();
        userModel.setEmail("test@gmail1.com");
        userModel.setName("testuser");
        userModel.setPassword("encodedpassword");

        // Mock behavior of dependencies
        when(modelMapperMock.map(registerDTO, UserModels.class)).thenReturn(userModel);
        when(passwordEncoderMock.encode(registerDTO.getPassword())).thenReturn("encodedpassword");

        // Mock behavior of RoleRepository to save the role
        when(roleRepositoryMock.save(any(RoleModels.class))).thenReturn(roleModels);

        // Mock behavior of UserRepository to save the user
        when(userRepositoryMock.save(userModel)).thenReturn(userModel);

        // Invoke the addUserModel method and capture the result
        UserModels savedUser = userService.addUserModel(registerDTO);

        // Assert that the savedUser is not null and contains expected data
        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getName());
        assertEquals("encodedpassword", savedUser.getPassword());

        // Verify that mock interactions occurred as expected
        verify(modelMapperMock).map(registerDTO, UserModels.class);
        verify(passwordEncoderMock).encode(registerDTO.getPassword());
        verify(roleRepositoryMock).save(any(RoleModels.class)); // Verify role saving
        verify(userRepositoryMock).save(userModel);
    }


    @Test
    void testAddUserModel_ExistingEmail() {
        // Create a RegisterDTO object with test data
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("test@gmail.com");
        registerDTO.setName("testuser");
        registerDTO.setPassword("testpassword");

        // Mock behavior of UserRepository to simulate existing email
        when(userRepositoryMock.existsByEmail("test@gmail.com")).thenReturn(true);

        // Verify that DataValidationException is thrown when adding a user with existing email
        assertThrows(DataValidationException.class, () -> userService.addUserModel(registerDTO));
    }

    @Test
    void testAuthenticateUser_SuccessfulAuthentication() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@gmail.com");
        loginDTO.setPassword("testpassword");

        UserModels user = new UserModels();
        user.setEmail("test@gmail.com");
        user.setPassword(passwordEncoderMock.encode("testpassword"));

        Authentication authentication = mock(Authentication.class);

        when(userRepositoryMock.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoderMock.matches("testpassword", user.getPassword())).thenReturn(true);
        when(authenticationManagerMock.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProviderMock.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProviderMock.generateToken(authentication)).thenReturn("generated_token");

        // Act
        Cookie cookie = userService.authenticateUser(loginDTO);

        // Assert
        assertNotNull(cookie);
        assertEquals(TOKEN_TYPE+"generated_token", cookie.getValue());
        assertEquals("/", cookie.getPath());

        verify(userRepositoryMock, times(1)).findByEmail("test@gmail.com");
        verify(passwordEncoderMock, times(1)).matches("testpassword", user.getPassword());
        verify(authenticationManagerMock, times(1)).authenticate(any());
        verify(jwtTokenProviderMock, times(1)).generateToken(authentication);
        verify(userRepositoryMock, times(1)).save(user);
    }

    @Test
    void testAuthenticateUser_UserNotFound() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@gmail.com");
        loginDTO.setPassword("testpassword");

        when(userRepositoryMock.findByEmail("test@gmail.com")).thenReturn(Optional.empty());

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.authenticateUser(loginDTO));
    }

    @Test
    void testAuthenticateUser_IncorrectPassword() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@gmail.com");
        loginDTO.setPassword("testpassword");

        UserModels user = new UserModels();
        user.setEmail("test@gmail.com");
        user.setPassword(passwordEncoderMock.encode("anotherpassword"));

        when(userRepositoryMock.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoderMock.matches("testpassword", user.getPassword())).thenReturn(false);

        // Assert
        assertThrows(AccessDeniedException.class, () -> userService.authenticateUser(loginDTO));
    }
}
