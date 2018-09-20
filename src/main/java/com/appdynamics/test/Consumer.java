package com.appdynamics.test;

/**
 * Hello world!
 */
public class Consumer extends Broker {

	public static void main(String[] args) throws Exception {
		
		thread(new HelloWorldConsumer(), false);
		
		
	}

	
}