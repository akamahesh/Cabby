package com.roadyo.passenger.pojo;


/*{
    "errNum":"21",
    "errFlag":"0",
    "errMsg":"Got the details!",
    "dis":"6.6 km",
    "fare":"119.448",
    "curDis":"1 m",
    "t":{},
    "t1":{},
    "ar":{}
}*/

public class FareCalculation 
{
	String errNum;
	String errFlag;
	String errMsg;
	String dis;
	String fare;
	String curDis;
	public String getErrNum() {
		return errNum;
	}
	public String getErrFlag() {
		return errFlag;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public String getDis() {
		return dis;
	}
	public String getFare() {
		return fare;
	}
	public String getCurDis() {
		return curDis;
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
	public void setDis(String dis) {
		this.dis = dis;
	}
	public void setFare(String fare) {
		this.fare = fare;
	}
	public void setCurDis(String curDis) {
		this.curDis = curDis;
	}
	
}
