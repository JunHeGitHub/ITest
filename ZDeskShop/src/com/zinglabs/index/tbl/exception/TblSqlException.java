package com.zinglabs.index.tbl.exception;

import com.zinglabs.index.tbl.exception.ApplicationException;

public class TblSqlException extends ApplicationException{
	public TblSqlException(String errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}

	public TblSqlException(String errorCode,Throwable t){
		super(errorCode,t);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}

