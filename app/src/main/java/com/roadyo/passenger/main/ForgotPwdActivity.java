package com.roadyo.passenger.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.ourcabby.passenger.R;
import com.threembed.utilities.OkHttpRequestObject;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

public class ForgotPwdActivity extends Activity implements OnClickListener
{
	private EditText email;
	private Button submit;
	private RelativeLayout Back;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forgot_pwd);
		intialize();
		
	}

	private void intialize() 
	{
		email=(EditText)findViewById(R.id.email_frgt_pwd);
		Back=(RelativeLayout)findViewById(R.id.back_forgot_password);
		submit=(Button)findViewById(R.id.submit_frgt_pwd);
		
		Back.setOnClickListener(this);
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		if(v.getId()==R.id.back_forgot_password)
		{
			finish();
			overridePendingTransition(R.anim.anim_three, R.anim.anim_four); 
			return;
		}
		
		if(v.getId()==R.id.submit_frgt_pwd)
		{
			if(!email.getText().toString().isEmpty())
			{
				if(validateEmail(email.getText().toString()))
				{
					if(Utility.isNetworkAvailable(ForgotPwdActivity.this))
						 BackgroundFrgtPwd();
					else
						Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), ForgotPwdActivity.this);
				}
				else
				{
					Utility.ShowAlert(getResources().getString(R.string.enter_valid_email), ForgotPwdActivity.this);
				}
			}
			else
			{
				Utility.ShowAlert(getResources().getString(R.string.email_empty), ForgotPwdActivity.this);
			}
			return;
		}
	}

	private void BackgroundFrgtPwd() {
		JSONObject jsonObject = new JSONObject();
		final BookAppointmentResponse[] response = new BookAppointmentResponse[1];
		final SessionManager sessionManager=new SessionManager(ForgotPwdActivity.this);
		final ProgressDialog dialogL=com.threembed.utilities.Utility.GetProcessDialogNew(ForgotPwdActivity.this,getResources().getString(R.string.please_wait));
		dialogL.setCancelable(true);
		if (dialogL!=null)
		{
			dialogL.show();
		}
		try {
			jsonObject.put("ent_email",email.getText().toString());
			jsonObject.put("ent_user_type","2");
			Utility.printLog("ent_email: "+email.getText().toString());
			Utility.printLog("ent_email: "+email.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "forgotPassword", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				if(result!=null)
				{
					Gson gson = new Gson();
					response[0] =gson.fromJson(result, BookAppointmentResponse.class);
					if (dialogL!=null)
					{
						dialogL.dismiss();
					}

					if(response[0] !=null)
					{
						if(response[0].getErrFlag().equals("0"))
						{
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									ForgotPwdActivity.this);

							// set title
							alertDialogBuilder.setTitle(getResources().getString(R.string.message));

							// set dialog message
							alertDialogBuilder
									.setMessage(response[0].getErrMsg())
									.setCancelable(false)

									.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener()
									{
										public void onClick(DialogInterface dialog,int id)
										{
											// if this button is clicked, just close
											// the dialog box and do nothing
											dialog.cancel();
											finish();
											overridePendingTransition(R.anim.anim_three, R.anim.anim_four);
										}
									});

							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
							// show it
							alertDialog.show();

						}
						else
						{
							Utility.ShowAlert(response[0].getErrMsg(), ForgotPwdActivity.this);
						}
					}
					else
					{
						Utility.ShowAlert(getResources().getString(R.string.server_error),ForgotPwdActivity.this);
					}
				}

			}
			@Override
			public void onError(String error) {
				if (dialogL!=null)
				{
					dialogL.cancel();
				}
				Utility.printLog("on error for the login "+error);
				Toast.makeText(ForgotPwdActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}

	public boolean validateEmail(String email)
	{
	    boolean isValid = false;
	   

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches())
	    {
	        isValid = true;
	    }
	    
	    return isValid;
	}
	
	@Override
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(R.anim.anim_three, R.anim.anim_four); 
		
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
