package com.roadyo.passenger.main;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.threembed.utilities.SessionManager;

public class NetworkChangeReceiver extends BroadcastReceiver 
{
	private Notification notification;
	SessionManager sessionManager;
	@Override
	public void onReceive(final Context context, final Intent intent) 
	{
		String status = NetworkService.getConnectivityStatusString(context);


        sessionManager=new SessionManager(context);
		String[] networkStatus = status.split(",");

		try
		{
			if("0".equals(networkStatus[1]))
			{
				MainActivityDrawer.network_error.setVisibility(View.VISIBLE);
			}
			else
			{
				MainActivityDrawer.network_error.setVisibility(View.GONE);

			}
		}
			catch (Exception e)
			{

			}
	}
}
