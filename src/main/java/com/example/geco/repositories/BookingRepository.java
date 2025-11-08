package com.example.geco.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.geco.domains.Booking;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Integer>{
	List<Booking> findAllByOrderByVisitDateAsc();
}
