package com.example.carmodels.Controler;

import com.example.carmodels.Models.DTO.LoginDTO;
import com.example.carmodels.Models.DTO.RegisterDTO;
import com.example.carmodels.service.UserModelService;
import jakarta.servlet.http.HttpServletRequest;
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

    public UserModelController(UserModelService service) {
        this.service = service;
    }


    /**
     * Displays the user registration form.
     *
     * @param model Model object to add attributes for the view
     * @return String representing the view name for registration form
     */
    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "register";
    }

    /**
     * Handles the submission of the user registration form.
     *
     * @param registerDTO RegisterDTO object containing user registration data
     * @return String representing the redirection to the home page
     */
    @PostMapping("/register")
    public String registerUser(@Valid RegisterDTO registerDTO) {
        service.addUserModel(registerDTO);
        return "redirect:/auth/login";
    }

    /**
     * Displays the user login form.
     *
     * @param model Model object to add attributes for the view
     * @return String representing the view name for login form
     */
    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("loginUser", new LoginDTO());
        return "login";
    }

    /**
     * Controller method for handling user login.
     * Attempts to authenticate the user using the provided login credentials.
     * If authentication is successful, a redirect to the home page is performed.
     *
     * @param loginDTO The data transfer object containing the user's login credentials.
     * @param response The HTTP response object to add cookies for user authentication.
     * @return A redirect to the home page upon successful authentication.
     */
    @PostMapping("/login")
    public String loginUser(@Valid LoginDTO loginDTO, HttpServletResponse response) {
        // Authenticate the user and add authentication cookies to the response
        response.addCookie(service.authenticateUser(loginDTO));
        // Redirect the user to the home page
        return "redirect:/";
    }
}


