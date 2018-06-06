package com.appdynamics.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ForcedException extends RuntimeException {

	final static Logger LOG = LogManager.getLogger(ForcedException.class.getName());
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
		LOG.error(Application.APP_NAME+": "+message);
	}

	public ForcedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	

}
