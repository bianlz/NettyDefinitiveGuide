package com.bianlz.ndg.p10.httpXml.pojo;

import java.io.StringReader;
import java.io.StringWriter;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;

public class TestOrder {
	private IBindingFactory factory = null;
	private StringWriter writer = null;
	private StringReader reader = null;
	private static final String CHARSET="UTF-8";
	public String encode2Xml(Order order)throws Exception{
		factory = BindingDirectory.getFactory(Order.class);
		writer = new StringWriter();
		IMarshallingContext msc = factory.createMarshallingContext();
		msc.setIndent(2);
		msc.marshalDocument(order, CHARSET, null, writer);
		String xmlStr = writer.toString();
		writer.close();
		System.out.println(xmlStr);
		return xmlStr;
	}
	public Order decode2Order(String xmlBody)throws Exception{
		reader = new StringReader(xmlBody);
		if(factory==null){
			factory = BindingDirectory.getFactory(Order.class);
		}
		IUnmarshallingContext umsc = factory.createUnmarshallingContext();
		Order order = (Order)umsc.unmarshalDocument(reader);
		return order;
	}
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		TestOrder test = new TestOrder();
		Order order = OrderFactory.getOrder();
		String body = test.encode2Xml(order);
		Order order2 = test.decode2Order(body);
		System.out.println(order2.getOrderNumber());
	}

}
