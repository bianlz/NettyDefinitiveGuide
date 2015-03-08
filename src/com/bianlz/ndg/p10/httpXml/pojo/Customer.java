package com.bianlz.ndg.p10.httpXml.pojo;

import java.util.List;

public class Customer {
	private long customerNumber;
	private String firstName;
	private String lastName;
	private List<String> mideleNames;
	public long getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(long customerNumber) {
		this.customerNumber = customerNumber;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public List<String> getMideleNames() {
		return mideleNames;
	}
	public void setMideleNames(List<String> mideleNames) {
		this.mideleNames = mideleNames;
	}
	
}
