package com.example.loginsystemwithemailverification.exceptions;

public record ApiException (
        String message,
        int statusCode,
        String LocalDateTime,
        String path
){
}
