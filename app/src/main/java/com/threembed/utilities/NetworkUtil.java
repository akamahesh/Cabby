package com.threembed.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class NetworkUtil implements ConnectionCallbacks, OnConnectionFailedListener,LocationListener
{
    private final static int defaultUpdateInterval = 2000;
    private final static int defaultFastestInterval = 1000;
    private final static int defaultTimeout = 10000;

    private Activity activity;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private NetworkNotifier informer;

    private int updateInterval;
    private int fastestInterval;
    private int timeout;
    private boolean locationUpdated = false;
    private boolean googleAPIConnected = false;
    private final boolean isPlayService;
    private AlertDialog dialoge;
    private  Handler handler;
    private Runnable settingtask;


    /**
     *@Definition constructor with default settings
     */
    public NetworkUtil(Activity activity,NetworkNotifier informer)
    {
        this(activity,defaultUpdateInterval,defaultFastestInterval,defaultTimeout,informer);

    }
    /**
     *@Definition constructor with customized settings
     */
    public NetworkUtil(Activity activity,int updateInterval,int fastestinterval,int timeout,NetworkNotifier informer)
    {
        this.activity = activity;
        this.updateInterval = updateInterval;
        this.informer = informer;
        this.fastestInterval = fastestinterval;
        this.timeout = timeout;
        this.isPlayService = checkPlayServices();
        if(isPlayService)
        {
            buildGoogleApiClient();
        }

    }

    /**
     * @Definition builds GoogleApiClient
     */
    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * @Definition Connects GoogleApi
     * @return true if able to call connect
     */
    public boolean connectGoogleApiClient()
    {
        if(isPlayService && !googleAPIConnected){
            mGoogleApiClient.connect();
        }

        return isPlayService && !googleAPIConnected;
    }
    /**
     * @Definition disconnects GoogleApi
     * @return true if able to call disconnect
     */

	/* public boolean disconnectGoogleApiClient()
		{
			if(isPlayService && googleAPIConnected)
			{*/
    public boolean disconnectGoogleApiClient()
    {
        if(isPlayService && googleAPIConnected )
        {
            if(handler!=null)
            {

                if(settingtask!=null)
                {
                    handler.removeCallbacks(settingtask);
                    settingtask = null;
                }
                handler = null;
            }
            removeLocationUpdates();
            mGoogleApiClient.disconnect();
            googleAPIConnected = false;
            locationUpdated = false;
            if(dialoge!=null)
            {
                dialoge.dismiss();
                dialoge = null;
            }
            informer.updatedInfo("disconnectGoogleApiClient Success");
            return isPlayService;
        }
        informer.updatedInfo("disconnectGoogleApiClient Failed");
        return isPlayService;
    }
    /**
     * @Definition checks Google PlayServices
     */

    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                try {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                            9000).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                informer.updatedInfo("Found error for GooglePlayService, Resolution started. Error Dialoge Shown");
            }
            else
            {
                informer.updatedInfo("This device is not supported. GooglePlayServices Not Found.");
            }
            return false;
        }
        informer.updatedInfo("GooglePlayServices Found.");
        return true;
    }
    /**
     *  @Definition starts Location Updates and checks for timeout period if location is found or not
     */
    private void startLocationUpdates() {
        this.mLocationRequest = LocationRequest.create();
        this.mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.mLocationRequest.setInterval(updateInterval);
        this.mLocationRequest.setFastestInterval(fastestInterval);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

        handler  = new Handler();
        settingtask = new Runnable() {
            @Override
            public void run() {
                //special check for googleApi connected cause randomly switches are possible
                if(!locationUpdated || !googleAPIConnected)
                {
                    try
                    {
                       showSettingsAlert();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    informer.updatedInfo("LocationService not able to get Location");
                }
                else
                {
                    if(dialoge!=null)
                    {
                        dialoge.dismiss();
                        dialoge = null;
                        informer.updatedInfo("Location found and dismissed the alert dialoge");
                    }
                    informer.updatedInfo("Timeout expired and Location found");
                }

            }
        };
        handler.postDelayed(settingtask, timeout);
    }

    /**
     *  @Definition stops Location Updates
     */
    private boolean removeLocationUpdates() {
        if(googleAPIConnected)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates
                    (
                    mGoogleApiClient, this);
            informer.updatedInfo("removeLocationUpdates success");
            return true;
        }
        informer.updatedInfo("removeLocationUpdates Failed");
        return false;
    }


    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        informer.updatedInfo("GoogleAPI Connection Failed "+arg0);

    }

    @Override
    public void onConnected(Bundle arg0) {

        informer.updatedInfo("GoogleAPI Connected ");
        googleAPIConnected =true;
        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        informer.updatedInfo("GoogleAPI Suspended "+arg0);

    }

    @Override
    public void onLocationChanged(Location arg0) {
        if(arg0!=null)
        {
            locationUpdated = true;
            informer.updatedInfo("Location Changed");
            informer.locationUpdates(arg0);
        }
        else
        {
            informer.locationFailed("Location service started but location was provided null");
        }

    }
    /**
     * @Definition Showing AlertDialoge for no Location
     */

    private void  showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        // Setting Dialog Title
        alertDialog.setTitle("GPS Settings");
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setMessage("GPS or Netwrok is not enabled. Do you want to go to settings menu?");


        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(intent);
                dialoge = null;
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {

                dialog.dismiss();
                dialoge = null;
            }
        });

        // Showing Alert Message
        try {
            this.dialoge = alertDialog.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}