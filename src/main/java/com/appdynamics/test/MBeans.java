package com.appdynamics.test;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="mbeans")
@XmlAccessorType (XmlAccessType.FIELD)
public class MBeans {
	
	@javax.xml.bind.annotation.XmlElement
	public List<MBeanApp> application;
	
	@XmlAccessorType (XmlAccessType.FIELD)
	public static class MBeanApp{
		
		@XmlAttribute
		public String name;
		
		
		
		@javax.xml.bind.annotation.XmlElement
		public List<MBean> mbean;
		
	}
	
	@XmlAccessorType (XmlAccessType.FIELD)
	public static class MBean{
		
		@XmlAttribute
		public String type;
		
		@XmlAttribute
		public String objectName;
		
		@javax.xml.bind.annotation.XmlElement
		public List<MBAttribute> attribute;
	}
	
	@XmlAccessorType (XmlAccessType.FIELD)
	public static class MBAttribute{
		@XmlAttribute
		public String name;
		
		@XmlAttribute
		public String type;
		
		@XmlAttribute
		public String attrType;
		
		@XmlAttribute
		public String startingValue;
		
		@XmlAttribute
		public String valueFromHeader;
	}

}
