package com.roadyo.passenger.main;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.roadyo.passenger.pojo.FareCalculation;
import com.ourcabby.passenger.R;
import com.threembed.utilities.GpsListener;
import com.threembed.utilities.OkHttpRequestObject;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

public class FareQuoteActivity extends Activity implements OnClickListener
{
	private String mPICKUP_ADDRESS,mDROPOFF_ADDRESS ;
	private TextView pickup_address,dropoff_address;
	private TextView back;
	private TextView Fare_Amount,Current_Distance,Dropoff_Distance;
	private String getFareResponse,car_type_id;
	private double currentLatitude,currentLongitude;
	private FareCalculation getFare;
	private String from_latitude,from_longitude,to_latitude,to_longitude;
	private RelativeLayout Relative_Pickup_Location,Relative_Drop_Location;  //RL_fare_quote,
	private SessionManager session;
	private RelativeLayout fare_quote_details;
	private String fare;
	private Button enterDestiny;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fare_quote);
		
		GpsListener gpsListener = new GpsListener(FareQuoteActivity.this);
		Location location = gpsListener.getLatLng();
		currentLatitude = location.getLatitude();
		currentLongitude = location.getLongitude();
		
		initialize();
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if(extras!=null)
		{
			 mPICKUP_ADDRESS = extras.getString("PICKUP_ADDRESS");
			 mDROPOFF_ADDRESS = extras.getString("DROPOFF_ADDRESS");
			 from_latitude = extras.getString("FromLatitude");
			 from_longitude = extras.getString("FromLongitude");
			 to_latitude = extras.getString("ToLatitude");
			 to_longitude = extras.getString("ToLongitude");
			 car_type_id = extras.getString("TypeId");
		}
		pickup_address.setText(mPICKUP_ADDRESS);
		dropoff_address.setText(mDROPOFF_ADDRESS);
		getUserProfile();
	}
	
	private void initialize()
	{
		session = new SessionManager(FareQuoteActivity.this);
		pickup_address=(TextView)findViewById(R.id.pickup_location_address);
		dropoff_address=(TextView)findViewById(R.id.dropoff_location_address);
		back=(TextView)findViewById(R.id.fare_quote_back);
		Fare_Amount = (TextView)findViewById(R.id.txt_fare_amount);
		Current_Distance = (TextView)findViewById(R.id.current_distance);
		Dropoff_Distance = (TextView)findViewById(R.id.dropoff_distance);
		//RL_fare_quote = (RelativeLayout)findViewById(R.id.rl_fare_quote);
		Relative_Pickup_Location = (RelativeLayout)findViewById(R.id.relative_pickup_location);
		Relative_Drop_Location = (RelativeLayout)findViewById(R.id.relative_dropoff_location);
		enterDestiny=(Button) findViewById(R.id.enterDestiny);
		enterDestiny.setOnClickListener(this);
		back.setOnClickListener(this);
		fare_quote_details=(RelativeLayout) findViewById(R.id.fare_quote_details);
	}
	
	@Override
	public void onClick(View v) 
	{
		if(v.getId()==R.id.fare_quote_back)
		{
			 Intent returnIntent = new Intent();
			 
			 returnIntent.putExtra("FROM_LATITUDE",from_latitude);
			 returnIntent.putExtra("FROM_LONGITUDE",from_longitude);
			 returnIntent.putExtra("from_SearchAddress",mPICKUP_ADDRESS);
			 returnIntent.putExtra("TO_LATITUDE",to_latitude);
			 returnIntent.putExtra("TO_LONGITUDE",to_longitude);
			 returnIntent.putExtra("to_SearchAddress",mDROPOFF_ADDRESS);
			 returnIntent.putExtra("FARE",fare);
			 setResult(RESULT_OK, returnIntent);
			 finish();
	    	 overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
		}
		
		if(v.getId()==R.id.relative_pickup_location)
		{
			Intent addressIntent=new Intent(FareQuoteActivity.this, SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", "Pickup Location");
			startActivityForResult(addressIntent, 1);
			overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;
		}
		
		if(v.getId()==R.id.relative_dropoff_location)
		{
			Intent addressIntent=new Intent(FareQuoteActivity.this, SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", "Dropoff Location");
			startActivityForResult(addressIntent, 2);
			overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;
		}
		
		if(v.getId()==R.id.enterDestiny)
		{
			Intent addressIntent=new Intent(FareQuoteActivity.this, SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", "Dropoff Location");
			startActivityForResult(addressIntent, 2);
			overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
	
		Utility.printLog("inside onActivityResult requestCode="+requestCode);
		
		if(requestCode==1)
		{
			if(resultCode == Activity.RESULT_OK)
			{
			String latitudeString=data.getStringExtra("LATITUDE_SEARCH");
			String longitudeString=data.getStringExtra("LONGITUDE_SEARCH");
			String searchAddress=data.getStringExtra("SearchAddress");
			
			Utility.printLog("theja fare_quote pick_up fare1="+latitudeString+" "+longitudeString); 
			
			from_latitude=latitudeString;
			from_longitude=longitudeString;
			mPICKUP_ADDRESS=searchAddress;
			pickup_address.setText(searchAddress);
			
			Utility.printLog("theja fare_quote pick_up fare2="+from_latitude+" "+from_longitude); 
			getUserProfile();
			}
		}
		if(requestCode==2)
		{
			if(resultCode == Activity.RESULT_OK)
			{
			String latitudeString=data.getStringExtra("LATITUDE_SEARCH");
			String longitudeString=data.getStringExtra("LONGITUDE_SEARCH");
			String searchAddress=data.getStringExtra("SearchAddress");
			
			to_latitude=latitudeString;
			to_longitude=longitudeString;
			mDROPOFF_ADDRESS=searchAddress;
			dropoff_address.setText(searchAddress);
			
			getUserProfile();
			}
		}
	}

	private void getUserProfile() {
		JSONObject jsonObject = new JSONObject();
		final ProgressDialog dialogL;
		dialogL=com.threembed.utilities.Utility.GetProcessDialog(FareQuoteActivity.this);

		if (dialogL!=null) {
			dialogL.show();
		}
		try {
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Utility.printLog("dataandTime "+curenttime);
			jsonObject.put("ent_sess_token", session.getSessionToken());
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_type_id",car_type_id);
			jsonObject.put("ent_curr_lat",String.valueOf(currentLatitude));
			jsonObject.put("ent_curr_long",String.valueOf(currentLongitude));
			jsonObject.put("ent_from_lat",from_latitude);
			jsonObject.put("ent_from_long",from_longitude);
			jsonObject.put("ent_to_lat",to_latitude);
			jsonObject.put("ent_to_long",to_longitude);
			jsonObject.put("ent_date_time",curenttime);
			Utility.printLog("params to get fare "+jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "fareCalculator", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				getFareResponse = result;
				getUserInfo(dialogL);

			}
			@Override
			public void onError(String error) {
				dialogL.dismiss();
				Toast.makeText(FareQuoteActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}

	private void getUserInfo(ProgressDialog dialogL)
	{
		dialogL.dismiss();
		Utility.printLog("jsonErrorParsing is ---> " + getFareResponse);

		try
		{
			fare_quote_details.setVisibility(View.VISIBLE);
			JSONObject jsnResponse = new JSONObject(getFareResponse);

			String jsonErrorParsing = jsnResponse.getString("errFlag");

			parseResponse();

			if(getFare!=null)
			{
				if(getFare.getErrFlag().equals("0"))
				{
					String cudis,dis;
					cudis=getFare.getCurDis();
					//cudis=round(Double.parseDouble(getFare.getCurDis())*0.621);
					cudis=cudis.replace(" ", "\n");
					dis=getFare.getDis();
				   // dis=round(Double.parseDouble(getFare.getDis())*0.621);
					dis=dis.replace(" ", "\n");
					Fare_Amount.setText(getResources().getString(R.string.currencuSymbol)+" "+round(Double.parseDouble(getFare.getFare())));
					fare=Fare_Amount.getText().toString();
					Current_Distance.setText(cudis);
					Dropoff_Distance.setText(dis);
				}
				else
				{
					Toast.makeText(FareQuoteActivity.this, getFare.getErrMsg(),Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FareQuoteActivity.this);

				// set title
				alertDialogBuilder.setTitle(getResources().getString(R.string.error));

				// set dialog message
				alertDialogBuilder
				.setMessage(getResources().getString(R.string.server_error))
				.setCancelable(false)

				.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.dismiss();
					}
				});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			Utility.ShowAlert("Server error!!", FareQuoteActivity.this);
			Utility.printLog("params catch  get fare " + e);

		}
	}

	private void parseResponse()
	{
		Utility.printLog("getFare parseResponse  " + getFareResponse);
		Gson gson = new Gson();
		getFare = gson.fromJson(getFareResponse, FareCalculation.class);
	}
	
	private String round(double value)
	{
		String s = String.format("%.2f", value);
	    Utility.printLog("rounded value="+s);
	    
	    return s;
    }
	
	@Override
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, "8c41e9486e74492897473de501e087dbc6d9f391");
	}
	 
	@Override
	protected void onStop()
	{
		super.onStop();		
		FlurryAgent.onEndSession(this);
	}
}
