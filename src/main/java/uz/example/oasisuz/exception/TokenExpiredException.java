package uz.example.oasisuz.exception;

import java.text.MessageFormat;

public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException(String token){
        super(MessageFormat.format("Token {0} was expired",token));
    }
}
