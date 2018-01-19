package com.roadyo.passenger.main;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubException;
import com.ourcabby.passenger.R;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivityDrawer extends FragmentActivity
{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;
	public static FrameLayout main_frame_layout;
	public static TextView navigationHeader;
	// used to store app title
	private CharSequence mTitle;
	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	ImageButton button_menu;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private int currentTabStatus=-1;
	private ActionBar actionBar;
	public static ImageView nav_img;
	private boolean backPressedToExitOnce = false;
	private Toast toast = null;
	private ProgressDialog dialogL;
	public static RelativeLayout network_error;
	public static RelativeLayout gpsErrorLayout;
	public boolean showpopup=false;
	public SessionManager session;
	public static Button edit_profile;
	public static Button cancel_trip;
	public static Button driver_tip;
	String typeId,emailId,fullName;
	public static TextView textForTip;
	private Pubnub pubnub;

	@Override
	protected void onResume()
	{
		showpopup=true;
		super.onResume();

		if(!session.getPresenceChn().equals(""))
		{
			new SubcribePresenceChannel().execute();
		}
		else
		{
          new SubcribePresenceChannelStatic().execute();
		}
	}

	public void setLocale(String lang) {

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


	/**
	 *subscribing to my server channel to listen all available drivers around you
	 */
	class SubcribePresenceChannel extends AsyncTask<String,Void,String>
	{
		@Override
		protected String doInBackground(String... params)
		{
			Utility.printLog("CONTROL INSIDE MyChannel Subscribe "+session.getChannelName());
			String[] new_channels=new String[1];
			new_channels[0]=session.getPresenceChn();
			subscribe(new_channels);
			return null;
		}
	}

	class SubcribePresenceChannelStatic extends AsyncTask<String,Void,String>
	{
		@Override
		protected String doInBackground(String... params)
		{
			Utility.printLog("CONTROL INSIDE MyChannel Subscribe "+"presenceChn_roadyo1.0_channel");
			String[] new_channels=new String[1];
			new_channels[0]="presenceChn_roadyo1.0_channel";
			subscribe(new_channels);
			return null;
		}
	}


	private void subscribe(String[] channels)
	{
		for(int i=0;i<channels.length;i++)
			//args.put("channel",session.getChannelName());
			try
			{
				pubnub.subscribe(channels, new Callback()
				{

					@Override
					public void successCallback(String channel, Object message)
					{
						Utility.printLog("success from presense" + message.toString());
					}
				});
			}
			catch (Exception e)
			{

			}
	}


	@Override
	protected void onPause()
	{

		killToast();
		super.onPause();

			String[] subscribed_channels1 = pubnub.getSubscribedChannelsArray();
			Utility.printLog("Appointments details subscribed_channels my channel=" + session.getChannelName());
			ArrayList<String> unsubscribeChannels1= new ArrayList<String>();
			for(int i=0;i<subscribed_channels1.length;i++)
			{
				Utility.printLog("Appointments details subscribed_channels at status 9" + i + " " + subscribed_channels1[i]);


				unsubscribeChannels1.add(subscribed_channels1[i]);

				if(unsubscribeChannels1.size()>0)
				{
					Utility.printLog("Appointments details channels unsubscribeChannels channel list size status 9=" + unsubscribeChannels1.size());
					String[] new_un_sub_channels=new String[unsubscribeChannels1.size()];
					new_un_sub_channels=unsubscribeChannels1.toArray(new_un_sub_channels);
					new BackgroundUnSubscribeChannels().execute(new_un_sub_channels);
				}

			}
		Utility.printLog("INSIDE ON PAUSE MAIN ACTIVITY");

	}

	/**
	 * unsubscribing the drivers channels who are going out of the application
	 */
	class BackgroundUnSubscribeChannels extends AsyncTask<String,Void,String>
	{
		@Override
		protected String doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			Utility.printLog("CONTROL INSIDE UnSubscribe Channels");
			pubnub.unsubscribe(params);
			return null;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_main_drawer);
		session = new SessionManager(MainActivityDrawer.this);

		network_error =(RelativeLayout)findViewById(R.id.network_error_layout);
		gpsErrorLayout = (RelativeLayout)findViewById(R.id.gps_error_layout);
		main_frame_layout = (FrameLayout)findViewById(R.id.main_layout);
		Intent intent1= getIntent();
		typeId=intent1.getStringExtra("TYPEID");
		emailId=intent1.getStringExtra("EMAILID");
		fullName=intent1.getStringExtra("FULLNAME");

		pubnub = new Pubnub(VariableConstants.PUBNUB_PUBLISH_KEY, VariableConstants.PUBUB_SUBSCRIBE_KEY, "", true);
		pubnub.setUUID(session.getLoginId());
		try
		{
			pubnub.presence(session.getPresenceChn(), new Callback() {
				@Override
				public void successCallback(String channel, Object message)
				{
					Utility.printLog("success from presence "+message);
				}
			});
		} catch (PubnubException e) {
			e.printStackTrace();
			Utility.printLog("success from presence " + e);

		}



		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

	    mDrawerList = (ListView) findViewById(R.id.list_slidermenu12);


		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// What's hot, We  will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));

		//navDrawerItems.add(new NavDrawerItem("    " + navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));


		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
		mDrawerList.setAdapter(adapter);

		actionBar = getActionBar();
		actionBar.setIcon(android.R.color.transparent);
		//actionBar.setHomeAsUpIndicator(R.drawable.home_btn_menu_on);
		initActionbar() ;



		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.menu_btn_selector, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		)
	{
			public void onDrawerClosed(View view) {
				//getActionBar().setTitle(mTitle);

				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				//getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if(savedInstanceState == null)
			displayView(0);
		navigationHeader.setVisibility(View.INVISIBLE);
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}




	private void initActionbar()
	{
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.background)));
		LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout mActionBarCustom = (LinearLayout)inflater.inflate(R.layout.customactionbar, null);
		button_menu = (ImageButton)mActionBarCustom.findViewById(R.id.button_menu);
		button_menu.setVisibility(View.GONE);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setCustomView(mActionBarCustom);
		actionBar.setDisplayShowCustomEnabled(true);
		navigationHeader=(TextView)mActionBarCustom.findViewById(R.id.navigationHeader);
		nav_img = (ImageView)mActionBarCustom.findViewById(R.id.nav_img);
		edit_profile = (Button)mActionBarCustom.findViewById(R.id.edit_profile);
		cancel_trip= (Button) mActionBarCustom.findViewById(R.id.cancel_trip);
		driver_tip= (Button) mActionBarCustom.findViewById(R.id.driverTip);
		textForTip= (TextView) mActionBarCustom.findViewById(R.id.textForTip);

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		Utility.printLog("options in onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Utility.printLog("options in onOptionsItemSelected");
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item))
		{

			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId())
		{

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		Utility.printLog("options in onPrepareOptionsMenu");
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments

		Fragment fragment = null;
		switch(position)
		{
			case 0:
				currentTabStatus=0;
				fragment = new HomePageFragment();
				nav_img.setVisibility(View.VISIBLE);
				navigationHeader.setVisibility(View.INVISIBLE);
				edit_profile.setVisibility(View.INVISIBLE);
				break;

			case 1:
				currentTabStatus=1;
				fragment = new WalletFragment();
				nav_img.setVisibility(View.INVISIBLE);
				navigationHeader.setVisibility(View.VISIBLE);
				cancel_trip.setVisibility(View.INVISIBLE);
				edit_profile.setVisibility(View.INVISIBLE);
				break;

			case 2:
				currentTabStatus=2;
				fragment=new Appointments();
				nav_img.setVisibility(View.INVISIBLE);
				navigationHeader.setVisibility(View.VISIBLE);
				cancel_trip.setVisibility(View.INVISIBLE);
				edit_profile.setVisibility(View.INVISIBLE);
				break;

			case 3:
				//getActionBar().setTitle("Appointments");
				//getActionBar().setIcon(null);
				currentTabStatus=3;
				fragment=new ProfileFragment();
				nav_img.setVisibility(View.INVISIBLE);
				navigationHeader.setVisibility(View.VISIBLE);
				cancel_trip.setVisibility(View.INVISIBLE);
				edit_profile.setVisibility(View.VISIBLE);


				break;

			case 5:
				currentTabStatus=5;
				fragment = new InviteFragment();
				nav_img.setVisibility(View.INVISIBLE);
				navigationHeader.setVisibility(View.VISIBLE);
				cancel_trip.setVisibility(View.INVISIBLE);
				edit_profile.setVisibility(View.INVISIBLE);

				break;


			case 4:
				//getActionBar().setTitle("Payments");
				//getActionBar().setIcon(null);
				currentTabStatus=4;
				fragment=new Support_new();
				nav_img.setVisibility(View.INVISIBLE);
				navigationHeader.setVisibility(View.VISIBLE);
				cancel_trip.setVisibility(View.INVISIBLE);
				edit_profile.setVisibility(View.INVISIBLE);
				break;
			case 6:
				currentTabStatus=6;
				fragment=new AboutFragment();
				nav_img.setVisibility(View.INVISIBLE);
				navigationHeader.setVisibility(View.VISIBLE);
				cancel_trip.setVisibility(View.INVISIBLE);
				edit_profile.setVisibility(View.INVISIBLE);
				break;

			default:
				break;
		}



		if (fragment != null)
		{

			android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
			android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.frame_container, fragment);
			fragmentTransaction.commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			if(currentTabStatus==0)
			{
				//setTitle("Home");
				nav_img.setVisibility(View.VISIBLE);
				edit_profile.setVisibility(View.INVISIBLE);

			}



			else
				setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}

	}


	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		navigationHeader.setText(mTitle);
		//getActionBar().setTitle(mTitle);
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public static ProgressDialog GetProcessDialog(Activity activity)
	{
		// prepare the dialog box
		ProgressDialog dialog = new ProgressDialog(activity,5);
		// make the progress bar cancelable
		dialog.setCancelable(false);
		// set a message text
		dialog.setMessage(activity.getResources().getString(R.string.loggingOut));
		// show it
		return dialog;
	}

	public static String getDeviceId(Context context)
	{
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();

	}

	@Override
	public void onBackPressed()
	{
		if(currentTabStatus==0)
		{

          if(VariableConstants.CONFIRMATION_CALLED)
		  {
			  MainActivityDrawer.this.getActionBar().show();

			  VariableConstants.CONFIRMATION_CALLED=false;
			  currentTabStatus=0;
			  Fragment fragment = new HomePageFragment();
			  android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
			  android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			  fragmentTransaction.replace(R.id.frame_container, fragment);
			  fragmentTransaction.commit();

			  // update selected item and title, then close the drawer
			  mDrawerList.setItemChecked(0, true);
			  mDrawerList.setSelection(0);
			  //setTitle("Home");
			  nav_img.setVisibility(View.VISIBLE);
			  navigationHeader.setVisibility(View.GONE);

			  mDrawerLayout.closeDrawer(mDrawerList);
		  }
			else
		  {
			  if (backPressedToExitOnce) {
				 // finish();
				  moveTaskToBack(true);
				  overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			  } else {
				  this.backPressedToExitOnce = true;
				  showToast("Press again to exit");
				  new Handler().postDelayed(new Runnable() {

					  @Override
					  public void run() {
						  backPressedToExitOnce = false;
					  }
				  }, 2000);
			  }
		  }


		}
		else
		{
			currentTabStatus=0;
			Fragment fragment = new HomePageFragment();
			android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
			android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.frame_container, fragment);
			fragmentTransaction.commit();

			mDrawerList.setItemChecked(0, true);
			mDrawerList.setSelection(0);
			nav_img.setVisibility(View.VISIBLE);
			navigationHeader.setVisibility(View.GONE);
			edit_profile.setVisibility(View.GONE);

			mDrawerLayout.closeDrawer(mDrawerList);


		}


	}

	private void showToast(String message)
	{
		if (this.toast == null)
		{
			// Create toast if found null, it would he the case of first call only
			this.toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

		} else if (this.toast.getView() == null) {
			// Toast not showing, so create new one
			this.toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

		} else {
			// Updating toast message is showing
			this.toast.setText(message);
		}

		// Showing toast finally
		this.toast.show();
	}

	private void killToast() {
		if (this.toast != null) {
			this.toast.cancel();
		}
	}





}
