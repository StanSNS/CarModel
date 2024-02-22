package com.example.carmodels.Controler;

import com.example.carmodels.Models.DTO.LoginDTO;
import com.example.carmodels.Models.DTO.RegisterDTO;
import com.example.carmodels.Security.AuthUser;
import com.example.carmodels.Service.UserModelService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class UserModelController {

    private final UserModelService service;
    private final AuthUser authUser;

    /**
     * Constructor injecting required services.
     *
     * @param service  The user model service.
     * @param authUser The authentication user service.
     */
    public UserModelController(UserModelService service, AuthUser authUser) {
        this.service = service;
        this.authUser = authUser;
    }

    /**
     * Displays the registration form if the user is not logged in.
     *
     * @param model The model to be populated with data.
     * @return The view for user registration.
     */
    @GetMapping("/register")
    public String showRegister(Model model) {
        if (authUser.isUserLogged()) {
            return "redirect:/";
        }
        model.addAttribute("registerUser", new RegisterDTO());
        return "register";
    }

    /**
     * Handles user registration.
     *
     * @param registerDTO The registration data transfer object.
     * @return Redirects to the login page after successful registration.
     */
    @PostMapping("/register")
    public String registerUser(@Valid RegisterDTO registerDTO) {
        if (authUser.isUserLogged()) {
            return "redirect:/";
        }
        service.addUserModel(registerDTO);
        return "redirect:/auth/login";
    }

    /**
     * Displays the login form if the user is not logged in.
     *
     * @param model The model to be populated with data.
     * @return The view for user login.
     */
    @GetMapping("/login")
    public String showLogin(Model model) {
        if (authUser.isUserLogged()) {
            return "redirect:/";
        }
        model.addAttribute("loginUser", new LoginDTO());
        return "login";
    }

    /**
     * Handles user login.
     *
     * @param loginDTO The login data transfer object.
     * @param response The HTTP response object to add cookies for user authentication.
     * @return Redirects to the home page after successful login.
     */
    @PostMapping("/login")
    public String loginUser(@Valid LoginDTO loginDTO, HttpServletResponse response) {
        if (authUser.isUserLogged()) {
            return "redirect:/";
        }
        response.addCookie(service.authenticateUser(loginDTO));
        return "redirect:/";
    }
}


