package com.example.carmodels.CarModelControler;

import org.springframework.ui.Model;
import com.example.carmodels.Models.CarModels;
import com.example.carmodels.service.CarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class CarModelController {

    @Autowired
    CarModelService service;

    @GetMapping("/")
    public String findall(Model model){
        model.addAttribute("carmodels", service.findallCarModels());
        return "all";
    }

    @GetMapping("/add")
    public String lunchAddNewModel(Model model){
        model.addAttribute("carmodels", new CarModels());
        return "add";
    }

    @PostMapping("/addmodel")
    public String AddNewModel(CarModels carModels) {
        service.addCarModels(carModels);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String openEditPage(Model model, @PathVariable("id") int id){
        model.addAttribute("carmodels", service.findCarModelsById(id));
        return "edit";
    }

    @PostMapping("/updatemodel")
    public String updateModel(CarModels carModels){
        service.updateCarModels(carModels);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteModel(@PathVariable("id") int id ){
        service.deleteById(id);
        return "redirect:/";
    }

}
