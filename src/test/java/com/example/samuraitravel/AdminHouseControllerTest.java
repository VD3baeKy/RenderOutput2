/* Junit Sample Code */

package com.example.samuraitravel;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminHouseControllerTest {
	
	@Autowired
	private MockMvc junitTest;
	
	@Test
	public void testMethod1() throws Exception{
		
		junitTest.perform(get("/admin/houses"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/no-url"));
		
		
	}


}
