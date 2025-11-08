package com.example.geco.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.geco.repositories.AttractionRepository;

@Service
public class AttractionService {
	@Autowired
	AttractionRepository attractionRepository;
	
	public double getAttractionsNumber() {
		return attractionRepository.count();
	}
}
