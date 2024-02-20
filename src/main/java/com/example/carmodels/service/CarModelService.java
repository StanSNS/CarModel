package com.example.carmodels.service;

import com.example.carmodels.Models.CarModels;
import com.example.carmodels.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarModelService {

    @Autowired
    CarRepository repository;

    public List<CarModels> findallCarModels(){
       return (List<CarModels>) repository.findAll();
    }
    public CarModels findCarModelsById(int id){
        Optional<CarModels> result = repository.findById(id);
        if (result.isPresent()){
            return result.get();
        }
        return new CarModels();
    }

    public CarModels addCarModels(CarModels carModels){
        return repository.save(carModels);
    }

    public CarModels updateCarModels(CarModels carModels){
        Optional<CarModels> result = repository.findById(carModels.getId());
        CarModels existing=result.get();
        existing.setManufacturer(carModels.getManufacturer());
        existing.setCarModel(carModels.getCarModel());
        existing.setCarYear(carModels.getCarYear());
        return repository.save(existing);
    }

    public void deleteById(int id){
        repository.deleteById(id);
    }

}
