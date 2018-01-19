package com.roadyo.passenger.main;

import java.util.ArrayList;

public class SignUpResponse 
{

/*{
"errNum":"5",
"errFlag":"0",
"errMsg":"Signup completed!",
"token":"333532313336303632363633383733TXNe2SvraTCHTTXNe2SvraTCHTKHPwdm8KHPwdm8",
"expiryLocal":"2014-10-09 12:50:26",
"expiryGMT":"2014-10-09 11:50:26",
"email":"abc@def.com",
"flag":"1",
"joined":"2014-09-09 12:50:26",
"apiKey":"2ee51574176b15c2e",
"card":[
{
"errFlag":"1",
"errMsg":"Card not added",
"errNum":"16"
}
],
"mail":"0",
"types":[
{},
{}
],
"serverChn":"freetaxi",
"chn":"qp_352136062663873"
*/
	
	String errNum;
	String errFlag;
	String errMsg;
	String token;
	String expiryLocal;
	String expiryGMT;
	String email;
	String flag;
	String joined;
	String apiKey;
	String pub;

	public String getStipeKey() {
		return stipeKey;
	}

	public void setStipeKey(String stipeKey) {
		this.stipeKey = stipeKey;
	}

	String stipeKey;

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getPub() {
		return pub;
	}

	public void setPub(String pub) {
		this.pub = pub;
	}

	String sub;

	public String getPresenseChn() {
		return presenseChn;
	}

	public void setPresenseChn(String presenseChn) {
		this.presenseChn = presenseChn;
	}

	String presenseChn;
	String chn;
	String serverChn;
	String coupon;
	ArrayList<BookAppointmentResponse> card;
	 
	
	public ArrayList<BookAppointmentResponse> getCard() {
		return card;
	}
	public void setCard(ArrayList<BookAppointmentResponse> card) {
		this.card = card;
	}
	public String getChn() {
		return chn;
	}
	public void setChn(String chn) {
		this.chn = chn;
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
	public String getToken() {
		return token;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getServerChn() {
		return serverChn;
	}
	public String getCoupon() {
		return coupon;
	}
}
