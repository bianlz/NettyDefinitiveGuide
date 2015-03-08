package com.bianlz.ndg.p10.httpXml.pojo;

public class OrderFactory {
	public static Order getOrder(){
		Order order = new Order();
		order.setOrderNumber((long)(Math.random()*10000+1));
		order.setTotal((float)(Math.random()*100+1));
		Address address = new Address();
		address.setCity("BEIJING");
		address.setCountry("CHINA");
		address.setPostCode("10000");
		address.setState("BEIJING");
		address.setStreet1("BEIWU");
		address.setStreet2("BEIWUCHUANGXINYUAN");
		order.setBillTo(address);
		Customer customer = new Customer();
		customer.setCustomerNumber((long)(Math.random()*10000+1));
		customer.setFirstName("ZHANG");
		customer.setLastName("SAN");
		order.setCustomer(customer);
		order.setShipping(Shipping.INTERNATIONAL_MAIL);
		return order;
	}
}
