package com.zinglabs.index.tbl.exception;

public class CheckedException extends Exception {
	private static final long serialVersionUID = 9173044544173963854L;
	private String errorCode;
	
	public CheckedException(String errorCode){		
		super();
		this.errorCode = errorCode;
	}
	public CheckedException(String errorCode,Throwable t){
		super(t);
		this.errorCode = errorCode;
	}
	
	public String getErrorCode(){
		return this.errorCode;
	}
}
