package controller;

import com.example.carmodels.Controler.UserModelController;
import com.example.carmodels.Models.DTO.LoginDTO;
import com.example.carmodels.Models.DTO.RegisterDTO;
import com.example.carmodels.service.UserModelService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserModelControllerTest {

    @Mock
    private UserModelService service;

    @Mock
    private Model model;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private UserModelController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testShowRegister() {
        String viewName = controller.showRegister(model);
        assertEquals("register", viewName);
        verify(model).addAttribute(eq("registerUser"), any(RegisterDTO.class));
    }

    @Test
    public void testRegisterUser() {
        RegisterDTO registerDTO = new RegisterDTO();
        String redirect = controller.registerUser(registerDTO);
        assertEquals("redirect:/auth/login", redirect);
        verify(service).addUserModel(registerDTO);
    }

    @Test
    public void testShowLogin() {
        String viewName = controller.showLogin(model);
        assertEquals("login", viewName);
        verify(model).addAttribute(eq("loginUser"), any(LoginDTO.class));
    }

    @Test
    public void testLoginUser() {
        LoginDTO loginDTO = new LoginDTO();
        Cookie cookie = new Cookie("authCookie", "dummyValue");
        when(service.authenticateUser(loginDTO)).thenReturn(cookie);

        String redirect = controller.loginUser(loginDTO, response);

        assertEquals("redirect:/", redirect);
        verify(response).addCookie(cookie);
    }
}
