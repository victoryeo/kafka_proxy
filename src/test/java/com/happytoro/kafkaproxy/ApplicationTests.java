package com.happytoro.kafkaproxy;

import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@SpringBootTest(classes=ApplicationTests.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class ApplicationTests {
	@Test
	void contextLoads() {
	}

	@Value("${testvalue}")
	String testvalue;

	@Test
	void test(){
		assertThat(testvalue).isEqualTo("unittest");
	}
}
