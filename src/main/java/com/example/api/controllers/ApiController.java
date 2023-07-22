package com.example.api.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.dto.QueryDto;
import com.example.api.entities.Car;
import com.example.api.entities.Person;
import com.example.api.services.ApiService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiController {
    private final ApiService apiService;

    @GetMapping("people")
    public List<Person> getPeople(HttpServletRequest request, Pageable pageable) throws Exception {
        QueryDto queryDto = new QueryDto(request.getQueryString());
        return apiService.getPeople(queryDto, pageable);
    }

    @PatchMapping("cars/{id}")
    public Car patchCar(@PathVariable("id") int id, @RequestBody Car car) {
        return apiService.patchCar(id, car);
    }
}
