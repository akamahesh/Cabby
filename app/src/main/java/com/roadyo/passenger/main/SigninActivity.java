
package com.roadyo.passenger.main;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.roadyo.passenger.pojo.LoginResponse;
import com.ourcabby.passenger.R;
import com.threembed.utilities.AppLocationService;
import com.threembed.utilities.GpsListener;
import com.threembed.utilities.OkHttpRequestObject;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

public class SigninActivity extends Activity implements OnClickListener
{
	private static final String TAG = "SigninActivity";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private EditText username,password;
	private TextView forgot_password;
	private ImageButton back;
	private Button login;
	private RelativeLayout RL_Signin;
	private GoogleCloudMessaging gcm;
	private static LoginResponse response;
	private SessionManager session;
	private double currentLatitude,currentLongitude;
	private String current_city_name;
	private AppLocationService appLocationService;
	private String regid,strServerResponse,jsonErrorParsing;
	private Context context;
	private String deviceid,SENDER_ID = VariableConstants.PROJECT_ID;
	Location gpsLocation ;
	private CallbackManager callbackManager;
	private static final int RC_SIGN_IN = 786;
	private GoogleApiClient mGoogleApiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);
		session=new SessionManager(SigninActivity.this);
		initialize();
		appLocationService = new AppLocationService(SigninActivity.this);

		gpsLocation	= appLocationService.getLocation(LocationManager.GPS_PROVIDER);

		if(gpsLocation != null)
		{
			currentLatitude = gpsLocation.getLatitude();
			currentLongitude = gpsLocation.getLongitude();
		}
		else
		{
			Location nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

			if(nwLocation != null)
			{
				currentLatitude = nwLocation.getLatitude();
				currentLongitude = nwLocation.getLongitude();
			}
		}

		if(checkPlayServices())
		{
			if (gcm == null)
			{
				gcm=GoogleCloudMessaging.getInstance(SigninActivity.this);
			}

			SessionManager session=new SessionManager(SigninActivity.this);
			regid=session.getRegistrationId();

			if(regid==null)
			{
				new BackgroundForRegistrationId().execute();
			}
			else
			{
				deviceid=getDeviceId(context);
			}


		}
		else
		{
			Utility.printLog("No valid Google Play Services APK found.");
		}
	}



	private class BackgroundForRegistrationId extends AsyncTask<String, Void, String>
	{
		private ProgressDialog dialogL;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialogL=Utility.GetProcessDialog(SigninActivity.this);

			if (dialogL!=null) {
				dialogL.show();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			SessionManager session=new SessionManager(SigninActivity.this);

			try {

				deviceid=getDeviceId(SigninActivity.this);
				regid = gcm.register(SENDER_ID);

				session.storeRegistrationId(regid);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (dialogL!=null) {
				dialogL.dismiss();
			}

			if(regid==null)
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SigninActivity.this,5);

				// set title
				alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

				// set dialog message
				alertDialogBuilder
						.setMessage(getResources().getString(R.string.slow_internet_connection))
						.setCancelable(false)
				/*.setPositiveButton("Refresh",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {

						}
					  })*/
						.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								//closing the application
								finish();
							}
						});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}
		}
	}


	private void initialize()
	{
		username=(EditText)findViewById(R.id.user_name);
		password=(EditText)findViewById(R.id.password);
		forgot_password=(TextView)findViewById(R.id.forgot_password);
		back=(ImageButton)findViewById(R.id.login_back_btn);
		login=(Button)findViewById(R.id.login_btn);
		RL_Signin=(RelativeLayout)findViewById(R.id.rl_signin);
		RelativeLayout google_login_layout = (RelativeLayout) findViewById(R.id.google_login_layout);
		RelativeLayout fb_login_layout = (RelativeLayout) findViewById(R.id.fb_login_layout);

		if (fb_login_layout != null) {
			fb_login_layout.setOnClickListener(this);
		}
		if (google_login_layout != null) {
			google_login_layout.setOnClickListener(this);
		}

		/**
		 * for social login
		 */
		FacebookSdk.sdkInitialize(SigninActivity.this);
		callbackManager = CallbackManager.Factory.create();
		loginFacebookSdk();
		initializeForGoogle();

		login.setOnClickListener(this);
		forgot_password.setOnClickListener(this);
		back.setOnClickListener(this);
		RL_Signin.setOnClickListener(this);
	}

	private void initializeForGoogle() {
		// Configure sign-in to request the user's ID, email address, and basic profile. ID and
		// basic profile are included in DEFAULT_SIGN_IN.
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();

// Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();
	}

	// [START handleSignInResult]
	private void handleSignInResult(GoogleSignInResult result) {
		if (result.isSuccess()) {
			// Signed in successfully, show authenticated UI.
			GoogleSignInAccount acct = result.getSignInAccount();
			setGoogleData(acct);
			signOut();
		}
		Utility.printLog("google login status "+result.getStatus().getStatusCode());
	}

	private void setGoogleData(GoogleSignInAccount acct) {
		// Get account information
		Log.d("user_first_name_et",acct.getPhotoUrl()
				+"\t"+acct.getDisplayName()
				+"\t"+acct.getEmail()
				+"\t"+acct.getId()
		);
		String[] splitedWord = acct.getDisplayName().split("\\s+");
		Utility.printLog("get fname and last name "+splitedWord[0]+" "+splitedWord[1]);
		String emailId = acct.getEmail();
		String id = acct.getId();
		signOut();
		UserLoginForSocial(emailId,id,splitedWord[0],splitedWord[1], String.valueOf(acct.getPhotoUrl()),"3","");
	}


	private void signOut() {
		Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
				new ResultCallback<Status>() {
					@Override
					public void onResult(@NonNull Status status) {
					}
				});
	}

	/**
	 * <h>LoginFacebookSdk</h>
	 * <p>
	 *     This method is being called from onCreate method. this method is called
	 *     when user click on LoginWithFacebook button. it contains three method onSuccess,
	 *     onCancel and onError. if login will be successfull then success method will be
	 *     called and in that method we obtain user all details like id, email, name, profile pic
	 *     etc. onCancel method will be called if user click on facebook with login button and
	 *     suddenly click back button. onError method will be called if any problem occurs like
	 *     internet issue.
	 * </p>
	 */
	private void loginFacebookSdk()
	{
		LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject object, GraphResponse response) {
						Log.d("LoginActivity", response.toString());
						String fbid = object.optString("id");
						String fb_emailId = object.optString("email");
						String fb_firstName = object.optString("first_name");
						String fb_lastName = object.optString("last_name");
						String gender = object.optString("gender");
						String fb_pic = "https://graph.facebook.com/" + fbid + "/picture";
						Utility.printLog("fb response fbid"+fbid+" fb_emailId"+fb_emailId+" fb_firstName"+fb_firstName
								+" fb_lastName"+fb_lastName+" fb_pic"+fb_pic);
						UserLoginForSocial(fb_emailId,fbid,fb_firstName,fb_lastName,fb_pic,"2",gender);
					}
				});
				Bundle parameters = new Bundle();
				parameters.putString("fields", "id,name,email,gender, birthday,first_name,last_name");
				request.setParameters(parameters);
				request.executeAsync();
			}

			@Override
			public void onCancel() {
				Toast.makeText(getApplicationContext(),getString(R.string.network_connection_fail),Toast.LENGTH_LONG).show();
			}

			@Override
			public void onError(FacebookException error) {
				Toast.makeText(getApplicationContext(),getString(R.string.network_connection_fail),Toast.LENGTH_LONG).show();
				if (error instanceof FacebookAuthorizationException) {
					if (AccessToken.getCurrentAccessToken() != null) {
						LoginManager.getInstance().logOut();
					}
				}
			}
		});
	}

	private void UserLoginForSocial(final String fb_emailId, final String fbid, final String fb_firstName,
									final String fb_lastName, final String fb_pic, final String type, final String gender )
	{
		final ProgressDialog dialogL=com.threembed.utilities.Utility.GetProcessDialogNew(SigninActivity.this,getResources().getString(R.string.loggingIn));
		dialogL.setCancelable(false);
		dialogL.show();
		JSONObject jsonObject=new JSONObject();
		try
		{
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			jsonObject.put("ent_email",fb_emailId);
			jsonObject.put("ent_password", fbid);
			jsonObject.put("ent_dev_id",  deviceid);
			jsonObject.put("ent_push_token", session.getRegistrationId());
			jsonObject.put("ent_latitude", currentLatitude);
			jsonObject.put("ent_longitude", currentLongitude);
			jsonObject.put("ent_date_time", curenttime);
			jsonObject.put("ent_device_type","2");
			jsonObject.put("ent_login_type",type);
			Utility.printLog("params to login "+jsonObject);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL+"slaveLogin", jsonObject, new OkHttpRequestObject.JsonRequestCallback()
		{
			@Override
			public void onSuccess(String result)
			{
				dialogL.dismiss();
				Utility.printLog("reposne without parese "+result);
				getUserLoginInfoForSocial(result,fb_emailId,fbid,fb_firstName,fb_lastName,fb_pic,type,gender);
			}

			@Override
			public void onError(String error)
			{
				dialogL.dismiss();
				Utility.printLog("on error for the login "+error);
				Toast.makeText(getApplicationContext(),getString(R.string.network_connection_fail),Toast.LENGTH_LONG).show();
			}
		});
	}

	private void getUserLoginInfoForSocial(String result,String fb_emailId,String fbid,String fb_firstName,String fb_lastName,String fb_pic,String type,String gender)
	{
		if(!result.equals(""))
		{
			Gson gson = new Gson();
			LoginResponse response = gson.fromJson(result, LoginResponse.class);
			Utility.printLog("Login Response "+result+" "+fb_pic);
			if(response.getErrFlag().equals("0"))
			{
				VariableConstants.COUNTER=0;
				//success code
				session.storeCurrencySymbol("$");
				session.storeRegistrationId(regid);
				session.storeDeviceId(deviceid);
				session.storeSessionToken(response.getToken());
				session.setIsLogin(true);
				session.storeLoginId(fb_emailId);
				session.storeServerChannel(response.getServerChn());
				session.storeChannelName(response.getChn());
				session.storeCouponCode(response.getCoupon());
				session.setPresenceChn(response.getPresenseChn());
				//Move to map activity
				Intent intent=new Intent(SigninActivity.this,MainActivityDrawer.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			}

			else if(response.getErrFlag().equals("1") && response.getErrNum().equals("8"))
			{
				Utility.printLog("fb pic in login "+fb_pic);
				Intent intent=new Intent(SigninActivity.this,SignupActivity.class);
				intent.putExtra("EMAIL",fb_emailId);
				intent.putExtra("PASSWORD",fbid);
				intent.putExtra("FIRST_NAME",fb_firstName);
				intent.putExtra("LAST_NAME",fb_lastName);
				intent.putExtra("USER_PIC",fb_pic) ;
				intent.putExtra("TYPE",type) ;
				intent.putExtra("GENDER",gender) ;
				startActivity(intent);
			}

			else
			{
				Toast.makeText(getApplicationContext(),response.getErrMsg(),Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			Toast.makeText(getApplicationContext(),getString(R.string.network_connection_fail),Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onClick(View v)
	{
		if(v.getId()==R.id.login_back_btn)
		{
			session.setSplahVideo(true);
			finish();
			overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
		}
		if(v.getId()==R.id.rl_signin)
		{
			session.setSplahVideo(true);

			finish();
			overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
		}
		if(v.getId()==R.id.login_btn)
		{
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(login.getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);

			if(validateFields())
			{
				if(Utility.isNetworkAvailable(SigninActivity.this))
				{
					UserLogin();
				}
				else
				{
					showAlert(getResources().getString(R.string.network_connection_fail));
				}
			}
			return;
		}

		if(v.getId()==R.id.forgot_password)
		{
			Intent intent=new Intent(SigninActivity.this,ForgotPwdActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.anim_two, R.anim.anim_one);
			return;
		}
		if(v.getId()== R.id.fb_login_layout)
		{
			if (Utility.isNetworkAvailable(SigninActivity.this))
				LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
			else
				Toast.makeText(getApplicationContext(),getString(R.string.network_connection_fail),Toast.LENGTH_LONG).show();
		}
		if(v.getId()== R.id.google_login_layout)
		{
			signInWithGoogle();
		}
	}

	private void signInWithGoogle() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		callbackManager.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case RC_SIGN_IN:
				// Result returned from launching the Intent from
				//   GoogleSignInApi.getSignInIntent(...);
				GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
				handleSignInResult(result);
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void UserLogin() {
		JSONObject jsonObject = new JSONObject();
		final ProgressDialog dialogL=com.threembed.utilities.Utility.GetProcessDialogNew(SigninActivity.this,getResources().getString(R.string.loggingIn));
		dialogL.setCancelable(false);
		if (dialogL!=null)
		{
			dialogL.show();
		}
		try {
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			if (username.getText().toString().matches("[0-9]+")) {
				jsonObject.put("ent_email","+91"+username.getText().toString());
			}
			else {
				jsonObject.put("ent_email",username.getText().toString());
			}
			jsonObject.put("ent_password",password.getText().toString());
			jsonObject.put("ent_dev_id",deviceid);
			jsonObject.put("ent_push_token",regid);
			jsonObject.put("ent_latitude",""+currentLatitude);
			jsonObject.put("ent_longitude",""+currentLongitude);
			jsonObject.put("ent_device_type","2");
			jsonObject.put("ent_login_type","1");
			jsonObject.put("ent_date_time",curenttime);
			Utility.printLog("params to login "+jsonObject);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "slaveLogin", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				if (dialogL!=null)
				{
					dialogL.cancel();
				}
				strServerResponse = result;
				getUserLoginInfo(dialogL);
			}
			@Override
			public void onError(String error) {
				if (dialogL!=null)
				{
					dialogL.cancel();
				}
				Utility.printLog("on error for the login "+error);
				Toast.makeText(SigninActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}

	private void getUserLoginInfo(ProgressDialog dialogL)
	{
		dialogL.dismiss();
		Gson gson = new Gson();
		response = gson.fromJson(strServerResponse, LoginResponse.class);
		Utility.printLog("Login Response "+strServerResponse);

		if(response.getErrFlag().equals("0"))
		{
			VariableConstants.COUNTER=0;
			//success code
			session.storeCurrencySymbol("$");
			session.storeRegistrationId(regid);
			session.storeDeviceId(deviceid);
			session.storeSessionToken(response.getToken());
			session.storeDeviceId(deviceid);
			session.setIsLogin(true);
			session.storeLoginId(username.getText().toString());
			session.storeServerChannel(response.getServerChn());
			session.storeChannelName(response.getChn());
			session.storeCouponCode(response.getCoupon());
			session.setPresenceChn(response.getPresenseChn());
			//Move to map activity
			Intent intent=new Intent(SigninActivity.this,MainActivityDrawer.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
		}

		else
		{
			Toast.makeText(getApplicationContext(),response.getErrMsg(),Toast.LENGTH_SHORT).show();
		}

	}

	private boolean checkPlayServices()
	{
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if(resultCode != ConnectionResult.SUCCESS)
		{
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
			{
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			}
			else
			{
				finish();
			}
			return false;
		}
		return true;
	}


	//to get device id
	public  String getDeviceId(Context context)
	{
		/*TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();*/

		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();

	}

	private boolean validateFields() {
		if(username.getText().toString().isEmpty())
		{
			showAlert(getResources().getString(R.string.email_empty));
			return false;
		}

		if(password.getText().toString().isEmpty())
		{
			showAlert(getResources().getString(R.string.password_empty));
			return false;
		}
		return true;
	}


	private void showAlert(String message)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,5);

		// set title
		alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

		// set dialog message
		alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)

				.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						//closing the application
						dialog.dismiss();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	@Override
	public void onBackPressed()
	{
		/*Intent intent=new Intent(SigninActivity.this,SplashActivity.class);
		intent.putExtra("NO_ANIMATION",true);
		startActivity(intent);*/
		session.setSplahVideo(true);
		finish();
		overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
	}


	@Override
	protected void onStart()
	{
		super.onStart();
		mGoogleApiClient.connect();
		FlurryAgent.onStartSession(this, "8c41e9486e74492897473de501e087dbc6d9f391");
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}
