package com.roadyo.passenger.pojo;

public class GetCarDetails
{
	
	/*{
	    "errNum":"21",
	    "errFlag":"0",
	    "errMsg":"Got the details!",
	    "model":"BMW M sedan",
	    "plateNo":"ka 04 ev 1966"

	}*/
	
	String errNum,errFlag,errMsg,model,plateNo,rating;

	
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getErrNum() {
		return errNum;
	}

	public String getErrFlag() {
		return errFlag;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public String getRating() {
		return rating;
	}

	public void setErrNum(String errNum) {
		this.errNum = errNum;
	}

	public void setErrFlag(String errFlag) {
		this.errFlag = errFlag;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}
}
