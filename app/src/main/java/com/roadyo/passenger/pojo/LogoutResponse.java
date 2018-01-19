package com.roadyo.passenger.pojo;

public class LogoutResponse {
	
	 //{"errNum":"29","errFlag":"0","errMsg":"Logged out!","test":55}

	
	String errFlag;
	String errMsg;
	
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
	
	
	
}
