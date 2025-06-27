package com.smart.pay.channel.basechannel.kernel;

import com.smart.pay.common.tools.CommonConstants;
import com.smart.pay.common.tools.ResultWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 通道接口调用主入口
 */
@Component
public class ChannelHandler implements InitializingBean, ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(ChannelHandler.class);

    ChannelStore channelStore;

    public Map<String, Object> handle(String channelProductTag, String methodTag, Map<String,Object> params){

        Map<String, Object> result = null;

        //@TODO  根据channelProductTag 获取可用通道
        String channelTag = channelProductTag;

        ChannelStore.ChannelRunnable channelRunnable = channelStore.findApiRunnable(channelTag);

        if(channelRunnable==null){
            return ResultWrap.err(LOG, CommonConstants.FALIED, "未找到可用通道!");
        }

        try {
            Method method = channelRunnable.getTargetMethodMap().get(methodTag);
            if(method==null){
                return ResultWrap.err(LOG, CommonConstants.FALIED, "未找到可用接口!");
            }

            result = channelRunnable.run(method,params);//
        } catch (Exception e) {
        	LOG.error("Exception error", e);
        } 
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        channelStore.loadChannelsFromSpringBeans();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        channelStore = new ChannelStore(applicationContext);
    }
}
