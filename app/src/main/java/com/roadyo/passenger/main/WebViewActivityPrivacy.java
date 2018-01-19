package com.roadyo.passenger.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.flurry.android.FlurryAgent;
import com.ourcabby.passenger.R;

public class WebViewActivityPrivacy extends Activity implements OnClickListener
{
	private ImageButton back;
	private RelativeLayout RL_Webview;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webviewprivacy);
		
		initialize();
		
		String url="";
		WebView mWeb=(WebView)findViewById(R.id.webView1);
		Bundle bundle=getIntent().getExtras();
		
		if(bundle!=null)
		{
			url=bundle.getString("URL");
			mWeb.loadUrl(url);
		}
	}

	private void initialize() 
	{
		back=(ImageButton)findViewById(R.id.login_back_button);
		RL_Webview = (RelativeLayout)findViewById(R.id.rl_webview);
		RL_Webview.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
	}

	@Override
	public void onClick(View v)
	{
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
