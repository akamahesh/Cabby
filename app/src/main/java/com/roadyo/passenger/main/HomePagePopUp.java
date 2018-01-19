package com.roadyo.passenger.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ourcabby.passenger.R;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;

public class HomePagePopUp extends Activity implements OnTouchListener,OnClickListener
{
	private RelativeLayout relativeLayout;
	private String Base_Fair,Base_Fair_per_Min,Base_Fair_per_Km,ServiceType;
	private String ETA,Min_Fair,Max_Size,distance,surge;
	private ImageButton cancel;
	private SessionManager session;
	private TextView base_fair,base_fair_per_min,base_fair_per_km,serviceType;
	private TextView eta,min_fair,max_size,max_size_num,text,value,textSurge;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_details);

		session = new SessionManager(HomePagePopUp.this);

		Utility.printLog("HomePagePopUp  onCreate ");
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if(extras!=null)
		{
			distance = extras.getString("DISTANCE");
			Base_Fair=extras.getString("BASEFARE");
			Min_Fair=extras.getString("MINFARE");
			Base_Fair_per_Min=extras.getString("PRICEPERMINUTE");
			Base_Fair_per_Km=extras.getString("PRICEPERKM");
			Max_Size=extras.getString("MAXSIZE");
			ServiceType=extras.getString("CARTYPE");
			surge=extras.getString("SURGE");
		}

		initialize();
		relativeLayout = (RelativeLayout)findViewById(R.id.alertDialog);
		relativeLayout.setOnTouchListener(this);
	}

	private void initialize()
	{
		base_fair = (TextView) findViewById(R.id.base_fair);
		base_fair_per_min = (TextView) findViewById(R.id.base_fair_min);
		base_fair_per_km = (TextView) findViewById(R.id.base_fair_km);
		serviceType = (TextView) findViewById(R.id.service_type);
		eta = (TextView) findViewById(R.id.eta_time);
		max_size_num= (TextView) findViewById(R.id.max_size_num);
		min_fair = (TextView) findViewById(R.id.min_fair_amount);
		cancel = (ImageButton) findViewById(R.id.service_type_cancel);
		max_size= (TextView) findViewById(R.id.max_size);
		cancel.setVisibility(View.GONE);
		value= (TextView) findViewById(R.id.value);
		textSurge= (TextView) findViewById(R.id.textSurge);


		text= (TextView) findViewById(R.id.eta);
		max_size_num.setText(Max_Size);

		if(surge.equals("") || (Double.parseDouble(surge))<=1)
		{
			base_fair.setText(getResources().getString(R.string.currencuSymbol) + " " + Base_Fair + " Base fare");
			base_fair_per_km.setText(getResources().getString(R.string.currencuSymbol)+" " +Base_Fair_per_Km+ "/Miles");
			base_fair_per_min.setText(getResources().getString(R.string.currencuSymbol)+" " +Base_Fair_per_Min+ "/Min and");
			min_fair.setText(getResources().getString(R.string.currencuSymbol)+" "+Min_Fair);

		}
		else
		{
			base_fair.setText(getResources().getString(R.string.currencuSymbol) + " " + (Double.parseDouble(Base_Fair)*Double.parseDouble(surge)) + " Base fare");
			base_fair_per_km.setText(getResources().getString(R.string.currencuSymbol)+" " +(Double.parseDouble(Base_Fair_per_Km)*Double.parseDouble(surge))+ "/Miles");
			base_fair_per_min.setText(getResources().getString(R.string.currencuSymbol)+" " +(Double.parseDouble(Base_Fair_per_Min)*Double.parseDouble(surge))+ "/Min and");
			min_fair.setText(getResources().getString(R.string.currencuSymbol)+" "+(Double.parseDouble(Min_Fair)*Double.parseDouble(surge)));
		}

		if(surge.equals("") || (Double.parseDouble(surge))<=1)
		{

			textSurge.setText("");
			value.setText("");

		}
		else
		{
			textSurge.setText(getResources().getString(R.string.surge_price));
			value.setText(surge +"x");
		}

			text.setText(getResources().getString(R.string.distance));
			eta.setText(distance);



		serviceType.setText(ServiceType);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		});
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		Intent returnIntent = new Intent();
		setResult(RESULT_OK,returnIntent);
		finish();
		return false;
	}

	@Override
	public void onClick(View v)
	{
		if(v.getId()==R.id.service_type_cancel)
		{
			Intent returnIntent = new Intent();
			setResult(RESULT_OK,returnIntent);
			finish();
		}

	}

}