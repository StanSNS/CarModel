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

    /**
     * Displays all car models.
     *
     * @param model The model to be populated with data.
     * @return The view for displaying all car models.
     */
    @GetMapping("/")
    public String findall(Model model) {
        if (!authUser.isUserLogged()) {
            return "redirect:/auth/login";
        }

        model.addAttribute("carmodels", service.findallCarModels());
        return "all";
    }

    /**
     * Displays the form to add a new car model.
     *
     * @param model The model to be populated with data.
     * @return The view for adding a new car model.
     */
    @GetMapping("/add")
    public String lunchAddNewModel(Model model) {
        if (!authUser.isUserLogged()) {
            return "redirect:/auth/login";
        }

        model.addAttribute("carmodels", new CarModels());
        return "add";
    }

    /**
     * Handles the addition of a new car model.
     *
     * @param carModels The car model object to be added.
     * @return Redirects to the home page after adding the new model.
     */
    @PostMapping("/addmodel")
    public String AddNewModel(CarModels carModels) {
        if (!authUser.isUserLogged()) {
            return "redirect:/auth/login";
        }

        service.addCarModels(carModels);
        return "redirect:/";
    }

    /**
     * Displays the form to edit a car model.
     *
     * @param model The model to be populated with data.
     * @param id    The ID of the car model to be edited.
     * @return The view for editing a car model.
     */
    @GetMapping("/edit/{id}")
    public String openEditPage(Model model, @PathVariable("id") int id) {
        if (!authUser.isUserLogged()) {
            return "redirect:/auth/login";
        }

        model.addAttribute("carmodels", service.findCarModelsById(id));
        return "edit";
    }

    /**
     * Handles the update of a car model.
     *
     * @param carModels The car model object to be updated.
     * @return Redirects to the home page after updating the model.
     */
    @PostMapping("/updatemodel")
    public String updateModel(CarModels carModels) {
        if (!authUser.isUserLogged()) {
            return "redirect:/auth/login";
        }

        service.updateCarModels(carModels);
        return "redirect:/";
    }

    /**
     * Deletes a car model.
     *
     * @param id The ID of the car model to be deleted.
     * @return Redirects to the home page after deleting the model.
     */
    @GetMapping("/delete/{id}")
    public String deleteModel(@PathVariable("id") int id) {
        if (!authUser.isUserLogged()) {
            return "redirect:/auth/login";
        }

        service.deleteById(id);
        return "redirect:/";
    }
}