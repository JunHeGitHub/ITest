package com.zinglabs.index.tbl.exception;

import com.zinglabs.index.tbl.exception.ApplicationException;


 
public class NeedLoginException extends ApplicationException{

	public NeedLoginException(String errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}

	public NeedLoginException(String errorCode,Throwable t){
		super(errorCode,t);
	}
	
	public NeedLoginException(){
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
