package com.example.f1.auth_service.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorObject {
    private String statusCode;
    private String message;
    private Date timeStamp;
}
