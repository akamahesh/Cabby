package com.roadyo.passenger.pojo;

import java.util.ArrayList;

import com.roadyo.passenger.pubnu.pojo.DriversNew;

public class AvailableDrivers {
	
	/*"masArr":[
	  		{
	  		"d":3.1435662483909,
			"chn":"qd_29277D52-ED11-4E9C-9C95-6E5678A6D433",
			"e":"srikanth@gmail.com",
			"lt":13.028866767883,
			"lg":77.589653015137
	  		},
	  		{
	  		"d":5.1435662483909,
	  		"lg":77.58952331543,
	  		"chn":"qd_B06EC27A-5815-48DD-A374-59A0750416E5",
	  		"e":"andy@yahoo.com",
	  		"lt":13.028897285461
	  		}
	  		]*/
	
	String lg,chn,e,lt,d;
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

	public String getLg() {
		return lg;
	}

	public String getChn() {
		return chn;
	}

	public String getE() {
		return e;
	}

	public String getLt() {
		return lt;
	}

	public void setLg(String lg) {
		this.lg = lg;
	}

	public void setChn(String chn) {
		this.chn = chn;
	}

	public void setE(String e) {
		this.e = e;
	}

	public void setLt(String lt) {
		this.lt = lt;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}
	
}
