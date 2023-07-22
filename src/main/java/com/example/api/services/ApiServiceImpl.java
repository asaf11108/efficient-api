package com.example.api.services;

import org.springframework.stereotype.Service;

import com.example.api.SpecificationBuilder;
import com.example.api.dto.QueryDto;
import com.example.api.entities.Car;
import com.example.api.entities.Person;
import com.example.api.mappers.CarMapper;
import com.example.api.repositories.CarRepository;
import com.example.api.repositories.CityRepository;
import com.example.api.repositories.PersonRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiServiceImpl implements ApiService {
    private final PersonRepository personRepository;
    private final CarRepository carRepository;
    private final CityRepository cityRepository;

    
    @Override
    public List<Person> getPeople(QueryDto queryDto, Pageable pageable) {
        Specification<Person> spec = SpecificationBuilder.build(queryDto.getFilters());
        return personRepository.findAll(spec, pageable);
    }

    @Override
    public Car patchCar(int id, Car newCar) {
        return carRepository.findById(id)
        .map(car -> {
            CarMapper.INSTANCE.updateCarFromDto(car, newCar);
            return carRepository.save(car);
        }).orElseThrow();
    }
    
}
