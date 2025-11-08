package com.example.geco.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.geco.domains.Attraction;

@Repository
public interface AttractionRepository  extends CrudRepository<Attraction, Integer>{
}
