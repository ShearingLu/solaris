package com.smart.pay.gateway;


import javax.servlet.MultipartConfigElement;

import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.smart.pay.gateway.filters.AuthenticationHeaderFilter;



@SpringBootApplication
@Controller
@EnableZuulProxy
public class SmartGatewayStartup extends WebMvcConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SmartGatewayStartup.class);


    public static void main(String[] args) throws Exception {

    /*    if (args.length == 0) System.getProperties().setProperty("LOG_ROOT", ".");
        if (args.length != 0) System.getProperties().setProperty("LOG_ROOT", "/product");*/
        if (args.length == 0) args = new String[] { "--spring.profiles.active=prod" };

        SpringApplication.run(SmartGatewayStartup.class, args);

        logger.info("服务启动完毕，请检查是否有异常...");

    }


    @Bean
    public AuthenticationHeaderFilter authenticationHeadFilter() {
        return new AuthenticationHeaderFilter();
    }


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("12800000KB");
        factory.setMaxRequestSize("12800000KB");
        return factory.createMultipartConfig();
    }


    @Bean
    public FilterRegistrationBean characterEncodingFilter() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        filter.setFilter(characterEncodingFilter);
        return filter;
    }


    @Bean
    public CorsFilter corsFilter() {

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        config.addAllowedOrigin("*");
        /*
         * config.addAllowedOrigin("http://127.0.0.1"); config.addAllowedOrigin("http://192.168.31.84"); config.addAllowedOrigin("http://192.168.31.1");
         */
        // config.addAllowedOrigin("http://api.yqilai.cn");
        // config.addAllowedOrigin("http://wechat.yqilai.cn");
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);

    }


    @Bean
    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {

            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
                tomcat.addConnectorCustomizers(new TomcatConnectorCustomizer() {

                    @Override
                    public void customize(Connector connector) {
                        connector.setMaxPostSize(100000000);
                    }

                });

            }
        };
    }
}
