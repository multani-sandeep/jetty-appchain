package com.appdynamics.test;

import java.net.URI;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;

/**
 * Hello world!
 */
public class Broker {

	protected static final String BRKR_ENDPOINT="tcp://localhost:61616"; //"vm://localhost"
	protected static final String BINDING="broker:("+BRKR_ENDPOINT+")";
	protected static final String QUEUE_NAME="TEST.FOO";
	public static void main(String[] args) throws Exception {
		thread(new BrokerThread(), false);
	}

	public static void thread(Runnable runnable, boolean daemon) {
		Thread brokerThread = new Thread(runnable);
		brokerThread.setDaemon(daemon);
		brokerThread.start();
	}

	public static class BrokerThread implements Runnable {
		public void run() {
			try {
				BrokerService broker = BrokerFactory.createBroker(new URI(BINDING));// "broker:(tcp://localhost:61616)"));
				broker.start();
			} catch (Exception e) {
				System.out.println("Caught: " + e);
				e.printStackTrace();
			}
		}
	}

	public static class HelloWorldProducer implements Runnable {
		public void run() {
			try {
				// Create a ConnectionFactory
				ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BRKR_ENDPOINT);

				// Create a Connection
				Connection connection = connectionFactory.createConnection();
				connection.start();

				// Create a Session
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

				// Create the destination (Topic or Queue)
				Destination destination = new ActiveMQQueue(QUEUE_NAME);// =
																			// session.createQueue("TEST.FOO");

				// Create a MessageProducer from the Session to the Topic or
				// Queue
				MessageProducer producer = session.createProducer(destination);
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

				// Create a messages
				String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
				TextMessage message = session.createTextMessage(text);
				ObjectMessage oMsg = session.createObjectMessage(new String("message=test"));

				// Tell the producer to send the message
				System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
				//producer.send(message);
				producer.send(oMsg);

				// Clean up
				session.close();
				connection.close();
			} catch (Exception e) {
				System.out.println("Caught: " + e);
				e.printStackTrace();
			}
		}
	}

	public static class HelloWorldConsumer implements Runnable, ExceptionListener {
		public void run() {
			try {

				// Create a ConnectionFactory
				ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BRKR_ENDPOINT);

				// Create a Connection
				Connection connection = connectionFactory.createConnection();
				connection.start();

				connection.setExceptionListener(this);

				// Create a Session
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

				// Create the destination (Topic or Queue)
				Destination destination = new ActiveMQQueue(QUEUE_NAME);// =
																			// session.createQueue("TEST.FOO");

				// Create a MessageConsumer from the Session to the Topic or
				// Queue
				MessageConsumer consumer = session.createConsumer(destination);

				// Wait for a message
				Message message = consumer.receive(1000);

				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					String text = textMessage.getText();
					System.out.println("Received: " + text);
				} else {
					System.out.println("Received: " + message);
				}

				consumer.close();
				session.close();
				connection.close();
			} catch (Exception e) {
				System.out.println("Caught: " + e);
				e.printStackTrace();
			}
		}

		public synchronized void onException(JMSException ex) {
			System.out.println("JMS Exception occured.  Shutting down client.");
		}
	}
}