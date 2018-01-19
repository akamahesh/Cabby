package com.roadyo.passenger.pojo;

import java.io.Serializable;
import java.util.ArrayList;
public class Support_new_pojo implements Serializable
{
  
	private String errNum;
	private String errFlag;
	private String errMsg;
	ArrayList<Support_values> support;
	public String getErrNum() {
		return errNum;
	}
	public void setErrNum(String errNum) {
		this.errNum = errNum;
	}
	public String getErrFlag() {
		return errFlag;
	}
	public void setErrFlag(String errFlag) {
		this.errFlag = errFlag;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public ArrayList<Support_values> getSupport() {
		return support;
	}
	public void setSupport(ArrayList<Support_values> support) {
		this.support = support;
	}
	
}
