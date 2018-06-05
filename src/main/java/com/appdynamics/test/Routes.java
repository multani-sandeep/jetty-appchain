package com.appdynamics.test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement (name="routes")
@XmlAccessorType (XmlAccessType.FIELD)
public class Routes {
	
	@XmlElement (name="route", type=Route.class) 
	public List<Route> routes = new ArrayList<Route>();
	
	@XmlAccessorType (XmlAccessType.FIELD)
	public static class Route{
		@XmlAttribute
		public String url;
		@XmlAttribute (name="default")
		public boolean isDefaultRoute = false;
		
		@XmlElement (name="node")
		public List<Node> nodes = new ArrayList<Node>();
		
	}
	
	@XmlAccessorType (XmlAccessType.FIELD)
	public static class Node{
		@XmlAttribute
		public String name;
		@XmlElement (name= "step")
		public List<Step> steps = new ArrayList<Step>();
	}
	
	@XmlAccessorType (XmlAccessType.FIELD)
	public static class Step{
		@XmlAttribute
		public int sequence;
		@XmlElement (name= "http")
		public List<Http> http = new ArrayList<Http>();
		@XmlElement (name= "serve")
		public List<Serve> serve = new ArrayList<Serve>();
	}
	
	@XmlAccessorType (XmlAccessType.FIELD)
	public static class Http{
		@XmlAttribute
		public String url;
		@XmlElement
		public String payload;
	}
	
	@XmlAccessorType (XmlAccessType.FIELD)
	public static class Serve{
		@XmlElement
		public String payload;
	}
}
