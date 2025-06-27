package com.smart.pay.payment.util;



//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.mock.web.MockServletContext;
//import org.springframework.test.context.junit4.SpringRunner;



//@RunWith(SpringRunner.class)
//@SpringBootTest(classes=MockServletContext.class)
public class TestRedis {

	
	@Autowired
    private StringRedisTemplate stringRedisTemplate;

//    @Test
//    public void testSet() throws Exception {
//        stringRedisTemplate.opsForValue().set("username", "zhangsan");
//        Assert.assertEquals("zhangsan", stringRedisTemplate.opsForValue().get("username"));
//    }
	
	
}
