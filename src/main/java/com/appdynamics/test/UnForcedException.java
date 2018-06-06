package com.appdynamics.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnForcedException extends RuntimeException{

	final static Logger LOG = LogManager.getLogger(UnForcedException.class.getName());
	
	public UnForcedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UnForcedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		LOG.error(message,cause);
	}

	public UnForcedException(String message, Throwable cause) {
		super(message, cause);
		LOG.error(message,cause);
	}

	public UnForcedException(String message) {
		super(message);
		LOG.error(message);
	}

	public UnForcedException(Throwable cause) {
		super(cause);
		LOG.error(cause);
	}

}
