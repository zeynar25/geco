package com.example.geco.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.geco.domains.FeedbackCategory;

@RestController
@RequestMapping("/feedback-category")
public class FeedbackCategoryController extends AbstractController {
	@PostMapping
	public ResponseEntity<FeedbackCategory> addFeedbackCategory(@RequestBody FeedbackCategory category) {
		FeedbackCategory savedCategory = feedbackCategoryService.addCategory(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<FeedbackCategory> getFeedbackCategory(@PathVariable int id) {
		FeedbackCategory category = feedbackCategoryService.getCategory(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<FeedbackCategory>> getAllFeedbackCategories() {
		List<FeedbackCategory> categories = feedbackCategoryService.getAllCategories();
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<FeedbackCategory> updateFeedbackCategory(@PathVariable int id, @RequestBody FeedbackCategory category) {
		category.setFeedbackCategoryId(id);
		FeedbackCategory updatedCategory = feedbackCategoryService.updateCategory(category);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<FeedbackCategory> deleteFeedbackCategory(@PathVariable int id) {
		feedbackCategoryService.deleteCategory(id);
	    return ResponseEntity.noContent().build();
	}
}
