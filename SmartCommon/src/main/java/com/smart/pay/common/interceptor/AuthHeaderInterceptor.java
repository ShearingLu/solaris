package com.smart.pay.common.interceptor;




import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;


public class AuthHeaderInterceptor implements HandlerInterceptor {

    private static Logger       log = LoggerFactory.getLogger(AuthHeaderInterceptor.class);

    private static ObjectMapper objectMapper;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        UserInfo userInfo = null;
        String userInfoJson = request.getHeader("user-info");

        if (userInfoJson != null && !userInfoJson.trim().equals("")) {
            log.info(String.format("AuthenticationHeaderInterceptor userInfoJson: %s", userInfoJson));

            try {
                userInfoJson = URLDecoder.decode(userInfoJson, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info(String.format("AuthenticationHeaderInterceptor userInfoJson  utf-8: %s", userInfoJson));

            try {
                userInfo = objectMapper.readValue(userInfoJson, UserInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            RequestUtil.setupUserInfo(userInfoJson, userInfo);

            log.info(String.format("AuthenticationHeaderInterceptor userInfo: %s", userInfo));
        }

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
