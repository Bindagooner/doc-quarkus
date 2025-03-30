package com.example.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class ErrorResponse {
    private String code;
    private String message;
}
