package com.smart.pay.timer;

import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.smart.pay.common.exception.ApiExceptionHandler;
import com.smart.pay.common.interceptor.AuthHeaderInterceptor;



@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
public class SmartTimerStartup extends WebMvcConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SmartTimerStartup.class);


    public static void main(String[] args) throws Exception {

       /* if (args.length == 0) System.getProperties().setProperty("LOG_ROOT", ".");
        if (args.length != 0) System.getProperties().setProperty("LOG_ROOT", "/product");*/
        if (args.length == 0) args = new String[] { "--spring.profiles.active=prod" };

        SpringApplication.run(SmartTimerStartup.class, args);

    }


    @Autowired
    private Environment env;


    @Bean
    public Executor myAsync() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(16);
        executor.setQueueCapacity(8192);
        executor.setThreadNamePrefix("MyExecutor-");

        // rejection-policy����pool�Ѿ��ﵽmax size��ʱ����δ���������
        // CALLER_RUNS���������߳���ִ�����񣬶����е��������ڵ��߳���ִ��
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }


    @Bean
    public DataSource getDataSource() throws Exception {
        Properties props = new Properties();

        props.put("driverClassName", env.getProperty("spring.datasource.driver-class-name"));
        props.put("url", env.getProperty("spring.datasource.url"));
        props.put("username", env.getProperty("spring.datasource.username"));
        props.put("password", env.getProperty("spring.datasource.password"));

        /*
         * String className = env.getProperty("spring.datasource.driver-class-name"); props.put("driverClassName","com.mysql.jdbc.Driver");
         * 
         * String url = env.getProperty("spring.datasource.url"); props.put("url", "jdbc:mysql://localhost:3306/notice?useUnicode=true&characterEncoding=UTF-8")
         * ;
         * 
         * String userName = env.getProperty("spring.datasource.username"); props.put("username", "root");
         * 
         * String passWord = env.getProperty("spring.datasource.password"); props.put("password", "TongQi2017!");
         */

        return DruidDataSourceFactory.createDataSource(props);
    }


    /*
     * @Bean public DataSource druidDataSource(@Value("{spring.datasource.driver-class-name}") String driver,
     * 
     * @Value("{spring.datasource.url}") String url,
     * 
     * @Value("{spring.datasource.username}") String username,
     * 
     * @Value("{spring.datasource.password}") String password){
     * 
     * DruidDataSource druidDataSource = new DruidDataSource();
     * 
     * druidDataSource.setDriverClassName(driver); druidDataSource.setUrl(url); druidDataSource.setUsername(username); druidDataSource.setPassword(password);
     * 
     * try{ druidDataSource.setFilters("stat, wall"); } catch(SQLException e){ e.printStackTrace(); }
     * 
     * return druidDataSource; }
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthHeaderInterceptor()).addPathPatterns("/**");
    }


    @Bean
    public ApiExceptionHandler getApiExceptionHander() {
        return new ApiExceptionHandler();
    }


    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
