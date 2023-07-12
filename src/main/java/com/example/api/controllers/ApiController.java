package com.example.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.dto.QueryDto;
import com.example.api.entities.Person;
import com.example.api.repositories.PersonRepository;
import com.example.api.services.ApiService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiController {
    private final ApiService apiService;

    @GetMapping
    // Other option to get raw query params
    // @Context UriInfo uriInfo
    // uriInfo.getRequestUri().getQuery()
    public List<Person> getApi(HttpServletRequest request) throws Exception {
        QueryDto queryDto = new QueryDto(request.getQueryString());
        return apiService.getApi(queryDto);
    }
}
