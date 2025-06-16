package com.Ecommerce.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(SellerException.class)
    public ResponseEntity<ErrorDetails> sellerExceptionHandler(SellerException se, WebRequest wr){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setDetails(wr.getDescription(false));
        errorDetails.setError(se.getMessage());
        errorDetails.setTimeStamp(LocalDateTime.now());
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST)    ;
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorDetails> productExceptionHandler(ProductException pe,WebRequest wr){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setDetails(wr.getDescription(false));
        errorDetails.setTimeStamp(LocalDateTime.now());
        errorDetails.setError(pe.getMessage());
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }

}
