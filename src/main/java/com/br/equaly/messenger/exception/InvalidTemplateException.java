package com.br.equaly.messenger.exception;

public class InvalidTemplateException extends RuntimeException{
    private static final String message = "Invalid Email Template Key";

    public InvalidTemplateException(){
        super(message);
    }
}
