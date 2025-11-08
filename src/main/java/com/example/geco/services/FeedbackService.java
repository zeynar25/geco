package com.example.geco.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.geco.domains.Feedback;
import com.example.geco.dto.FeedbackResponse;
import com.example.geco.repositories.AccountRepository;
import com.example.geco.repositories.BookingRepository;
import com.example.geco.repositories.FeedbackRepository;

@Service
public class FeedbackService {
	@Autowired
	private FeedbackRepository feedbackRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	
	private BookingRepository bookingRepository;
	
	public void validateFeedback(Feedback feedback) {
	    List<String> categories = Arrays.asList(
	            "facilities",
	            "staff & service",
	            "attractions",
	            "cleanliness",
	            "overall experience"
        );
	    
	    accountRepository.findById(feedback.getUserId().getUserId())
	            .orElseThrow(() -> new IllegalArgumentException("Account not found."));

	    bookingRepository.findById(feedback.getBookingId().getBookingId())
	            .orElseThrow(() -> new IllegalArgumentException("Booking not found."));


	    if (feedback.getCategory() == null) {
		    throw new IllegalArgumentException("Select a category first");
		}
	    
	    if (!categories.contains(feedback.getCategory())) {
		    throw new IllegalArgumentException("category choices are facilities, staff&service, attractions, cleanliness, overall experience");
		}
	    
	    if (feedback.getStars() < 0 || feedback.getStars() > 5) {
	        throw new IllegalArgumentException("Stars must be between 0 and 5.");
	    }
		
		if (feedback.getComment() == null || feedback.getComment().trim().length() < 10) {
		    throw new IllegalArgumentException("Feedback comment must be at least 10 characters long.");
		}
		
		if (feedback.getSuggestion() == null || feedback.getSuggestion().trim().length() < 10) {
		    throw new IllegalArgumentException("Suggestion comment must be at least 10 characters long.");
		}
	}
	
	public FeedbackResponse toResponse(Feedback feedback) {
		return new FeedbackResponse(
				feedback.getFeedbackId(),
				feedback.getUserId(),
				feedback.getBookingId(),
				feedback.getCategory(),
				feedback.getStars(),
				feedback.getComment(),
				feedback.getSuggestion()
		);
	}
	
	public FeedbackResponse addFeedback(Feedback feedback) {
		validateFeedback(feedback);
		Feedback savedFeedback = feedbackRepository.save(feedback);

	    return toResponse(savedFeedback);
	}
	
	public double getAverageRating() {
		return feedbackRepository.getAverageStars();
	}
}
