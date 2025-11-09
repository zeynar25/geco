package com.example.geco.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.geco.domains.Feedback;
import com.example.geco.dto.FeedbackResponse;

@RestController
public class FeedbackController extends AbstractController {
	@PostMapping("/feedback")
	public ResponseEntity<?> addFeedback(@RequestBody Feedback feedback) {
		try {
			FeedbackResponse savedFeedback = feedbackService.addFeedback(feedback);
			return new ResponseEntity<>(savedFeedback, HttpStatus.CREATED);
	        
	    } catch (IllegalArgumentException e) {
	    	Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("error", e.getMessage());
	        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	    }
	}
	
	// to implement
	@GetMapping("/feedback/{id}")
	public ResponseEntity<Feedback> getFeedback(@PathVariable int id) {
		return new ResponseEntity<>(new Feedback(), HttpStatus.OK);
	}
	
	// to implement
	@GetMapping("/feedback/{category}/{year}/{month}")
	public ResponseEntity<List<Feedback>> getFeedbackByCategory(@PathVariable String category, @PathVariable int year, @PathVariable int month) {
		List<Feedback> feedbacks = new ArrayList();
		return new ResponseEntity<>(feedbacks, HttpStatus.OK);
	}
	
	// to implement
	@PatchMapping("/feedback")
	public ResponseEntity<Feedback> updateFeedback(@RequestBody Feedback feedback) {
		return new ResponseEntity<>(new Feedback(), HttpStatus.OK);
	}

	// to implement
	@DeleteMapping("/feedback/{id}")
	public ResponseEntity<Feedback> deleteFeedback(@PathVariable int id) {
		return new ResponseEntity<>(new Feedback(), HttpStatus.OK);
	}
}
