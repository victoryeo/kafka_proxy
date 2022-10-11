package com.happytoro.kafkaproxy;

import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes=KafkaproxyApplicationTests.class)
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class KafkaproxyApplicationTests {
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
