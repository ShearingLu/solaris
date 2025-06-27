package com.smart.pay.discovery;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient
public class DiscoveryStartup {

    private static final Logger logger = LoggerFactory.getLogger(DiscoveryStartup.class);


    public static void main(String[] args) {

       /* if (args.length == 0) System.getProperties().setProperty("LOG_ROOT", ".");
        if (args.length != 0) System.getProperties().setProperty("LOG_ROOT", "/product");*/
        if (args.length == 0) args = new String[] { "--spring.profiles.active=prod" };

        SpringApplication.run(DiscoveryStartup.class, args);

        logger.info("服务启动完毕，请检查是否有异常...");
    }


    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }


    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}