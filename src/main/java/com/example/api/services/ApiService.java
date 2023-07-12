package com.example.api.services;

import java.util.List;

import com.example.api.dto.QueryDto;
import com.example.api.entities.Person;

public interface ApiService {
    List<Person> getApi(QueryDto queryDto);
}
