package com.bianlz.ndg.p14.message;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Header {
	private int crcCode = 0xabef0101;
	private int length;
	private long sessionId;
	private byte type;
	private byte priority;
	private Map<String,Object> attachment = new HashMap<String,Object>();
	public int getCrcCode() {
		return crcCode;
	}
	public void setCrcCode(int crcCode) {
		this.crcCode = crcCode;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public long getSessionId() {
		return sessionId;
	}
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public byte getPriority() {
		return priority;
	}
	public void setPriority(byte priority) {
		this.priority = priority;
	}
	public Map<String, Object> getAttachment() {
		return attachment;
	}
	public void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = "";
		Field[] field = this.getClass().getDeclaredFields();
		for(Field f:field){
			try {
				str += " ,[ " +f.getName()+" : " + f.get(this) +" ] ";
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!"".equals(str)){
			str.replaceFirst(",", "");
		}
		return str;
	}
}
