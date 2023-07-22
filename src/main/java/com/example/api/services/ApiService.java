package com.example.api.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.example.api.dto.QueryDto;
import com.example.api.entities.Car;
import com.example.api.entities.Person;

public interface ApiService {
    List<Person> getPeople(QueryDto queryDto, Pageable pageable);

    Car patchCar(int id, Car car);
}
