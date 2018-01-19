package com.roadyo.passenger.pojo;

public class ValidVerificationCodeResponse
{
	
	/*
	 {"errNum":"109","errFlag":"0","errMsg":"Phone verified.","test":"1"} 
	 */
	
	String errNum,errFlag,errMsg,test;

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

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
	
	

}
