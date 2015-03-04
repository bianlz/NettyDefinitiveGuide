package com.bianlz.ndg.p7.serializable;

import java.io.Serializable;

public class SubScribeResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int subReqID;
	private int respCode;
	private String desc;
	public int getSubReqID() {
		return subReqID;
	}
	public void setSubReqID(int subReqID) {
		this.subReqID = subReqID;
	}
	public int getRespCode() {
		return respCode;
	}
	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "SubScribeResp [ subReqID = " +this.subReqID 
				+ " , respCode = "+ this.respCode 
				+" , describe = "+ this.desc + "]";
	}
	
	

}
