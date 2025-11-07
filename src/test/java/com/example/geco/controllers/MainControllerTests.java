package com.example.geco.controllers;

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
import com.example.geco.services.AccountService;
import com.example.geco.services.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class MainControllerTests {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void canAddAccount() throws Exception{
		Account userA = DataUtil.createUserA();
		String userJson = objectMapper.writeValueAsString(userA);
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(userJson)
		).andExpect(
				MockMvcResultMatchers.status().isCreated()
		);
	}
}
