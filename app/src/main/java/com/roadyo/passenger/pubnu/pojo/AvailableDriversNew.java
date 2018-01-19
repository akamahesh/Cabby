package com.roadyo.passenger.pubnu.pojo;

import java.util.ArrayList;

import com.roadyo.passenger.pubnu.pojo.DriversNew;

public class AvailableDriversNew {
	
	/*"masArr":[
	  		{
	  		"d":3.1435662483909,
			"chn":"qd_29277D52-ED11-4E9C-9C95-6E5678A6D433",
			"e":"srikanth@gmail.com",
			"lt":13.028866767883,
			"lg":77.589653015137n,chn,lt,e_id,lg,bid,d
	  		},
	  		{
	  		"d":5.1435662483909,
	  		"lg":77.58952331543,
	  		"chn":"qd_B06EC27A-5815-48DD-A374-59A0750416E5",
	  		"e":"andy@yahoo.com",
	  		"lt":13.028897285461
	  		}
	  		]*/
	
	String tid;
	ArrayList<DriversNew> mas;
	
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public ArrayList<DriversNew> getMas() {
		return mas;
	}
	public void setMas(ArrayList<DriversNew> mas) {
		this.mas = mas;
	}
}
