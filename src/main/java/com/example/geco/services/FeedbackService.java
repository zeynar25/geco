package com.example.geco.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.geco.domains.Feedback;
import com.example.geco.domains.FeedbackCategory;
import com.example.geco.dto.FeedbackResponse;
import com.example.geco.repositories.AccountRepository;
import com.example.geco.repositories.BookingRepository;
import com.example.geco.repositories.FeedbackCategoryRepository;
import com.example.geco.repositories.FeedbackRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FeedbackService {
	@Autowired
	private FeedbackCategoryRepository feedbackCategoryRepository;
	
	@Autowired
	private FeedbackRepository feedbackRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private BookingRepository bookingRepository;
	
	public void validateFeedback(Feedback feedback) {
		if (feedback.getAccount() == null || feedback.getAccount().getAccountId() == null) {
	        throw new IllegalArgumentException("Account is missing or invalid.");
	    }
		
	    accountRepository.findById(
	    		feedback.getAccount().getAccountId()
	    ).orElseThrow(
	    		() -> new EntityNotFoundException("Account not found.")
		);

	    if (feedback.getBooking() == null || feedback.getBooking().getBookingId() == null) {
	        throw new IllegalArgumentException("Booking is missing or invalid.");
	    }

	    bookingRepository.findById(
	    		feedback.getBooking().getBookingId()
	    ).orElseThrow(
	    		() -> new EntityNotFoundException("Booking not found.")
	    );
	    
	    if (feedback.getCategory() == null || feedback.getCategory().getFeedbackCategoryId() == null) {
	        throw new IllegalArgumentException("Category is missing or invalid.");
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
				feedback.getAccount(),
				feedback.getBooking(),
				feedback.getCategory().getLabel(),
				feedback.getStars(),
				feedback.getComment(),
				feedback.getSuggestion()
		);
	}
	
	
	public FeedbackResponse addFeedback(Feedback feedback) {
		validateFeedback(feedback);
		
		// Check if the account's feedback for that booking already exist.
		if (feedbackRepository.existsByBooking_BookingId(feedback.getBooking().getBookingId())) {
			throw new EntityNotFoundException("Feedback for this booking already exist.");
		}
		
		FeedbackCategory existingCategory = feedbackCategoryRepository.findById(
	    		feedback.getCategory().getFeedbackCategoryId())
		.orElseThrow(() ->
				new EntityNotFoundException("Category not found.")
		);
		
	    String providedLabel = feedback.getCategory().getLabel().trim().toLowerCase();
	    String actualLabel = existingCategory.getLabel().trim().toLowerCase();
	    
	    if (!providedLabel.equals(actualLabel)) {
	        throw new IllegalArgumentException(
	            "Category label does not match the stored category name for this ID."
	        );
	    }
	    
		Feedback savedFeedback = feedbackRepository.save(feedback);

	    return toResponse(savedFeedback);
	}
	
	public double getAverageRating() {
		return feedbackRepository.getAverageStars();
	}
}
