package com.roadyo.passenger.pojo;

import java.util.ArrayList;

public class LoginResponse 
{
	
	
/*	{"errNum":"9",
 * "errFlag":"0",
 * "errMsg":"Login completed!",
 * "token":"33353635353530353937303935j3gYwjL4DYql4wvJSeOX3533j3gYwjL4DYql4wvJSeOX",
 * "expiryLocal":"2014-06-23 06:35:58",
 * "expiryGMT":"2014-06-23 10:35:58",
 * "email":"varun11@gmail.com",
 * "profilePic":"PAvarun201420052024200520172009temp_photo.jpg",
 * "flag":"1",
 * "paypal":"2",
 * "joined":"2014-05-24 04:15:10",
 * "apiKey":"2ee51574176b15c2e",
 * "chn":"up_356555059709553",
 * "cards":[],
 * "types":[{"type_id":"1","type_name":"Starter","max_size":"4","basefare":"200","min_fare":"80","price_per_min":"15","price_per_km":"15","type_desc":"Basic service"},
 * {"type_id":"2","type_name":"Silver","max_size":"4","basefare":"250","min_fare":"120","price_per_min":"18","price_per_km":"18","type_desc":"Semi luxury "},
 * {"type_id":"3","type_name":"Gold","max_size":"6","basefare":"300","min_fare":"150","price_per_min":"22","price_per_km":"22","type_desc":"Sleeper Luxury"},
 * {"type_id":"4","type_name":"Platinum","max_size":"6","basefare":"400","min_fare":"250","price_per_min":"30","price_per_km":"30","type_desc":"Super sleeper luxury"}]}
*/	
	
	
	/*{

    "errNum":"9",
    "errFlag":"0",
    "errMsg":"Login completed!",
    "token":"3335333230323036333136307QgizwPu6d7AYDO49Vp83239307QgizwPu6d7AYDO49Vp8",
    "expiryLocal":"2014-10-11 12:50:26",
    "expiryGMT":"2014-10-11 11:50:26",
    "email":"varun@mobifyi.com",
    "profilePic":"PAVARUN201420092006201020512039temp_photo.jpg",
    "flag":"1",
    "joined":"2014-09-06 09:51:36",
    "apiKey":"2ee51574176b15c2e",
    "cards":[
        {
            "id":"card_14Zgn545d84DOxoazQ4Z0oTB",
            "last4":"4444",
            "type":"MasterCard",
            "exp_month":"10",
            "exp_year":"2020"
        }
    ],
    "types":[
    ],
    "serverChn":"freetaxi",
    "chn":"qp_353202063160290",
    "noVehicleType":"THANK YOU FOR SIGNING UP! UNFORTUNATELY WE ARE NOT IN YOUR CITY YET, PLEASE DO SEND US AN EMAIL AT INFO@ROADYO.NET, IF YOU WILL LIKE US THERE!"

}*/
	
	String errNum;
	String errFlag;
	String errMsg;
	String token;
	String expiryLocal;
	String expiryGMT;
	String profilePic;
	String flag;
	String joined;
	String chn,serverChn;
	String apiKey;
	String email,noVehicleType;
	String coupon;
	String paypal;
	String presenseChn;
	String pub;

	public String getStipeKey() {
		return stipeKey;
	}

	public void setStipeKey(String stipeKey) {
		this.stipeKey = stipeKey;
	}

	String stipeKey;

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getPub() {
		return pub;
	}

	public void setPub(String pub) {
		this.pub = pub;
	}

	public String getPresenseChn() {
		return presenseChn;
	}

	public void setPresenseChn(String presenseChn) {
		this.presenseChn = presenseChn;
	}

	String sub;
	ArrayList<CarsDetailList> types;
	
	public ArrayList<CarsDetailList> getCarsdetails() 
	{
		return types;
	}
	public void setCarsdetails(ArrayList<CarsDetailList> cardetails)
	{
		this.types = cardetails;
	}
	public String getPaypal() {
		return paypal;
	}
	public void setPaypal(String paypal) {
		this.paypal = paypal;
	}
	public String getChn() {
		return chn;
	}
	public void setChn(String chn) {
		this.chn = chn;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getErrNum() {
		return errNum;
	}
	public void setErrNum(String errNum) {
		this.errNum = errNum;
	}
	public String getErrFlag() {
		return errFlag;
	}
	public void setErrFlag(String errFlag) {
		this.errFlag = errFlag;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getExpiryLocal() {
		return expiryLocal;
	}
	public void setExpiryLocal(String expiryLocal) {
		this.expiryLocal = expiryLocal;
	}
	public String getExpiryGMT() {
		return expiryGMT;
	}
	public void setExpiryGMT(String expiryGMT) {
		this.expiryGMT = expiryGMT;
	}
	public String getProfilePic() {
		return profilePic;
	}
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getJoined() {
		return joined;
	}
	public void setJoined(String joined) {
		this.joined = joined;
	}
	public String getServerChn() {
		return serverChn;
	}
	public void setServerChn(String serverChn) {
		this.serverChn = serverChn;
	}
	public String getNoVehicleType() {
		return noVehicleType;
	}
	public void setNoVehicleType(String noVehicleType) {
		this.noVehicleType = noVehicleType;
	}
	public String getCoupon() {
		return coupon;
	}
	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}
}
