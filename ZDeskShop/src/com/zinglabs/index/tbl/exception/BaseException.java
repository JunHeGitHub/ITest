package com.zinglabs.index.tbl.exception;


public class BaseException extends Exception {

    static final long serialVersionUID = -5829545098534135052L;

    
    private String exceptionMessage;

   
    public BaseException() {
    }

   
    public BaseException(String msg) {
        this.exceptionMessage = msg;
    }

    public BaseException(String msg, Throwable e) {
        this.exceptionMessage = msg;
        this.initCause(e);
    }

    
    public void setCause(Throwable e) {
        this.initCause(e);
    }

   
    public String toString() {
        String s = getClass().getName();
        return s + ": " + exceptionMessage;
    }

    
    public String getMessage() {
        return exceptionMessage;
    }
}