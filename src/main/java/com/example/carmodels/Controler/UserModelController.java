package com.example.carmodels.Controler;

import com.example.carmodels.Models.DTO.RegisterDTO;
import com.example.carmodels.service.UserModelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class UserModelController {

    @Autowired
    UserModelService service;


    /**
     * Displays the user registration form.
     *
     * @param model Model object to add attributes for the view
     * @return String representing the view name for registration form
     */
    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("users", new RegisterDTO());
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
        return "redirect:/";
    }

}


