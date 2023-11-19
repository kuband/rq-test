package com.example.rqchallenge.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UnexpectedException extends RuntimeException{
    private final HttpStatus errorCode;
}
