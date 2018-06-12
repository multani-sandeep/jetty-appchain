package com.appdynamics.test;

import java.util.ArrayList;
import java.util.List;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.ReflectionException;

import com.appdynamics.test.MBeans.MBean;

public class Queue extends JMXBean {

	public Queue(MBean mbean) {
		mbean.attribute.forEach(attr -> {
			try {
				map.put(attr.name, Class.forName(attr.type).getConstructor(String.class).newInstance("0"));

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public Object getAttribute(String attribute)
			throws AttributeNotFoundException, MBeanException, ReflectionException {
		return map.get(attribute);
	}

	@Override
	public void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
		map.put(attribute.getName(), attribute.getValue());

	}

	@Override
	public AttributeList getAttributes(String[] attributes) {
		final List<Attribute> attrList = new ArrayList<>();
		map.keySet().stream().forEach(key -> {
			attrList.add(new Attribute(key, map.get(key)));
		});
		return new AttributeList(attrList);
	}

	@Override
	public AttributeList setAttributes(AttributeList attributes) {
		attributes.asList().stream().forEach(attr -> {
			map.put(attr.getName(), attr.getValue());
		});
		return attributes;
	}

	@Override
	public Object invoke(String actionName, Object[] params, String[] signature)
			throws MBeanException, ReflectionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MBeanInfo getMBeanInfo() {
		List<MBeanAttributeInfo> inf = new ArrayList<>();
		map.entrySet().stream().forEach(entry -> {
			MBeanAttributeInfo attr = new MBeanAttributeInfo(entry.getKey(), (entry.getValue().getClass().equals(Boolean.class)?"boolean":entry.getValue().getClass().getName()),
					 "", true, true, entry.getValue().getClass().getName().toLowerCase().endsWith("boolean"), null);
			inf.add(attr);
		});
		return new MBeanInfo(Queue.class.getName(), "", inf.toArray(new MBeanAttributeInfo[inf.size()]),
				new MBeanConstructorInfo[0], new MBeanOperationInfo[0], new MBeanNotificationInfo[0]);
	}

}
