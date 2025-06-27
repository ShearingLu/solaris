package com.smart.pay.channel;

import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import javax.sql.DataSource;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
@Configuration
public class SmartChannelStartup extends WebMvcConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SmartChannelStartup.class);


    public static void main(String[] args) throws Exception {

       /* if (args.length == 0) System.getProperties().setProperty("LOG_ROOT", ".");
        if (args.length != 0) System.getProperties().setProperty("LOG_ROOT", "/product");*/
        if (args.length == 0) args = new String[] { "--spring.profiles.active=prod" };

        SpringApplication.run(SmartChannelStartup.class, args);

        logger.info("服务启动完毕，请检查是否有异常...");
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

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
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
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate(httpRequestFactory());
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        //设置整个连接池最大连接数 根据自己的场景决定
        connectionManager.setMaxTotal(400);
        //路由是对maxTotal的细分
        connectionManager.setDefaultMaxPerRoute(100);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(60000) //服务器返回数据(response)的时间，超过该时间抛出read timeout
                .setConnectTimeout(5000)//连接上服务器(握手成功)的时间，超出该时间抛出connect timeout
                .setConnectionRequestTimeout(1000)//从连接池中获取连接的超时时间，超过该时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
                .build();
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
    }
}

