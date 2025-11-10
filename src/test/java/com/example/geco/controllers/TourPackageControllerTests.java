package com.example.geco.controllers;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.geco.DataUtil;
import com.example.geco.domains.PackageInclusion;
import com.example.geco.domains.TourPackage;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TourPackageControllerTests extends AbstractControllerTest {
	@Nested
    class SuccessTests {
		@Test
		public void canAddPackage() throws Exception{
			PackageInclusion inclusionA = DataUtil.createPackageInclusionA();
			packageInclusionService.addInclusion(inclusionA);

			List<PackageInclusion> inclusions = new ArrayList<>();
			inclusions.add(inclusionA);
			
			TourPackage packageA = DataUtil.createPackageA(inclusions);
			String packageJson = objectMapper.writeValueAsString(packageA);
			
			mockMvc.perform(
					MockMvcRequestBuilders.post("/package")
						.contentType(MediaType.APPLICATION_JSON)
						.content(packageJson)
			).andExpect(
					MockMvcResultMatchers.status().isCreated()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.packageId").exists()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.description").value(packageA.getDescription())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.basePrice").value(packageA.getBasePrice())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.inclusions").exists()
			);
		}
		
		@Test
		public void canGetPackage() throws Exception {
			PackageInclusion inclusionA = DataUtil.createPackageInclusionA();
			packageInclusionService.addInclusion(inclusionA);

			List<PackageInclusion> inclusions = new ArrayList<>();
			inclusions.add(inclusionA);
			
			TourPackage packageA = DataUtil.createPackageA(inclusions);
			TourPackage savedPackageA = tourPackageService.addPackage(packageA);
			
			mockMvc.perform(
					MockMvcRequestBuilders.get("/package/" + savedPackageA.getPackageId())
						.contentType(MediaType.APPLICATION_JSON)
			).andExpect(
					MockMvcResultMatchers.status().isOk()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.packageId").value(savedPackageA.getPackageId())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.description").value(savedPackageA.getDescription())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.basePrice").value(savedPackageA.getBasePrice())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.inclusions").exists()
			);
		}
		
		@Test
		public void canGetAllPackages() throws Exception {
			PackageInclusion inclusionA = DataUtil.createPackageInclusionA();
			packageInclusionService.addInclusion(inclusionA);

			List<PackageInclusion> inclusions = new ArrayList<>();
			inclusions.add(inclusionA);
			
			TourPackage packageA = DataUtil.createPackageA(inclusions);
			TourPackage savedPackageA = tourPackageService.addPackage(packageA);
			
			TourPackage packageB = DataUtil.createPackageB(inclusions);
			TourPackage savedPackageB = tourPackageService.addPackage(packageB);
			
			mockMvc.perform(
					MockMvcRequestBuilders.get("/package")
						.contentType(MediaType.APPLICATION_JSON)
			).andExpect(
					MockMvcResultMatchers.status().isOk()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$[0].packageId").value(savedPackageA.getPackageId())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$[0].description").value(savedPackageA.getDescription())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$[0].basePrice").value(savedPackageA.getBasePrice())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$[0].inclusions").exists()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$[1].packageId").value(savedPackageB.getPackageId())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$[1].description").value(savedPackageB.getDescription())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$[1].basePrice").value(savedPackageB.getBasePrice())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$[1].inclusions").exists()
			);
		}
		
		@Test
		public void canGetAllPackagesEmpty() throws Exception {
			mockMvc.perform(
					MockMvcRequestBuilders.get("/package")
						.contentType(MediaType.APPLICATION_JSON)
			).andExpect(
					MockMvcResultMatchers.status().isOk()
			).andExpect(
					MockMvcResultMatchers.jsonPath("$").isEmpty()
			);
		}
		
		@Test
		public void canUpdatePackage() throws Exception{
			PackageInclusion inclusionA = DataUtil.createPackageInclusionA();
			packageInclusionService.addInclusion(inclusionA);

			List<PackageInclusion> inclusions = new ArrayList<>();
			inclusions.add(inclusionA);
			
			TourPackage packageA = DataUtil.createPackageA(inclusions);
			TourPackage savedPackageA = tourPackageService.addPackage(packageA);
			
			packageA.setDescription("new description for this package");
			packageA.setBasePrice(100);
			String packageJson = objectMapper.writeValueAsString(packageA);
			
			mockMvc.perform(
					MockMvcRequestBuilders.patch("/package/" + savedPackageA.getPackageId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(packageJson)
			).andExpect(
					MockMvcResultMatchers.status().isOk()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.packageId").exists()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.description").value(packageA.getDescription())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.basePrice").value(packageA.getBasePrice())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.inclusions").exists()
			);
		}

		@Test
		public void canUpdatePackageDescriptionOnly() throws Exception{
			PackageInclusion inclusionA = DataUtil.createPackageInclusionA();
			packageInclusionService.addInclusion(inclusionA);

			List<PackageInclusion> inclusions = new ArrayList<>();
			inclusions.add(inclusionA);
			
			TourPackage packageA = DataUtil.createPackageA(inclusions);
			TourPackage savedPackageA = tourPackageService.addPackage(packageA);

			TourPackage newPackage = new TourPackage(
					savedPackageA.getPackageId(),
					"new description for this package",
					null,
					null
			);
			
			String packageJson = objectMapper.writeValueAsString(newPackage);
			
			mockMvc.perform(
					MockMvcRequestBuilders.patch("/package/" + savedPackageA.getPackageId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(packageJson)
			).andExpect(
					MockMvcResultMatchers.status().isOk()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.packageId").exists()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.description").value(newPackage.getDescription())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.basePrice").value(savedPackageA.getBasePrice())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.inclusions").exists()
			);
		}

		@Test
		public void canUpdatePackagePriceOnly() throws Exception{
			PackageInclusion inclusionA = DataUtil.createPackageInclusionA();
			packageInclusionService.addInclusion(inclusionA);

			List<PackageInclusion> inclusions = new ArrayList<>();
			inclusions.add(inclusionA);
			
			TourPackage packageA = DataUtil.createPackageA(inclusions);
			TourPackage savedPackageA = tourPackageService.addPackage(packageA);

			TourPackage newPackage = new TourPackage(
					savedPackageA.getPackageId(),
					null,
					100,
					null
			);
			
			String packageJson = objectMapper.writeValueAsString(newPackage);
			
			mockMvc.perform(
					MockMvcRequestBuilders.patch("/package/" + savedPackageA.getPackageId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(packageJson)
			).andExpect(
					MockMvcResultMatchers.status().isOk()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.packageId").exists()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.description").value(savedPackageA.getDescription())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.basePrice").value(newPackage.getBasePrice())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.inclusions").exists()
			);
		}

		@Test
		public void canDeletePackage() throws Exception {
			PackageInclusion inclusionA = DataUtil.createPackageInclusionA();
			packageInclusionService.addInclusion(inclusionA);

			List<PackageInclusion> inclusions = new ArrayList<>();
			inclusions.add(inclusionA);
			
			TourPackage packageA = DataUtil.createPackageA(inclusions);
			TourPackage savedPackageA = tourPackageService.addPackage(packageA);
			
			mockMvc.perform(
					MockMvcRequestBuilders.delete("/package/" + savedPackageA.getPackageId())
						.contentType(MediaType.APPLICATION_JSON)
			).andExpect(
					MockMvcResultMatchers.status().isOk()
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.packageId").value(savedPackageA.getPackageId())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.description").value(savedPackageA.getDescription())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.basePrice").value(savedPackageA.getBasePrice())
			).andExpect(
	        		MockMvcResultMatchers.jsonPath("$.inclusions").exists()
			);
			
			mockMvc.perform(
					MockMvcRequestBuilders.get("/package/" + savedPackageA.getPackageId())
						.contentType(MediaType.APPLICATION_JSON)
			).andExpect(
					MockMvcResultMatchers.status().isNotFound()
			);
		}
	}
	
	@Nested
    class FailureTests {
		
	}
}