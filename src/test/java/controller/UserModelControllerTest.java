package controller;

import com.example.carmodels.Controler.UserModelController;
import com.example.carmodels.Models.DTO.LoginDTO;
import com.example.carmodels.Models.DTO.RegisterDTO;
import com.example.carmodels.Security.AuthUser;
import com.example.carmodels.Service.UserModelService;
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
    UserModelService service;

    @Mock
    AuthUser authUser;

    @Mock
    Model model;

    @Mock
    HttpServletResponse response;

    @InjectMocks
    UserModelController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void showRegister_NotLoggedIn_ReturnsRegisterView() {
        // Given
        when(authUser.isUserLogged()).thenReturn(false);

        // When
        String view = controller.showRegister(model);

        // Then
        assertEquals("register", view);
        verify(model).addAttribute(eq("registerUser"), any(RegisterDTO.class));
    }

    @Test
    void showRegister_LoggedIn_ReturnsRedirect() {
        // Given
        when(authUser.isUserLogged()).thenReturn(true);

        // When
        String view = controller.showRegister(model);

        // Then
        assertEquals("redirect:/", view);
        verifyNoInteractions(model);
    }

    @Test
    void registerUser_NotLoggedIn_AddsUserAndRedirectsToLogin() {
        // Given
        RegisterDTO registerDTO = new RegisterDTO();

        // When
        String view = controller.registerUser(registerDTO);

        // Then
        assertEquals("redirect:/auth/login", view);
        verify(service).addUserModel(registerDTO);
    }

    @Test
    void registerUser_LoggedIn_ReturnsRedirect() {
        // Given
        when(authUser.isUserLogged()).thenReturn(true);

        // When
        String view = controller.registerUser(new RegisterDTO());

        // Then
        assertEquals("redirect:/", view);
        verifyNoInteractions(service);
    }

    @Test
    void showLogin_NotLoggedIn_ReturnsLoginView() {
        // Given
        when(authUser.isUserLogged()).thenReturn(false);

        // When
        String view = controller.showLogin(model);

        // Then
        assertEquals("login", view);
        verify(model).addAttribute(eq("loginUser"), any(LoginDTO.class));
    }

    @Test
    void showLogin_LoggedIn_ReturnsRedirect() {
        // Given
        when(authUser.isUserLogged()).thenReturn(true);

        // When
        String view = controller.showLogin(model);

        // Then
        assertEquals("redirect:/", view);
        verifyNoInteractions(model);
    }

    @Test
    void loginUser_NotLoggedIn_AuthenticatesAndRedirectsToHome() {
        // Given
        LoginDTO loginDTO = new LoginDTO();

        // When
        String view = controller.loginUser(loginDTO, response);

        // Then
        assertEquals("redirect:/", view);
        verify(service).authenticateUser(loginDTO);
        verify(response).addCookie(any());
    }

    @Test
    void loginUser_LoggedIn_ReturnsRedirect() {
        // Given
        when(authUser.isUserLogged()).thenReturn(true);

        // When
        String view = controller.loginUser(new LoginDTO(), response);

        // Then
        assertEquals("redirect:/", view);
        verifyNoInteractions(service);
        verifyNoInteractions(response);
    }
}
