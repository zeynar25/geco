package com.example.geco.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.geco.domains.FeedbackCategory;

public interface FeedbackCategoryRepository extends JpaRepository<FeedbackCategory, Integer> {
	boolean existsByLabelIgnoreCase(String label);
}
