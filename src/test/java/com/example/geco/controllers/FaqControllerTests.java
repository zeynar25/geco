package com.example.geco.controllers;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.geco.AbstractControllerTest;
import com.example.geco.DataUtil;
import com.example.geco.domains.Faq;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FaqControllerTests extends AbstractControllerTest{
	@Nested
    class SuccessTests {
		@Test
		public void canAddFaq() throws Exception{
			Faq faqA = DataUtil.createFaqA();
			String faqJson = objectMapper.writeValueAsString(faqA);
			
			mockMvc.perform(
					MockMvcRequestBuilders.post("/faq")
						.contentType(MediaType.APPLICATION_JSON)
						.content(faqJson)
			).andExpect(
					MockMvcResultMatchers.status().isCreated()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.faqId").exists()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.question").value(faqA.getQuestion())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.answer").value(faqA.getAnswer())
			);
		}
		
		@Test
		public void canGetFaq() throws Exception {
			Faq faqA = DataUtil.createFaqA();
			faqService.addFaq(faqA);
			
			mockMvc.perform(
					MockMvcRequestBuilders.get("/faq/" + faqA.getFaqId())
						.contentType(MediaType.APPLICATION_JSON)
			).andExpect(
					MockMvcResultMatchers.status().isOk()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.faqId").value(faqA.getFaqId())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.question").value(faqA.getQuestion())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.answer").value(faqA.getAnswer())
			);
		}
		
		@Test
		public void canGetAllFaqs() throws Exception {
			Faq faqA = DataUtil.createFaqA();
			faqService.addFaq(faqA);
			
			Faq faqB = DataUtil.createFaqB();
			faqService.addFaq(faqB);
			
			mockMvc.perform(
					MockMvcRequestBuilders.get("/faq")
						.contentType(MediaType.APPLICATION_JSON)
			).andExpect(
					MockMvcResultMatchers.status().isOk()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$[0].faqId").value(faqA.getFaqId())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$[0].question").value(faqA.getQuestion())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$[0].answer").value(faqA.getAnswer())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$[1].faqId").value(faqB.getFaqId())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$[1].question").value(faqB.getQuestion())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$[1].answer").value(faqB.getAnswer())
			);
		}
		
		@Test
		public void canGetAllFaqsEmpty() throws Exception {
			mockMvc.perform(
					MockMvcRequestBuilders.get("/faq")
						.contentType(MediaType.APPLICATION_JSON)
			).andExpect(
					MockMvcResultMatchers.status().isOk()
			).andExpect(
					MockMvcResultMatchers.jsonPath("$").isEmpty()
			);
		}
		
		@Test
		public void canUpdateFaq() throws Exception {
			Faq faqA = DataUtil.createFaqA();
			Faq savedFaqA = faqService.addFaq(faqA);
			
			faqA.setQuestion("New question for the said FAQ.");
			faqA.setAnswer("New answer for the said FAQ.");
			String faqJson = objectMapper.writeValueAsString(faqA);
			
			mockMvc.perform(
					MockMvcRequestBuilders.patch("/faq/" + savedFaqA.getFaqId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(faqJson)
			).andExpect(
					MockMvcResultMatchers.status().isOk()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.faqId").value(savedFaqA.getFaqId())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.question").value(faqA.getQuestion())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.answer").value(faqA.getAnswer())
			);
		}
		
		@Test
		public void canUpdateFaqQuestionOnly() throws Exception {
			Faq faqA = DataUtil.createFaqA();
			Faq savedFaqA = faqService.addFaq(faqA);
			
			Faq newFaq = new Faq();
			newFaq.setQuestion("What is this park all about?");
			String faqJson = objectMapper.writeValueAsString(newFaq);
			
			mockMvc.perform(
					MockMvcRequestBuilders.patch("/faq/" + savedFaqA.getFaqId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(faqJson)
			).andExpect(
					MockMvcResultMatchers.status().isOk()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.faqId").value(savedFaqA.getFaqId())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.question").value(newFaq.getQuestion())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.answer").value(savedFaqA.getAnswer())
			);
		}
		
		@Test
		public void canUpdateFaqAnswerOnly() throws Exception {
			Faq faqA = DataUtil.createFaqA();
			Faq savedFaqA = faqService.addFaq(faqA);
			
			Faq newFaq = new Faq();
			newFaq.setAnswer("New answer for the said question.");
			String faqJson = objectMapper.writeValueAsString(newFaq);
			
			mockMvc.perform(
					MockMvcRequestBuilders.patch("/faq/" + savedFaqA.getFaqId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(faqJson)
			).andExpect(
					MockMvcResultMatchers.status().isOk()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.faqId").value(savedFaqA.getFaqId())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.question").value(savedFaqA.getQuestion())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.answer").value(newFaq.getAnswer())
			);
		}

		@Test
		public void canDeleteFaq() throws Exception {
			Faq faqA = DataUtil.createFaqA();
			Faq savedFaqA = faqService.addFaq(faqA);
			
			mockMvc.perform(
					MockMvcRequestBuilders.delete("/faq/" + savedFaqA.getFaqId())
						.contentType(MediaType.APPLICATION_JSON)
			).andExpect(
					MockMvcResultMatchers.status().isNoContent()
			);
			
			mockMvc.perform(
					MockMvcRequestBuilders.get("/faq/" + savedFaqA.getFaqId())
						.contentType(MediaType.APPLICATION_JSON)
			).andExpect(
					MockMvcResultMatchers.status().isNotFound()
			);
		}
	}
	
	@Nested
    class FailureTests {
		@Test
		public void cannotAddFaqMissingQuestion() throws Exception {
			Faq faqA = DataUtil.createFaqA();
			faqA.setQuestion(null);
			
			String faqJson = objectMapper.writeValueAsString(faqA);
			
			mockMvc.perform(
					MockMvcRequestBuilders.post("/faq")
						.contentType(MediaType.APPLICATION_JSON)
						.content(faqJson)
			).andExpect(
					MockMvcResultMatchers.status().isBadRequest()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.error").value("Question is missing.")
			);
		}
		
		@Test
		public void cannotAddFaqShortQuestion() throws Exception {
			Faq faqA = DataUtil.createFaqA();
			faqA.setQuestion("what?");
			
			String faqJson = objectMapper.writeValueAsString(faqA);
			
			mockMvc.perform(
					MockMvcRequestBuilders.post("/faq")
						.contentType(MediaType.APPLICATION_JSON)
						.content(faqJson)
			).andExpect(
					MockMvcResultMatchers.status().isBadRequest()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.error").value("Question must have at least 10 characters.")
			);
		}

		@Test
		public void cannotAddFaqMissingAnswer() throws Exception {
			Faq faqA = DataUtil.createFaqA();
			faqA.setAnswer(null);
			
			String faqJson = objectMapper.writeValueAsString(faqA);
			
			mockMvc.perform(
					MockMvcRequestBuilders.post("/faq")
						.contentType(MediaType.APPLICATION_JSON)
						.content(faqJson)
			).andExpect(
					MockMvcResultMatchers.status().isBadRequest()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.error").value("Answer is missing.")
			);
		}
		
		@Test
		public void cannotAddFaqShortAnswer() throws Exception {
			Faq faqA = DataUtil.createFaqA();
			faqA.setAnswer("what?");
			
			String faqJson = objectMapper.writeValueAsString(faqA);
			
			mockMvc.perform(
					MockMvcRequestBuilders.post("/faq")
						.contentType(MediaType.APPLICATION_JSON)
						.content(faqJson)
			).andExpect(
					MockMvcResultMatchers.status().isBadRequest()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.error").value("Answer must have at least 10 characters.")
			);
		}
		
		@Test
		public void cannotAddFaqQuestionAlreadyExist() throws Exception {
			Faq faqA = DataUtil.createFaqA();
			faqRepository.save(faqA);
			
			String faqJson = objectMapper.writeValueAsString(faqA);
			
			mockMvc.perform(
					MockMvcRequestBuilders.post("/faq")
						.contentType(MediaType.APPLICATION_JSON)
						.content(faqJson)
			).andExpect(
					MockMvcResultMatchers.status().isBadRequest()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.error").value("Question \"" + faqA.getQuestion() + "\" already exists.")
			);
		}
		
		@Test
		public void cannotGetFaq() throws Exception {
			int id = 0;
			mockMvc.perform(
					MockMvcRequestBuilders.get("/faq/" + id)
						.contentType(MediaType.APPLICATION_JSON)
			).andExpect(
					MockMvcResultMatchers.status().isNotFound()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.error").value("FAQ with ID \"" + id + "\" not found.")
			);
		}
		
		@Test
		public void cannotUpdateFaqMissingQuestionAndAnswer() throws Exception {
			Faq faqA = DataUtil.createFaqA();
			Faq savedFaqA = faqService.addFaq(faqA);;

			Faq newFaq = new Faq();
			newFaq.setQuestion(null);
			newFaq.setAnswer(null);
			String faqJson = objectMapper.writeValueAsString(newFaq);
			
			mockMvc.perform(
					MockMvcRequestBuilders.patch("/faq/" + savedFaqA.getFaqId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(faqJson)
			).andExpect(
					MockMvcResultMatchers.status().isBadRequest()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.error").value("FAQ question and answer is empty.")
			);
		}
		
		@Test
		public void cannotUpdateFaqMissing() throws Exception {
			Faq faqA = DataUtil.createFaqA();
			faqA.setFaqId(0);
			String faqJson = objectMapper.writeValueAsString(faqA);
			
			mockMvc.perform(
					MockMvcRequestBuilders.patch("/faq/" + faqA.getFaqId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(faqJson)
			).andExpect(
					MockMvcResultMatchers.status().isNotFound()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.error").value("FAQ with ID \"" + faqA.getFaqId() + "\" not found.")
			);
		}
		
		@Test
		public void cannotUpdateFaqQuestionAlreadyExist() throws Exception {
			Faq faqA = DataUtil.createFaqA();
			Faq savedFaqA = faqService.addFaq(faqA);
			
			Faq faqB = DataUtil.createFaqB();
			Faq savedFaqB = faqService.addFaq(faqB);
			
			faqA.setQuestion(savedFaqB.getQuestion());
			String faqJson = objectMapper.writeValueAsString(faqA);
			
			mockMvc.perform(
					MockMvcRequestBuilders.patch("/faq/" + savedFaqA.getFaqId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(faqJson)
			).andExpect(
					MockMvcResultMatchers.status().isBadRequest()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.error").value("Another FAQ with the same question already exists.")
			);
		}
		
		@Test
		public void cannotDeleteFaq() throws Exception {
			int id = 0;
			
			mockMvc.perform(
					MockMvcRequestBuilders.get("/faq/" + id)
						.contentType(MediaType.APPLICATION_JSON)
			).andExpect(
					MockMvcResultMatchers.status().isNotFound()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.error").value("FAQ with ID \"" + id + "\" not found.")
			);
		}
	} 
}
