package com.threembed.utilities;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.roadyo.passenger.main.SplashWithGps;


public class GpsListener 
{
	public static final String TAG = GpsListener.class.getName();

	private static final long DURATION_TO_FIX_LOST_MS = 10000;
	private static final long MINIMUM_UPDATE_TIME = 0;
	private static final float MINIMUM_UPDATE_DISTANCE = 0.0f;

	private LocationManager locationManager;
	private MyGpsListener gpsListener;

	private int satellitesTotal;
	private int satellitesUsed;

	public static boolean gpsEnabled;
	public static boolean gpsFix;
	public static double latitude;
	public static double longitude;
	public static float accuracy;
	public static Context context;

	public GpsListener(Context context) 
	{
		this.context = context;
		// ask Android for the GPS service
		locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		// make a delegate to receive callbacks
		gpsListener = new MyGpsListener();
		// ask for updates on the GPS status
		locationManager.addGpsStatusListener(gpsListener);
		// ask for updates on the GPS location
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
				MINIMUM_UPDATE_TIME, MINIMUM_UPDATE_DISTANCE, gpsListener);

	}

	public void stopGpsListener()
	{
		if (locationManager != null) 
		{
			// remove the delegates to stop the GPS
			locationManager.removeGpsStatusListener(gpsListener);
			locationManager.removeUpdates(gpsListener);
			locationManager = null;
		}
	}

	private LatLng updateView() 
	{
		getGrade();
		LatLng latLng = new LatLng(latitude, longitude);
		return latLng;
	}
	
	public Location getLatLng()
	{
		Location location = new Location("");
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		
		return location;
	}

	public boolean getGrade() 
	{
		if(!gpsEnabled) 
		{
			return false;
		}
		if(!gpsFix) 
		{
			//"Waiting for Fix"
			return true;
		}
		if(accuracy <= 10) 
		{
			if(SplashWithGps.dialog!=null && SplashWithGps.dialog.isShowing())
			{
				SplashWithGps.dialog.dismiss();
				SplashWithGps activity = new SplashWithGps();
				activity.gpsEnabled();
				
			}
			
			
			return true;
		}
		if(accuracy <= 30) 
		{
			if(SplashWithGps.dialog!=null && SplashWithGps.dialog.isShowing())
			{
				SplashWithGps.dialog.dismiss();
				SplashWithGps activity = new SplashWithGps();
				activity.gpsEnabled();
				
			}
			
			
			return true;
		}
		if(accuracy <= 100) 
		{
			if(SplashWithGps.dialog!=null && SplashWithGps.dialog.isShowing())
			{
				SplashWithGps.dialog.dismiss();
				SplashWithGps activity = new SplashWithGps();
				activity.gpsEnabled();
			}
			
			return true;
		}
		return false;
	}

	protected class MyGpsListener implements GpsStatus.Listener, LocationListener 
	{
		// the last location time is needed to determine if a fix has been lost
		private long locationTime = 0;

		@Override
		public void onGpsStatusChanged(int changeType) {
			if (locationManager != null) {

				// status changed so ask what the change was
				GpsStatus status = locationManager.getGpsStatus(null);
				switch(changeType) 
				{
				case GpsStatus.GPS_EVENT_FIRST_FIX:
					gpsEnabled = true;
					gpsFix = true;
					break;
				case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
					gpsEnabled = true;
					// if it has been more then 10 seconds since the last update, consider the fix lost
					gpsFix = System.currentTimeMillis() - locationTime < DURATION_TO_FIX_LOST_MS;
					break;
				case GpsStatus.GPS_EVENT_STARTED: // GPS turned on
					gpsEnabled = true;
					gpsFix = false;
					break;
				case GpsStatus.GPS_EVENT_STOPPED: // GPS turned off
					gpsEnabled = false;
					gpsFix = false;
					break;
				default:
					Log.w(TAG, "unknown GpsStatus event type. "+changeType);
					return;
				}

				// number of satellites, not useful, but cool
				int newSatTotal = 0;
				int newSatUsed = 0;
				for(GpsSatellite sat : status.getSatellites()) 
				{
					newSatTotal++;
					if(sat.usedInFix()) 
					{
						newSatUsed++;
					}
				}
				satellitesTotal = newSatTotal;
				satellitesUsed = newSatUsed;

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
			// dont need this info 
		}

		@Override
		public void onProviderEnabled(String provider) {
			// dont need this info 
		}

		@Override
		public void onProviderDisabled(String provider) {
			// dont need this info 
		}

	}
}
