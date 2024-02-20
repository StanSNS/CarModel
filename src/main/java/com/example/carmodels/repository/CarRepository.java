package com.example.carmodels.repository;

import com.example.carmodels.Models.CarModels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.yaml.snakeyaml.events.Event;

public interface CarRepository extends CrudRepository<CarModels, Integer> {
}
