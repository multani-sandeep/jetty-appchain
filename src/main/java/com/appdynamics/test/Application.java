package com.appdynamics.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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

import com.appdynamics.test.Routes.Delay;
import com.appdynamics.test.Routes.Error;
import com.appdynamics.test.Routes.Http;
import com.appdynamics.test.Routes.Node;
import com.appdynamics.test.Routes.Route;
import com.appdynamics.test.Routes.Serve;
import com.appdynamics.test.Routes.Step;

public class Application extends HttpServlet {

	public static Routes ROUTES;
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

		} catch (JAXBException e) {
			e.printStackTrace();
		}
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
		log((Object)rnd);
		return rnd<serve.load;
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
					Serve srv = step.serve.stream().sorted(new Comparator<Serve>() {
						@Override
						public int compare(Serve o1, Serve o2) {
							return o1.load<=o2.load?-1:1;
						}
					}).filter(serve -> {
						return serveMe(step, serve);
					}).findFirst().get();
					serve(req, resp, srv);
					
					step.delay.forEach(delay -> {
						delay(req, resp, delay);
					});

				});
			} catch (ForcedException exc) {
				log("Exception occured:", exc);
				throw exc;
			}

		}
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
			Thread.sleep(delay.msec * new java.util.Random().nextInt(delay.random));
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
