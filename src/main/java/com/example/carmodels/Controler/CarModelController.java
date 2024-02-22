package com.example.carmodels.Controler;

import com.example.carmodels.Models.Entity.CarModels;
import com.example.carmodels.Security.AuthUser;
import com.example.carmodels.Service.CarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CarModelController {

    @Autowired
    CarModelService service;

    @Autowired
    AuthUser authUser;

    @GetMapping("/")
    public String findall(Model model) {
        if (!authUser.isUserLogged()) {
            return "redirect:/auth/login";
        }

        model.addAttribute("carmodels", service.findallCarModels());
        return "all";
    }

    @GetMapping("/add")
    public String lunchAddNewModel(Model model) {
        if (!authUser.isUserLogged()) {
            return "redirect:/auth/login";
        }

        model.addAttribute("carmodels", new CarModels());
        return "add";
    }

    @PostMapping("/addmodel")
    public String AddNewModel(CarModels carModels) {
        if (!authUser.isUserLogged()) {
            return "redirect:/auth/login";
        }

        service.addCarModels(carModels);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String openEditPage(Model model, @PathVariable("id") int id) {
        if (!authUser.isUserLogged()) {
            return "redirect:/auth/login";
        }

        model.addAttribute("carmodels", service.findCarModelsById(id));
        return "edit";
    }

    @PostMapping("/updatemodel")
    public String updateModel(CarModels carModels) {
        if (!authUser.isUserLogged()) {
            return "redirect:/auth/login";
        }

        service.updateCarModels(carModels);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteModel(@PathVariable("id") int id) {
        if (!authUser.isUserLogged()) {
            return "redirect:/auth/login";
        }

        service.deleteById(id);
        return "redirect:/";
    }

}