package com.smart.pay.channel.basechannel.kernel;

import com.google.common.collect.Maps;
import com.smart.pay.channel.basechannel.kernel.annotation.ChannelAPIMapping;
import com.smart.pay.channel.basechannel.kernel.annotation.ChannelMapping;
import com.smart.pay.common.tools.CommonConstants;
import com.smart.pay.common.tools.ResultWrap;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ChannelStore {
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(ChannelStore.class);
    private ApplicationContext applicationContext;

    private HashMap<String, ChannelRunnable> apiMap = new HashMap<String, ChannelRunnable>();

    public ChannelStore(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 装载所有实现支付通道
     */
    public void loadChannelsFromSpringBeans() {
        String[] names = applicationContext.getBeanNamesForAnnotation(ChannelMapping.class);//.getBeanDefinitionNames();
        Class<?> type;
        //反射
        for (String name : names) {
            type = applicationContext.getType(name);
            ChannelMapping mapping = type.getDeclaredAnnotation(ChannelMapping.class);
            if(mapping != null){
                HashMap<String,Method> methodHashMap = new HashMap<>();
                for (Method m : type.getMethods()) {
                    // 通过反射拿到ChannelAPIMapping注解
                    ChannelAPIMapping apiMapping = m.getAnnotation(ChannelAPIMapping.class);
                    if (apiMapping != null) {
                        methodHashMap.put(apiMapping.value(),m);
                    }
                }
                addApiItem(mapping, name, methodHashMap);
            }
        }
    }

    private void addApiItem(ChannelMapping apiMapping, String beanName, HashMap<String,Method> methodMap) {
        ChannelRunnable apiRun = new ChannelRunnable();
        apiRun.apiName = apiMapping.value();
        apiRun.targetMethodMap = methodMap;
        apiRun.targetName = beanName;
        apiRun.channelMapping=apiMapping;
        apiMap.put(apiMapping.value(), apiRun);
    }

    public ChannelRunnable findApiRunnable(String apiName) {
        return apiMap.get(apiName);
    }

    public class ChannelRunnable {
        String apiName;
        String targetName;//通道接口name
        Object target; // 具体通道接口实现
        HashMap<String,Method> targetMethodMap = new HashMap<>();
        ChannelMapping channelMapping;

        public Map<String, Object> run(Method method, Map<String, Object> params) {
            if (target == null) {
                target = applicationContext.getBean(targetName);
            }
            try {
				return (Map<String, Object>)method.invoke(target, params.get("data"));
			} catch (Exception e) {
				log.error("method.invoke error", e);
			}

			return ResultWrap.init(CommonConstants.FALIED, "系统异常", null);
            
        }
        
        public Map<String, Object> returnMap(int code, String msg, Object data) {
            Map<String, Object> map = Maps.newHashMap();
            map.put(CommonConstants.RESP_CODE, code);
            map.put(CommonConstants.RESP_MESSAGE, msg);
            map.put(CommonConstants.RESULT, data);
            return map;
        }

        public String getApiName() {
            return apiName;
        }

        public String getTargetName() {
            return targetName;
        }

        public Object getTarget() {
            return target;
        }

        public HashMap<String,Method> getTargetMethodMap() {
            return targetMethodMap;
        }

        public ChannelMapping getChannelMapping() {
            return channelMapping;
        }
    }
}
