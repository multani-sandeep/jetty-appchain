package com.appdynamics.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

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

public class Application extends HttpServlet {

	public static Routes ROUTES = new Routes();

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {

			//File file = this.getClass().getResnew File("routing.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Routes.class);
			Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			System.out.println(this.getClass().getResourceAsStream("routing.xml"));
			jaxbUnMarshaller.unmarshal(this.getClass().getResourceAsStream("routing.xml"));
			jaxbMarshaller.marshal(ROUTES, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void log(Object... objects) {
		Arrays.stream(objects).forEach(obj -> {
			System.out.println(System.getProperty("app") + " : " + obj);
		});
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

		proxyOrServe(req, resp);
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	protected void proxyOrServe(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if ("server".equals(System.getProperties().getProperty("type"))) {
			serve(req, resp);
		} else {
			proxy(req, resp);
		}
	}

	private void proxy(HttpServletRequest req, HttpServletResponse resp) throws ClientProtocolException, IOException {
		log("Proxy");
		String url = "http://localhost:" + System.getProperties().getProperty("down_port") + "/" + req.getRequestURI();

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", "Test");
		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		PrintWriter wrtr = resp.getWriter();
		wrtr.print(result.toString());
		wrtr.flush();

	}

	private void serve(HttpServletRequest req, HttpServletResponse resp) {
		log("Serve");

	}

}
