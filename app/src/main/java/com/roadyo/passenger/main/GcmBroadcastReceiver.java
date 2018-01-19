package com.roadyo.passenger.main;

import java.util.List;

import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver 
{
	Bundle extras;
	@Override
	public void onReceive(final Context context, Intent intent)
	{
		/*extras = intent.getExtras();
		 Utility.printLog("","GcmBroadcastReceiver Intent in Driver App : "+intent);
		 String action=extras.getString("action");
		 String type = extras.getString("apptType");


		 Utility.printLog("","GcmBroadcastReceiver Action in B.Reciver "+action);
			extras = intent.getExtras();
			Utility.printLog("GcmBroadcastReceiver extras in Reciver ="+extras);
			Utility.printLog("GcmBroadcastReceiver action in Reciver ="+action+" type="+type);*/

		Utility.printLog("Inside onReceive GcmBroadcastReceiver");
		Utility.printLog("GcmBroadcastReceiver Intent in Reciver : "+intent);

		extras = intent.getExtras();
		String action=extras.getString("action");
		String message=extras.getString("payload");
		String r=extras.getString("r");

		Utility.printLog("GcmBroadcastReceiver extras in Reciver ="+extras);
		SessionManager session =new SessionManager(context);
		boolean isbkrnd=isApplicationSentToBackground(context);

		Utility.printLog("isApplicationSentToBackground "+isbkrnd);



		if("10".equals(action))
		{
			session.setDriverOnWay(false);
			Utility.printLog("Wallah set as fasle gcm 10");
			session.setDriverArrived(false);
			session.setInvoiceRaised(false);
			session.setTripBegin(false);

			session.setDriverCancelledApt(true);

			if(r.equals("4"))
			{
              session.setCancellationType("Passenger did not come.");
			}
			else if(r.equals("5"))
			{
				session.setCancellationType("Address wrong.");

			}
			else if(r.equals("6"))
			{
				session.setCancellationType("Passenger asked to cancel.");

			}
			else if(r.equals("7"))
			{
				session.setCancellationType("Dont charge customer.");

			}
			else
			{
				session.setCancellationType("Others.");

			}

			if(HomePageFragment.visibleStatus())
			{
				Intent homeIntent=new Intent("com.threembed.driverapp.activity.push.popup");
				context.sendBroadcast(homeIntent);
			}
			else if(!isbkrnd)
			{
				session.setDriverCancelledApt(false);

				Intent i = new Intent();
				i.setClassName("com.ourcabby.passenger","com.roadyo.passenger.main.MainActivityDrawer");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(i);  
			} 
		}

		else if("12".equals(action))
		{
			session.setPayload(message);
			session.setIsUserRejectedFromAdmin("true");

			if (HomePageFragment.visibleStatus())
			{
				Intent homeIntent=new Intent("com.threembed.driverapp.activity.push.popup");//com.threembed.driverapp.activity.push.popup
				Utility.printLog("extras=" + extras);
				session.setIsLogin(false);
				homeIntent.putExtras(extras);
				context.sendBroadcast(homeIntent);				
			}
			else if (!isbkrnd)
			{
				session.setIsLogin(false);
				Intent i = new Intent(context,SplashActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i); 
			}
		}
		
		
		/*Bundle[{action=6, payload=Driver on way, d=2015-02-15 12:25:00, e=Guddugaurav@mobifyi.com, r=4.333333333333333, t=2, dt=20150215122500,
		id=5954, ph=123456789, ltg=13.02871,77.589651666667, pic=imageTue02102015181131.jpeg, from=440129443070, 
		sname=Guddu Gaurav, collapse_key=do_not_collapse}]*/
		//Driver on the Way
		// if(action.equals("6") && !session.isDriverOnWay())
		
		//if(type!=null)
		 if("6".equals(action) && !session.isDriverOnWay() )//later booking on the way//&& "2".equals(type)
		{
			session.setDriverOnWay(true);
			Utility.printLog("Wallah set as true gcm 6 later");
			session.setDriverArrived(false);
			session.setTripBegin(false);
			session.setInvoiceRaised(false);


			String latLon=extras.getString("ltg");
			Utility.printLog("GcmBroadcastReceiver LATLON: "+latLon);
			String[] temp=latLon.split(",");

			Utility.printLog("GcmBroadcastReceiver Temp[0]: "+temp[0]);
			Utility.printLog("GcmBroadcastReceiver Temp[1]: "+temp[1]);

			session.storeDocName(extras.getString("sname"));
			session.storeDocPic(VariableConstants.IMAGE_BASE_URL+extras.getString("pic"));
			session.storeDocPH(extras.getString("ph"));
			session.storeLat_DOW(temp[0]);
			session.storeLon_DOW(temp[1]);
			session.storeAptDate(extras.getString("dt"));
			session.storeDriverEmail(extras.getString("e"));
			session.storeDriverRating(extras.getString("r"));

			if(HomePageFragment.visibleStatus())
			{
				Intent homeIntent=new Intent("com.threembed.driverapp.activity.push.popup");
				homeIntent.putExtra("ACTION","ON_THE_WAY");
				context.sendBroadcast(homeIntent);
			}
			else if(!isbkrnd)
			{
				Intent i = new Intent();
				i.setClassName("com.ourcabby.passenger","com.roadyo.passenger.main.MainActivityDrawer");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(i);
			}
		}

		//Driver Reached
		else if("7".equals(action) && (!session.isDriverOnArrived() && !session.getBookingId().equals("0")))//&& ("1".equals(type)
		{
			session.setDriverOnWay(false);
			session.setDriverArrived(true); //Roshani
			session.setTripBegin(false);
			session.setInvoiceRaised(false);

			session.storeDocName(extras.getString("sname"));
			session.storeDocPic(VariableConstants.IMAGE_BASE_URL + extras.getString("pic"));
			session.storeDocPH(extras.getString("ph"));
			session.storeAptDate(extras.getString("dt"));
			session.storeDriverEmail(extras.getString("smail"));
			session.storeDriverRating(extras.getString("r"));


			if(HomePageFragment.visibleStatus())
			{
				Intent homeIntent=new Intent("com.threembed.driverapp.activity.push.popup");
				homeIntent.putExtra("ACTION","REACHED");
				context.sendBroadcast(homeIntent);
			}
			else if(!isbkrnd)
			{
				Intent i = new Intent();
				i.setClassName("com.ourcabby.passenger","com.roadyo.passenger.main.MainActivityDrawer");
				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);  
			}
		}
		else if( "7".equals(action)&& (!session.isDriverOnArrived()))// && ("2".equals(type))
		{
			session.setDriverOnWay(false);
			Utility.printLog("Wallah set as fasle gcm 7 later");
			session.setDriverArrived(true);//roshani
			session.setTripBegin(false);
			session.setInvoiceRaised(false);

			session.storeDocName(extras.getString("sname"));
			session.storeDocPic(VariableConstants.IMAGE_BASE_URL + extras.getString("pic"));
			session.storeDocPH(extras.getString("ph"));
			session.storeAptDate(extras.getString("dt"));
			session.storeDriverEmail(extras.getString("smail"));
			session.storeDriverRating(extras.getString("r"));


			if(HomePageFragment.visibleStatus())
			{
				Intent homeIntent=new Intent("com.threembed.driverapp.activity.push.popup");
				homeIntent.putExtra("ACTION","REACHED");
				context.sendBroadcast(homeIntent);
			}
			else if(!isbkrnd)
			{
				Intent i = new Intent();
				i.setClassName("com.ourcabby.passenger","com.roadyo.passenger.main.MainActivityDrawer");
				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);  
			}
		
		}

		//journey begin
		// if(!MainActivityDrawer.DriverInvoiceRaised)
		else if("8".equals(action) && (!session.isTripBegin() && !session.getBookingId().equals("0")))//&& ("1".equals(type) )
		{

			/*Bundle[{action=8, payload=Journey started, t=2, dt=2015-2-15 12:25:00, id=5954, ph=123456789, 
					pic=imageTue02102015181131.jpeg, from=440129443070, smail=Guddugaurav@mobifyi.com, 
					sname=Guddu Gaurav, collapse_key=do_not_collapse}]*/

			session.setDriverOnWay(false);
			session.setDriverArrived(false);
			session.setTripBegin(true);//roshani
			session.setInvoiceRaised(false);

			session.storeDocName(extras.getString("sname"));
			session.storeDocPic(VariableConstants.IMAGE_BASE_URL + extras.getString("pic"));
			session.storeDocPH(extras.getString("ph"));

			session.storeAptDate(extras.getString("dt"));
			session.storeDriverEmail(extras.getString("smail"));
			session.storeDriverRating(extras.getString("r"));
			
			if(HomePageFragment.visibleStatus())
			{
				Intent homeIntent=new Intent("com.threembed.driverapp.activity.push.popup");
				homeIntent.putExtra("ACTION","COMPLETE");
				context.sendBroadcast(homeIntent);
			}
			else if(!isbkrnd)
			{
				Intent i = new Intent();
				i.setClassName("com.ourcabby.passenger","com.roadyo.passenger.main.MainActivityDrawer");
				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);  
			}
		}
		else if(( "8".equals(action)) && (!session.isTripBegin()))//"2".equals(type) &&
		{
			/*Bundle[{action=8, payload=Journey started, t=2, dt=2015-2-15 12:25:00, id=5954, ph=123456789, 
					pic=imageTue02102015181131.jpeg, from=440129443070, smail=Guddugaurav@mobifyi.com, 
					sname=Guddu Gaurav, collapse_key=do_not_collapse}]*/

			session.setDriverOnWay(false);
			Utility.printLog("Wallah set as fasle gcm 8 later");
			session.setDriverArrived(false);
			session.setTripBegin(true);
			session.setInvoiceRaised(false);

			session.storeDocName(extras.getString("sname"));
			session.storeDocPic(VariableConstants.IMAGE_BASE_URL+extras.getString("pic"));
			session.storeDocPH(extras.getString("ph"));

			session.storeAptDate(extras.getString("dt"));
			session.storeDriverEmail(extras.getString("smail"));
			session.storeDriverRating(extras.getString("r"));
			
			if(HomePageFragment.visibleStatus())
			{
				Intent homeIntent=new Intent("com.threembed.driverapp.activity.push.popup");
				homeIntent.putExtra("ACTION","COMPLETE");
				context.sendBroadcast(homeIntent);
			}
			else if(!isbkrnd)
			{
				Intent i = new Intent();
				i.setClassName("com.ourcabby.passenger","com.roadyo.passenger.main.MainActivityDrawer");
				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);  
			}
		}

		//Apointment Complete(INVOICE RAISED)
		else if(( ("9".equals(action))))//"1".equals(type) &&
		{
			session.setDriverOnWay(false);
			session.setDriverArrived(false);
			session.setTripBegin(false);
			session.setInvoiceRaised(true);

			session.storeDocName(extras.getString("sname"));
			session.storeDriverEmail(extras.getString("smail"));
			

			if(HomePageFragment.visibleStatus())
			{
				Intent homeIntent=new Intent("com.threembed.driverapp.activity.push.popup");
				homeIntent.putExtra("ACTION","COMPLETE");
				context.sendBroadcast(homeIntent);
			}
			else if(!isbkrnd)
			{
				Intent i = new Intent();
				i.setClassName("com.ourcabby.passenger","com.roadyo.passenger.main.MainActivityDrawer");
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);  
			}
		}

		ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
		startWakefulService(context, (intent.setComponent(comp)));
		setResultCode(Activity.RESULT_OK);
	}

	public static boolean isApplicationSentToBackground(final Context context)
	{
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty())
		{
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) 
			{
				return true;
			}
		}
		return false;
	}

}
