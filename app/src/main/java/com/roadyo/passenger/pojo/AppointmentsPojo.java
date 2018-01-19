package com.roadyo.passenger.pojo;

public class AppointmentsPojo
{
	
	String name;
	String date,aptDate;
	String status,payStatus;
	String pic;
	String time;

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	String bid;

	public String getApptType() {
		return apptType;
	}

	public void setApptType(String apptType) {
		this.apptType = apptType;
	}

	String apptType;
	String email,phone,location,notes,statCode,amount,drop_address,distance,cancel_status;
	
	public AppointmentsPojo(String name,String bid,String apptType,String apt_date, String date, String status,String paystatus, String pic,String time,String email,String phone,String loc,String notes,String statcode,String Amount,String drop,String distance,String cancel_status) {
		super();
		this.name = name;
		this.aptDate = apt_date; 
		this.date = date;
		this.status = status;
		this.payStatus = paystatus;
		this.pic = pic;
		this.time=time;
		this.email=email;
		this.phone=phone;
		this.location=loc;
		this.notes=notes;
		this.statCode=statcode;
		this.amount=Amount;
		this.drop_address=drop;
		this.distance=distance;
		this.cancel_status=cancel_status;
		this.apptType=apptType;
		this.bid=bid;
	}
	
	
	public String getCancel_status() {
		return cancel_status;
	}


	public void setCancel_status(String cancel_status) {
		this.cancel_status = cancel_status;
	}


	public String getAptDate() {
		return aptDate;
	}
	public void setAptDate(String aptDate) {
		this.aptDate = aptDate;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getDrop_address() {
		return drop_address;
	}
	public void setDrop_address(String drop_address) {
		this.drop_address = drop_address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getStatCode() {
		return statCode;
	}
	public String getAmount() {
		return amount;
	}
	public void setStatCode(String statCode) {
		this.statCode = statCode;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getDistance() {
		return distance;
	}

}
