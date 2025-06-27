package com.smart.pay.gateway;




import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ApiErrorController implements ErrorController {

    private static Logger log = LoggerFactory.getLogger(ApiErrorController.class);

    @Value("${error.path:/error}")
    private String        errorPath;


    @Override
    public String getErrorPath() {
        return errorPath;
    }


    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "${error.path:/error}", produces = "application/json")
    public @ResponseBody ResponseEntity error(HttpServletRequest request) {

        final int status = getErrorStatus(request);
        final String errorMessage = getErrorMessage(request);

        // printErrorInfo(request, status, errorMessage);
        printErrorInfo(request, status, (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));

        return ResponseEntity.status(status).body(errorMessage);
    }


    private int getErrorStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        // Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        return statusCode != null ? statusCode : HttpStatus.INTERNAL_SERVER_ERROR.value();
    }


    private String getErrorMessage(HttpServletRequest request) {
        final Throwable exc = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        // final Throwable exc = (Throwable) request.getAttribute("javax.servlet.error.exception");

        return exc != null ? exc.getMessage() : "unknow exception";
    }


    void printErrorInfo(HttpServletRequest rr, final int status, final String errMesg) {
        String strFomat = "[%-15s][%-4s][%-60s][%s][%s][%s]";

        Object uri = rr.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        if (uri == null) uri = rr.getRequestURI();

        log.error(String.format(strFomat, rr.getRemoteAddr(), rr.getMethod(), uri, status, errMesg, rr.getHeader("user-agent")));
    }


    void printErrorInfo(HttpServletRequest rr, final int status, final Throwable exc) {
        String strFomat = "[%-15s][%-4s][%-60s][%s][%s]";

        Object uri = rr.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        if (uri == null) uri = rr.getRequestURI();

        log.error(String.format(strFomat, rr.getRemoteAddr(), rr.getMethod(), uri, status, rr.getHeader("user-agent")), exc);
    }
}