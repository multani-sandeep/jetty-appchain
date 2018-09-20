package com.appdynamics.test;

/**
 * Hello world!
 */
public class Producer extends Broker{

	public static void main(String[] args) throws Exception {
		
		thread(new HelloWorldProducer(), false);
	}	
}