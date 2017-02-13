package com.zinglabs.index.tbl.exception;

import com.zinglabs.index.tbl.exception.ApplicationException;

public class AuthorityException extends ApplicationException{
	public AuthorityException(String errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}

	public AuthorityException(String errorCode,Throwable t){
		super(errorCode,t);
	}
	
	public AuthorityException(){
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
