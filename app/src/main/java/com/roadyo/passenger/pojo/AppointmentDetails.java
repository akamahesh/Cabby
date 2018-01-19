package com.roadyo.passenger.pojo;

public class AppointmentDetails {
		
	/*{
    "apntDt":"2014-08-26 19:50:12",
    "pPic":"DATest2014200820252010201320415576temp_pic.jpg",
    "email":"varundriver@gmail.com",
    "status":"You have cancelled.",
    "apptType":"1",
    "fname":"Test",
    "phone":"8123868425",
    "apntTime":"07:50 pm",
    "apntDate":"2014-08-26",
    "apptLat":"13.0288",
    "apptLong":"77.5896",
    "payStatus":"0",
    "addrLine1":"46 1st Main Rd",
    "addrLine2":"RBI Colony Hebbal Bengaluru Karnataka 560024",
    "dropLine1":"46, 1st Main Rd, RBI Colony, Hebbal, Bengaluru, Karnataka 560024",
    "dropLine2":"",
    "notes":"",
    "bookType":"",
    "amount":"20",
    "statCode":"4",
    "distance":"0"
		},
		*/
	
	String apntDt;
	String pPic;
	String email;
	String status;
	String apptType;
	String fname;
	String phone;
	String apntTime;
	String apntDate;
	String apptLat;
	String apptLong;
	String payStatus;
	String addrLine1;
	String addrLine2;
	String dropLine1;
	String dropLine2;
	String notes;
	String bookType;
	String amount;
	String statCode;
	String distance;
	String cancel_status;
	public String getCancel_status() {
		return cancel_status;
	}
	public void setCancel_status(String cancel_status) {
		this.cancel_status = cancel_status;
	}
	public String getDropLine1() {
		return dropLine1;
	}
	public String getDropLine2() {
		return dropLine2;
	}
	public String getAmount() {
		return amount;
	}
	public String getStatCode() {
		return statCode;
	}
	public void setDropLine1(String dropLine1) {
		this.dropLine1 = dropLine1;
	}
	public void setDropLine2(String dropLine2) {
		this.dropLine2 = dropLine2;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public void setStatCode(String statCode) {
		this.statCode = statCode;
	}
	public String getApntDt() {
		return apntDt;
	}
	public void setApntDt(String apntDt) {
		this.apntDt = apntDt;
	}
	public String getpPic() {
		return pPic;
	}
	public void setpPic(String pPic) {
		this.pPic = pPic;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getApntTime() {
		return apntTime;
	}
	public void setApntTime(String apntTime) {
		this.apntTime = apntTime;
	}
	public String getApntDate() {
		return apntDate;
	}
	public void setApntDate(String apntDate) {
		this.apntDate = apntDate;
	}
	public String getApptLat() {
		return apptLat;
	}
	public void setApptLat(String apptLat) {
		this.apptLat = apptLat;
	}
	public String getApptLong() {
		return apptLong;
	}
	public void setApptLong(String apptLong) {
		this.apptLong = apptLong;
	}
	public String getAddrLine1() {
		return addrLine1;
	}
	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
	}
	public String getAddrLine2() {
		return addrLine2;
	}
	public void setAddrLine2(String addrLine2) {
		this.addrLine2 = addrLine2;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getApptType() {
		return apptType;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public String getBookType() {
		return bookType;
	}
	public void setApptType(String apptType) {
		this.apptType = apptType;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public void setBookType(String bookType) {
		this.bookType = bookType;
	}
}
