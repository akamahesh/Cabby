package com.roadyo.passenger.main;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.ourcabby.passenger.R;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;

public class InviteFragment extends Fragment implements OnClickListener
{
	private  View view;
	private TextView coupon_code;
	private SessionManager session;
    private String shareContent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		if(view != null) 
		{
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}

		try 
		{
			view = inflater.inflate(R.layout.invite_screen, container, false);
			session = new SessionManager(getActivity());
			initialize();

		} catch (InflateException e)
		{
			Utility.printLog("onCreateView  InflateException "+e);
		}
		
		coupon_code.setText(session.getCouponCode());


		Utility.printLog("coupon code"+session.getCouponCode());
		if(Utility.isNetworkAvailable(getActivity()))
		{
		}
		else
		{
			Intent homeIntent=new Intent("com.ourcabby.passenger.internetStatus");
			homeIntent.putExtra("STATUS", "0");
			getActivity().sendBroadcast(homeIntent);
		}

		return view;
	}

	private void initialize()
	{
		/*facebook_share = (ImageView)view.findViewById(R.id.facebook_share);
		twitter_share = (ImageView)view.findViewById(R.id.twitter_share);
		message_share = (ImageView)view.findViewById(R.id.message_share);
		email_share = (ImageView)view.findViewById(R.id.email_share);*/
		coupon_code = (TextView)view.findViewById(R.id.share_code);
		RelativeLayout invite_friends_layout = (RelativeLayout) view.findViewById(R.id.invite_friends_layout);
		TextView share_text = (TextView) view.findViewById(R.id.share_text);
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView invite_friends = (TextView) view.findViewById(R.id.invite_friends);
	/*	ImageView whatsapp_share = (ImageView)view.findViewById(R.id.whatsapp_share);
		ImageView telegram = (ImageView)view.findViewById(R.id.telegram);*/

		Typeface typeFaceBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Bold.ttf");
		Typeface typeFaceRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");
		share_text.setTypeface(typeFaceBold);
		coupon_code.setTypeface(typeFaceBold);
		title.setTypeface(typeFaceRegular);
		invite_friends.setTypeface(typeFaceRegular);

		/*facebook_share.setOnClickListener(this);
		twitter_share.setOnClickListener(this);
	message_share.setOnClickListener(this);
		email_share.setOnClickListener(this);
		whatsapp_share.setOnClickListener(this);
		telegram.setOnClickListener(this);*/

		invite_friends_layout.setOnClickListener(this);
		shareContent="Hi, download and sign up on OurCabby using my code "+session.getCouponCode()+" and get an exclusive promo code!\n" +
				"To download go to"+"https://play.google.com/store/apps/details?id=com.ourcabby.passenger";
		MainActivityDrawer.driver_tip.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View v) 
	{
		if(v.getId()==R.id.invite_friends_layout)
		{
			Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,getResources().getString(R.string.registeron)+" " +getResources().getString(R.string.app_name));
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareContent);
			startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
		}
	}

	/**
	 * Indicates whether the specified app ins installed and can used as an intent. This
	 * method checks the package manager for installed packages that can
	 * respond to an intent with the specified app. If no suitable package is
	 * found, this method returns false.
	 *
	 * @param context The application's environment.
	 * @param appName The name of the package you want to check
	 *
	 * @return True if app is installed
	 */
	public static boolean isAppAvailable(Context context, String appName)
	{
		PackageManager pm = context.getPackageManager();
		try
		{
			pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
			return true;
		}
		catch (PackageManager.NameNotFoundException e)
		{
			return false;
		}
	}
}
