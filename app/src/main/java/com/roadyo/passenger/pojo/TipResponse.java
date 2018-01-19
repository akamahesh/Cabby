package com.roadyo.passenger.pojo;

import java.io.Serializable;

public class TipResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7075557595076476717L;
	/*{
		"errNum":"98",
		"errFlag":"0",
		"errMsg":"Tip updated, thank you.",
		"fare":"0",
		"tip":"0",
		"amount":"0",
		"discount":"",
		"code":""
		}*/
	
	String errNum,errFlag,errMsg,fare,tip,amount,discount,code;

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

	public String getFare() {
		return fare;
	}

	public void setFare(String fare) {
		this.fare = fare;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
