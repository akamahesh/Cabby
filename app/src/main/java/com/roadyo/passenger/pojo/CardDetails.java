package com.roadyo.passenger.pojo;

import java.io.Serializable;

public class CardDetails implements Serializable
{
/*	 		"id": "card_103e8Q2bDfXaXQObckX8jJXr",
            "last4": "4242",
            "type": "Visa",
            "exp_month": 6,
            "exp_year": 2017*/
	
	String type;
	String last4;
	String exp_month;
	String exp_year;
	
	String id;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLast4() {
		return last4;
	}
	public void setLast4(String last4) {
		this.last4 = last4;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getExp_month() {
		return exp_month;
	}
	public void setExp_month(String exp_month) {
		this.exp_month = exp_month;
	}
	public String getExp_year() {
		return exp_year;
	}
	public void setExp_year(String exp_year) {
		this.exp_year = exp_year;
	}
	
	
	
}
