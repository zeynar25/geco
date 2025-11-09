package com.example.geco.controllers;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.geco.DataUtil;
import com.example.geco.domains.Account;
import com.example.geco.domains.UserDetail;
import com.example.geco.dto.SignupRequest;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AccountControllerTests extends AbstractControllerTest {
	@Nested
    class SuccessTests {
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
	}
	
	@Nested
    class FailureTests {
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
	}
}
