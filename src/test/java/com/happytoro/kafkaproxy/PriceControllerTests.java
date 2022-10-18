package com.happytoro.kafkaproxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.http.MediaType;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import com.happytoro.kafkaproxy.price.model.Price;
import com.happytoro.kafkaproxy.price.service.PriceService;
import com.happytoro.kafkaproxy.web.PriceController;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext
@TestPropertySource(locations = "classpath:application-test.properties")
public class PriceControllerTests {
  
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	PriceService priceService;
	
	@Autowired
	private PriceController priceController;

	@Test
	public void contextLoads() throws Exception {
		assertThat(priceController).isNotNull();
		assertThat(priceService).isNotNull();
	}

	@Test
		public void test_PriceController_getPrice() throws Exception {

			Price item = new Price("1", "AAPL", 100.00f, "1");
			List<Price> prices = new ArrayList<Price>();
			prices.add(item);
			
			when(priceService.fetchPriceList()).thenReturn(prices);
			
			mockMvc.perform(
				get("/price/list")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("price")));
				}
}