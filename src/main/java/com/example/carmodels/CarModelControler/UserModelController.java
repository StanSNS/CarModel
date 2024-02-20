package com.example.carmodels.CarModelControler;

import com.example.carmodels.Models.UserModels;
import com.example.carmodels.service.UserModelService;
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

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("users", new UserModels());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(UserModels userModels) {
        service.addUserModel(userModels);
        return "redirect:/";
    }

}


