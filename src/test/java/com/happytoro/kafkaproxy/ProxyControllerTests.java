package com.happytoro.kafkaproxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;
import static org.hamcrest.CoreMatchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.HashMap;
import java.util.Map;

import com.happytoro.kafkaproxy.web.ProxyController;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ProxyControllerTests {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
	private ProxyController controller;

	@Test
	public void contextLoads() throws Exception {
		System.out.println("controller " + controller);
		assertThat(controller).isNotNull();
	}

	@Test
	public void test_ProxyController_appName() throws Exception {

		MvcResult result = mockMvc.perform(
			get("/api/appname")
		 .contentType(MediaType.APPLICATION_JSON))
		 .andReturn();
		String contentP = result.getResponse().getContentAsString();
		System.out.println("contentP "+contentP);

		mockMvc.perform(
       get("/api/appname")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", is("kafkaproxy")));
	}

	@Test
	public void test_ProxyController_order_null_access_token() throws Exception {

		Map<String,Object> body = new HashMap<>();
		body.put("orderID","1");
		body.put("tokenType","stock");
		body.put("tokenName","AAPL");
		body.put("orderType", 0);
		body.put("price","2");
		body.put("quantity","2");

		ObjectMapper mapper = new ObjectMapper();
		//mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson=ow.writeValueAsString(body);

		MvcResult result = mockMvc.perform(
			post("/api/order")
		 .contentType(MediaType.APPLICATION_JSON)
		 .content(requestJson))
		 .andReturn();
		String contentP = result.getResponse().getContentAsString();
		System.out.println("contentP "+contentP);

		mockMvc.perform(
			post("/api/order")
		 .contentType(MediaType.APPLICATION_JSON)
		 .content(requestJson))
		 .andExpect(status().isBadRequest())
		 .andExpect(jsonPath("$", is("Access token is null")));

	}
}
