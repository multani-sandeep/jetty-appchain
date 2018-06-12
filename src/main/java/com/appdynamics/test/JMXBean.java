package com.appdynamics.test;

import java.util.HashMap;
import java.util.Map;

import javax.management.DynamicMBean;

public abstract class JMXBean implements DynamicMBean{

	protected Map<String, Object> map = new HashMap<>();
	

}
