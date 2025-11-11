package com.example.geco.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.geco.domains.Faq;
import com.example.geco.repositories.FaqRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class FaqService {
	@Autowired
	FaqRepository faqRepository;
	
	public Faq addFaq(Faq faq) {
		if (faq.getQuestion() == null || faq.getQuestion().isBlank()) {
	        throw new IllegalArgumentException("Question is missing.");
	    }
		
		if (faq.getQuestion().length() < 10) {
	        throw new IllegalArgumentException("Question must have at least 10 characters.");
	    }
		
		if (faq.getAnswer() == null || faq.getAnswer().isBlank()) {
	        throw new IllegalArgumentException("Answer is missing.");
		}
		
		if (faq.getAnswer().length() < 10) {
	        throw new IllegalArgumentException("Answer must have at least 10 characters.");
	    }
		
		faq.setQuestion(faq.getQuestion().trim());
		if (faqRepository.existsByQuestionIgnoreCase(faq.getQuestion())) {
			throw new IllegalArgumentException("Question \"" + faq.getQuestion() + "\" already exists.");
		}
		
		return faqRepository.save(faq);
	}
	
	public Faq getFaq(int id) {
		return faqRepository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("FAQ with ID \"" + id + "\" not found."));
	}
	
	public List<Faq> getAllFaqs() {
		return faqRepository.findAll();
	}
	
	public Faq updateFaq(Faq faq) {
		if (faq.getQuestion() == null && faq.getAnswer() == null) {
			throw new IllegalArgumentException("FAQ question and answer is empty.");
		}
		
		Faq existingFaq = faqRepository.findById(faq.getFaqId())
	            .orElseThrow(() -> new EntityNotFoundException("FAQ with ID \"" + faq.getFaqId() + "\" not found."));
		
		if (faq.getQuestion() != null) {
	        String newQuestion = faq.getQuestion().trim();

			// Check if it duplicates another record.
			// New question is not equal to the existing question of the id
			// And this question does not exist in FAQ records.
	        if (!existingFaq.getQuestion().equalsIgnoreCase(newQuestion) &&
	                faqRepository.existsByQuestionIgnoreCase(newQuestion)) {
	            throw new IllegalArgumentException("Another FAQ with the same question already exists.");
	        }

	        existingFaq.setQuestion(newQuestion);
	    }
		
		if (faq.getAnswer() != null) {
		    existingFaq.setAnswer(faq.getAnswer().trim());
		}
		
		return faqRepository.save(existingFaq);
	}
	
	public void deleteFaq(int id) {
		Faq faq = faqRepository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("FAQ with ID \"" + id + "\" not found."));
	    
		faqRepository.delete(faq);
	}
}
