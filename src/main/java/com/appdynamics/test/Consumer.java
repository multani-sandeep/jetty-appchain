package com.appdynamics.test;

/**
 * Hello world!
 */
public class Consumer extends Broker {

	public static void main(String[] args) throws Exception {
		while (true) {
		thread(new HelloWorldConsumer(), false);
		Thread.sleep(1000);
		}
	}

	
}