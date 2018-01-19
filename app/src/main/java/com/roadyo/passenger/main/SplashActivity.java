package com.roadyo.passenger.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.ourcabby.passenger.R;
import com.threembed.utilities.OkHttpRequestObject;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends FragmentActivity implements OnClickListener
{
	private LinearLayout login_register;
	private Button signin,register;
	private SessionManager session;
	public boolean gpsEnabled;
	public boolean gpsFix;
	public double latitude;
	public double longitude;
	private LocationManager locationManager;
	private MyGpsListener gpsListener;
	private long DURATION_TO_FIX_LOST_MS = 10000;
	private long MINIMUM_UPDATE_TIME = 0;
	private float MINIMUM_UPDATE_DISTANCE = 0.0f;
	private Dialog dialog;
	private boolean isGpsEnable = false;
	BookAppointmentResponse response;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);

		intializeVariables() ;
		session=new SessionManager(SplashActivity.this);
		session.storeCurrencySymbol("$");

		// ask Android for the GPS service
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			isGpsEnable = true;
		}
		// make a delegate to receive callbacks
		gpsListener = new MyGpsListener();
		// ask for updates on the GPS status
		locationManager.addGpsStatusListener(gpsListener);
		// ask for updates on the GPS location
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MINIMUM_UPDATE_TIME, MINIMUM_UPDATE_DISTANCE, gpsListener);

		Utility.printLog("splash gpsEnabled=" + gpsEnabled);



		if(Utility.isNetworkAvailable(SplashActivity.this))
		{
			Utility.printLog("splash isGpsEnable="+isGpsEnable);
			Thread timer=new Thread()
			{
				@Override
				public void run()
				{
					try
					{
						sleep(500);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					finally
					{
						if(isGpsEnable)
						{
							if(session.isLoggedIn())
							{
								if(Utility.isNetworkAvailable(SplashActivity.this))
								{
									 BackgroundSessionCheck();
								}
								else
								{
									runOnUiThread(new Runnable()
									{
										public void run()
										{
											Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SplashActivity.this);
										}
									});
								}
							}
							else
							{
								runOnUiThread(new Runnable()
								{
									public void run()
									{
										login_register.setVisibility(View.VISIBLE);

										YoYo.with(Techniques.SlideInLeft)
												.duration(700)
												.playOn(findViewById(R.id.signin));

										YoYo.with(Techniques.SlideInRight)
												.duration(700)
												.playOn(findViewById(R.id.register));

									}
								});
							}
						}
						else
						{
							runOnUiThread(new Runnable()
							{
								public void run()
								{
									showGpsAlert();
								}
							});
						}
					}
				}
			};
			timer.start();
		}
		else
		{
			ShowAlert();
		}

	}


	private void ShowAlert()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this);

		// set title
		alertDialogBuilder.setTitle("Alert");

		// set dialog message
		alertDialogBuilder
				.setMessage("Network connection fail")
				.setCancelable(false)


				.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//closing the application
						dialog.dismiss();
						//startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
						finish();
					}
				});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();

	}

	private void BackgroundSessionCheck() {
		JSONObject jsonObject = new JSONObject();
		try {
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Utility.printLog("dataandTime "+curenttime);
			Utility.printLog("splash getSessionToken="+session.getSessionToken());
			Utility.printLog("splash getSessionDeviceId="+session.getDeviceId());
			jsonObject.put("ent_sess_token",session.getSessionToken());
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_user_type","2");
			jsonObject.put("ent_date_time",curenttime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "checkSession", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				Utility.printLog("resposne for check session "+result);
				if(result!=null)
				{
					Gson gson = new Gson();
					response=gson.fromJson(result,BookAppointmentResponse.class);
					if(response!=null)
					{
						if(response.getErrFlag().equals("0"))
						{
							if(Utility.isNetworkAvailable(SplashActivity.this))
							{
								Intent intent=new Intent(SplashActivity.this,MainActivityDrawer.class);
								startActivity(intent);
								finish();
								//overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
							}
							else
							{
								Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SplashActivity.this);
							}
						}
						else
						{
							Toast.makeText(SplashActivity.this, response.getErrMsg(), Toast.LENGTH_SHORT).show();

							session.setIsLogin(false);

							login_register.setVisibility(View.VISIBLE);


							YoYo.with(Techniques.BounceInLeft)
									.duration(700)
									.playOn(findViewById(R.id.signin));

							YoYo.with(Techniques.BounceInRight)
									.duration(700)
									.playOn(findViewById(R.id.register));
							//signin.startAnimation(fadein);
							//register.startAnimation(fadein);
						}

					}
					else
					{
						//Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SplashActivity.this);
					}
				}

			}
			@Override
			public void onError(String error) {
				Utility.printLog("on error for the login "+error);
				Toast.makeText(SplashActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}

	private void intializeVariables()
	{
		login_register=(LinearLayout)findViewById(R.id.login_buttons);
		signin=(Button)findViewById(R.id.signin);
		register=(Button)findViewById(R.id.register);

		signin.setOnClickListener(this);
		register.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onClick(View v)
	{
		// Listener when Register button is clicked
		if(v.getId()==R.id.register)
		{
			if(Utility.isNetworkAvailable(SplashActivity.this))
			{
				final Intent intent=new Intent(SplashActivity.this,SignupActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
			}
			else
			{
				Utility.ShowAlert(getResources().getString(R.string.network_connection_fail),SplashActivity.this);
			}
		}

		if(v.getId()==R.id.signin)
		{
			if(Utility.isNetworkAvailable(SplashActivity.this))
			{
				Intent intent=new Intent(SplashActivity.this,SigninActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
			}
			else
			{
				Utility.ShowAlert(getResources().getString(R.string.network_connection_fail),SplashActivity.this);
			}
		}
	}

	public void setLocale(String lang)
	{

		try {
			Locale myLocale = new Locale(lang);
			Resources res = getResources();
			DisplayMetrics dm = res.getDisplayMetrics();
			Configuration conf = res.getConfiguration();
			conf.locale = myLocale;
			res.updateConfiguration(conf, dm);


		} catch (Exception e) {
			Utility.printLog("select_language inside Exception" + e.toString());
		}
	}

	public void showGpsAlert()
	{
		dialog = new Dialog(SplashActivity.this);
		dialog.setTitle("No Location Access");
		dialog.setContentView(R.layout.gps_alert);

		Button btn = (Button) dialog.findViewById(R.id.go_to_settings);
		btn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				finish();
			}
		});
		dialog.show();
	}

	private void GpsEnabled()
	{
		Thread timer=new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					sleep(1000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				finally
				{
					if(session.isLoggedIn())
					{
						if(Utility.isNetworkAvailable(SplashActivity.this))
						{
							 BackgroundSessionCheck();
						}
						else
						{
							runOnUiThread(new Runnable()
							{
								public void run()
								{
									Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SplashActivity.this);
								}
							});
						}
					}
					else
					{
						runOnUiThread(new Runnable()
						{
							public void run()
							{
								login_register.setVisibility(View.VISIBLE);
								YoYo.with(Techniques.BounceInLeft)
										.duration(700)
										.playOn(findViewById(R.id.signin));

								YoYo.with(Techniques.BounceInRight)
										.duration(700)
										.playOn(findViewById(R.id.register));

							}
						});
					}
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if(locationManager != null)
		{
			// remove the delegates to stop the GPS
			locationManager.removeGpsStatusListener(gpsListener);
			locationManager.removeUpdates(gpsListener);
			locationManager = null;
		}
	}


	protected class MyGpsListener implements GpsStatus.Listener, LocationListener
	{
		// the last location time is needed to determine if a fix has been lost
		private long locationTime = 0;

		@Override
		public void onGpsStatusChanged(int changeType)
		{
			Utility.printLog("splash onGpsStatusChanged="+gpsEnabled);

			if(locationManager != null)
			{
				// status changed so ask what the change was
				switch(changeType)
				{
					case GpsStatus.GPS_EVENT_FIRST_FIX:

						Utility.printLog("splash GPS_EVENT_FIRST_FIX");
						if(dialog!=null && dialog.isShowing())
						{
							dialog.dismiss();
							GpsEnabled();
						}
						gpsEnabled = true;
						gpsFix = true;

						break;
					case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
						Utility.printLog("splash GPS_EVENT_SATELLITE_STATUS");
						if(dialog!=null && dialog.isShowing())
						{
							dialog.dismiss();
							GpsEnabled();
						}
						gpsEnabled = true;
						// if it has been more then 10 seconds since the last update, consider the fix lost
						gpsFix = System.currentTimeMillis() - locationTime < DURATION_TO_FIX_LOST_MS;
						break;
					case GpsStatus.GPS_EVENT_STARTED: // GPS turned on
						Utility.printLog("splash GPS_EVENT_STARTED");
						if(dialog!=null && dialog.isShowing())
						{
							dialog.dismiss();
							GpsEnabled();
						}
						gpsEnabled = true;
						gpsFix = false;
						break;
					case GpsStatus.GPS_EVENT_STOPPED: // GPS turned off
						Utility.printLog("splash GPS_EVENT_STOPPED");
						showGpsAlert();
						gpsEnabled = false;
						gpsFix = false;
						break;
					default:
						showGpsAlert();
						Utility.printLog("splash default "+changeType);
						return;
				}
				//updateView();
			}
		}

		@Override
		public void onLocationChanged(Location location)
		{
			locationTime = location.getTime();
			latitude = location.getLatitude();
			longitude = location.getLongitude();

			if(location.hasAccuracy())
			{
				// rolling average of accuracy so "Signal Quality" is not erratic

			}
			//updateView();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {


		}

		@Override
		public void onProviderDisabled(String provider) {
		}

	}




	@Override
	public void onBackPressed() {
		if(dialog!=null && dialog.isShowing())
		{
			dialog.dismiss();
		}
		finish();
		overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
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

