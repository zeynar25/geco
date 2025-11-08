package com.example.geco.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.geco.domains.Feedback;
import com.example.geco.repositories.FeedbackRepository;

@Service
public class FeedbackService {
	@Autowired
	private FeedbackRepository feedbackRepository;
	
	public double getAverageRating() {
		return feedbackRepository.getAverageStars();
	}
}
