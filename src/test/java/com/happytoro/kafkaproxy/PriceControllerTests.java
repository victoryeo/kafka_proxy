package com.happytoro.kafkaproxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.http.MediaType;
import static org.hamcrest.Matchers.containsString;
import static org.assertj.core.api.Assertions.assertThat;

import com.happytoro.kafkaproxy.web.PriceController;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class PriceControllerTests {
  
  @Autowired
  private MockMvc mockMvc;

  @Autowired
	private PriceController controller;

	@Test
	public void contextLoads() throws Exception {
		System.out.println("controller " + controller);
		assertThat(controller).isNotNull();
	}

  @Test
	public void test_PriceController_getPrice() throws Exception {

		mockMvc.perform(
       get("/price/list")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().string(containsString("price")));
	}
}