package com.roadyo.passenger.main;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.devmarvel.creditcardentry.library.CreditCardForm;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.roadyo.passenger.pojo.AddCardResponse;
import com.ourcabby.passenger.R;
import com.roadyo.passenger.pojo.UploadImgeResponse;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.threembed.utilities.OkHttpRequestObject;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.UltilitiesDate;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

public class SignupPayment extends FragmentActivity implements OnClickListener
{
	private Button back,skip,done;
	private String TAG = "SignupPayment";
	
	private static final String MY_CARDIO_APP_TOKEN = "327da493c96f4900a330fae1826f4968";//"376f217254c64a2ca0179d5a459eda1a"

	private int MY_SCAN_REQUEST_CODE = 100;
	ProgressDialog dialogL;
	private String deviceid,regid,referalCode;
	private double currentLatitude,currentLongitude;
	private String cardNo,expiryMonth,expiryYear,cvv,firstName,lastName,email,password,mobileNo;
	private ProgressDialogFragment progressFragment;
	private EditText card_man,cvc_man,month_man,year_man;
	String access_token;
	private File mFileTemp=SignupActivity.mFileTemp;
	private LinearLayout linearLayout_credit_card;
	private CreditCardForm form;
	private RelativeLayout credit_card_details_layout;
	AddCardResponse response;

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signup_payment_new);

		Bundle bundle=getIntent().getExtras();
		
		if(bundle!=null)
		{
			regid=bundle.getString("REGID");
			deviceid=bundle.getString("DEVICEID");
			firstName=bundle.getString("FIRSTNAME");
			lastName=bundle.getString("LASTNAME");
			email=bundle.getString("EMAIL");
			password=bundle.getString("PASSWORD");
			mobileNo=bundle.getString("MOBILE");
			referalCode=bundle.getString("referral");
		}


		intialize();
		progressFragment = ProgressDialogFragment.newInstance(R.string.progressMessage);
	}

	private void intialize()
	{
		back = (Button) findViewById(R.id.signup_payment_back);
		skip = (Button) findViewById(R.id.signup_payment_skip);
		done = (Button) findViewById(R.id.payment_done);
		credit_card_details_layout = (RelativeLayout)findViewById(R.id.relative_credit_card_details);

		card_man=(EditText)findViewById(R.id.credit_card_number);
		cvc_man=(EditText)findViewById(R.id.card_cvv);
		month_man=(EditText)findViewById(R.id.card_month);
		year_man=(EditText)findViewById(R.id.card_year);

		linearLayout_credit_card = (LinearLayout) findViewById(R.id.layer);
		form = new CreditCardForm(this);
		linearLayout_credit_card.addView(form);

		//card_man.addTextChangedListener(mTextEditorWatcher);

		//back.setOnClickListener(this);
		skip.setOnClickListener(this);
		done.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		if(v.getId()==R.id.signup_payment_back)
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					SignupPayment.this);

			// set title
			alertDialogBuilder.setTitle(getResources().getString(R.string.cancel_account_creation));

			// set dialog message
			alertDialogBuilder
			.setMessage(getResources().getString(R.string.cancel_account_creation_alert))
			.setCancelable(false)
			.setPositiveButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity
					Intent intent=new Intent(SignupPayment.this,SplashActivity.class);
					startActivity(intent);
					SignupPayment.this.finish();
					overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);

				}
			})
			.setNegativeButton(getResources().getString(R.string.no),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, just close
					// the dialog box and do nothing
					dialog.cancel();
				}
			});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();

		}

		if(v.getId()==R.id.signup_payment_skip)
		{
			showAlertCard(getResources().getString(R.string.youHaveToFill));
		}

		if(v.getId()==R.id.payment_done)
		{
			if(form.isCreditCardValid())
			{
				com.devmarvel.creditcardentry.library.CreditCard card = form.getCreditCard();

				card.getCardNumber();
				card.getExpDate();
				card.getSecurityCode();




				String[] exp = card.getExpDate().split("/");

				expiryMonth = exp[0];
				expiryYear = exp[1];

				cardNo=card.getCardNumber();
				cvv=card.getSecurityCode();


				saveCreditCard(cardNo,expiryMonth,expiryYear,cvv);
			}
			else
			{
				showAlert(getResources().getString(R.string.enterValidCard));
			}

			return;
		}
	}



	public void onScanPress(View v) 
	{
		// This method is set up as an onClick handler in the layout xml
		Intent scanIntent = new Intent(this, CardIOActivity.class);

		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: true
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
		// if set, developers should provide their own manual entry mechanism in the app
		scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true); // default: false
		// MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
		startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);


		if(data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) 
		{
			credit_card_details_layout.setVisibility(View.VISIBLE);
			linearLayout_credit_card.setVisibility(View.GONE);
			
			
			CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

			// Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
			//resultStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
			;
			card_man.setText(scanResult.getFormattedCardNumber().toString().replace(" ", ""));
			Utility.printLog("card details "+scanResult.getFormattedCardNumber().toString().replace(" ",""));

			if(scanResult.expiryMonth!=0)
			{
				month_man.setText(""+scanResult.expiryMonth);
			}
			if(scanResult.expiryYear!=0)
			{
				year_man.setText(""+scanResult.expiryYear);
			}
			if (scanResult.cvv != null)
			{ 
				// Never log or display a CVV
				cvc_man.setText(""+scanResult.cvv.toString());
			}
		}
	}

	public void saveCreditCard(String cardNo,String expiryMonth,String expiryYear,String cvv)
	{
		SessionManager  sessionManager=new SessionManager(SignupPayment.this);
		Card card = new Card(cardNo,
				Integer.parseInt(expiryMonth),
				Integer.parseInt(expiryYear),
				cvv);

		boolean validation = card.validateCard();
		if(validation) 
		{
			startProgress();
			new Stripe(this).createToken(
					card,
					sessionManager.getStripeKey()					,
					new TokenCallback() {
						public void onSuccess(Token token)
						{
							// getTokenList().addToList(token);
							access_token=token.getId();

							finishProgress();
							if(Utility.isNetworkAvailable(SignupPayment.this))
							{
								BackGroundAddCard();
							}
							else
								Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SignupPayment.this);

						}
						public void onError(Exception error) {
							handleError(error.getLocalizedMessage());
							finishProgress();
						}
					});
		}
		else 
		{
			Utility.ShowAlert(getResources().getString(R.string.You_did_not_enter_valid_card), SignupPayment.this);
		}
	}

	private void startProgress() 
	{
		progressFragment.show(getSupportFragmentManager(), "progress");
	}

	private void finishProgress() 
	{
		progressFragment.dismiss();
	}

	private void handleError(String error) {
		DialogFragment fragment = ErrorDialogFragment.newInstance(R.string.validationErrors, error);
		fragment.show(getSupportFragmentManager(), "error");
	}



	private void showAlert(String message)
	{
		// TODO Auto-generated method stub
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

		// set dialog message
		alertDialogBuilder
		.setMessage(message)
		.setCancelable(false)
		.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog,int id)
			{
				dialog.dismiss();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}


	private void showAlertCard(String string) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

		// set dialog message
		alertDialogBuilder
		.setMessage(string)
		.setCancelable(false)
		.setPositiveButton(getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.dismiss();


			}
		})
		.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				//closing the application
				if(Utility.isNetworkAvailable(SignupPayment.this))
				{
				//	new BackgroundTaskSignUp().execute();
					Intent intent=new Intent(SignupPayment.this,MainActivityDrawer.class);
					startActivity(intent);
					finish();
					overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
				}
				else
				{
					getResources().getString(R.string.network_connection_fail);
				}
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
		// TODO Auto-generated method stub

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupPayment.this);

		// set title
		alertDialogBuilder.setTitle(getResources().getString(R.string.success));

		// set dialog message
		alertDialogBuilder
		.setMessage(getResources().getString(R.string.signedUp))
		.setCancelable(false)
		.setPositiveButton(getResources().getString(R.string.yes),new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog,int id) 
			{
				// if this button is clicked, close
				// current activity
				Intent intent=new Intent(SignupPayment.this,SplashActivity.class);
				startActivity(intent);
				SignupPayment.this.finish();
				overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			}
		})
		.setNegativeButton(getResources().getString(R.string.no),new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog,int id) 
			{
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
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

	private void BackGroundAddCard() {
		JSONObject jsonObject = new JSONObject();
		dialogL=com.threembed.utilities.Utility.GetProcessDialog(SignupPayment.this);
		if (dialogL!=null)
		{
			dialogL.show();
			dialogL.setCancelable(false);
		}

		try {
			SessionManager session=new SessionManager(SignupPayment.this);
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			jsonObject.put("ent_sess_token",session.getSessionToken() );
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_token",access_token);
			jsonObject.put("ent_date_time",curenttime);
			Utility.printLog("params to add card "+jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "addCard", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				Gson gson = new Gson();
				response=gson.fromJson(result,AddCardResponse.class);
				if (dialogL!=null)
				{
					dialogL.dismiss();
				}
				if(response!=null)
				{
					if(response.getErrFlag().equals("0"))
					{
						Intent intent=new Intent(SignupPayment.this,MainActivityDrawer.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
					}
					else
					{
						Utility.ShowAlert(response.getErrMsg(),SignupPayment.this);
					}
				}
				else
				{
					Utility.ShowAlert(getResources().getString(R.string.server_error),SignupPayment.this);
				}
			}
			@Override
			public void onError(String error) {
				if (dialogL!=null)
				{
					dialogL.cancel();
				}
				Utility.printLog("on error for the login "+error);
				Toast.makeText(SignupPayment.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}
}
