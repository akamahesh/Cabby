package com.roadyo.passenger.pojo;

public class LiveBookingResponse {
	
	/*{
		"errNum":"39",
		"errFlag":"0",
		"errMsg":"Request submitted, you will get a confirmation message when driver responds!",
		"email":"driverv@gmail.com",
		"apptDt":"2014-07-10 13:00:47",
		"dt":"20140710130047",
		"model":"BMW M sedan",
		"plateNo":"ka 04 ev 1966",
		"rating":"3.6",
		}*/
	
	/*{

    "errNum":"39",
    "errFlag":"0",
    "errMsg":"Request submitted, you will get a confirmation message when driver responds!",
    "email":"varun@mobifyi.com",
    "apptDt":"2014-09-06 19:56:10",
    "dt":"20140906195610",
    "model":"BMW M sedan",
    "plateNo":"KA-12 2141",
    "rating":"0",
    "t":{},
    "chn":"qd_352136062663873",
    "bid":"6"

}*/
	String errNum,errFlag,errMsg,dt,rating;
	String email,apptDt,model,plateNo,chn,bid;

	public String getErrNum() {
		return errNum;
	}

	public String getErrFlag() {
		return errFlag;
	}

	public String getErrMsg() {
		return errMsg;
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

	public String getEmail() {
		return email;
	}

	public String getApptDt() {
		return apptDt;
	}

	public String getModel() {
		return model;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setApptDt(String apptDt) {
		this.apptDt = apptDt;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public String getDt() {
		return dt;
	}

	public String getRating() {
		return rating;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getChn() {
		return chn;
	}

	public void setChn(String chn) {
		this.chn = chn;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}
	
}
