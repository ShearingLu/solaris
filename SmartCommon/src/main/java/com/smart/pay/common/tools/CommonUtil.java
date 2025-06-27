package com.smart.pay.common.tools;



import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;


/**
 * @author john 2017年11月3日 下午1:19:30
 */
public class CommonUtil {

    /**
     * 获取客户端IP
     * 
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }


    /**
     * 判断是否是ajax请求
     * 
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null && "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()));
    }


    /**
     * 获取随机字符串
     * 
     * @param n
     *            随机字符串位数
     * @return
     */
    public static String getRandomString(int n) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 获取随机字符串(纯数字)
     * 
     * @param n
     *            随机字符串位数
     * @return
     */
    public static String getRandomNum(int n) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 抽奖
     * 
     * @param drawList
     *            抽取的概率列表
     * @return int 抽取到的奖品索引
     */
    public static int randomDraw(List<Double> drawList) {
        // 计算概率最小值
        Double min = 0d;
        for (int i = 0; i < drawList.size(); i++) {
            Double probability = drawList.get(i);
            if (probability <= 0) { throw new IllegalArgumentException("The number in the set must be greater than zero"); }
            if (min == 0 || probability < min) {
                min = probability;
            }
        }
        DecimalFormat dFormat = new DecimalFormat();
        dFormat.setMaximumFractionDigits(11);
        String minStr = dFormat.format(min);
        Integer nt = minStr.length() - minStr.indexOf(".") - 1;
        // 生成范围值数组
        double m = Math.pow(10d, nt.doubleValue());
        int[] scope = new int[drawList.size()];
        int s = 0;
        for (int i = 0; i < drawList.size(); i++) {
            Double probability = drawList.get(i);
            int t = (int) (multiply(m, probability));
            if (s == 0) {
                scope[s] = t;
            } else {
                scope[s] = scope[s - 1] + t;
            }
            s++;
        }
        // 随机抽取
        Random random = new Random();
        int drawInt = random.nextInt(scope[scope.length - 1]);
        for (int i = 0; i < scope.length; i++) {
            int start;
            int end = scope[i];
            if (i == 0) {
                start = 0;
            } else {
                start = scope[i - 1];
            }
            if (start <= drawInt && drawInt < end) {
                drawInt = i;
                break;
            }
        }
        return drawInt;
    }


    /**
     * 判断是否是移动端
     * 
     * @param request
     * @return
     */
    public static boolean isMobile(HttpServletRequest request) {
        boolean flag = false;
        String ua = request.getHeader("User-Agent");
        String[] agent = { "Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser" };
        if (!ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))) {
            // 排除 苹果桌面系统
            if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
                for (String item : agent) {
                    if (ua.contains(item)) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }


    /**
     * 精确的加法运算
     * 
     * @param v1
     *            被加数
     * @param v2
     *            加数
     * @return (v1 + v2)
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }


    /**
     * 精确的减法运算
     * 
     * @param v1
     *            被减数
     * @param v2
     *            减数
     * @return (v1 - v2)
     */
    public static double subtract(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }


    /**
     * 精确的乘法运算
     * 
     * @param v1
     *            被乘数
     * @param v2
     *            乘数
     * @return (v1 * v2)
     */
    public static double multiply(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }


    /**
     * 精确的除法运算
     * 
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @param scale
     *            当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入(可设为10提高精度)
     * @return (v1 / v2)
     */
    public static double divide(double v1, double v2, int scale) {
        if (scale < 0) { throw new IllegalArgumentException("The scale must be a positive integer or zero"); }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 精确的小数位四舍五入
     * 
     * @param v
     *            需要四舍五入的数字
     * @param scale
     *            精度
     * @return
     */
    public static double round(double v, int scale) {
        if (scale < 0) { throw new IllegalArgumentException("The scale must be a positive integer or zero"); }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    
    public static SortedMap getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map properties = request.getParameterMap();
        // 返回值Map
        SortedMap returnMap = new TreeMap();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if(null == valueObj){
                value = "";
            }else if(valueObj instanceof String[]){
                String[] values = (String[])valueObj;
                for(int i=0;i<values.length;i++){
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length()-1);
            }else{
                value = valueObj.toString();
            }
            returnMap.put(name, value.trim());
        }
        return returnMap;
    }

}
