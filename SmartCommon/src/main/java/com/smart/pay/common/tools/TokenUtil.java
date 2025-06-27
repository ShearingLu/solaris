package com.smart.pay.common.tools;




import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class TokenUtil {

    // private static final Logger LOG = LoggerFactory.getLogger(TokenUtil.class);

    static String createToken2(long userId) {// 永不过期，用于测试
        String userToken = Jwts.builder().setSubject("TongQi").claim("merchantId", userId).signWith(SignatureAlgorithm.HS512, Constss.SECRETKEY).compact();

        return userToken;
    }


    public static String createToken(String userId) {
        String userToken = Jwts.builder().setSubject("TongQi").setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 600))
                .claim("merchantId", userId).signWith(SignatureAlgorithm.HS512, Constss.SECRETKEY).compact();
        // String userToken = Jwts.builder().setSubject("TongQi").setExpiration(new Date(new Date().getTime() + 1000 * 60 * 60 *
        // 24 * 600)).claim("userId", userId).signWith(SignatureAlgorithm.HS512, Constss.SECRETKEY).compact();

        return userToken;
    }


    public static String getUserId(String token) throws Exception {

        Claims claims = Jwts.parser().setSigningKey(Constss.SECRETKEY).parseClaimsJws(token).getBody();
        // Map<String, Object> map = new HashMap<>();

        Object uid = claims.get("merchantId");
        if (uid == null) return null;
        return uid.toString();

        // Long userId = (Long) claims.get("userId");
        // return userId;
    }


    /** 验证token */
    public static Object validToken(String token) throws Exception {

        Map<String, Object> map = new HashMap<>();
        String userId;
        try {
            userId = TokenUtil.getUserId(token);
        } catch (Exception e) {
            map.put(Constss.RESP_CODE, Constss.ERROR_TOKEN);
            map.put(Constss.RESP_MESSAGE, "token无效");
            return map;
        }

        return userId;

    }


    public static void main(String[] args) {
        System.out.println(TokenUtil.createToken("1564413232"));
        try {
            String userId = TokenUtil.getUserId(
                    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJUb25nUWkiLCJ1c2VySWQiOjB9.OLXKC4MBZkrrZRyyS9dYY8yA98SNOr9K-O3baSfy-mred0QJWuSWGRRrQqu6CptoEHXMbFYJoLVylY_oEm1Vxg");

            System.out.println(userId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
