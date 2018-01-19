package com.roadyo.passenger.pojo;

public class CarsDetailList 
{
	
 /*"types":[{"type_id":"1","type_name":"Starter","max_size":"4","basefare":"200","min_fare":"80","price_per_min":"15","price_per_km":"15","type_desc":"Basic service"},
 {"type_id":"2","type_name":"Silver","max_size":"4","basefare":"250","min_fare":"120","price_per_min":"18","price_per_km":"18","type_desc":"Semi luxury "},
 {"type_id":"3","type_name":"Gold","max_size":"6","basefare":"300","min_fare":"150","price_per_min":"22","price_per_km":"22","type_desc":"Sleeper Luxury"},
 {"type_id":"4","type_name":"Platinum","max_size":"6","basefare":"400","min_fare":"250","price_per_min":"30","price_per_km":"30","type_desc":"Super sleeper luxury"}]}*/
	String type_id;
	String type_name;
	String max_size;
	String basefare;
	String min_fare;
	String price_per_min;
	String price_per_km;
	String type_desc;
	
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public String getMax_size() {
		return max_size;
	}
	public void setMax_size(String max_size) {
		this.max_size = max_size;
	}
	public String getBasefare() {
		return basefare;
	}
	public void setBasefare(String basefare) {
		this.basefare = basefare;
	}
	public String getMin_fare() {
		return min_fare;
	}
	public void setMin_fare(String min_fare) {
		this.min_fare = min_fare;
	}
	public String getPrice_per_min() {
		return price_per_min;
	}
	public void setPrice_per_min(String price_per_min) {
		this.price_per_min = price_per_min;
	}
	public String getPrice_per_km() {
		return price_per_km;
	}
	public void setPrice_per_km(String price_per_km) {
		this.price_per_km = price_per_km;
	}
	public String getType_desc() {
		return type_desc;
	}
	public void setType_desc(String type_desc) {
		this.type_desc = type_desc;
	}
	
}
