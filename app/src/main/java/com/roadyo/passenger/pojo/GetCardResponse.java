package com.roadyo.passenger.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class GetCardResponse implements Serializable{
	
	
/*	"errNum": "52",
    "errFlag": "0",
    "errMsg": "Card added!",
    "cards": [
       {
             "type": "Visa",
             "last4": "1234",
             "exp": "0318",
             "id": "54546241353a134530001"
       },
       {
             "type": "Visa",
             "last4": "1234",
             "exp": "0318",
             "id": "54546241353a134530001"
       },{}
    ]
*/
	
	String errNum;
	String errFlag;
	String errMsg;
	String def,walletBal;
	public String getDef() {
		return def;
	}
	public void setDef(String def) {
		this.def = def;
	}
	ArrayList<CardDetails> cards;
	
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

	public String getWalletBal() {
		return walletBal;
	}

	public ArrayList<CardDetails> getCards() {
		return cards;
	}
	public void setCards(ArrayList<CardDetails> cards) {
		this.cards = cards;
	}
	
	
	
}
