package com.smart.pay.common.tools;

import javax.servlet.http.HttpServletRequest;


public class IPUtils {

	
	

	
	/** 
     *  
     * getRemoteIP:获取远程请求客户端的外网IP <br/> 
     *  
     * @param request 
     *            请求实体对象 
     * @return ip 外网ip<br/> 
     */  
    public static String getRemoteIP(HttpServletRequest request) {  
        String ip = request.getHeader("x-forwarded-for");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }  
	
    
    
    public static boolean matches(String text) {  
        if (text != null && !text.isEmpty()) {  
            // 定义正则表达式  
            String regex = "^(1\\d{2}|2[0-4 ]\\d|25[0-5]|[1-9]\\d|[1-9])\\." 
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."  
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."  
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";  
            // 判断ip地址是否与正则表达式匹配  
            if (text.matches(regex)) {  
            // 返回判断信息  
                return true;  
            } else {  
            // 返回判断信息  
              return false;  
            }  
        }  
        // 返回判断信息  
        return false;  
    }  
	
    
    
}
