package com.roadyo.passenger.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.ourcabby.passenger.R;
import com.threembed.utilities.SessionManager;

import org.w3c.dom.Text;

public class TermsActivity extends Activity implements OnClickListener
{
	private TextView terms1,terms2;
	private ImageButton back;
	private RelativeLayout rl_TermsandCond;
	private SessionManager sessionManager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.termsandcond);

		iniializeVariables();
	}

	private void iniializeVariables()
	{
		terms1=(TextView)findViewById(R.id.btn_terms_conditions);
		terms2=(TextView)findViewById(R.id.btn_privacy_policy);
		back=(ImageButton)findViewById(R.id.back_button_terms);
		rl_TermsandCond=(RelativeLayout)findViewById(R.id.rl_TermsandCond);
		sessionManager=new SessionManager(this);

		terms1.setOnClickListener(this);
		terms2.setOnClickListener(this);
		back.setOnClickListener(this);
		rl_TermsandCond.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_terms_conditions)
		{
			Intent intent=new Intent(TermsActivity.this,WebViewActivity.class);
			intent.putExtra("URL","http://138.197.111.208/terms_of_use.php");
			startActivity(intent);
			overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
		}

		if(v.getId()==R.id.btn_privacy_policy)
		{
			Intent intent=new Intent(TermsActivity.this,WebViewActivityPrivacy.class);
			intent.putExtra("URL","http://138.197.111.208/privacy_policy.php");
			startActivity(intent);
			overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
		}

		if(v.getId()==R.id.back_button_terms)
		{
			finish();
			overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
		}
		if(v.getId()==R.id.rl_TermsandCond)
		{
			finish();
			overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
		}
	}


	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
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
