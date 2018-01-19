package com.roadyo.passenger.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.roadyo.passenger.pojo.UploadImgeResponse;
import com.roadyo.passenger.pojo.ValidVerificationCodeResponse;
import com.roadyo.passenger.pojo.VerificationCodeResponse;
import com.ourcabby.passenger.R;
import com.threembed.utilities.OkHttpRequestObject;
import com.threembed.utilities.ReadSms;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SignupVerificationActivity extends Activity
{
	TextView verificationnumber_tv,verificationnumber_countrycode_tv;
	Button resend_verificationcode_bt,signup_back;
	EditText et_verificationcode;
	String regid,deviceid,firstName,lastName,email,password,mobileNo,referal;
	Button signup_payment_skip;
	SessionManager sessionManager;
	public static File mFileTemp;
	String lat,longitude,type;
	boolean picSelected;
	private ReadSms readSms;
	VerificationCodeResponse verificationResponse;
	ValidVerificationCodeResponse response;
	ProgressDialog dialogL;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signup_verification_activity);
		Bundle bundle=getIntent().getExtras();
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			mFileTemp = new File(Environment.getExternalStorageDirectory(), VariableConstants.TEMP_PHOTO_FILE_NAME);
		}
		else
		{
			mFileTemp = new File(getFilesDir(), VariableConstants.TEMP_PHOTO_FILE_NAME);
		}



		readSms = new ReadSms() {
			@Override
			protected void onSmsReceived(String str) {
				//Call the verify SMS code API

				/**
				 * to filter the OTP from last 5 numbers
				 */
				String substring = str.substring(Math.max(str.length() - 6, 0));

				et_verificationcode.setText(substring);
			}
		};
		IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		intentFilter.setPriority(1000);
		this.registerReceiver(readSms, intentFilter);

		if(bundle!=null)
		{
			regid=bundle.getString("REGID");
			deviceid=bundle.getString("DEVICEID");
			firstName=bundle.getString("FIRSTNAME");
			lastName=bundle.getString("LASTNAME");
			email=bundle.getString("EMAIL");
			password=bundle.getString("PASSWORD");
			mobileNo=bundle.getString("MOBILE");
			referal=bundle.getString("REFERAL");
			picSelected=bundle.getBoolean("BOOLEAN");
			lat=bundle.getString("LATITUDE");
			type=bundle.getString("TYPE");
			longitude=bundle.getString("LONGITUDE");
			Utility.printLog("pic selected falg "+picSelected);

		}
		initializations();
	}

	void initializations()
	{
		verificationnumber_tv=(TextView)findViewById(R.id.verificationnumber_tv);
		verificationnumber_countrycode_tv=(TextView)findViewById(R.id.verificationnumber_countrycode_tv);
		resend_verificationcode_bt=(Button)findViewById(R.id.resend_verificationcode_bt);
		et_verificationcode=(EditText)findViewById(R.id.et_verificationcode);
		signup_back=(Button) findViewById(R.id.signup_back);
		verificationnumber_tv.setText(mobileNo);
		signup_payment_skip=(Button) findViewById(R.id.signup_payment_skip);
		sessionManager=new SessionManager(SignupVerificationActivity.this);

		et_verificationcode.setFocusableInTouchMode(true);
		et_verificationcode.setFocusable(true);
		et_verificationcode.requestFocus();
		signup_payment_skip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (et_verificationcode.getText().toString().equals("")) {
					Utility.ShowAlert(getResources().getString(R.string.enter_verificationcode), SignupVerificationActivity.this);

				} else {
					Utility.ShowAlert(getResources().getString(R.string.enter_correct_verification), SignupVerificationActivity.this);

				}
			}
		});

		et_verificationcode.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				Utility.printLog(" inside onTextChanged  :: " + count);
				count=s.length();
				Utility.printLog(" inside onTextChanged  s.length() :: " + count);
				if(count==5)
				{
					Utility.printLog("inside onTextChanged  length is 5  ::");
					BackgroundForValid_VerificationCode();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after)
			{

				Utility.printLog(" inside beforeTextChanged  :: " + count);
				count=s.length();
				Utility.printLog(" inside beforeTextChanged  s.length() :: " + count);

			}
			@Override
			public void afterTextChanged(Editable s)
			{

				Utility.printLog(" inside afterTextChanged   :: " + s);

				Utility.printLog(" inside afterTextChanged  s.length() :: " + s.length());

			}
		});



		resend_verificationcode_bt.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				BackgroundForGettingVerificationCode();

			}
		});

		signup_back.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				finish();

			}
		});
	}

	private void BackgroundForGettingVerificationCode() {
		JSONObject jsonObject = new JSONObject();
		final ProgressDialog dialogL= Utility.GetProcessDialogNew(SignupVerificationActivity.this, getResources().getString(R.string.waitForVerification));
		dialogL.setCancelable(true);
		if (dialogL!=null)
		{
			dialogL.setCancelable(false);
			dialogL.show();
		}
		try {
			jsonObject.put("ent_mobile",verificationnumber_tv.getText().toString());
			jsonObject.put("ent_user_type", "2");
			Utility.printLog("params to login "+jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "getVerificationCode", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				Gson gson = new Gson();
				verificationResponse=gson.fromJson(result,VerificationCodeResponse.class);
				if(dialogL!=null)
				{
					dialogL.dismiss();
				}
				if(verificationResponse!=null)
				{
					if(verificationResponse.getErrFlag().equals("0"))
					{

						showAlert(verificationResponse.getErrMsg());

					}
					else
					{
						showAlert(verificationResponse.getErrMsg());
					}
				}
				else
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupVerificationActivity.this);
					// set title
					alertDialogBuilder.setTitle(getResources().getString(R.string.alert));
					// set dialog message
					alertDialogBuilder
							.setMessage(getResources().getString(R.string.server_error))
							.setCancelable(false)

							.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog,int id)
								{
									// if this button is clicked, just close
									// the dialog box and do nothing
									finish();
								}
							});
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
					// show it
					alertDialog.show();
				}
			}
			@Override
			public void onError(String error) {
				if (dialogL!=null)
				{
					dialogL.cancel();
				}
				Utility.printLog("on error for the login "+error);
				Toast.makeText(SignupVerificationActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}

	private void BackgroundForValid_VerificationCode() {
		JSONObject jsonObject = new JSONObject();
		final ValidVerificationCodeResponse[] response = new ValidVerificationCodeResponse[1];

		dialogL= Utility.GetProcessDialogNew(SignupVerificationActivity.this, getResources().getString(R.string.verifying_otp));
		dialogL.setCancelable(true);
		if (dialogL!=null)
		{
			dialogL.setCancelable(false);
			dialogL.show();
		}
		try {
			jsonObject.put("ent_mobile",mobileNo);
			jsonObject.put("ent_user_type", "2");
			jsonObject.put("ent_code",et_verificationcode.getText().toString());
			Utility.printLog("paramt to verify "+jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "verifyPhone", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				Gson gson = new Gson();
				response[0] =gson.fromJson(result,ValidVerificationCodeResponse.class);
				if(dialogL!=null)
				{
					dialogL.dismiss();
				}
				if(response[0]!=null)
				{
					if(response[0].getErrFlag().equals("0"))
					{
						BackgroundTaskSignUp();
					}
					else
					{
						showAlert(response[0].getErrMsg());
						et_verificationcode.setText(null);
					}


				}
				else
				{

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupVerificationActivity.this);
					// set title
					alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

					// set dialog message
					alertDialogBuilder
							.setMessage(getResources().getString(R.string.server_error))
							.setCancelable(false)

							.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog,int id)
								{
									// if this button is clicked, just close
									// the dialog box and do nothing
									finish();
								}
							});
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
					// show it
					alertDialog.show();
				}
			}
			@Override
			public void onError(String error) {
				if (dialogL!=null)
				{
					dialogL.cancel();
				}
				Utility.printLog("on error for the login "+error);
				Toast.makeText(SignupVerificationActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}

	private void showAlert(String message)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

		// set dialog message
		alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						//closing the application
						dialog.dismiss();
					}
				});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}

	private void BackgroundTaskSignUp() {
		JSONObject jsonObject = new JSONObject();
		final SignUpResponse[] response = new SignUpResponse[1];
		dialogL=com.threembed.utilities.Utility.GetProcessDialogNew(SignupVerificationActivity.this,getResources().getString(R.string.signing_you_up));
		dialogL.setCancelable(true);
		if(dialogL!=null)
		{
			dialogL.setCancelable(false);
			dialogL.show();
		}
		try {
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			jsonObject.put("ent_first_name",firstName);
			jsonObject.put("ent_last_name",lastName);
			jsonObject.put("ent_email",email);
			jsonObject.put("ent_password",password);
			jsonObject.put("ent_mobile",mobileNo);
			jsonObject.put("ent_login_type",type);
			//jsonObject.put("ent_city",current_city_name);
/*			jsonObject.put("ent_latitude",String.valueOf(currentLatitude));
			jsonObject.put("ent_longitude",String.valueOf(currentLongitude));*/
			if(lat!=null && longitude !=null)
			{
				jsonObject.put("ent_latitude",lat);
				jsonObject.put("ent_longitude",longitude);
			}
			else
			{
				jsonObject.put("ent_latitude","26.3544482");
				jsonObject.put("ent_longitude","49.7122909");
			}

			if(referal!=null)
				jsonObject.put("ent_referral_code",referal);
			jsonObject.put("ent_terms_cond","true");
			jsonObject.put("ent_pricing_cond", "true");

			jsonObject.put("ent_dev_id",deviceid);
			jsonObject.put("ent_push_token",regid);
			jsonObject.put("ent_device_type","2");
			jsonObject.put("ent_date_time",curenttime);
			Utility.printLog("SignUp Response: params" + jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "slaveSignup", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				Gson gson = new Gson();
				response[0] =gson.fromJson(result, SignUpResponse.class);
				if(response[0] !=null)
				{
					Utility.printLog("SignUp Response: params" + result);
					if(response[0].getErrFlag().equals("0"))
					{
						if(response[0].getToken()!=null)
							sessionManager.storeSessionToken(response[0].getToken());
						sessionManager.storeRegistrationId(regid);
						sessionManager.storeLoginId(email);
						sessionManager.storeDeviceId(deviceid);
						sessionManager.setIsLogin(true);
						sessionManager.storeServerChannel(response[0].getServerChn());
						//session.storeLoginResponse(jsonResponse);
						//session.storeCarTypes(jsonResponse);
						sessionManager.storeChannelName(response[0].getChn());
						sessionManager.storeCouponCode(response[0].getCoupon());
						sessionManager.setPresenceChn(response[0].getPresenseChn());


						if(Utility.isNetworkAvailable(SignupVerificationActivity.this))
						{
							 BackGroundTaskForUploadImage();
						}
						else
						{
							Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SignupVerificationActivity.this);
						}
					}
					else
					{
						Toast.makeText(SignupVerificationActivity.this, response[0].getErrMsg(), Toast.LENGTH_SHORT).show();
						if(dialogL!=null)
						{
							dialogL.dismiss();
						}
					}
				}
				else
				{
					Toast.makeText(SignupVerificationActivity.this,getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
					if(dialogL!=null)
					{
						dialogL.dismiss();
					}
				}
			}
			@Override
			public void onError(String error) {
				if (dialogL!=null)
				{
					dialogL.cancel();
				}
				Utility.printLog("on error for the login "+error);
				Toast.makeText(SignupVerificationActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}

	private void BackGroundTaskForUploadImage() {
		long chunkLength=1000*1024;
		long totalBytesRead=0;
		long bytesRemaining ;
		long FILE_SIZE = 0;
		String fileName;
		ProgressDialog dialogL ;
		File mFile;

		FileInputStream fin = null;
		totalBytesRead = 0;
		bytesRemaining=0;

		//mFile=new File("/sdcard"+"/SneekPeek", "picture"+".jpg");
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			//mFile= new File(Environment.getExternalStorageDirectory(), VariableConstants.TEMP_PHOTO_FILE_NAME);
			mFile=mFileTemp;
		}
		else
		{
			//mFile= new File(getFilesDir(),VariableConstants.TEMP_PHOTO_FILE_NAME);
			mFile=mFileTemp;
		}


		String temp=com.threembed.utilities.Utility.getCurrentDateTimeStringGMT();
		temp=new String(temp.trim().replace(" ", "20"));
		temp=new String(temp.trim().replace(":", "20"));
		temp=new String(temp.trim().replace("-", "20"));

		fileName="PA"+firstName+temp+mFile.getName();

		try
		{
			fin= new FileInputStream(mFile);
		}
		catch (FileNotFoundException e2)
		{
			e2.printStackTrace();
		}

		if(mFile.isFile() && mFile.length()>0)
		{
			FILE_SIZE = mFile.length();
		}

		while (totalBytesRead < FILE_SIZE )
		{
			try
			{
				bytesRemaining = FILE_SIZE-totalBytesRead;

				if ( bytesRemaining < chunkLength ) // Remaining Data Part is Smaller Than CHUNK_SIZE
				{
					chunkLength = bytesRemaining;
				}

				byte []chunk = null;
				chunk=new byte[(int) chunkLength];

				byte fileContent[] = new byte[(int) chunkLength];
				try
				{
					fin.read(fileContent,0,(int)chunkLength);
				} catch (IOException e)
				{
					e.printStackTrace();
				}

				System.arraycopy(fileContent, 0, chunk, 0, (int) chunkLength);
				//byte[] encoded = Base64.encodeBase64(chunk);
				byte[] encoded = Base64.encode(chunk, Base64.NO_WRAP);
				String encodedString = new String(encoded);
				Utility utility=new Utility();
				totalBytesRead=totalBytesRead+chunkLength;

				JSONObject jsonObject = new JSONObject();
				try {

					if(!picSelected)
					{
						String curenttime=utility.getCurrentGmtTime();
						jsonObject.put("ent_sess_token",sessionManager.getSessionToken());
						jsonObject.put("ent_dev_id",sessionManager.getDeviceId());
						jsonObject.put("ent_snap_name","");
						jsonObject.put("ent_snap_chunk","");
						jsonObject.put("ent_upld_from","2");
						jsonObject.put("ent_snap_type","1");
						jsonObject.put("ent_offset","1");
						jsonObject.put("ent_date_time",curenttime);
					}
					else
					{
						String curenttime=utility.getCurrentGmtTime();
						jsonObject.put("ent_sess_token",sessionManager.getSessionToken());
						jsonObject.put("ent_dev_id",sessionManager.getDeviceId());
						jsonObject.put("ent_snap_name",fileName+".jpg");
						jsonObject.put("ent_snap_chunk",encodedString);
						jsonObject.put("ent_upld_from","2");
						jsonObject.put("ent_snap_type","1");
						jsonObject.put("ent_offset","1");
						jsonObject.put("ent_date_time",curenttime);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "uploadImage", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
					@Override
					public void onSuccess(String result) {
						Utility.printLog("resposne for check session "+result);
						if(result!=null)
						{
							Gson gson=new Gson();
							response=gson.fromJson(result,ValidVerificationCodeResponse.class);
							if(response.getErrFlag().equals("0"))
							{
								Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_success), Toast.LENGTH_SHORT).show();
								sessionManager.setIsLogin(true);
								//Move to map activity
								Intent intent=new Intent(SignupVerificationActivity.this,MainActivityDrawer.class);
								startActivity(intent);
								finish();
								overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
							}
							else
							{
								Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_success), Toast.LENGTH_SHORT).show();
								sessionManager.setIsLogin(true);
								Intent intent=new Intent(SignupVerificationActivity.this,MainActivityDrawer.class);
								startActivity(intent);
								finish();
								overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
							}
						}
						else
						{
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_success), Toast.LENGTH_SHORT).show();
							sessionManager.setIsLogin(true);
							//Move to map activity
							Intent intent=new Intent(SignupVerificationActivity.this,MainActivityDrawer.class);
							startActivity(intent);
							finish();
							overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
						}
					}
					@Override
					public void onError(String error) {
						Utility.printLog("on error for the login "+error);
						Toast.makeText(SignupVerificationActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
					}
				});
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		this.unregisterReceiver(readSms);
	}
}
