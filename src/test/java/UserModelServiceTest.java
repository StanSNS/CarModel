import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.carmodels.service.UserModelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.carmodels.Models.DTO.RegisterDTO;
import com.example.carmodels.Models.Entity.UserModels;
import com.example.carmodels.repository.UserRepository;

public class UserModelServiceTest {

    // Mocks for dependencies
    @Mock
    UserRepository userRepositoryMock;

    @Mock
    ModelMapper modelMapperMock;

    @Mock
    PasswordEncoder passwordEncoderMock;

    // Service to be tested, with dependencies injected
    @InjectMocks
    UserModelService userService;

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

        // Create a UserModels object with test data
        UserModels userModel = new UserModels();
        userModel.setEmail("test@gmail1.com");
        userModel.setName("testuser");
        userModel.setPassword("encodedpassword");

        // Mock behavior of dependencies
        when(modelMapperMock.map(registerDTO, UserModels.class)).thenReturn(userModel);
        when(passwordEncoderMock.encode(registerDTO.getPassword())).thenReturn("encodedpassword");
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
        verify(userRepositoryMock).save(userModel);
    }
}
