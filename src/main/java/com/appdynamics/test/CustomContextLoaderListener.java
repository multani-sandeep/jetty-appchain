package com.appdynamics.test;

import java.util.Arrays;

import javax.servlet.ServletContextEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

public class CustomContextLoaderListener extends ContextLoaderListener {
	
	static final Logger LOG = LogManager.getLogger(CustomContextLoaderListener.class.getName());
	public CustomContextLoaderListener() {
	}

	public CustomContextLoaderListener(WebApplicationContext context) {
		super(context);
	}

	public void contextInitialized(ServletContextEvent event) {
		
		if(event.getServletContext().getInitParameter("enableCXFForCSV")!=null){
			final String appName = System.getProperty("appdynamics.agent.tierName");
			String csvToEnable= (String)event.getServletContext().getInitParameter("enableCXFForCSV");
			if(!Arrays.asList(csvToEnable.split(",")).stream().anyMatch(toEnable ->{ return toEnable.equals(appName);})){
				LOG.warn("Not starting CXF context for App: "+appName+". To enable add app-name enableCXFForCSV in web.xml");
				return;
			}
			
		}
		
		
		initWebApplicationContext(event.getServletContext());
	}

	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
	}
}