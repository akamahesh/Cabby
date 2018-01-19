package com.roadyo.passenger.main;

public class BookAppointmentResponse {
	
/*	{
	    "errNum": "39",
	    "errFlag": "0",
	    "errMsg": "Request submitted, you will get a confirmation message when doctor responds!",
	    "test": {
	        "insEnt": [
	            {
	                "36": {
	                    "$id": "530dc4b54176b1641100000e"
	                }
	            }
	        ],
	        "errNum": 44
	    }
	}*/
	
	
	
	String errNum;
	String errFlag;
	String errMsg;

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


	
	
}
