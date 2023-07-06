package com.example.api.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.api.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    List<Person> findAll(Specification<Person> spec);
}
