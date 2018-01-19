package com.roadyo.passenger.pubnu.pojo;

import java.io.Serializable;

public class DriverDetailsAroundYou implements Serializable{
	
/*	"docs": [
		        {
		            "name": "lillian",
		            "image": "lillian.png",
		            "rating": 0.6,
		            "email": "lillian@yahoo.com",
		            "lat": 34.070622,
		            "lon": -118.399851
		        },

		    ]*/
	
	String name;
	String image;
	String rating;
	String email;
	String lname;
	String dis;
	double lat;
	double lon;

	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getDis() {
		return dis;
	}
	public void setDis(String dis) {
		this.dis = dis;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	
	

}
