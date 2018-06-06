package com.appdynamics.test;

public class UnForcedException extends RuntimeException{

	final static java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(UnForcedException.class.getName());
	
	public UnForcedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UnForcedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UnForcedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UnForcedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UnForcedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
