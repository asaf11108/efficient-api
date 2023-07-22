package com.example.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.api.entities.City;

public interface CityRepository extends JpaRepository<City, Integer> {
    
}
