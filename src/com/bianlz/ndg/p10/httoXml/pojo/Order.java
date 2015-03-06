package com.bianlz.ndg.p10.httoXml.pojo;

public class Order {
	private Long orderNumber;
	private Customer customer;
	private	Address billTo;
	private Float total;
	private Shipping shipping;
	public Long getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Address getBillTo() {
		return billTo;
	}
	public void setBillTo(Address billTo) {
		this.billTo = billTo;
	}
	public Float getTotal() {
		return total;
	}
	public void setTotal(Float total) {
		this.total = total;
	}
	public Shipping getShipping() {
		return shipping;
	}
	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}
	
}
