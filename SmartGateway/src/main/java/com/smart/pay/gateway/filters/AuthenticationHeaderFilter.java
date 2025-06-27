package com.smart.pay.gateway.filters;





import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationHeaderFilter extends ZuulFilter {

    private static Logger       log       = LoggerFactory.getLogger(AuthenticationHeaderFilter.class);

    private static final String secretKey = "tq-20170822";
    //
    // // TODO Sai
    // // 需要验证Token的URL白名单，重构为：不验Token的白名单，换句话，默认全部验证Token。另外，配置对外开可见的URL白名单，默认就是不可见。白名单通过数据库管理
    private static final String[][] preAuthenticationIgnoreUris = { 
    		{ "/smartmerchant/merchant/login", "*" },
    		{ "/smartmerchant/merchant/new", "*" },
    		{ "/smartchannel/topup/request", "*" },
    		{ "/smartchannel/alipaywap/callback", "*" },
    		{ "/smartchannel/pay/success", "*" },
    		{"/smartpayment/pay/gateway", "*"},
    		{"/smartpayment/pay/gateway/withdraw", "*"},
    		{"/smartpayment/pay/hello", "*"},
    		{"/smartpayment/pay/rabbitmq", "*"},
    		{"/smartpayment/pay/rabbitmq/confirm", "*"},
    		{"/smartpayment/pay/order/query", "*"},
    		{"/smarttimer/merchant/test", "*"},
    		{"/smartchannel/alipay/topup", "*"},
    		{"/smartchannel/wexinpay/topup", "*"},
    		{"/smartchannel/wxpaywap/callback", "*"},
    		{"/smartclearing/order/notify/test", "*"},
    		{"/smartmerchant/merchant/monitor", "*"},
    		{"/smartchannel/mobile/payment/release", "*"},
    		{"/smartchannel/personal/request", "*"},
    		{"/smartchannel/mobile/equipment/error", "*"},
    		{"/smartchannel/alipaywap/personal/callback", "*"},
    		{"/smartchannel/personal/sysbusy", "*"}
    };
    //
    // // 所有request都需要验证Token，除了上面ignore的
    private static final String[][] preAuthenticationMustUris = { { "/", "*" } };



    @Override
    public String filterType() {
        return "pre";
    }


    @Override
    public int filterOrder() {
        return 1;
    }


    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        
        try {
        	request.setCharacterEncoding("UTF-8");
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        String uri = request.getRequestURI().toString().toLowerCase();
        String method = request.getMethod();
        log.info(String.format("====AuthenticationHeaderFilter.shouldFilter - httpmethod: (%s)", method));
        log.info(String.format("====AuthenticationHeaderFilter.shouldFilter - url", uri));
        for (int i = 0; i < preAuthenticationIgnoreUris.length; i++) {
	        if (uri.startsWith(preAuthenticationIgnoreUris[i][0].toLowerCase()) && (preAuthenticationIgnoreUris[i][1].equals("*") || method.equalsIgnoreCase(preAuthenticationIgnoreUris[i][1]))) {
	        	log.info("this will be not use filter");
	        	return false;
	        }
        }

        return true;
    }


    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String uri = request.getRequestURI().toString().toLowerCase();
        log.warn(String.format("[%-15s][%-4s][%-51s][%s]", request.getRemoteAddr(), request.getMethod(), uri, request.getHeader("user-agent")));
        String method = request.getMethod();
        
        ctx.addZuulRequestHeader("merchant_id", "");
        
        String token = request.getParameter("token");
        Claims claims = null;
        String merchantId = "";
        if (token != null && !token.trim().equals("null") && !token.trim().equals("")) {
            log.info(String.format("Token: [%s]", token));
            try {
                
            	claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
                log.info("claims is:" + claims);
                merchantId = (String) claims.get("merchantId");
                
                
            } catch (SignatureException e) {
                this.stopZuulRoutingWithError(ctx, HttpStatus.UNAUTHORIZED, "Invalid Merchant Token for the API (" + request.getRequestURI().toString() + ")");
                return null;
            } catch (ExpiredJwtException e) {
                this.stopZuulRoutingWithError(ctx, HttpStatus.UNAUTHORIZED, "Expired Merchant Token for the API (" + request.getRequestURI().toString() + ")");
                return null;
            }
        }

        // 3. set userInfo to HTTP header
        if (!merchantId.equalsIgnoreCase("")) {
            ctx.addZuulRequestHeader("merchant_id", merchantId);
        }

        // 4. stop the filter chain if userInfo is must
        
//        if (merchantId.equalsIgnoreCase("")) {
//        	for (int i=0; i<preAuthenticationMustUris.length; i++) {
//        			if (uri.startsWith(preAuthenticationMustUris[i][0].toLowerCase()) && (preAuthenticationMustUris[i][1].equals("*") || method.equalsIgnoreCase(preAuthenticationMustUris[i][1])) ) {
//        				log.info(String.format("token is missed for %s", uri));
//
//        				this.stopZuulRoutingWithError(ctx, HttpStatus.UNAUTHORIZED, "User Login is needed for the API (" + request.getRequestURI().toString() + ")");
//        				return null;
//        			}
//        	}
//        }
        return null;
    }


    private void stopZuulRoutingWithError(RequestContext ctx, HttpStatus status, String responseText) {

        ctx.removeRouteHost();
        ctx.setResponseStatusCode(status.value());
        ctx.setResponseBody(responseText);
        ctx.setSendZuulResponse(false);
    }
}
