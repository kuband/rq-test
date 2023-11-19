package com.example.rqchallenge.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Error {
    private final int status;
    private final String message;
}
