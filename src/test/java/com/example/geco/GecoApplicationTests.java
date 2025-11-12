package com.example.geco;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GecoApplicationTests extends AbstractControllerTest {
	@Nested
    class SuccessTests {
		@Test
		public void areYouStillHappy() throws Exception {
			// Of course!!!!!!!!
		}
	}
	
	@Nested
    class FailureTests {
		
	}
}
