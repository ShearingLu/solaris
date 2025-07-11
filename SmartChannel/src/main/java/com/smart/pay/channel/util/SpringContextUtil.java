package com.smart.pay.channel.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Component
public class SpringContextUtil implements ApplicationContextAware {

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;  
	}
	
	  
    // Spring应用上下文环境  
    private static ApplicationContext applicationContext;
  
    /** 
     * @return ApplicationContext 
     */  
    public static ApplicationContext getApplicationContext() {
        return applicationContext;  
    }  
  
    /** 
     * 获取对象 
     *  
     * @param name 
     * @return Object
     * @throws BeansException
     */  
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);  
    }  
    
    
    public static Object getBeanOfType(String name) throws BeansException {
    	try {
			Map<String, ?> beansMap = applicationContext.getBeansOfType(Class.forName(name));
			Collection<?> valueSet = beansMap.values();
			ArrayList<?> valueList = new ArrayList<>(valueSet);
			return valueList.get(0);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
    }  

}
