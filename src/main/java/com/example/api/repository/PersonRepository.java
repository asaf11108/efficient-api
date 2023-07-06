package com.example.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.api.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
}
