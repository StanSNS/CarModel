package com.example.carmodels.repository;

import com.example.carmodels.Models.Entity.CarModels;
import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<CarModels, Integer> {
}
