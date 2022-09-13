package com.happytoro.kafkaproxy.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@RestController
@RequestMapping("/api/")
public class ProxyController {
	@Value("${spring.application.name}")
	String appName;
    Logger logger = LogManager.getLogger(RestController.class);

    @GetMapping("/test")
    public String create1() {
        logger.info(appName);
        return appName;
    }

    @PostMapping("/test")
    public String create2() {
        logger.info("ok");
        return "ok";
    }
}