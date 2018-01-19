package com.roadyo.passenger.pubnu.pojo;

import java.util.ArrayList;

public class DriversAroundYouResponse {
	

	/*
	    {
		"errNum":"101",
		"flag":0,
		"errFlag":0,
		"type":3,
		"docs":[
		{
		"image":"",
		"lon":77.58965,
		"rating":0,
		"email":"surender@3embed.com",
		"lat":13.02889,
		"name":"Surender"
		}
		],
		"a":1,
		"errMsg":"Drivers found!"
		}
	*/
	
	
	
	String errNum;
	String flag;
	String errFlag;
	String errMsg;
	String type;
	String a;
	ArrayList<DriverDetailsAroundYou> docs;
	
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getFlag() 
	{
		return flag;
	}
	public void setFlag(String flag) 
	{
		this.flag = flag;
	}
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
	public ArrayList<DriverDetailsAroundYou> getDocs() {
		return docs;
	}
	public void setDocs(ArrayList<DriverDetailsAroundYou> docs) {
		this.docs = docs;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	

}
