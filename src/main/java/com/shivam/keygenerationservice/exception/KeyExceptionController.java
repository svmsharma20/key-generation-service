
package com.shivam.keygenerationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class KeyExceptionController {

    @ExceptionHandler(value = KeyNotFoundException.class)
    public ResponseEntity<String> exception(KeyNotFoundException exception){
        return new ResponseEntity<>(KeyNotFoundException.KEY_NOT_FOUND_EXCEPTION, HttpStatus.NOT_FOUND);
    }
}
