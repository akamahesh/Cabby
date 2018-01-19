package com.roadyo.passenger.main;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ourcabby.passenger.R;
import com.threembed.utilities.VariableConstants;

public class GcmIntentService extends IntentService {
	private static final String TAG = "GcmIntentService";
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	Bundle extras;


	public GcmIntentService() {
		super(VariableConstants.PROJECT_ID);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i("", "Intent in Doc App 2: " + intent);
		extras = intent.getExtras();

		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that GCM
			 * will be extended in the future with new message types, just ignore
			 * any message types you're not interested in, or that you don't
			 * recognize.
			 */
			if (GoogleCloudMessaging.
					MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				sendNotification("Send error: " + extras.toString(), "");
			} else if (GoogleCloudMessaging.
					MESSAGE_TYPE_DELETED.equals(messageType)) {
				sendNotification("Deleted messages on server: " +
						extras.toString(), "");
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.
					MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				// This loop represents the service doing some work.
				/*for (int i=0; i<5; i++)
                {
                    Log.i(TAG, "Working... " + (i+1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try 
                    {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) 
                    {
                    }
                }*/
				Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
				// Post notification of received message.
				String message = null;
				String extra = null;
				String dis = "";
				String dur = "";

				if (extras != null && extras.toString().length() > 0) {
					message = extras.getString("payload");
					//message=extras.getString("alert");
					extra = extras.getString("action");
					dis = extras.getString("dis");
					dur = extras.getString("eta");
					Log.i(TAG, "extra............." + extra);


					Log.i("", "log DIS: " + dis);
					Log.i("", "log ETA: " + dur);
				}
				sendNotification(message, extra);
				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);

	}

	private void sendNotification(String msg, String action) {
		Log.i(TAG, "sendNotification  msg " + msg);
		mNotificationManager = (NotificationManager)
				this.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivityDrawer.class), 0);

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
						.setSmallIcon(R.drawable.ic_launcher)
						.setContentTitle(getResources().getString(R.string.app_name))
						.setStyle(new NotificationCompat.BigTextStyle()
								.bigText(msg))
						.setDefaults(Notification.DEFAULT_SOUND)
						.setContentText(msg);
		mBuilder.setContentIntent(contentIntent);
		// mNotificationManager. |= Notification.DEFAULT_SOUND;
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());


/*		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager)
				this.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, msg, when);*/

		String title = this.getString(R.string.app_name);
		PendingIntent intent = null;
		if (action != null) {
			Log.i("", "inside action!=null push");

			Log.i("", "action: " + action);
			if (action.equals("5")) {
				String payload = extras.getString("payload");
				Log.i("", "Data recived payload " + payload);

				String sname = extras.getString("sname");
				Log.i("", "Data recived sname " + sname);


				String dt = extras.getString("dt");
				Log.i("", "Data recived dt " + dt);

				String smail = extras.getString("smail");
				Log.i("", "Data recived smail " + smail);
			}

			Intent notificationIntent = new Intent(this, SplashActivity.class);
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
					Intent.FLAG_ACTIVITY_SINGLE_TOP);

			intent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		}

	/*	notification.setLatestEventInfo(this, title, msg, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;

		//notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.ipl_horn);

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notification);*/
	}

}