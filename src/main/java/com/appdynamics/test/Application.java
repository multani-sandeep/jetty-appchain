package com.appdynamics.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
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

import org.apache.commons.lang3.time.StopWatch;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.springframework.mock.web.MockHttpServletResponse;

import com.appdynamics.cxf.commercial_cpm.megaswitch.v1.b2brouting.B2BRoutingPortType;
import com.appdynamics.cxf.commercial_cpm.megaswitch.v1.b2brouting.avail.RequestXml;
import com.appdynamics.cxf.commercial_cpm.megaswitch.v1.b2brouting.avail.RequestXmlResponse;
import com.appdynamics.cxf.commercial_cpm.megaswitch.v1.b2brouting.avail.RequestXmlResponse.RequestXmlResult;
import com.appdynamics.cxf.commercial_cpm.megaswitch.v1.b2brouting.avail.RequestXmlResponse.RequestXmlResult.ResponseRoot;
import com.appdynamics.cxf.commercial_cpm.megaswitch.v1.b2brouting.avail.RequestXmlResponse.RequestXmlResult.ResponseRoot.DataListRoot;
import com.appdynamics.cxf.commercial_cpm.megaswitch.v1.b2brouting.avail.RequestXmlResponse.RequestXmlResult.ResponseRoot.DataListRoot.AvailabilityResponse;
import com.appdynamics.cxf.commercial_cpm.megaswitch.v1.b2brouting.avail.RequestXmlResponse.RequestXmlResult.ResponseRoot.DataListRoot.AvailabilityResponse.AvailabilityList;
import com.appdynamics.cxf.commercial_cpm.megaswitch.v1.b2brouting.avail.RequestXmlResponse.RequestXmlResult.ResponseRoot.DataListRoot.AvailabilityResponse.AvailabilityList.Availability;
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
import com.appdynamics.test.Routes.SQL;
import com.appdynamics.test.Routes.Serve;
import com.appdynamics.test.Routes.Step;

public class Application extends HttpServlet implements B2BRoutingPortType//, com.appdynamics.cxf.commercial_cpm.megaswitch.v1.b2bavailabilityoft.B2BAvailabilityPortType 
{

	public static Routes ROUTES;
	public static MBeans MBEANS;
	public static DBs DATABASES;
	public static MBeanApp MBEAN_APP;
	public static Route DEF_ROUTE;
	public static String APP_NAME = System.getProperty("appdynamics.agent.tierName");
	public static final String SOAP_SERVICE = "SOAP_SERVICE";

	private static final int CONNECTION_TIMEOUT_SEC = 120;

	static final Logger LOG = LogManager.getLogger(Application.class.getName());
	
	//@Override
	public RequestXmlResponse oftB2BAvailabilityRequest(
			com.appdynamics.cxf.commercial_cpm.megaswitch.v1.b2brouting.oftavail.RequestXml soapRequest) {
		log("b2BAvailabilityRequestOFT");
		HttpServletRequest request = (HttpServletRequest) PhaseInterceptorChain.getCurrentMessage().get("HTTP.REQUEST");
		HttpServletResponse response = (HttpServletResponse) PhaseInterceptorChain.getCurrentMessage()
				.get("HTTP.RESPONSE");
		RequestXmlResponse soapResponse = new RequestXmlResponse();
		soapResponse.setRequestXmlResult(new RequestXmlResult());
		soapResponse.getRequestXmlResult().setResponseRoot(new ResponseRoot());
		soapResponse.getRequestXmlResult().getResponseRoot()
				.setAPIVersion(soapRequest.getOInputXml().getOFTB2BAvailabilityRequest().getAPIVersion());
		soapResponse.getRequestXmlResult().getResponseRoot().setSuccess(BigInteger.ONE);
		soapResponse.getRequestXmlResult().getResponseRoot()
				.setDataListRoot(new DataListRoot());
		soapResponse.getRequestXmlResult().getResponseRoot().getDataListRoot().setAvailabilityResponse(new AvailabilityResponse());
		soapResponse.getRequestXmlResult().getResponseRoot().getDataListRoot().getAvailabilityResponse()
				.setAvailabilityList(
						new AvailabilityList());
		Availability availability = new Availability();
		soapResponse.getRequestXmlResult().getResponseRoot().getDataListRoot().getAvailabilityResponse()
				.getAvailabilityList().getAvailability().add(availability);
		try {
			
			request.setAttribute(SOAP_SERVICE, "/commercial-cpm/megaswitch/v1/b2brouting/flight-search");

			route(request, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return soapResponse;
	}

	@Override
	public RequestXmlResponse b2BAvailabilityRequest(RequestXml soapRequest) {
		log("b2BAvailabilityRequest");
		HttpServletRequest request = (HttpServletRequest) PhaseInterceptorChain.getCurrentMessage().get("HTTP.REQUEST");
		HttpServletResponse response = (HttpServletResponse) PhaseInterceptorChain.getCurrentMessage()
				.get("HTTP.RESPONSE");
		RequestXmlResponse soapResponse = new RequestXmlResponse();
		soapResponse.setRequestXmlResult(new RequestXmlResponse.RequestXmlResult());
		soapResponse.getRequestXmlResult().setResponseRoot(new RequestXmlResponse.RequestXmlResult.ResponseRoot());
		soapResponse.getRequestXmlResult().getResponseRoot()
				.setAPIVersion(soapRequest.getOInputXml().getB2BAvailabilityRequest().getAPIVersion());
		soapResponse.getRequestXmlResult().getResponseRoot().setSuccess(BigInteger.ONE);
		soapResponse.getRequestXmlResult().getResponseRoot()
				.setDataListRoot(new RequestXmlResponse.RequestXmlResult.ResponseRoot.DataListRoot());
		soapResponse.getRequestXmlResult().getResponseRoot().getDataListRoot().setAvailabilityResponse(
				new RequestXmlResponse.RequestXmlResult.ResponseRoot.DataListRoot.AvailabilityResponse());
		soapResponse.getRequestXmlResult().getResponseRoot().getDataListRoot().getAvailabilityResponse()
				.setAvailabilityList(
						new RequestXmlResponse.RequestXmlResult.ResponseRoot.DataListRoot.AvailabilityResponse.AvailabilityList());
		RequestXmlResponse.RequestXmlResult.ResponseRoot.DataListRoot.AvailabilityResponse.AvailabilityList.Availability availability = new RequestXmlResponse.RequestXmlResult.ResponseRoot.DataListRoot.AvailabilityResponse.AvailabilityList.Availability();
		soapResponse.getRequestXmlResult().getResponseRoot().getDataListRoot().getAvailabilityResponse()
				.getAvailabilityList().getAvailability().add(availability);
		try {
			
			request.setAttribute(SOAP_SERVICE, "/commercial-cpm/megaswitch/v1/b2brouting/flight-search");

			route(request, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return soapResponse;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Routes.class);
			Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			ROUTES = (Routes) jaxbUnMarshaller.unmarshal(this.getClass().getResourceAsStream("/routing.xml"));
			for (Route route : ROUTES.routes) {
				System.out.println("All Routes #############: "+route.url);
			}
			System.out.println();
			// jaxbMarshaller.marshal(ROUTES, System.out);
			DEF_ROUTE = ROUTES.routes.stream().filter(route -> {
				return route.isDefaultRoute;
			}).findAny().get();

			jaxbContext = JAXBContext.newInstance(MBeans.class);
			jaxbUnMarshaller = jaxbContext.createUnmarshaller();
			jaxbMarshaller = jaxbContext.createMarshaller();

			MBEANS = (MBeans) jaxbUnMarshaller.unmarshal(this.getClass().getResourceAsStream("/mbeans.xml"));
			// jaxbMarshaller.marshal(MBEANS, System.out);
			registerMbeans();

			// Initialize sqlite schema

			jaxbContext = JAXBContext.newInstance(DBs.class);
			jaxbUnMarshaller = jaxbContext.createUnmarshaller();
			jaxbMarshaller = jaxbContext.createMarshaller();

			DATABASES = (DBs) jaxbUnMarshaller.unmarshal(this.getClass().getResourceAsStream("/dbs.xml"));

			Class.forName("org.sqlite.JDBC");

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static boolean isSoap(HttpServletRequest request) {
		return (request.getAttribute(SOAP_SERVICE) != null);
	}

	private static String getRequestURI(HttpServletRequest request) {
		return isSoap(request) ? request.getAttribute(SOAP_SERVICE).toString() : request.getRequestURI() ;
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
					} else if (mbean.type.equals("Route")) {
						server.registerMBean(new com.appdynamics.test.CamelRoute(mbean), objName);
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
		// System.out.println(APP_NAME + ": " +
		// Arrays.stream(objects).map(Object::toString).collect(Collectors.joining("
		// ")));
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("############ IN SERVICE");
		route(req, resp);
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	protected static final boolean serveMe(Step step, Serve serve) {
		int rnd = new Random().nextInt(100);
		log((Object) rnd, " ", serve.load);
		return rnd < serve.load;
	}

	protected static final boolean serveMe(Step step, MethodWrapper methodWrapper) {
		int rnd = new Random().nextInt(100);
		log((Object) rnd, " ", methodWrapper.load);
		return rnd < methodWrapper.load;
	}

	protected void route(HttpServletRequest req, HttpServletResponse resp) throws ClientProtocolException, IOException {
		System.out.println("############ IN ROUTE");
		Routes.Route matchingRoute = ROUTES.routes.stream()
				.filter(route -> {
			return getRequestURI(req).
					endsWith(route.url) 
					&& route.nodes.stream()
					.anyMatch(node -> {
				return node.name.equals(APP_NAME);
			});
		}).findFirst().orElse(DEF_ROUTE);

		if (matchingRoute != null) {
			System.out.println("matchingRoute URL = "+matchingRoute.url);
			System.out.println("matchingRoute NODE = "+matchingRoute.nodes.get(0).name);
			System.out.println(matchingRoute.nodes.get(0).steps);
			// System.out.println(matchingRoute.nodes.get(0).steps.stream().findAny().get().http.get(0).url);
			List<Step> list = matchingRoute.nodes.get(0).steps;
			if (list != null && list.size() > 0) {
				for (Step step : list) {
					for (Http http : step.http) {
						System.out.println("HTTP URL = ########### "+http.url);
					}
				}
			}else {
				System.out.println("Step list = : "+list );
			}
		} else {
			System.out.println(matchingRoute);
		}
		
		if (matchingRoute.isDefaultRoute) {
			log("Switching to default route");
			serve(req, resp, matchingRoute.nodes.get(0).steps.get(0).serve.get(0));
		} else {

			MethodWrapperCallback callback = new MethodWrapperCallback() {

				StopWatch timer = null;
				MeanCalc calc = new MeanCalc();
				volatile boolean timerStarted = false;

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

				@Override
				public StopWatch startTimer() {
					timer = new StopWatch();
					timer.start();
					timerStarted = true;
					return timer;
				}

				@Override
				public StopWatch stopTimer() {
					if (timer != null && isTimerStarted()) {
						timer.stop();
					}
					return timer;
				}

				@Override
				public StopWatch getTimer() {
					return timer;
				}

				@Override
				public boolean isTimerStarted() {
					return timerStarted;
				}

				@Override
				public MeanCalc getMeanCalc() {
					return calc;
				}

			};

			Node matchingNode = matchingRoute.nodes.stream().filter(node -> {
				return node.name.equals(APP_NAME);
			}).findFirst().get();

			log("Found matching route " + matchingRoute.url + " for node " + matchingNode.name);
			try {
				matchingNode.steps.forEach(step -> {
					step.error.forEach(error -> {
						error(req, resp, step, error);
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
						if (step.serve != null && !step.serve.isEmpty()) {
							Serve srv = step.serve.stream().sorted(new Comparator<Serve>() {
								@Override
								public int compare(Serve o1, Serve o2) {
									return o1.load <= o2.load ? -1 : 1;
								}
							}).filter(serve -> {
								return serveMe(step, serve);
							}).findFirst().orElse(step.serve.get(0));
							serve(req, resp, srv);
						}

						if (step.methodWrapper != null && !step.methodWrapper.isEmpty()) {

							MethodWrapper methodWrapper = step.methodWrapper.stream()
									.sorted(new Comparator<MethodWrapper>() {
										@Override
										public int compare(MethodWrapper o1, MethodWrapper o2) {
											return o1.load <= o2.load ? -1 : 1;
										}
									}).filter(mWrapper -> {
										return serveMe(step, mWrapper);
									}).findFirst().orElse(step.methodWrapper.get(0));

							if (methodWrapper.name.equals("updateMbeanStats")) {

								updateMbeanStats(req, resp, step, methodWrapper, callback);

							}
						}
					} else {

						step.serve.forEach(serve -> {
							serve(req, resp, serve);
						});

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
							updateMbeanStats(req, resp, step, method, null);
						});

						step.methodWrapper.stream().filter(method -> {
							return method.name.equals("updateMbeanStats");
						}).forEach(method -> {
							updateMbeanStats(req, resp, step, method, callback);
						});

					}

				});

			} catch (ForcedException exc) {
				log("Exception occured:", exc);
				throw exc;
			}

		}
	}

	public static class MeanCalc {
		public Long minTime;
		public Long maxTime;

		public Long getMean() {
			return (Long) minTime == null || maxTime == null ? Long.MIN_VALUE : ((maxTime - minTime / 2) + minTime);
		}
	}

	public static interface MethodWrapperCallback {

		public MeanCalc getMeanCalc();

		public StopWatch startTimer();

		public StopWatch stopTimer();

		public StopWatch getTimer();

		public boolean isTimerStarted();

		public void execute(HttpServletRequest req, HttpServletResponse resp, Http http);

		public void execute(HttpServletRequest req, HttpServletResponse resp, Serve serve);

		public void execute(HttpServletRequest req, HttpServletResponse resp, Delay delay);
	}

	private void updateMbeanStats(HttpServletRequest req, HttpServletResponse resp, Step step, Method method,
			MethodWrapperCallback callback) {
		{
			if (MBEAN_APP != null && MBEAN_APP.mbean != null)
				MBEAN_APP.mbean.stream().filter(mbean -> {
					ObjectName objName = null;
					try {
						objName = new ObjectName(mbean.objectName);
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
					return method.queueName != null && !method.queueName.isEmpty()
							&& ((mbean.type.equals("Queue")
									&& method.queueName.equals(objName.getKeyProperty("destinationName")))
									|| (mbean.type.equals("Route")
											&& method.queueName.equals(objName.getKeyProperty("name"))));
				}).forEach(mbean -> {
					mbean.attribute.stream().filter(attr -> {
						return attr.attrType != null
								&& (attr.attrType.equals("increment") || attr.attrType.equals("transient"));
					}).forEach(attr -> {
						incrementCounter(req, resp, mbean, attr, method);
					});
					mbean.attribute.stream().filter(attr -> {
						return attr.attrType != null && (attr.attrType.equals("max-time")
								|| attr.attrType.equals("min-time") || attr.attrType.equals("mean-time"));
					}).forEach(attr -> {
						callback.startTimer();
					});
					mbean.attribute.stream().filter(attr -> {
						return attr.valueFromHeader != null;
					}).forEach(attr -> {
						updateCounterWithValueFromHeader(req, resp, mbean, attr);
					});
				});
		}
		boolean failed = false;
		try {
			MethodWrapper mWrap = (MethodWrapper) method;
			try {
				mWrap.error.forEach(error -> {
					error(req, resp, step, error);
				});
			} catch (ForcedException e) {
				failed = true;
				throw e;
			}
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
			mWrap.sql.select.forEach(select -> {
				try {
					executeSelectSql(req, resp, mWrap.sql, select);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		} finally {
			final boolean incrementFailedCount = failed;
			{
				if (MBEAN_APP != null && MBEAN_APP.mbean != null)
					MBEAN_APP.mbean.stream().filter(mbean -> {
						ObjectName objName = null;
						try {
							objName = new ObjectName(mbean.objectName);
						} catch (Exception e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
						return method.queueName != null && !method.queueName.isEmpty()
								&& ((mbean.type.equals("Queue")
										&& method.queueName.equals(objName.getKeyProperty("destinationName")))
										|| (mbean.type.equals("Route")
												&& method.queueName.equals(objName.getKeyProperty("name"))));
					}).forEach(mbean -> {
						mbean.attribute.stream().sorted(new Comparator<MBAttribute>() {
							@Override
							public int compare(MBAttribute o1, MBAttribute o2) {
								return o1.attrType == null || o2.attrType == null ? 0
										: o1.attrType.equals("mean-time") ? 1 : o2.attrType.equals("mean-time") ? 1 : 0;
							}
						}).filter(attr -> {
							return attr.attrType != null && (attr.attrType.equals("max-time")
									|| attr.attrType.equals("min-time") || attr.attrType.equals("mean-time"));
						}).forEach(attr -> {
							updateTimes(req, resp, mbean, attr, method);
						});
						if (incrementFailedCount) {
							mbean.attribute.stream().filter(attr -> {
								return attr.attrType != null && attr.attrType.equals("error");
							}).forEach(attr -> {
								incrementCounter(req, resp, mbean, attr, method);
							});
						} else {
							mbean.attribute.stream().filter(attr -> {
								return attr.attrType != null && attr.attrType.equals("transient");
							}).forEach(attr -> {
								decrementCounter(req, resp, mbean, attr, method);
							});
						}
					});
			}
		}
	}

	private void executeSelectSql(HttpServletRequest req, HttpServletResponse resp, SQL sql, String select) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + Database.getDBPath(DATABASES) + "/" + sql.db);
			statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery(select);
			log("Select sql:", select, " executed successfully against", sql.db);
			System.out.println("Select SQL  ############= "+select);
			
			StringBuilder table = new StringBuilder();
			table.append("<html>" +
			           "<body>" +
			           "<table border=\"1\">" +
			           "<tr>" +
			           "<th>FLIGHTS</th>" +
			           "</tr>");
			while (rs.next()) {
				log(sql.name, rs.getInt(2));
				System.out.println("Result from DB ############= "+rs.getInt(2));
				table.append("<tr><td>")
			       .append(rs.getString(2))
			       .append("</td><td>");
			}
			table.append("</table>" +
			           "</body>" +
			           "</html>");
			String html = table.toString();
			Serve serve = new Serve();
			serve.payload = html;
			serve(req, resp, serve);

		} catch (Exception e) {
			e.printStackTrace();
			throw new UnForcedException();
		} finally {
			try {
				if (!connection.isClosed()) {
					statement.close();
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new UnForcedException();
			}
		}

	}

	private void updateTimes(HttpServletRequest req, HttpServletResponse resp, MBean mbean, MBAttribute attr,
			Method method) {
		final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		log("Update times for ", mbean.objectName, " ", attr.attrType);
		try {
			if (attr.attrType != null && (attr.attrType.equals("max-time") || attr.attrType.equals("min-time")
					|| attr.attrType.equals("mean-time")) && method instanceof MethodWrapperCallback) {
				MethodWrapperCallback mWrapper = (MethodWrapperCallback) method;
				ObjectName objName = new ObjectName(mbean.objectName);
				Integer timeAttr = (Integer) server.getAttribute(objName, attr.name);
				if (mWrapper.isTimerStarted()) {
					mWrapper.stopTimer();
				}

				if (attr.attrType.equals("max-time")) {
					long elapsedTime = mWrapper.getTimer().getTime();
					mWrapper.getMeanCalc().maxTime = elapsedTime;
					if (elapsedTime > timeAttr) {
						server.setAttribute(objName, new Attribute(attr.name, elapsedTime));
					}
				} else if (attr.attrType.equals("min-time")) {
					long elapsedTime = mWrapper.getTimer().getTime();
					mWrapper.getMeanCalc().minTime = elapsedTime;
					if (elapsedTime < timeAttr) {
						server.setAttribute(objName, new Attribute(attr.name, elapsedTime));
						mWrapper.getMeanCalc().maxTime = elapsedTime;
					}
				} else if (attr.attrType.equals("mean-time")) {
					Long mean = mWrapper.getMeanCalc().getMean();
					if (mean > timeAttr) {
						server.setAttribute(objName, new Attribute(attr.name, mean));
					}
				}
			}

		} catch (AttributeNotFoundException | ReflectionException | MBeanException | InstanceNotFoundException
				| MalformedObjectNameException | InvalidAttributeValueException e) {
			throw new RuntimeException(e);
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
			updateCounter = updateCounter > 0 ? updateCounter - 1 : 0;
			log("Counter " + attr.name + " for object key:" + objName.getKeyProperty(mbean.nameKey) + " "
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
			log("Counter " + attr.name + " for object key:" + objName.getKeyProperty(mbean.nameKey) + " "
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

	private void error(HttpServletRequest req, HttpServletResponse resp, Step step, Error error)
			throws ForcedException {
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
			long injectDelay = 0;
			if (delay.random != null) {
				injectDelay = (delay.msec * new java.util.Random().nextInt(delay.random));
			} else if (delay.gaussMean != null) {
				injectDelay = (long) (delay.msec
						* Math.abs((new java.util.Random().nextGaussian() * delay.gaussDeviation) + delay.gaussMean));
			}
			log("Delay: " + injectDelay + " ms");
			Thread.sleep(injectDelay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private HttpClient sharedClient = null;

	private HttpClient getHttpClient(String host, int port) {
		if (sharedClient == null) {
			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
			// Increase max total connection to 200
			cm.setMaxTotal(200);
			// Increase default max connection per route to 20
			cm.setDefaultMaxPerRoute(20);
			// Increase max connections for localhost:80 to 50
			HttpHost localhost = new HttpHost(host, port);
			cm.setMaxPerRoute(new HttpRoute(localhost), 50);

			sharedClient = HttpClients.custom().setConnectionManager(cm)
					.setConnectionTimeToLive(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS).build();
		}
		return sharedClient;
	}

	private void proxy(HttpServletRequest req, HttpServletResponse resp, Http http)
			throws ClientProtocolException, IOException {

		String url = http.url;
		log("Proxy", url);

		URL url1 = new URL(url);
		HttpClient client = getHttpClient(url1.getHost(), url1.getPort());

		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", "Test");
		boolean logHeaders = false;
		for (Enumeration<String> headers = req.getHeaderNames(); headers.hasMoreElements();) {
			String headerName = headers.nextElement();
			if (logHeaders)
				log(">", headerName, req.getHeader(headerName));
			if (!headerName.toLowerCase().equals("content-length")) {
				request.addHeader(headerName, req.getHeader(headerName));
			}

		}

		HttpResponse response = null;
		try {
			response = client.execute(request);

			log("Response Code : ", response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() != 200) {
				LOG.error("HTTP error " + response.getStatusLine().getStatusCode() + " received from " + url);
			}
			if (!isSoap(req)){
				resp.setStatus(HttpServletResponse.SC_OK);
			}

			try {

				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}

				if (!isSoap(req)) {
					PrintWriter wrtr = resp.getWriter();
					Arrays.stream(response.getAllHeaders()).forEach(downHeader -> {
						if (!downHeader.getName().toLowerCase().equals("content-length")
								&& !downHeader.getName().toLowerCase().equals("transfer-encoding")
								&& !downHeader.getName().toLowerCase().equals("date")) {
							if (logHeaders)
								log("<", downHeader.getName(), downHeader.getValue());
							resp.addHeader(downHeader.getName(), downHeader.getValue());

						}
					});

					wrtr.print(result.toString());
					wrtr.flush();
				}
			} catch (Exception e) {
				log(e);
				throw e;
			}
		} finally {
			request.releaseConnection();
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
				// log("<", headerName, req.getHeader(headerName));
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
