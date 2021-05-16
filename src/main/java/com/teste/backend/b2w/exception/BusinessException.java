package com.teste.backend.b2w.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BusinessException extends RuntimeException {

    private String message;
    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

}
