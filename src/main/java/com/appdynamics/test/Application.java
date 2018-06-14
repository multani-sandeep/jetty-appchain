package com.appdynamics.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.appdynamics.test.MBeans.MBAttribute;
import com.appdynamics.test.MBeans.MBean;
import com.appdynamics.test.MBeans.MBeanApp;
import com.appdynamics.test.Routes.Delay;
import com.appdynamics.test.Routes.Error;
import com.appdynamics.test.Routes.Http;
import com.appdynamics.test.Routes.Method;
import com.appdynamics.test.Routes.MethodWrapper;
import com.appdynamics.test.Routes.Node;
import com.appdynamics.test.Routes.Route;
import com.appdynamics.test.Routes.Serve;
import com.appdynamics.test.Routes.Step;

public class Application extends HttpServlet {

	public static Routes ROUTES;
	public static MBeans MBEANS;
	public static MBeanApp MBEAN_APP;
	public static Route DEF_ROUTE;
	public static String APP_NAME = System.getProperty("appdynamics.agent.tierName");

	static final Logger LOG = LogManager.getLogger(Application.class.getName());

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Routes.class);
			Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			ROUTES = (Routes) jaxbUnMarshaller.unmarshal(this.getClass().getResourceAsStream("/routing.xml"));
			jaxbMarshaller.marshal(ROUTES, System.out);
			DEF_ROUTE = ROUTES.routes.stream().filter(route -> {
				return route.isDefaultRoute;
			}).findAny().get();

			jaxbContext = JAXBContext.newInstance(MBeans.class);
			jaxbUnMarshaller = jaxbContext.createUnmarshaller();
			jaxbMarshaller = jaxbContext.createMarshaller();

			MBEANS = (MBeans) jaxbUnMarshaller.unmarshal(this.getClass().getResourceAsStream("/mbeans.xml"));
			jaxbMarshaller.marshal(MBEANS, System.out);
			registerMbeans();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private static void registerMbeans() {
		final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		MBEANS.application.stream().filter(application -> {
			return application.name.equals(APP_NAME);
		}).forEach(application -> {
			application.mbean.forEach(mbean -> {
				try {
					ObjectName objName = new ObjectName(mbean.objectName);
					if (mbean.type.equals("Queue")) {
						server.registerMBean(new Queue(mbean), objName);
					}
					MBEAN_APP = application;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		});
	}

	public static void log(Object... objects) {
		LOG.info(APP_NAME + ": " + Arrays.stream(objects).map(Object::toString).collect(Collectors.joining(" ")));
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		service(req, resp);
	}

	@Override
	protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		service(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		service(req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		service(req, resp);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		service(req, resp);
	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		service(req, resp);
	}

	@Override
	protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		service(req, resp);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// proxyOrServe(req, resp);
		route(req, resp);
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	protected static final boolean serveMe(Step step, Serve serve) {
		int rnd = new Random().nextInt(100);
		log((Object) rnd, " ", serve.load);
		return rnd < serve.load;
	}

	protected void route(HttpServletRequest req, HttpServletResponse resp) throws ClientProtocolException, IOException {
		Routes.Route matchingRoute = ROUTES.routes.stream().filter(route -> {
			return req.getRequestURI().endsWith(route.url) && route.nodes.stream().anyMatch(node -> {
				return node.name.equals(APP_NAME);
			});
		}).findFirst().orElse(DEF_ROUTE);

		if (matchingRoute.isDefaultRoute) {
			log("Switching to default route");
			serve(req, resp, matchingRoute.nodes.get(0).steps.get(0).serve.get(0));
		} else {
			MethodWrapperCallback callback = new MethodWrapperCallback() {

				@Override
				public void execute(HttpServletRequest req, HttpServletResponse resp, Http http) {
					try {
						proxy(req, resp, http);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void execute(HttpServletRequest req, HttpServletResponse resp, Delay delay) {
					delay(req, resp, delay);
				}

				@Override
				public void execute(HttpServletRequest req, HttpServletResponse resp, Serve serve) {
					serve(req, resp, serve);
				}

			};
			Node matchingNode = matchingRoute.nodes.stream().filter(node -> {
				return node.name.equals(APP_NAME);
			}).findFirst().get();
			log("Found matching route " + matchingRoute.url + " for node " + matchingNode.name);
			try {
				matchingNode.steps.forEach(step -> {
					step.error.forEach(error -> {
						error(req, resp, error);
					});
					step.http.forEach(http -> {
						try {
							proxy(req, resp, http);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
					if (step.weighted) {
						Serve srv = step.serve.stream().sorted(new Comparator<Serve>() {
							@Override
							public int compare(Serve o1, Serve o2) {
								return o1.load <= o2.load ? -1 : 1;
							}
						}).filter(serve -> {
							return serveMe(step, serve);
						}).findFirst().orElse(step.serve.get(0));
						serve(req, resp, srv);
					} else {
						step.serve.forEach(serve -> {
							serve(req, resp, serve);
						});
					}

					step.delay.forEach(delay -> {
						delay(req, resp, delay);
					});
					step.method.stream().filter(method -> {
						return method.name.equals("copyFromHeader");
					}).forEach(method -> {
						copyFromHeader(req, resp, method);
					});

					step.method.stream().filter(method -> {
						return method.name.equals("updateMbeanStats");
					}).forEach(method -> {
						updateMbeanStats(req, resp, method, null);
					});

					step.methodWrapper.stream().filter(method -> {
						return method.name.equals("updateMbeanStats");
					}).forEach(method -> {
						updateMbeanStats(req, resp, method, callback);
					});

				});
			} catch (ForcedException exc) {
				log("Exception occured:", exc);
				throw exc;
			}

		}
	}

	public static interface MethodWrapperCallback {
		public void execute(HttpServletRequest req, HttpServletResponse resp, Http http);

		public void execute(HttpServletRequest req, HttpServletResponse resp, Serve serve);

		public void execute(HttpServletRequest req, HttpServletResponse resp, Delay delay);
	}

	private void updateMbeanStats(HttpServletRequest req, HttpServletResponse resp, Method method,
			MethodWrapperCallback callback) {
		MBEAN_APP.mbean.stream().filter(mbean -> {
			return method.queueName != null && mbean.objectName.contains(method.queueName);
		}).forEach(mbean -> {
			mbean.attribute.stream().filter(attr -> {
				return attr.attrType != null
						&& (attr.attrType.equals("increment") || attr.attrType.equals("transient"));
			}).forEach(attr -> {
				incrementCounter(req, resp, mbean, attr, method);
			});
			mbean.attribute.stream().filter(attr -> {
				return attr.valueFromHeader != null;
			}).forEach(attr -> {
				updateCounterWithValueFromHeader(req, resp, mbean, attr);
			});
		});
		try {
			MethodWrapper mWrap = (MethodWrapper) method;
			mWrap.http.forEach(http -> {
				try {
					proxy(req, resp, http);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			mWrap.serve.forEach(http -> {
				serve(req, resp, http);
			});
			mWrap.delay.forEach(delay -> {
				try {
					delay(req, resp, delay);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		} finally {
			MBEAN_APP.mbean.stream().filter(mbean -> {
				return method.queueName != null && mbean.objectName.contains(method.queueName);
			}).forEach(mbean -> {
				mbean.attribute.stream().filter(attr -> {
					return attr.attrType != null && attr.attrType.equals("transient");
				}).forEach(attr -> {
					decrementCounter(req, resp, mbean, attr, method);
				});
			});
		}
	}

	private void updateCounterWithValueFromHeader(HttpServletRequest req, HttpServletResponse resp, MBean mbean,
			MBAttribute attr) {
		final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName objName = new ObjectName(mbean.objectName);
			Object fromRequest = req.getAttribute(attr.valueFromHeader);
			if (fromRequest != null) {
				if (attr.type.equals(Integer.class.getName())) {
					fromRequest = new Integer(fromRequest.toString());
				}
				server.setAttribute(objName, new Attribute(attr.name, fromRequest));
			}
		} catch (InvalidAttributeValueException | AttributeNotFoundException | ReflectionException | MBeanException
				| InstanceNotFoundException | MalformedObjectNameException e) {
			throw new RuntimeException(e);
		}

	}

	private void decrementCounter(HttpServletRequest req, HttpServletResponse resp, MBean mbean, MBAttribute attr,
			Method method) {
		final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName objName = new ObjectName(mbean.objectName);
			Integer updateCounter = (Integer) server.getAttribute(objName, attr.name);
			if (updateCounter == null) {
				updateCounter = new Integer(attr.startingValue);
			}
			updateCounter--;
			log("Counter " + attr.name + " for queue name:" + objName.getKeyProperty("destinationName") + " "
					+ updateCounter);

			server.setAttribute(objName, new Attribute(attr.name, updateCounter));
		} catch (InvalidAttributeValueException | AttributeNotFoundException | ReflectionException | MBeanException
				| InstanceNotFoundException | MalformedObjectNameException e) {
			throw new RuntimeException(e);
		}

	}

	private void incrementCounter(HttpServletRequest req, HttpServletResponse resp, MBean mbean, MBAttribute attr,
			Method method) {
		final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName objName = new ObjectName(mbean.objectName);
			Integer updateCounter = (Integer) server.getAttribute(objName, attr.name);
			if (updateCounter == null) {
				updateCounter = new Integer(attr.startingValue);
			}
			updateCounter++;
			log("Counter " + attr.name + " for queue name:" + objName.getKeyProperty("destinationName") + " "
					+ updateCounter);

			server.setAttribute(objName, new Attribute(attr.name, updateCounter));
		} catch (InvalidAttributeValueException | AttributeNotFoundException | ReflectionException | MBeanException
				| InstanceNotFoundException | MalformedObjectNameException e) {
			throw new RuntimeException(e);
		}

	}

	private void copyFromHeader(HttpServletRequest req, HttpServletResponse resp, Method method) {
		Map<String, String> map = new HashMap<>();
		method.param.stream().filter(param -> {
			return param.from.equals("header");
		}).forEach(param -> {
			map.put(param.name, req.getHeader(param.key));
		});
		logBusinessTxnData(map);
	}

	public void logBusinessTxnData(Map<String, String> txnData) {
		log("Txn Data", txnData);
	}

	private void error(HttpServletRequest req, HttpServletResponse resp, Error error) throws ForcedException {
		if (new Random().nextInt(100) <= error.errsPerHundred) {
			String message = "Generic Error";
			log("Send 500 error");

			if (error.errorType != null && error.errorType.type.equals("exception")) {
				message = error.errorType.message == null ? message : error.errorType.message;
				throw new ForcedException(error.errorType.message);
			}
			try {
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				PrintWriter wrtr = resp.getWriter();
				wrtr.write(message);
				wrtr.flush();

			} catch (IOException e) {
				throw new UnForcedException(e);
			}
		}

	}

	private void delay(HttpServletRequest req, HttpServletResponse resp, Delay delay) {
		try {
			if (delay.random != null) {
				Thread.sleep(delay.msec * new java.util.Random().nextInt(delay.random));
			} else if (delay.gaussMean != null) {
				Thread.sleep((long) (delay.msec
						* Math.abs((new java.util.Random().nextGaussian() * delay.gaussDeviation) + delay.gaussMean)));
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void proxyOrServe(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if ("server".equals(System.getProperties().getProperty("type"))) {
			serve(req, resp, DEF_ROUTE.nodes.get(0).steps.get(0).serve.get(0));
		} else {
			proxy(req, resp, DEF_ROUTE.nodes.get(0).steps.get(0).http.get(0));
		}
	}

	private void proxy(HttpServletRequest req, HttpServletResponse resp, Http http)
			throws ClientProtocolException, IOException {

		String url = http.url;
		log("Proxy", url);

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", "Test");
		for (Enumeration<String> headers = req.getHeaderNames(); headers.hasMoreElements();) {
			String headerName = headers.nextElement();
			log(">", headerName, req.getHeader(headerName));
			request.addHeader(headerName, req.getHeader(headerName));
		}
		HttpResponse response = client.execute(request);

		log("Response Code : ", response.getStatusLine().getStatusCode());
		if (response.getStatusLine().getStatusCode() != 200) {
			LOG.error("HTTP error" + response.getStatusLine().getStatusCode() + " received from " + url);
		}
		resp.setStatus(HttpServletResponse.SC_OK);

		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			PrintWriter wrtr = resp.getWriter();
			Arrays.stream(response.getAllHeaders()).forEach(downHeader -> {
				if (!downHeader.getName().toLowerCase().equals("content-length")
						|| !downHeader.getName().toLowerCase().equals("transfer-encoding")) {
					log("<", downHeader.getName(), downHeader.getValue());
					resp.addHeader(downHeader.getName(), downHeader.getValue());
				}
			});

			wrtr.print(result.toString());
			wrtr.flush();
			// resp.flushBuffer();
		} catch (Exception e) {
			log(e);
			throw e;
		}

	}

	@Override
	public void log(String msg) {
		log((Object) msg);
	}

	@Override
	public void log(String message, Throwable t) {
		log((Object) message, t);
	}

	private void serve(HttpServletRequest req, HttpServletResponse resp, Serve serve) {
		log("Serve");
		try {
			for (Enumeration<String> headers = req.getHeaderNames(); headers.hasMoreElements();) {
				String headerName = headers.nextElement();
				log(">", headerName, req.getHeader(headerName));
			}
			PrintWriter wrtr = resp.getWriter();
			wrtr.print(serve.payload);
			wrtr.flush();
			// resp.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
