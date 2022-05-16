package com.hackerrank.sample.controller;

import com.hackerrank.sample.exception.NoWeatherFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WeatherControllerAdvice {

    @ExceptionHandler(NoWeatherFoundException.class)
    public ResponseEntity handleWeatherNotFound() {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleGeneralException() {
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
