package com.roadyo.passenger.pubnu.pojo;

import java.util.ArrayList;

import com.roadyo.passenger.pojo.AvailableDrivers;

public class PubNubResponse {
	
	/*{

    "masArr":[
        {
            "d":4.7330358786713,
            "chn":"qd_7694F696-162D-4020-8057-E586DAA3980F",
            "e":"test@3embed.com",
            "lt":13.028869628906,
            "lg":77.589653015137
        },
        {
            "d":4.8312577829005,
            "chn":"qd_3C3C7FDE-13DC-4F27-B1F6-6B7F912AB4D4",
            "e":"rosh@gmail.com",
            "lt":13.028870582581,
            "lg":77.589653015137
        },
        
    ],
    "st":3,
    "flag":0,
    "es":[
        "test@3embed.com",
        "rosh@gmail.com",
        "rahul@3embed.com",
        "abhishek@mobifyi.com",
        "dharma@mobifyi.com"
    ],
    
    "types":[
{
"min_fare":25,
"type_desc":"Freetaxi Gold service",
"type_name":"Freetaxi Gold",
"basefare":20,
"price_per_min":1,
"price_per_km":1,
"max_size":6,
"type_id":2
}
],
    "a":2,
    "tp":1
    "bid":"6",
}*/
	
	/*{
	    "dt":"2014-09-06 19:56:10",
	    "lg":77.58962856,
	    "a":"6",
	    "chn":"qd_352136062663873",
	    "lt":13.02880154,
	    "ph":"8123868425",
	    "bid":"6",
	    "e_id":"varun@mobifyi.com"

	}*/
	
	String a,st,flag,tp,n,chn,lt,e_id,lg,bid,d;
	ArrayList<AvailableDrivers> masArr;
	ArrayList<String> es;
	ArrayList<PubnubCarTypes> types;
	
	
	public ArrayList<PubnubCarTypes> getTypes() {
		return types;
	}

	public void setTypes(ArrayList<PubnubCarTypes> types) {
		this.types = types;
	}

	public ArrayList<String> getEs() {
		return es;
	}

	public void setEs(ArrayList<String> es) {
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

	public ArrayList<AvailableDrivers> getMasArr() {
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

	public void setMasArr(ArrayList<AvailableDrivers> masArr) {
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
