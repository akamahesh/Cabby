package com.roadyo.passenger.pojo;

public class GetAppointmentDetails {
	/*{
    "errNum":"21",
    "errFlag":"0",
    "errMsg":"Got the details!",
    "cc_fee":"0",
    "cancel_status":"",
    "cancelAmt":"0",
    "code":"",
    "discount":"0",
    "tip":"0",
    "tipPercent":"0",
    "waitTime":"0",
    "statCode":"6",
    "distanceFee":"0",
    "tollFee":"0",
    "airportFee":"0",
    "parkingFee":"0",
    "fName":"M",
    "lName":"bnmn",
    "mobile":"944508569",
    "addr1":"16, 1st Main Rd, SBM Colony, Anandnagar, Hebbal",
    "addr2":"",
    "dropAddr1":"",
    "dropAddr2":"",
    "amount":"10.00",
    "pPic":"aa_default_profile_pic.gif",
    "dis":"0",
    "dur":"",
    "fare":"0",
    "pickLat":"13.0311",
    "pickLong":"77.5883",
    "ltg":"13.0288383,77.5896556",
    "dropLat":"0",
    "dropLong":"0",
    "apptDt":"2016-05-08 10:48:58",
    "pickupDt":"",
    "dropDt":"",
    "email":"m@gmail.com",
    "dt":"20160508104858",
    "bid":"98",
    "apptType":"1",
    "chn":"qd_867634021585144",
    "plateNo":"638653",
    "model":"Clio3",
    "payStatus":"1",
    "reportMsg":"",
    "payType":"2",
    "avgSpeed":"0",
    "share":"http:\/\/www.roadyo.in\/roadyo1.0\/admin\/track.php?id=98",
    "carImage":"http:\/\/www.roadyo.in\/roadyo1.0\/pics\/",
    "r":"3.9"

	}*/
	
	String errNum,errFlag,errMsg,fName,lName,mobile,addr1,addr2,dropAddr1,dropAddr2,amount,bid,chn,plateNo,model,payStatus,reportMsg;
	String pPic,dis,dur,subTotal,pickLat,pickLong,dropLat,dropLong,apptDt,pickupDt,dropDt,discount,email,dt,id,apptType,share,carImage,r;
    String baseFee;
	String discountType,fare;

	public String getDiscountVal() {
		return discountVal;
	}

	public void setDiscountVal(String discountVal) {
		this.discountVal = discountVal;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	String discountVal;

	public String getMin_fare() {
		return min_fare;
	}

	public void setMin_fare(String min_fare) {
		this.min_fare = min_fare;
	}

	String min_fare;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	String code;

	public String getFare() {
		return fare;
	}

	public String getTipPercent() {
		return tipPercent;
	}

	public void setTipPercent(String tipPercent) {
		this.tipPercent = tipPercent;
	}

	String tipPercent;

	public String getDistanceFee() {
		return distanceFee;
	}

	public void setDistanceFee(String distanceFee) {
		this.distanceFee = distanceFee;
	}

	String distanceFee;

	public String getTimeFee() {
		return timeFee;
	}

	public void setTimeFee(String timeFee) {
		this.timeFee = timeFee;
	}

	String timeFee;
	String tollFee;
	String airportFee;
	String parkingFee;
	String tip;
	String walletDeducted;

	public String getAmountWoutwallet() {
		return amountWoutwallet;
	}

	public String getWalletDeducted() {
		return walletDeducted;
	}

	String amountWoutwallet;

	public String getStatCode() {
		return statCode;
	}

	public void setStatCode(String statCode) {
		this.statCode = statCode;
	}

	String statCode;

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getParkingFee() {
		return parkingFee;
	}

	public void setParkingFee(String parkingFee) {
		this.parkingFee = parkingFee;
	}

	public String getAirportFee() {
		return airportFee;
	}

	public void setAirportFee(String airportFee) {
		this.airportFee = airportFee;
	}

	public String getTollFee() {
		return tollFee;
	}

	public void setTollFee(String tollFee) {
		this.tollFee = tollFee;
	}

	public String getBaseFee() {
		return baseFee;
	}

	public void setBaseFee(String baseFee) {
		this.baseFee = baseFee;
	}

	String payType;

	public String getCashCollected() {
		return cashCollected;
	}

	String cashCollected;
	public String getCarImage() {
		return carImage;
	}

	public String getR() {
		return r;
	}

	public void setR(String r) {
		this.r = r;
	}

	public void setCarImage(String carImage) {
		this.carImage = carImage;
	}

	public String getShare() {
		return share;
	}
	public void setShare(String share) {
		this.share = share;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getErrNum() {
		return errNum;
	}
	public String getErrFlag() {
		return errFlag;
	}
	public String getfName() {
		return fName;
	}
	public String getlName() {
		return lName;
	}
	public String getMobile() {
		return mobile;
	}
	public String getAddr1() {
		return addr1;
	}
	public String getAddr2() {
		return addr2;
	}
	public String getDropAddr1() {
		return dropAddr1;
	}
	public String getDropAddr2() {
		return dropAddr2;
	}
	public String getAmount() {
		return amount;
	}
	public String getpPic() {
		return pPic;
	}
	public String getDis() {
		return dis;
	}
	public String getDur() {
		return dur;
	}

	public String getPickLat() {
		return pickLat;
	}
	public String getPickLong() {
		return pickLong;
	}
	public String getDropLat() {
		return dropLat;
	}
	public String getDropLong() {
		return dropLong;
	}
	public String getApptDt() {
		return apptDt;
	}
	public String getPickupDt() {
		return pickupDt;
	}
	public String getDropDt() {
		return dropDt;
	}
	public String getDiscount() {
		return discount;
	}
	public String getDt() {
		return dt;
	}
	public String getId() {
		return id;
	}
	public String getApptType() {
		return apptType;
	}
	public void setErrNum(String errNum) {
		this.errNum = errNum;
	}
	public void setErrFlag(String errFlag) {
		this.errFlag = errFlag;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}
	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}
	public void setDropAddr1(String dropAddr1) {
		this.dropAddr1 = dropAddr1;
	}
	public void setDropAddr2(String dropAddr2) {
		this.dropAddr2 = dropAddr2;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public void setpPic(String pPic) {
		this.pPic = pPic;
	}
	public void setDis(String dis) {
		this.dis = dis;
	}
	public void setDur(String dur) {
		this.dur = dur;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public void setPickLat(String pickLat) {
		this.pickLat = pickLat;
	}
	public void setPickLong(String pickLong) {
		this.pickLong = pickLong;
	}
	public void setDropLat(String dropLat) {
		this.dropLat = dropLat;
	}
	public void setDropLong(String dropLong) {
		this.dropLong = dropLong;
	}
	public void setApptDt(String apptDt) {
		this.apptDt = apptDt;
	}
	public void setPickupDt(String pickupDt) {
		this.pickupDt = pickupDt;
	}
	public void setDropDt(String dropDt) {
		this.dropDt = dropDt;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public void setDt(String dt) {
		this.dt = dt;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setApptType(String apptType) {
		this.apptType = apptType;
	}
	public String getBid() {
		return bid;
	}
	public String getChn() {
		return chn;
	}
	public String getPlateNo() {
		return plateNo;
	}
	public String getModel() {
		return model;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public String getReportMsg() {
		return reportMsg;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public void setChn(String chn) {
		this.chn = chn;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public void setReportMsg(String reportMsg) {
		this.reportMsg = reportMsg;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
