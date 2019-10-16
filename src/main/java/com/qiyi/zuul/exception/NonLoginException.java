package com.qiyi.zuul.exception;

public class NonLoginException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public NonLoginException(){
		super();
	}
    public NonLoginException(String message){
        super(message);
    }
}
