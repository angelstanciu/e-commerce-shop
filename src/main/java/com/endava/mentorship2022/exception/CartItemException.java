package com.endava.mentorship2022.exception;

import org.springframework.core.NestedRuntimeException;

public class CartItemException extends NestedRuntimeException {
    public CartItemException(String msg) {
        super(msg);
    }
}
