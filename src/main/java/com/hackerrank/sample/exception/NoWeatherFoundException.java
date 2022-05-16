package com.hackerrank.sample.exception;

public class NoWeatherFoundException extends RuntimeException {

    public NoWeatherFoundException() {
    }

    public NoWeatherFoundException(String message) {
        super(message);
    }

    public String getMessage() {
        return "Weather ID is not found!!";
    }
}
