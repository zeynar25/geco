package com.example.geco.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.geco.DataUtil;
import com.example.geco.domains.Account;
import com.example.geco.domains.Attraction;
import com.example.geco.domains.FeedbackCategory;
import com.example.geco.domains.UserDetail;
import com.example.geco.dto.AttractionResponse;
import com.example.geco.dto.SignupRequest;
import com.example.geco.repositories.AccountRepository;
import com.example.geco.repositories.AttractionRepository;
import com.example.geco.repositories.FeedbackCategoryRepository;
import com.example.geco.repositories.UserDetailRepository;
import com.example.geco.services.AccountService;
import com.example.geco.services.AttractionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class MainControllerTests {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	AttractionService attractionService;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	UserDetailRepository userDetailRepository;
	
	@Autowired
	AttractionRepository attractionRepository;
	
	@Autowired
	FeedbackCategoryRepository feedbackCategoryRepository;
	
	@BeforeEach
	void setup() {
	    accountRepository.deleteAll();
	    userDetailRepository.deleteAll();
	    attractionRepository.deleteAll();
	    feedbackCategoryRepository.deleteAll();
	}
	
	@Test
	public void canAddAccount() throws Exception {
		UserDetail detailA = DataUtil.createUserDetailA();
		Account accountA = DataUtil.createAccountA(detailA);

		SignupRequest request = new SignupRequest(accountA, detailA);
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/account")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestJson)
		).andExpect(
				MockMvcResultMatchers.status().isCreated()
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.passwordNotice").exists()
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.surname").value(detailA.getSurname())
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.firstName").value(detailA.getFirstName())
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.email").value(detailA.getEmail())
		);
	}
	
	@Test
	public void cannotAddAccountImproperPassword() throws Exception {
		UserDetail detailA = DataUtil.createUserDetailA();
		
		Account accountA = DataUtil.createAccountA(detailA);
		accountA.setPassword("123"); // too short for a password.
		

	    SignupRequest request = new SignupRequest(accountA, detailA);
	    String requestJson = objectMapper.writeValueAsString(request);

	    mockMvc.perform(
	            MockMvcRequestBuilders.post("/account")
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .content(requestJson)
	    ).andExpect(
	    		MockMvcResultMatchers.status().isBadRequest()
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.error").value("Password must have at least 8 characters.")
		);
	}
	
	@Test
	public void canUpdateAccount() throws Exception {
		UserDetail detailA = DataUtil.createUserDetailA();
		Account accountA = DataUtil.createAccountA(detailA);
		
		SignupRequest request = new SignupRequest(accountA, detailA);
		accountService.addAccount(request);
		
		detailA.setEmail("new@gmail.com");
		SignupRequest newRequest = new SignupRequest(accountA, detailA);
	    String requestJson = objectMapper.writeValueAsString(newRequest);
		
		mockMvc.perform(
				MockMvcRequestBuilders.patch("/account")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestJson)
		).andExpect(
				MockMvcResultMatchers.status().isOk()
		).andExpect(
			MockMvcResultMatchers.jsonPath("$.passwordNotice").exists()
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.surname").value(detailA.getSurname())
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.firstName").value(detailA.getFirstName())
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.email").value(detailA.getEmail())
		);
	}
	
	@Test
	public void cannotUpdateAccountNotFound() throws Exception {
		UserDetail detailA = DataUtil.createUserDetailA();
		Account accountA = DataUtil.createAccountA(detailA);
		
		SignupRequest request = new SignupRequest(accountA, detailA);
		// Did not save request through accountService.
		
		accountA.setPassword("123321123321");
		SignupRequest newRequest = new SignupRequest(accountA, detailA);
	    String requestJson = objectMapper.writeValueAsString(newRequest);
		
		mockMvc.perform(
				MockMvcRequestBuilders.patch("/account")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestJson)
		).andExpect(
	    		MockMvcResultMatchers.status().isBadRequest()
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.error").value("Account not found.")
		);
	}
	
	@Test
	public void cannotUpdateAccountImproperPassword() throws Exception {
		UserDetail detailA = DataUtil.createUserDetailA();
		Account accountA = DataUtil.createAccountA(detailA);
		
		SignupRequest request = new SignupRequest(accountA, detailA);
		accountService.addAccount(request);
		
		accountA.setPassword("123"); // password too short.
		SignupRequest newRequest = new SignupRequest(accountA, detailA);
	    String requestJson = objectMapper.writeValueAsString(newRequest);
		
		mockMvc.perform(
				MockMvcRequestBuilders.patch("/account")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestJson)
		).andExpect(
	    		MockMvcResultMatchers.status().isBadRequest()
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.error").value("Password must have at least 8 characters.")
		);
	}
	
	@Test
	public void cannotUpdateAccountImproperEmail() throws Exception {
		UserDetail detailA = DataUtil.createUserDetailA();
		Account accountA = DataUtil.createAccountA(detailA);
		
		SignupRequest request = new SignupRequest(accountA, detailA);
		accountService.addAccount(request);
		
		detailA.setEmail("gmail.com"); // improper email.
		SignupRequest newRequest = new SignupRequest(accountA, detailA);
	    String requestJson = objectMapper.writeValueAsString(newRequest);
		
		mockMvc.perform(
				MockMvcRequestBuilders.patch("/account")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestJson)
		).andExpect(
	    		MockMvcResultMatchers.status().isBadRequest()
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.error").value("Please include a proper email address.")
		);
	}
	
	@Test
	public void canAddAttraction() throws Exception {
		Attraction attractionA = DataUtil.createAttractionA();
		String attractionJson = objectMapper.writeValueAsString(attractionA);
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/attraction")
					.contentType(MediaType.APPLICATION_JSON)
					.content(attractionJson)
		).andExpect(
				MockMvcResultMatchers.status().isCreated()
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$.name").value(attractionA.getName())
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$.description").value(attractionA.getDescription())
		);
	}
	
	@Test
	public void cannotAddAttractionImproperTitle() throws Exception {
		Attraction attraction = DataUtil.createAttractionA();
	    attraction.setName(""); // invalid title
	    String attractionJson = objectMapper.writeValueAsString(attraction);
	    
	    mockMvc.perform(
				MockMvcRequestBuilders.post("/attraction")
					.contentType(MediaType.APPLICATION_JSON)
					.content(attractionJson)
		).andExpect(
				MockMvcResultMatchers.status().isBadRequest()
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.error").value("Attraction name must have at least 1 character.")
		);
	}
	
	@Test
	public void cannotAddAttractionImproperDescription() throws Exception {
		Attraction attraction = DataUtil.createAttractionA();
	    attraction.setName("valid title"); 
	    attraction.setDescription("too short"); // invalid description
	    String attractionJson = objectMapper.writeValueAsString(attraction);
	    
	    mockMvc.perform(
				MockMvcRequestBuilders.post("/attraction")
					.contentType(MediaType.APPLICATION_JSON)
					.content(attractionJson)
		).andExpect(
				MockMvcResultMatchers.status().isBadRequest()
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.error").value("Attraction description must be at least 10 characters long.")
		);
	}
	
	@Test
	public void canGetAttraction() throws Exception {
		Attraction attractionA = DataUtil.createAttractionA();
		AttractionResponse savedAttractionA = attractionService.addAttraction(attractionA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/attraction/" + savedAttractionA.getAttractionId())
                    .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(
        		MockMvcResultMatchers.status().isOk()
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$.name").value(attractionA.getName())
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$.description").value(attractionA.getDescription())
		);
	}
	

	@Test
	public void canGetAllAttraction() throws Exception {
		Attraction attractionA = DataUtil.createAttractionA();
		AttractionResponse savedAttractionA = attractionService.addAttraction(attractionA);

		Attraction attractionB = DataUtil.createAttractionB();
		AttractionResponse savedAttractionB = attractionService.addAttraction(attractionB);
		
		mockMvc.perform(
                MockMvcRequestBuilders.get("/attraction")
                    .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
        		MockMvcResultMatchers.status().isOk()
        ).andExpect(
        		MockMvcResultMatchers.jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(2))
		).andExpect(
				MockMvcResultMatchers.jsonPath("$[0].name").value(savedAttractionA.getName())
		)
        .andExpect(
        		MockMvcResultMatchers.jsonPath("$[0].description").value(savedAttractionA.getDescription())
        ).andExpect(
        		MockMvcResultMatchers.jsonPath("$[1].name").value(savedAttractionB.getName())
		).andExpect(
				MockMvcResultMatchers.jsonPath("$[1].description").value(savedAttractionB.getDescription())
		);
	}
	
	@Test
	public void canUpdateAttraction() throws Exception {
		Attraction attractionA = DataUtil.createAttractionA();
		attractionService.addAttraction(attractionA);
		
		attractionA.setName("Ang Ina ng Kalikasan");
	    String attractionJson = objectMapper.writeValueAsString(attractionA);
		
		mockMvc.perform(
				MockMvcRequestBuilders.patch("/attraction/" + attractionA.getAttractionId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(attractionJson)
		).andExpect(
				MockMvcResultMatchers.status().isOk()
		).andExpect(
			MockMvcResultMatchers.jsonPath("$.attractionId").exists()
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.name").value(attractionA.getName())
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.description").value(attractionA.getDescription())
		);
	}

	@Test
	public void cannotUpdateAttractionImproperName() throws Exception {
		Attraction attractionA = DataUtil.createAttractionA();
		attractionService.addAttraction(attractionA);
		
		attractionA.setName("");
	    String attractionJson = objectMapper.writeValueAsString(attractionA);
		
		mockMvc.perform(
				MockMvcRequestBuilders.patch("/attraction/" + attractionA.getAttractionId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(attractionJson)
		).andExpect(
				MockMvcResultMatchers.status().isBadRequest()
		).andExpect(
			MockMvcResultMatchers.jsonPath("$.error").value("Attraction name must have at least 1 character.")
		);
	}

	@Test
	public void cannotUpdateAttractionImproperDescription() throws Exception {
		Attraction attractionA = DataUtil.createAttractionA();
		attractionService.addAttraction(attractionA);
		
		attractionA.setDescription("too short");
	    String attractionJson = objectMapper.writeValueAsString(attractionA);
		
		mockMvc.perform(
				MockMvcRequestBuilders.patch("/attraction/" + attractionA.getAttractionId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(attractionJson)
		).andExpect(
				MockMvcResultMatchers.status().isBadRequest()
		).andExpect(
			MockMvcResultMatchers.jsonPath("$.error").value("Attraction description must be at least 10 characters long.")
		);
	}

	@Test
	public void canDeleteAttraction() throws Exception {
		Attraction attractionA = DataUtil.createAttractionA();
		attractionService.addAttraction(attractionA);
		
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/attraction/" + attractionA.getAttractionId())
					.contentType(MediaType.APPLICATION_JSON)
		).andExpect(
				MockMvcResultMatchers.status().isOk()
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.attractionId").exists()
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.name").value(attractionA.getName())
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.description").value(attractionA.getDescription())
		);
	}
	
	@Test
	public void cannotDeleteAttraction() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/attraction/1")
					.contentType(MediaType.APPLICATION_JSON)
		).andExpect(
				MockMvcResultMatchers.status().isBadRequest()
		).andExpect(
				MockMvcResultMatchers.jsonPath("$.error").value("Attraction not found.")
		);
	}
	
	@Test
	public void canGetNumberOfAttractions() throws Exception {
		Attraction attractionA = DataUtil.createAttractionA();
		Attraction attractionB = DataUtil.createAttractionB();

		attractionService.addAttraction(attractionA);
		attractionService.addAttraction(attractionB);
		

		double n = attractionService.getAttractionsNumber();
		
		assertEquals(2, n);
	}
	
	@Test
	public void canAddFeedbackCategory() throws Exception {
		FeedbackCategory categoryA = DataUtil.createFeedbackCategoryA();
		String categoryJson = objectMapper.writeValueAsString(categoryA);
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/feedback-category")
					.contentType(MediaType.APPLICATION_JSON)
					.content(categoryJson)
		).andExpect(
				MockMvcResultMatchers.status().isCreated()
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$.feedbackCategoryId").exists()
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$.label").value(categoryA.getLabel())
		);
	}

	@Test
	public void cannotAddFeedbackCategoryImproperLabel() throws Exception {
		FeedbackCategory categoryA = DataUtil.createFeedbackCategoryA();
		categoryA.setLabel("");
		
		String categoryJson = objectMapper.writeValueAsString(categoryA);
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/feedback-category")
					.contentType(MediaType.APPLICATION_JSON)
					.content(categoryJson)
		).andExpect(
				MockMvcResultMatchers.status().isBadRequest()
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$.error").value("Label must at least have 1 character.")
		);
	}
	
	@Test
	public void cannotAddFeedbackCategoryAlreadyExist() throws Exception {
		FeedbackCategory categoryA = DataUtil.createFeedbackCategoryA();
		feedbackCategoryRepository.save(categoryA);
		
		String attractionJson = objectMapper.writeValueAsString(categoryA);
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/feedback-category")
					.contentType(MediaType.APPLICATION_JSON)
					.content(attractionJson)
		).andExpect(
				MockMvcResultMatchers.status().isBadRequest()
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$.error").value("Label \"" + categoryA.getLabel() + "\" already exist.")
		);
	}

	@Test
	public void canGetFeedbackCategory() throws Exception {
		FeedbackCategory categoryA = DataUtil.createFeedbackCategoryA();
		feedbackCategoryRepository.save(categoryA);
		
		mockMvc.perform(
				MockMvcRequestBuilders.get("/feedback-category/" + categoryA.getFeedbackCategoryId())
					.contentType(MediaType.APPLICATION_JSON)
		).andExpect(
				MockMvcResultMatchers.status().isOk()
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$.label").value(categoryA.getLabel())
		);
	}

	@Test
	public void cannotGetFeedbackCategory() throws Exception {
		int id = 0;
		
		mockMvc.perform(
				MockMvcRequestBuilders.get("/feedback-category/" + id)
					.contentType(MediaType.APPLICATION_JSON)
		).andExpect(
				MockMvcResultMatchers.status().isNotFound()
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$.error").value("Feedback Category with ID " + id + " not found.")
		);
	}

	@Test
	public void canGetAllFeedbackCategories() throws Exception {
		FeedbackCategory categoryA = DataUtil.createFeedbackCategoryA();
		feedbackCategoryRepository.save(categoryA);
		
		FeedbackCategory categoryB = DataUtil.createFeedbackCategoryB();
		feedbackCategoryRepository.save(categoryB);
		
		mockMvc.perform(
				MockMvcRequestBuilders.get("/feedback-category")
					.contentType(MediaType.APPLICATION_JSON)
		).andExpect(
				MockMvcResultMatchers.status().isOk()
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$[0].label").value(categoryA.getLabel())
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$[1].label").value(categoryB.getLabel())
		);
	}

	@Test
	public void canGetAllFeedbackCategoriesEmpty() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/feedback-category")
					.contentType(MediaType.APPLICATION_JSON)
		).andExpect(
				MockMvcResultMatchers.status().isOk()
		).andExpect(
				MockMvcResultMatchers.jsonPath("$").isEmpty()
		);
	}

	@Test
	public void canUpdateFeedbackCategory() throws Exception {
		FeedbackCategory categoryA = DataUtil.createFeedbackCategoryA();
		FeedbackCategory savedCategoryA = feedbackCategoryRepository.save(categoryA);
		
		categoryA.setLabel("new label");
		String categoryJson = objectMapper.writeValueAsString(categoryA);
		
		mockMvc.perform(
				MockMvcRequestBuilders.patch("/feedback-category/" + savedCategoryA.getFeedbackCategoryId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(categoryJson)
		).andExpect(
				MockMvcResultMatchers.status().isOk()
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$.feedbackCategoryId").exists()
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$.label").value(categoryA.getLabel())
		);
	}

	@Test
	public void cannotUpdateFeedbackCategoryNotFound() throws Exception {
		FeedbackCategory categoryA = DataUtil.createFeedbackCategoryA();
		// did not save categoryA to database.
		int id = 0;
		
		categoryA.setLabel("new label");
		String categoryJson = objectMapper.writeValueAsString(categoryA);
		
		mockMvc.perform(
				MockMvcRequestBuilders.patch("/feedback-category/" + id)
					.contentType(MediaType.APPLICATION_JSON)
					.content(categoryJson)
		).andExpect(
				MockMvcResultMatchers.status().isNotFound()
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$.error").value("Feedback category \"" + categoryA.getLabel() + "\" not found.")
		);
	}

	@Test
	public void cannotUpdateFeedbackCategoryImproperLabel() throws Exception {
		FeedbackCategory categoryA = DataUtil.createFeedbackCategoryA();
		FeedbackCategory savedCategoryA = feedbackCategoryRepository.save(categoryA);
		
		categoryA.setLabel("");
		String categoryJson = objectMapper.writeValueAsString(categoryA);
		
		mockMvc.perform(
				MockMvcRequestBuilders.patch("/feedback-category/" + savedCategoryA.getFeedbackCategoryId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(categoryJson)
		).andExpect(
				MockMvcResultMatchers.status().isBadRequest()
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$.error").value("Label must at least have 1 character.")
		);
	}
	
	@Test
	public void canDeleteFeedbackCategory() throws Exception {
		FeedbackCategory categoryA = DataUtil.createFeedbackCategoryA();
		FeedbackCategory savedCategoryA = feedbackCategoryRepository.save(categoryA);
		
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/feedback-category/" + savedCategoryA.getFeedbackCategoryId())
					.contentType(MediaType.APPLICATION_JSON)
		).andExpect(
				MockMvcResultMatchers.status().isOk()
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$.feedbackCategoryId").exists()
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$.label").value(savedCategoryA.getLabel())
		);
	}
	
	@Test
	public void cannotDeleteFeedbackCategoryNotFound() throws Exception {
		int id = 0;
		
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/feedback-category/" + id)
					.contentType(MediaType.APPLICATION_JSON)
		).andExpect(
				MockMvcResultMatchers.status().isNotFound()
		).andExpect(
        		MockMvcResultMatchers.jsonPath("$.error").value("Feedback category with ID " + id + " not found.")
		);
	}
	
	
	
	// to implement
	@Test
	public void canGetAverageMonthlyVisitors() throws Exception {
		
	}
	
	// to implement
	@Test
	public void canGetAverageYearlyVisitors() throws Exception {
		
	}
	
	// to implement
	@Test
	public void canGetAverageRating() throws Exception {
		
	}
	
	// to implement
	@Test
	public void canDisplayHomeData() throws Exception {
		
	}
}
