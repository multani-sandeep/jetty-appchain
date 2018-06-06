package com.appdynamics.test;

import java.util.logging.Level;

public class ForcedException extends RuntimeException {

	final static java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(ForcedException.class.getName());
	public ForcedException() {
		super();
		
	}

	public ForcedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ForcedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ForcedException(String message) {
		super(message);
		LOG.log(Level.SEVERE,Application.APP_NAME+": "+message);
	}

	public ForcedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	

}
