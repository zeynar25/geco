package com.example.geco.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.geco.domains.Attraction;

@Repository
public interface AttractionRepository  extends JpaRepository<Attraction, Integer>{
}
