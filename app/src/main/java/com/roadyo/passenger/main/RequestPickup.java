package com.roadyo.passenger.main;

import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.roadyo.passenger.pojo.LiveBookingResponse;
import com.ourcabby.passenger.R;
import com.threembed.utilities.OkHttpRequestObject;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.UltilitiesDate;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

/************************************************************
 * RequestPickup activity is used for sending request to drivers for live booking.
 * If no driver will accept your request simply this activity will finish.
 *
 * Here i'm getting all data from HomeFragment.
 * 
 ************************************************************/

public class RequestPickup extends Activity 
{
	private String from_latitude,from_longitude,to_latitude,to_longitude,zip_code,currenttime,mPromoCode;
	private String pickup_address,dropoff_address,getBookingResponse,car_Id,payment_type,laterBookingDate;
	private LiveBookingResponse liveBookingResponse;
	private RelativeLayout rl_blue,rl_red;
	private TextView hold_cancel;
	private ImageButton cancel;
	//private String addressLine1="",addressLine2="";
	private ArrayList<String> nearestDrivers = new ArrayList<String>();
	private int driverIndex=0;
	private String surge="",wallet="";
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Inflating the layout xml
		setContentView(R.layout.request_pickup);	
		initialize();

		//Getting information from previous activity
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if(extras!=null)
		{
			from_latitude = extras.getString("FromLatitude");
			from_longitude = extras.getString("FromLongitude");
			to_latitude = extras.getString("ToLatitude");
			to_longitude = extras.getString("ToLongitude");
			zip_code= extras.getString("Zipcode");
			pickup_address = extras.getString("PICKUP_ADDRESS");
			dropoff_address = extras.getString("DROPOFF_ADDRESS");
			nearestDrivers = (ArrayList<String>) extras.getSerializable("my_drivers");
			car_Id= extras.getString("Car_Type");
			payment_type = extras.getString("PAYMENT_TYPE");
			laterBookingDate = extras.getString("Later_Booking_Date");
			mPromoCode = extras.getString("coupon");
			surge=extras.getString("SURGE");
			wallet=extras.getString("WALLET");
			Utility.printLog("surge in request "+wallet);
		}
		
		Utility.printLog("RequestPickup Livebooking nearestDrivers size="+nearestDrivers.size());
		
		if(nearestDrivers.size()>0)
		{
			for(int i=0;i<nearestDrivers.size();i++)
			{
				Utility.printLog("RequestPickup nearestDrivers at "+i+" "+nearestDrivers.get(i));
			}
			
			String[] params = {nearestDrivers.get(driverIndex)};
			Utility.printLog("RequestPickup Livebooking nearestDriver email="+params[0]);
			NewBackGroundLiveBooking(params);
		}
		else
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RequestPickup.this);
			// set title
			alertDialogBuilder.setTitle(getResources().getString(R.string.alert));
			// set dialog message
			alertDialogBuilder
			.setMessage(getResources().getString(R.string.no_drivers_found))
			.setCancelable(false)

			.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog,int id) 
				{
					// if this button is clicked, just close the dialog box and do nothing
					dialog.dismiss();
					Intent returnIntent = new Intent();
					setResult(RESULT_OK,returnIntent);     
					finish();
					overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
				}
			});
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();
		
		}
		cancel.setOnLongClickListener(new OnLongClickListener() 
		{
			@Override
			public boolean onLongClick(View v)
			{
				// TODO Auto-generated method stub
				Utility.printLog("RequestPickup Livebooking Response inside setOnLongClickListener");
				hold_cancel.setText(getResources().getString(R.string.cancelling));
				rl_red.setVisibility(View.VISIBLE);
				rl_blue.setVisibility(View.INVISIBLE);
				
				CancelRequestPickup();
				
				return false;
			}
		});
	}

	
	/**
	 * Initializing all variables in my view
	 * 
	 */
	private void initialize()
	{
		rl_blue = (RelativeLayout)findViewById(R.id.relative_blue);
		rl_red = (RelativeLayout)findViewById(R.id.relative_red);
		cancel = (ImageButton)findViewById(R.id.btn_cancel);
		hold_cancel = (TextView)findViewById(R.id.request);
	}

	private void CancelRequestPickup() {
		final LiveBookingResponse[] response = new LiveBookingResponse[1];
		JSONObject jsonObject = new JSONObject();

		try {
			SessionManager session=new SessionManager(RequestPickup.this);

			jsonObject.put("ent_sess_token",session.getSessionToken() );
			jsonObject.put("ent_dev_id",session.getDeviceId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "cancelAppointmentRequest", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				getBookingResponse = result;
				Utility.printLog("RequestPickup Livebooking Success of getting CancelRequestPickup "+response);
				getCancelInfo();
			}
			@Override
			public void onError(String error) {
				Utility.printLog("on error for the login "+error);
				Toast.makeText(RequestPickup.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}

	
	/**
	 * Handling cancel response 
	 */
	private void getCancelInfo()
	{
		try
		{
			JSONObject jsnResponse = new JSONObject(getBookingResponse);

			String jsonErrorParsing = jsnResponse.getString("errFlag");

			Utility.printLog("jsonErrorParsing is ---> "+jsonErrorParsing);
			
			Gson gson = new Gson();
			liveBookingResponse = gson.fromJson(getBookingResponse, LiveBookingResponse.class);

			if(liveBookingResponse!=null)
			{
				if(liveBookingResponse.getErrFlag().equals("0"))
				{
					SessionManager session = new SessionManager(RequestPickup.this);
					session.setBookingCancelled(true);
					session.storeBookingId("0");
					session.storeAptDate(null);
					Toast.makeText(getApplicationContext(), liveBookingResponse.getErrMsg(), Toast.LENGTH_SHORT).show();
					Intent returnIntent = new Intent();
					setResult(RESULT_OK,returnIntent);     
					finish();
					overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
				}
				else
				{
					//Toast.makeText(getApplicationContext(), liveBookingResponse.getErrMsg(), Toast.LENGTH_SHORT).show();
					
					SessionManager session = new SessionManager(RequestPickup.this);
					session.setBookingCancelled(true);
					session.storeBookingId("0");
					session.storeAptDate(null);
					Toast.makeText(getApplicationContext(), liveBookingResponse.getErrMsg(), Toast.LENGTH_SHORT).show();
					Intent returnIntent = new Intent();
					setResult(RESULT_OK,returnIntent);     
					finish();
					overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
				
					/*Intent returnIntent = new Intent();
					setResult(RESULT_OK,returnIntent);     
					finish();
					overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);*/
				}
			}
		}
		catch(JSONException e)
		{
			Utility.printLog("exp "+e);
			e.printStackTrace();
			Utility.ShowAlert(getResources().getString(R.string.server_error), RequestPickup.this);
		}
	}
	
	/**
	 * calling an API for booking driver
	 * @INPUT
	 * Session token, device id, car work type, pickup address, pickup latitude, pickup longitude
	 * payment type, zipcode, drop location, current date time
	 */

	private void NewBackGroundLiveBooking(String []params) {
		final LiveBookingResponse[] response = new LiveBookingResponse[1];
		ProgressDialog dialogL;
		String dateandTime;
		int i=1;
		JSONObject jsonObject = new JSONObject();

		try {
			SessionManager session = new SessionManager(RequestPickup.this);
			Utility utility=new Utility();
			currenttime=utility.getCurrentGmtTime();
			dateandTime=UltilitiesDate.getLocalTime(currenttime);
			Utility.printLog("dataandTime "+currenttime);

			//Utility.printLog("RequestPickup Livebooking from_latitude="+from_latitude+" from_longitude="+from_longitude+" to_latitude="+to_latitude+" to_longitude="+to_longitude);
			//Utility.printLog("RequestPickup Livebooking curenttime in doInBackground="+currenttime);

			jsonObject.put("ent_sess_token",session.getSessionToken());
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_wrk_type",car_Id);
			jsonObject.put("ent_addr_line1",pickup_address);
			//kvPair.put("ent_addr_line2",addressLine2);
			jsonObject.put("ent_lat",from_latitude);
			jsonObject.put("ent_long",from_longitude);
			if(dropoff_address!=null)
			{
				jsonObject.put("ent_drop_addr_line1",dropoff_address);
				jsonObject.put("ent_drop_addr_line2"," ");
				jsonObject.put("ent_drop_lat",to_latitude);
				jsonObject.put("ent_drop_long",to_longitude);
			}
			if(mPromoCode!=null)
			{
				jsonObject.put("ent_coupon",mPromoCode);
			}
			jsonObject.put("ent_payment_type",payment_type);
			jsonObject.put("ent_zipcode",zip_code);
			jsonObject.put("ent_extra_notes"," ");

			jsonObject.put("ent_dri_email",params[0]);
			jsonObject.put("ent_card_id"," ");
			jsonObject.put("ent_later_dt",laterBookingDate);
			jsonObject.put("ent_date_time",currenttime);
			jsonObject.put("ent_surge",surge);
			if(wallet!=null)
			{
				jsonObject.put("ent_wallet",wallet);
			}
			Utility.printLog("aaa=" + session.getSessionToken());
			Utility.printLog("aaa="+session.getDeviceId());
			Utility.printLog("aaa="+currenttime);
			Utility.printLog("live booking params "+jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "liveBooking", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				Gson gson = new Gson();
				response[0] =gson.fromJson(result,LiveBookingResponse.class);
				if(response[0] !=null)
				{
					if(response[0].getErrFlag().equals("0") && response[0].getErrNum().equals("39"))
					{
						Utility.printLog("RequestPickup Livebooking onPostExecute sucess getErrNum="+ response[0].getErrNum());
						SessionManager session=new SessionManager(RequestPickup.this);
						session.setDriverOnWay(true);
						Utility.printLog("Wallah set as true RequestPickup");
						session.setDriverArrived(false);
						session.setTripBegin(false);
						session.setInvoiceRaised(false);

						session.storeAptDate(response[0].getApptDt());
						session.storeCarModel(response[0].getModel());
						session.storePlateNO(response[0].getPlateNo());
						session.storeDriverEmail(response[0].getEmail());
						session.storeCurrentAptChannel(response[0].getChn());
						session.storeBookingId(response[0].getBid());

						Intent returnIntent = new Intent();
						setResult(RESULT_OK,returnIntent);
						finish();
						overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
						return;
					}
					else if(response[0].getErrFlag().equals("0") && response[0].getErrNum().equals("78"))
					{
						Utility.printLog("RequestPickup Livebooking onPostExecute sucess getErrNum="+ response[0].getErrNum());

						SessionManager session=new SessionManager(RequestPickup.this);
						session.setDriverOnWay(false);
						Utility.printLog("Wallah set as true RequestPickup");
						session.setDriverArrived(false);
						session.setTripBegin(false);
						session.setInvoiceRaised(false);

						Intent returnIntent = new Intent();
						setResult(RESULT_OK,returnIntent);
						finish();
						overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
						return;
					}
					else
					{
						//again sending the request to other driver if previous driver is not accepted our request
						++driverIndex;
						if(nearestDrivers.size() > driverIndex)
						{
							String[] params = {nearestDrivers.get(driverIndex)};
							//Utility.printLog("Livebooking Response: RequestPickup nearestDrivers email onPostExecute sending request again to "+params[0]);


							Utility.printLog("RequestPickup Livebooking Response: RequestPickup nearestDrivers email onPostExecute sending request again to "+params[0]);
							NewBackGroundLiveBooking(params);
						}
						else
						{
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RequestPickup.this);
							// set title
							alertDialogBuilder.setTitle(getResources().getString(R.string.alert));
							// set dialog message
							alertDialogBuilder
									.setMessage(response[0].getErrMsg())
									.setCancelable(false)

									.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener()
									{
										public void onClick(DialogInterface dialog,int id)
										{
											// if this button is clicked, just close the dialog box and do nothing
											dialog.dismiss();
											Intent returnIntent = new Intent();
											setResult(RESULT_OK,returnIntent);
											finish();
											overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
										}
									});
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
							// show it
							alertDialog.show();
						}
					}
				}
				else
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RequestPickup.this);

					// set title
					alertDialogBuilder.setTitle(getResources().getString(R.string.error));

					// set dialog message
					alertDialogBuilder
							.setMessage(getResources().getString(R.string.server_error))
							.setCancelable(false)

							.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog,int id)
								{
									// if this button is clicked, just close the dialog box and do nothing
									dialog.dismiss();
									Intent returnIntent = new Intent();
									setResult(RESULT_OK,returnIntent);
									finish();
									overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
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
				Utility.printLog("on error for the login "+error);
				Toast.makeText(RequestPickup.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}
	

	//Using Flurry for App analytics
	@Override
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, "8c41e9486e74492897473de501e087dbc6d9f391");
	}

	//Using Flurry for App analytics
	@Override
	protected void onStop()
	{
		super.onStop();		
		FlurryAgent.onEndSession(this);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}

}
