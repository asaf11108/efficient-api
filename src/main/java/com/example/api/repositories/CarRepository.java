package com.example.api.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.api.entities.Car;


public interface CarRepository extends JpaRepository<Car, Integer> {
    
}
