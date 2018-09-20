package com.appdynamics.test;

/**
 * Hello world!
 */
public class Producer extends Broker{

	public static void main(String[] args) throws Exception {
		while (true) {
		// thread(new HelloWorldProducer(), false);
		thread(new HelloWorldProducer(), false);
		Thread.sleep(1000);
		}
	}	
}