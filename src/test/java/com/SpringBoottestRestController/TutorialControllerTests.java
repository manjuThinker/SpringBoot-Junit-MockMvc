package com.SpringBoottestRestController;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.SpringBoottestRestController.controller.TutorialController;
import com.SpringBoottestRestController.model.Tutorial;
import com.SpringBoottestRestController.repository.TutorialRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TutorialController.class)
public class TutorialControllerTests {
	
	 @MockBean
	  private TutorialRepository tutorialRepository;

	  @Autowired
	  private MockMvc mockMvc;

	  @Autowired
	  private ObjectMapper objectMapper;
	  
	  @Test
	  void shouldCreateTutorial() throws Exception {
	    Tutorial tutorial = new Tutorial(1, "Spring Boot @WebMvcTest", "Description", true);

	    mockMvc.perform(post("/api/tutorials").contentType(MediaType.APPLICATION_JSON)
	        .content(objectMapper.writeValueAsString(tutorial)))
	        .andExpect(status().isCreated())
	        .andDo(print());
	  }
	  
	  @Test
	  void shouldReturnTutorial() throws Exception {
	    long id = 1L;
	    Tutorial tutorial = new Tutorial(id, "Spring Boot @WebMvcTest", "Description", true);

	    when(tutorialRepository.findById(id)).thenReturn(Optional.of(tutorial));
	    mockMvc.perform(get("/api/tutorials/{id}", id)).andExpect(status().isOk())
	        .andExpect(jsonPath("$.id").value(id))
	        .andExpect(jsonPath("$.title").value(tutorial.getTitle()))
	        .andExpect(jsonPath("$.description").value(tutorial.getDescription()))
	        .andExpect(jsonPath("$.published").value(tutorial.isPublished()))
	        .andDo(print());
	  }
	  
	  @Test
	  void shouldReturnNotFoundTutorial() throws Exception {
	    long id = 1L;

	    when(tutorialRepository.findById(id)).thenReturn(Optional.empty());
	    mockMvc.perform(get("/api/tutorials/{id}", id))
	         .andExpect(status().isNotFound())
	         .andDo(print());
	  }
	  
	  @Test
	  void shouldReturnListOfTutorials() throws Exception {
	    List<Tutorial> tutorials = new ArrayList<>(
	        Arrays.asList(new Tutorial(1, "Spring Boot @WebMvcTest 1", "Description 1", true),
	            new Tutorial(2, "Spring Boot @WebMvcTest 2", "Description 2", true),
	            new Tutorial(3, "Spring Boot @WebMvcTest 3", "Description 3", true)));

	    when(tutorialRepository.findAll()).thenReturn(tutorials);
	    mockMvc.perform(get("/api/tutorials"))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.size()").value(tutorials.size()))
	        .andDo(print());
	  }


}
