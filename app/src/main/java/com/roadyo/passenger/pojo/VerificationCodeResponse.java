package com.roadyo.passenger.pojo;

import java.io.Serializable;

public class VerificationCodeResponse implements Serializable
{
	/*
	{"errNum":"107","errFlag":"0","errMsg":"Code sent.","test":"1"}
	 */
		/*{"errNum":"113","errFlag":"1","errMsg":"Mobile number is already registered, choose other.","test":"1"}*/
		
		String errNum,errFlag,errMsg;

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


		

}
