package com.example.geco.services;

import java.util.List;

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
	
	public void validateCategory(FeedbackCategory category) {
		if (category.getLabel() == null || category.getLabel().trim().isEmpty()) {
	        throw new IllegalArgumentException("Label must at least have 1 character.");
	    }
	}
	
	public FeedbackCategory addCategory(FeedbackCategory category) {
		validateCategory(category);
		
		String label = category.getLabel();
		
		// Check if this category label already exist.
		if (feedbackCategoryRepository.existsByLabelIgnoreCase(label)) {
			throw new IllegalArgumentException("Label \"" + label + "\" already exist.");
		}
		
		return feedbackCategoryRepository.save(category);
	}
	
	public FeedbackCategory getCategory(int id) {
		return feedbackCategoryRepository.findById(id)
				.orElseThrow(
	            		() -> new EntityNotFoundException("Feedback Category with ID " + id + " not found.")
	            );
	}
	
	public List<FeedbackCategory> getAllCategories() {
		return feedbackCategoryRepository.findAll();
	}
	
	public FeedbackCategory updateCategory(FeedbackCategory category) {
		FeedbackCategory existingCategory = feedbackCategoryRepository.findById(
				category.getFeedbackCategoryId()
		).orElseThrow(
				() -> new EntityNotFoundException("Feedback category \""+ category.getLabel() +"\" not found.")
		);
		
		validateCategory(category);
		String label = category.getLabel();
		
		// Check if this category label already exist.
		if (feedbackCategoryRepository.existsByLabelIgnoreCase(label)) {
			throw new IllegalArgumentException("Label \"" + label + "\" already exist.");
		}
		
		existingCategory.setLabel(category.getLabel());
		
		return feedbackCategoryRepository.save(existingCategory);
	}
	
	public FeedbackCategory deleteCategory(int id) {
		FeedbackCategory existingCategory = feedbackCategoryRepository.findById(
				id
		).orElseThrow(
				() -> new EntityNotFoundException("Feedback category with ID " + id + " not found.")
		);
		
		feedbackCategoryRepository.delete(existingCategory);
		
		return existingCategory;
	}
	
	
	
	
	// ---------------------------------------------------------------------------------------------------
	
	
	
	
	// Check if the account's feedback for that booking already exist.
	public void validateFeedback(Feedback feedback) {
	    accountRepository.findById(feedback.getUserId().getUserId())
	            .orElseThrow(() -> new IllegalArgumentException("Account not found."));

	    bookingRepository.findById(
	    		feedback.getBookingId().getBookingId()
	    ).orElseThrow(
	    		() -> new IllegalArgumentException("Booking not found.")
	    );
	    
	    if (feedback.getCategory() == null) {
	        throw new IllegalArgumentException("Category is null.");
	    }
	    
	    FeedbackCategory existingCategory = feedbackCategoryRepository.findById(
	    		feedback.getCategory().getFeedbackCategoryId())
		.orElseThrow(() ->
				new IllegalArgumentException("Category not found.")
		);
	    
	    String providedLabel = feedback.getCategory().getLabel().trim().toLowerCase();
	    String actualLabel = existingCategory.getLabel().trim().toLowerCase();
	    
	    if (!providedLabel.equals(actualLabel)) {
	        throw new IllegalArgumentException(
	            "Category label does not match the stored category name for this ID."
	        );
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
				feedback.getCategory().getLabel(),
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
