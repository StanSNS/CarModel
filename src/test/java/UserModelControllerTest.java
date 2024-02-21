import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.carmodels.Controler.UserModelController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import com.example.carmodels.Models.DTO.RegisterDTO;
import com.example.carmodels.service.UserModelService;

/**
 * Unit tests for the UserModelController class.
 */
public class UserModelControllerTest {

    // Mocks for dependencies
    @Mock
    UserModelService serviceMock;

    @Mock
    Model modelMock;

    // Controller to be tested, with dependencies injected
    @InjectMocks
    UserModelController controller;

    /**
     * Setup method to initialize mocks before each test case.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test method for showRegister functionality.
     */
    @Test
    void testShowRegister() {
        // Call the showRegister method and capture the returned view name
        String viewName = controller.showRegister(modelMock);

        // Assert that the returned view name is as expected
        assertEquals("register", viewName);

        // Verify that the modelMock's addAttribute method was called with expected parameters
        verify(modelMock).addAttribute(eq("users"), any(RegisterDTO.class));
    }

    /**
     * Test method for registerUser functionality.
     */
    @Test
    void testRegisterUser() {
        // Create a RegisterDTO object
        RegisterDTO registerDTO = new RegisterDTO();

        // Call the registerUser method and capture the returned view name
        String viewName = controller.registerUser(registerDTO);

        // Assert that the returned view name is as expected
        assertEquals("redirect:/", viewName);

        // Verify that the serviceMock's addUserModel method was called with the provided RegisterDTO object
        verify(serviceMock).addUserModel(registerDTO);
    }
}
