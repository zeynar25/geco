package com.example.geco.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.geco.domains.Feedback;

@Repository
public interface FeedbackRepository  extends JpaRepository<Feedback, Integer>{
	@Query("SELECT AVG(f.stars) FROM Feedback f")
	double getAverageStars();
	
	boolean existsByBooking_BookingId(int bookingId);
}
