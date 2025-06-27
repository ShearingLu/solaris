package com.smart.pay.common.exception;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class ApiExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);


    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleInvalidRequestError(InvalidRequestException ex) {
        log.error("handleInvalidRequestError - ", ex);
        return "BAD_REQUEST: " + ex.getMessage();
    }


    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String handleUnauthorizedExceptionError(UnauthorizedException ex) {
        log.error("handleUnauthorizedExceptionError - ", ex);
        return "UNAUTHORIZED: " + ex.getMessage();
    }


    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleRecordNotFoundExceptionError(UnauthorizedException ex) {
        log.error("handleRecordNotFoundExceptionError - ", ex);
        return "NOT_FOUND: " + ex.getMessage();
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleUnexpectedServerError(RuntimeException ex) {
        log.error("handleUnexpectedServerError - ", ex);
        // System.out.println("ex========" + ex);
        return "INTERNAL_SERVER_ERROR: " + ex.getMessage();
    }
}
