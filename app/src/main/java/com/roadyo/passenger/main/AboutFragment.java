package com.roadyo.passenger.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ourcabby.passenger.R;
import com.threembed.utilities.Utility;

public class AboutFragment extends Fragment implements android.view.View.OnClickListener
{
	private TextView Rate_GooglePlay,Legal;
	private TextView Roadyo,instagram;
	@Override
	public android.view.View onCreateView(android.view.LayoutInflater inflater,
										  android.view.ViewGroup container,
										  android.os.Bundle savedInstanceState)
	{

		View view  = inflater.inflate(R.layout.about_fragment, null);
		initLayoutId(view);
		initLayoutId(view);

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
	private void initLayoutId(View view)
	{

		Rate_GooglePlay = (TextView)view.findViewById(R.id.btn_rate_in_google_play);
		Legal = (TextView)view.findViewById(R.id.btn_legal);
		Roadyo = (TextView)view.findViewById(R.id.txt_roadyo_net);
		instagram = (TextView)view.findViewById(R.id.instagram);

		Rate_GooglePlay.setOnClickListener(this);
		Legal.setOnClickListener(this);
		Roadyo.setOnClickListener(this);
		instagram.setOnClickListener(this);

		MainActivityDrawer.driver_tip.setVisibility(View.INVISIBLE);
	}
	@Override
	public void onClick(View v)
	{
		if (v.getId()==R.id.txt_roadyo_net)
		{

			if (Utility.isNetworkAvailable(getActivity()))
			{
				String url = "http://ourcabby.com/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
			else
			{

				Utility.ShowAlert("Network connection fail", getActivity());
			}

		}
		if (v.getId()==R.id.btn_rate_in_google_play)
		{
			if (Utility.isNetworkAvailable(getActivity()))
			{
				Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
				Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
				try {
					startActivity(goToMarket);
				} catch (ActivityNotFoundException e)
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
				}
			}
			else
			{
				Utility.ShowAlert("Network connection fail", getActivity());

			}
		}
		if (v.getId()==R.id.instagram)
		{
			if (Utility.isNetworkAvailable(getActivity()))
			{
				String url = "https://www.facebook.com/ourcabby/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
			else
			{
				Utility.ShowAlert("Network connection fail", getActivity());
			}
		}
		if (v.getId()==R.id.btn_legal)
		{
			statrtncview();
		}

	}

	private void statrtncview()
	{

		Intent intent=new Intent(getActivity(), TermsActivity.class);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);

	}
}
