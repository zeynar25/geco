package com.example.geco.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.geco.domains.Feedback;

@Repository
public interface FeedbackRepository  extends CrudRepository<Feedback, Integer>{
	@Query("SELECT AVG(f.stars) FROM Feedback f")
	double getAverageStars();
}
