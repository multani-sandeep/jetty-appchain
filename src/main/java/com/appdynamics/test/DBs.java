package com.appdynamics.test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "dbs")
@XmlAccessorType(XmlAccessType.FIELD)
public class DBs {
	
	
	@XmlElement(name = "properties", type = Properties.class)
	public Properties config = new Properties();
	
	
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Properties {
		@XmlElement(name = "property", type = Property.class)
		public List<Property> properties = new ArrayList<Property>();
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Property {

		@XmlAttribute(name = "name")
		public String name;

		@XmlAttribute(name = "value")
		public String value;
	}

	@XmlElement(name = "db", type = DB.class)
	public List<DB> dbs = new ArrayList<DB>();

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class DB {
		@XmlAttribute(name = "name")
		public String name;

		@XmlElement(name = "schema")
		public Schema schema;

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Schema {

		@XmlElement(name = "table")
		public List<Table> tables = new ArrayList<Table>();
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Table {

		@XmlAttribute(name = "name")
		public String name;

		@XmlElement(name = "create")
		public String create = "";

		@XmlElement(name = "populate")
		public Populate populator;

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Populate {

		@XmlElement(name = "insert")
		public List<String> inserts = new ArrayList<String>();
	}
}
