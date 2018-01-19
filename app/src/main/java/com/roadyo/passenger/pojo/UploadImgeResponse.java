package com.roadyo.passenger.pojo;

public class UploadImgeResponse 
{
	/*
	 "picURL": "pics/PAdon201420012030201420472055temp_photo.jpg",
	    "errNum": "17",
	    "errFlag": "0",
	    "errMsg": "Upload Completed!",
	    "writeFlag": 3174*/
	
	
	String picURL;
	String errNum;
	String errFlag;
	String errMsg;
	String writeFlag;
	public String getPicURL() {
		return picURL;
	}
	public void setPicURL(String picURL) {
		this.picURL = picURL;
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
	public String getWriteFlag() {
		return writeFlag;
	}
	public void setWriteFlag(String writeFlag) {
		this.writeFlag = writeFlag;
	}
	
	
	
}
