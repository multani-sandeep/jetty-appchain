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
		@XmlAttribute
		public boolean weighted;
		@XmlElement (name= "http")
		public List<Http> http = new ArrayList<Http>();
		@XmlElement (name= "serve")
		public List<Serve> serve = new ArrayList<Serve>();
		@XmlElement (name= "delay")
		public List<Delay> delay = new ArrayList<Delay>();
		@XmlElement (name= "error")
		public List<Error> error = new ArrayList<Error>();
		@XmlElement (name= "method")
		public List<Method> method = new ArrayList<Method>();
		@XmlElement (name= "methodwrapper")
		public List<MethodWrapper> methodWrapper = new ArrayList<MethodWrapper>();
	}
	
	@XmlAccessorType (XmlAccessType.FIELD)
	public static class MethodWrapper extends Method{
		
		@XmlElement (name= "http")
		public List<Http> http = new ArrayList<Http>();
		@XmlElement (name= "serve")
		public List<Serve> serve = new ArrayList<Serve>();
		@XmlElement (name= "delay")
		public List<Delay> delay = new ArrayList<Delay>();
		@XmlElement (name= "error")
		public List<Error> error = new ArrayList<Error>();
		@XmlElement (name= "sql")
		public SQL sql = new SQL();
	}
	
	@XmlAccessorType (XmlAccessType.FIELD)
	public static class SQL{
		@XmlAttribute
		public String name;
		@XmlAttribute
		public String db;
		@XmlElement (name="select")
		public List<String> select = new ArrayList<String>();
		
	}
	
	@XmlAccessorType (XmlAccessType.FIELD)
	public static class Error{
		@XmlAttribute
		public String type;
		@XmlAttribute (name="errsperhundred")
		public Integer errsPerHundred;
		@XmlElement (name="errortype")
		public ErrorType errorType;
	}
	
	@XmlAccessorType (XmlAccessType.FIELD)
	public static class ErrorType{
		@XmlAttribute
		public String type;
		@XmlElement
		public String message;
	}
	
	
	@XmlAccessorType (XmlAccessType.FIELD)
	public static class Delay{
		@XmlAttribute
		public Integer msec;
		@XmlAttribute
		public Integer random;
		@XmlAttribute (name="g-mean")
		public Integer gaussMean;
		@XmlAttribute (name="g-deviation")
		public Integer gaussDeviation;
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
		@XmlAttribute
		public Integer load;
	}
	
	@XmlAccessorType (XmlAccessType.FIELD)
	public static class Method{
		@XmlAttribute
		public String name;
		
		@XmlAttribute
		public Integer load;
		
		@XmlAttribute
		public String queueName;
		
		@XmlElement(name="param")
		public List<Param> param;
	}
	
	@XmlAccessorType (XmlAccessType.FIELD)
	public static class Param{
		@XmlAttribute
		public String name;
		@XmlAttribute
		public String from;
		@XmlAttribute
		public String key;
		@XmlAttribute
		public String value;
		@XmlAttribute
		public Integer median;
		@XmlAttribute
		public Integer deviation;
	}
}
