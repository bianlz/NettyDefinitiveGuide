package com.bianlz.ndg.p14.message;


public class NettyMessage {
	private Header header;
	private Object body;
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "message header : [ "+header+" ]";
	}
	
	
}
