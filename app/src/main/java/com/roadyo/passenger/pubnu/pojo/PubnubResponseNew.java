package com.roadyo.passenger.pubnu.pojo;

import java.util.ArrayList;

import com.roadyo.passenger.pojo.AvailableDrivers;

public class PubnubResponseNew 
{
	/*masArr":[
	{ 
	"tid":1,
	"mas":[
	{
	"d":22.398394726144,
	"chn":"qd_352136062663873",
	"e":"kishore@gmail.com",
	"lt":13.02884224,
	"lg":77.58958957
	},
	{
	"d":4269.0117993985,
	"chn":"qd_6EDB16F1-7F9F-41FB-8540-4DEFD6070407",
	"e":"deva@yahoo.com",
	"lt":12.990862846375,
	"lg":77.593780517578
	}
	]
	}
	*/
	
	/*{

	    "n":"Dharma Krish",
	    "chn":"qd_A1000037CE3BC3",
	    "lt":13.028831558474515,
	    "e_id":"dharma@mobifyi.com",
	    "lg":77.58959056411352,
	    "tp":"1",
	    "a":4

	}*/

	/*    "masArr":[],
    "st":3,
    "flag":0,
    "types":[],
    "a":2,
    "tp":-1*/
	
	String a,st,flag,tp,n,chn,lt,e_id,lg,bid,d;
	ArrayList<AvailableDriversNew> masArr;
	ArrayList<DriverEmails> es;
	ArrayList<PubnubCarTypes> types;
	
	public ArrayList<PubnubCarTypes> getTypes() {
		return types;
	}

	public void setTypes(ArrayList<PubnubCarTypes> types) {
		this.types = types;
	}

	public ArrayList<DriverEmails> getEs() {
		return es;
	}

	public void setEs(ArrayList<DriverEmails> es) {
		this.es = es;
	}

	public String getN() {
		return n;
	}

	public String getChn() {
		return chn;
	}

	public String getLt() {
		return lt;
	}

	public String getE_id() {
		return e_id;
	}

	public String getLg() {
		return lg;
	}

	public void setN(String n) {
		this.n = n;
	}

	public void setChn(String chn) {
		this.chn = chn;
	}

	public void setLt(String lt) {
		this.lt = lt;
	}

	public void setE_id(String e_id) {
		this.e_id = e_id;
	}

	public void setLg(String lg) {
		this.lg = lg;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getSt() {
		return st;
	}

	public String getFlag() {
		return flag;
	}

	public String getTp() {
		return tp;
	}

	public ArrayList<AvailableDriversNew> getMasArr() {
		return masArr;
	}

	public void setSt(String st) {
		this.st = st;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public void setTp(String tp) {
		this.tp = tp;
	}

	public void setMasArr(ArrayList<AvailableDriversNew> masArr) {
		this.masArr = masArr;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}
}
