package com.capgemini.employeepayrolljdbc;

public class DBServiceException extends Exception{
	DBServiceExceptionType exceptionType;
	public DBServiceException(String message, DBServiceExceptionType exceptionType) {
		super(message);
		this.exceptionType=exceptionType;
	}
}

enum DBServiceExceptionType{
	SQL_EXCEPTION , CLASSNOTFOUNDEXCEPTION
}