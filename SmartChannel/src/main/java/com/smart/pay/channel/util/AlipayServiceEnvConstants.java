

/**

 * Alipay.com Inc.

 * Copyright (c) 2004-2014 All Rights Reserved.

 */

package com.smart.pay.channel.util;


/**
 * 支付宝服务窗环境常量（demo中常量只是参考，需要修改成自己的常量值）
 * 
 * @author taixu.zqq
 * @version $Id: AlipayServiceConstants.java, v 0.1 2014年7月24日 下午4:33:49 taixu.zqq Exp $
 */
public class AlipayServiceEnvConstants {

	  /**支付宝公钥-从支付宝生活号详情页面获取*/
		public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq9knqk4xqvyyhZo/W71+e+3lkeOLyyaxHpOsZPDQOEMZTakVZ3q7o1VjygrGo5ZfMkzw8vsHU5MAejvlxVij+xj/u0HIGJD36vVobLjh9Vrqf6j/PT9ghC3f8pCECox6o7GAmNLVqKkcVcjMNr4m9KwxkkD78nauSZ9Fx6xdhs5CZ9FzmxHnuUO+2H4z4M8W52dPlUCzylhLE+HVlM7tvGOeInh3ZSWZwNjgLnw9wtuJY4k7Stl2KrXvoQyVTMUTC9cvWm7DSvxQdtYckiRHf6e1whArM6tRRo8Oxo62GF3dTSUrQSSIvKMUvyqZ7Qz8apuvx7wTZG3oTvf78sJNlQIDAQAB";
	    //TODO !!!! 注：该私钥为测试账号私钥  开发者必须设置自己的私钥 , 否则会存在安全隐患 
	    public static final String PRIVATE_KEY       = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCi9bZqwiS6zsskx+iV9Fa71hc+RiTbpLPeD+Q1LnpLDxLem06dRQKyPO1CpMwtMlC2tU7pP9R7PX4Ip6V5xkurHlR8Rq84hSlfeGGaCDhehFD7Zl7Oga0bjV/PxxblYCnw/TcmvzcTO8HZbL0FrMWEGcbBUpSSAlqRjZNUEMChF/A3/mXtpAEjzVjGDV5dCq9apuIP9+E5cjksZM6FCbxeCpxzfiefhQiWbIe5aSQZxHheL7W9wYDprWpDO/Nzgs9oImFc/LM1U5kJKfB4Wpe8QAbyywWIHHemJRvWMERQSsfvBjn9T8WKaI7uLNPoNSXrEOVKNzPJu1gY1uMgP1lzAgMBAAECggEAL5HgpTt1M18IZ/0/RP7ljsGJWXFqcv+aTfl/mZKuulh1Hqxhf4FUrykf7XNj8tGcRCmbFnW+lhc2QlqF8I1VK2eYLAsKPUqJ8EeY3jkGE3k2lpykkk3CdaeIT4QDiAPRWnrgbZwl/KRWCx019IEhy1Um8uojWpf7Tj/wwPp+8Mcv2o2WZZy3zKl9THxR7iJ/bvda2BcsUSJKqZI0CRGONGUXzhcxP2OUytiJs9RMhLYDQ/xE36XpBi/mPfr/yWMK0dK41fND8GR28InM5bH7DmdF+cokDqkANIXQxbU0tDEYAynt/6QMMILUdMQRBUatSGxachOb980xM9ZN5CTk2QKBgQDR+GWaFJBd/51tRvzBb1w3b3je1Sh9uHJY54mCOhjK+sCflcy59gOZPPCEoUtHazj14F3J4Np2IGdx/yk2dXnNPRbF6OjlQ80q8PvuayZ0wnOmNALRXSuPpL+PVf/FFCLnwDXoSq6bgdRyBu/jflWJaksofOBNCdAYhhcRFQVoDwKBgQDGrxJTuGJlEr9Q1Y3biLaENcHaZaTRIkA648oiif/64D2iT7rHB2RXYLFNjpGpBBlCa9dzIwYoiCWfA3AMzq49UNqfVqMKay8BOSDVucWptUtgPGRTrwPQ5vD6tACqknSMbNLz/lBzIPWR+5WNwtnBlOO6HrzoSHX5NpyGIAK0XQKBgQDRpY7s1XOAJ2VsYuCj0yjkQO0SsQqI3M1vb1+hI5j1soD51nxfgP/+1RXhl5quaKSq+6cpltUJ9TjUXc0sjwdaoZBHc1J298e3BH7Hgz0Wf8ExReU6XijMd9a8q0WB5aUkeIpasZa9tlgEsmB7aPzHA9afSnA6+31Hvzrw12haiQKBgGrNH1/rpNDKNBdixH+3TM1jDFK3AtL8w2QkoLrSTczxD5CZpUBpw9GIg6a6NaiSMHXY/JbCxAOzs+13bpsj2tvRA8RVUQ0/sqPIrtLUquFgOCWCMTgew0FfcziA3D7UvVvi/77y/RH2LKuNfsYWcPGzdHrPHSMMGIafiaI7wNhtAoGBALaj76hyWdqGtB10hfyAVxohTU4N9GMrGCszjDu96PojLqNcoEBjH5Ji1bzYrC1Z84TJbS4D/3ylH1m+kXmR0W0A6mm0OO90Lpmg9KOZfVl4mbs99oehCSj0O5UGfBYqCjWyG+W/1iqdAFNM8yxFGTojJhVCyXNXmRqGjDMmKei3"; 
	    //TODO !!!! 注：该公钥为测试账号公钥  开发者必须设置自己的公钥 ,否则会存在安全隐患
	    public static final String PUBLIC_KEY        = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAovW2asIkus7LJMfolfRWu9YXPkYk26Sz3g/kNS56Sw8S3ptOnUUCsjztQqTMLTJQtrVO6T/Uez1+CKelecZLqx5UfEavOIUpX3hhmgg4XoRQ+2ZezoGtG41fz8cW5WAp8P03Jr83EzvB2Wy9BazFhBnGwVKUkgJakY2TVBDAoRfwN/5l7aQBI81Yxg1eXQqvWqbiD/fhOXI5LGTOhQm8Xgqcc34nn4UIlmyHuWkkGcR4Xi+1vcGA6a1qQzvzc4LPaCJhXPyzNVOZCSnweFqXvEAG8ssFiBx3piUb1jBEUErH7wY5/U/FimiO7izT6DUl6xDlSjczybtYGNbjID9ZcwIDAQAB"; 
	    /** 服务窗appId  */
	    //TODO !!!! 注：该appId必须设为开发者自己的生活号id  
	    public static final String APP_ID            = "2018050102614250";
	    
	    /**支付宝公钥-从支付宝生活号详情页面获取*/
	    public static final String ALIPAY_PUBLIC_KEY2 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkQn0pvVy/sjIbC2sBqygYgvfeGIM/j54hgfaKNX5DVLk7vlO1Wgz4wunzMqbIPtKmC7ZPQ3btwqPKjOYBGHoUb1y1Eos0M5+fV0xEBrKuPhng7znbEjoMxdPECurks1igwwIoPS7TEavZlVjUOnQiv9VUe0VhwF8f2hjCvl1HLS6dRa3vEXnsGukw6qg0+jzPiRuKPSLIkpy/1LoJDQ29Vn1tJL7A9XtE+zPSzzA6xETWWhnwRzFIx81k0O7ehya0Vls8LXth3xfmeZxCTZCENsYk8wIvaFp7MCvDJkzahdQmDaCVUCvmn/8jAmCZB03/pn2IjgfCKtfrtV3akq91wIDAQAB";
	    //TODO !!!! 注：该私钥为测试账号私钥  开发者必须设置自己的私钥 , 否则会存在安全隐患 
	    public static final String PRIVATE_KEY2       = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDmbDu+R8oPDuT6OaIDROw172Foss7dcWATEfqI+qXg474keyTLxwS3p2Q9vt1XWE+4VY00ysNr8mBnKCp/IZe6JMNMqCKNpn1y1pVQk/sDUDliY8SdPzkL7bAyghP88AvdxamKBg/SxrXNP5F0Xnu1n2iFIsJreBxerfA3CZMOUSF/7WVWQMnU31trCcxgl/J2Yx32n1hZ6XaLqABMOGJ8s2cd1laS4do5dDj2rOhG+Ork7eSf0WelDxpBMLLgiDjruHH/xTTHs+7gXQY2nIBrgcArToLdl91Yu8AweZ74O7o/p0U6sfyMc2NFISr8DJ+OR2eOZsJShh0KptvqjxMtAgMBAAECggEAGtKsnbjjp58XPp2o43KJgLnOAA8Yj6ScB3xHStzKpIQ+ZSbEoMgZH3vn6lwmjf1kwlOLLYMQ/qbnW/rL/6Kcc/nFlk3YP98KqWybUxb22GrlUzPD9n4emOnhYe/Mfdw/tWBGHSAZhXLGRQ9KUl6RThIxa5YhRbtkqgzbcry1lqvYeqOAxvuwIjhrr1eUqppJxihgUKMx/he6UW/K0jX88NOd5i/7Voaq/in0nRMUFHWIbs7gd4puP8RoV4P9QAlT+I8AR726VzV6POABC1TYMe5IzxBQXIE+NP/h8oYNHBPjjtoNOowrAcfzyh5+fe7bbMdGrjaZ0nOriIYIR+UNrQKBgQD++TMns4ErtNyAPq1lxjGGl1hzbDl5Rz1aprqjG8PTtMCEpY52WAWbnaeOzO2/FYtKSgvnAAvWH7E4pRkew1X00l4pzLYkRiFQae7TTsOLJEmYiqkwVrbuQ/jsCGQDDrViYtGxWVWD2EyqU2qo6BazEmXPYpb+Jm8QS5EC7SXtfwKBgQDnWbqyU+jSG0tw+dmZO7TDK2B8qzt4AyUqc70Ra1pPzWZTun23RjxU/wbbyqisUALJxFkJ7NqMUCh9YUpj9cgx2Skgrc6Zr5WXK0pDTakfd0F1Zic80nXdqEVdL7OjJ/B1xRbxPzzxkyIjwEZxjdMuXez0Xff3JGw3QW6cONxtUwKBgQDYWCqHrVAU/cl7J7OJf7nWMeTjSxkZRtFQcdg7PfvTTrNr2e4fjGB1wjfCey/fyy/9QQcIYomojguZvLyr1aLBsR4k6YZDER6DVN9IfLR/A7NOs5kLYwik8xfrbtMtpPVTHfY/PUXKMWYbv9vopcVc/GdWqJjl3Uav1O6eRQrBkQKBgGy5B3ss4dpzx1V1paRUodOiu/p6wjzZRYsYzz6zc8LTJYatusz5rfrHyyJZsEEibN0DcFCwxdhxB0Y9BafKZuW+R4y7Ab/J+4QKbSEKCzPlaJbShhgYVBnyLX7onwRLDb9a+zgu3uYsnjus7a4J17gPnf8Ndi3oKQ8iqeey9SxPAoGBAPGrJCJYZp3VvRRTYSRzX4+5T4WzvLgP+9GGzNKJD3vP9i79knbsuDSJreaQfQ7QVjUAvUSe4awIkcs/07n/Dxp6WmkMS0OLbVrWUa33FgysJTOBzyllgaMuEvOmuSl+KiuOyAoxqBRLpdSxS7jPnTLLB2SMHBuckrolZEG/1OqS";
	    //TODO !!!! 注：该公钥为测试账号公钥  开发者必须设置自己的公钥 ,否则会存在安全隐患
	    public static final String PUBLIC_KEY2        = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5mw7vkfKDw7k+jmiA0TsNe9haLLO3XFgExH6iPql4OO+JHsky8cEt6dkPb7dV1hPuFWNNMrDa/JgZygqfyGXuiTDTKgijaZ9ctaVUJP7A1A5YmPEnT85C+2wMoIT/PAL3cWpigYP0sa1zT+RdF57tZ9ohSLCa3gcXq3wNwmTDlEhf+1lVkDJ1N9bawnMYJfydmMd9p9YWel2i6gATDhifLNnHdZWkuHaOXQ49qzoRvjq5O3kn9FnpQ8aQTCy4Ig467hx/8U0x7Pu4F0GNpyAa4HAK06C3ZfdWLvAMHme+Du6P6dFOrH8jHNjRSEq/AyfjkdnjmbCUoYdCqbb6o8TLQIDAQAB"; 
	    /** 服务窗appId  */
	    //TODO !!!! 注：该appId必须设为开发者自己的生活号id  
	    public static final String APP_ID2            = "2018043002613372";
	    
	    /**签名编码-视支付宝服务窗要求*/
	    public static final String SIGN_CHARSET      = "UTF-8";

	    /**字符编码-传递给支付宝的数据编码*/
	    public static final String CHARSET           = "UTF-8";

	    /**签名类型-视支付宝服务窗要求*/
	    public static final String SIGN_TYPE         = "RSA2";
	    
	    /**开发者账号PID*/
	    public static final String PARTNER           = "2088131076490171";
	    
	    /**支付宝网关*/
        public static final String ALIPAY_GATEWAY    = "https://openapi.alipay.com/gateway.do";

        /**授权访问令牌的授权类型*/
        public static final String GRANT_TYPE        = "authorization_code";
}