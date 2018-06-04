package com.appdynamics.test;

import java.util.List;

public class Routes {
	public List<Route> routes;
	
	public static class Route{
		public String url;
		public Boolean defaultRoute;
		public List<Node> nodes;
	}
	
	public static class Node{
		public String name;
		public List<Step> steps;
	}
	
	public static class Step{
		public int sequence;
		public List<Http> https;
	}
	
	public static class Http{
		public String url;
		public String payload;
	}
}
