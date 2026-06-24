package com.basisttha.Kreta.Exception;

public class InvalidRefreshTokenException extends RuntimeException{
    public InvalidRefreshTokenException(String m){
        super(m);
    }
}
