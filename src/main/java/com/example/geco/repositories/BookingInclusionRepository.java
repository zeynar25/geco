package com.example.geco.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.geco.domains.BookingInclusion;

@Repository
public interface BookingInclusionRepository extends JpaRepository<BookingInclusion, Integer>{

}
