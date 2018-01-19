package com.roadyo.passenger.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.roadyo.passenger.pojo.UploadImgeResponse;
import com.roadyo.passenger.pojo.VerificationCodeResponse;
import com.roadyo.passenger.pubnu.pojo.CheckMobileNoResponse;
import com.ourcabby.passenger.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.threembed.utilities.AppLocationService;
import com.threembed.utilities.InternalStorageContentProvider;
import com.threembed.utilities.OkHttpRequestObject;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

import eu.janmuller.android.simplecropimage.CropImage;

public class SignupActivity extends AppCompatActivity implements OnClickListener
{
	private static final String TAG = "SignupActivity";
	private EditText firstName,lastName,email,mobileNo,password,referral_code;//zipcode,confirm_password;
	private TextView Terms_Cond;
	private CheckBox chkBox;
	private ImageView profile_pic;
	private Button back,next;
	String mobileNumberWithoutZero;
	private boolean isPasswordStrength=true;
	private final int REQUEST_CODE_GALLERY      = 0x1;
	private final int REQUEST_CODE_TAKE_PICTURE = 0x2;
	private final int REQUEST_CODE_CROP_IMAGE   = 0x3;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private String SENDER_ID = VariableConstants.PROJECT_ID;
	public static File mFileTemp;
	private GoogleCloudMessaging gcm;
	private String regid,deviceid;
	private Context context;
	private SessionManager session;
	private boolean isEmailVaild = false;
	private double currentLatitude,currentLongitude;
	private boolean isMobileValid = false;
	private final int selectedCountryList =4 ;
	private boolean isCountrySelected=false;
	private String name;
	private boolean picSelected=false;
	VerificationCodeResponse verificationResponse;
	ProgressDialog dialogL;
	CheckMobileNoResponse mobileResponse;
	BookAppointmentResponse response;
	private String type="1";
	String fbPic="";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_one);
		session = new SessionManager(SignupActivity.this);
		initialize();

		AppLocationService appLocationService = new AppLocationService(SignupActivity.this);
		Location gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
		if(gpsLocation != null)
		{
			currentLatitude = gpsLocation.getLatitude();
			currentLongitude = gpsLocation.getLongitude();
			Utility.printLog("canGetLocation lat="+currentLatitude+" lng="+currentLongitude);
		}
		else
		{
			Location nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
			if(nwLocation != null)
			{
				currentLatitude = nwLocation.getLatitude();
				currentLongitude = nwLocation.getLongitude();
			}
			else
			{
				//showSettingsAlert();
			}
		}

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			mFileTemp = new File(Environment.getExternalStorageDirectory(), VariableConstants.TEMP_PHOTO_FILE_NAME);
		}
		else
		{
			mFileTemp = new File(getFilesDir(),VariableConstants.TEMP_PHOTO_FILE_NAME);
		}


		if (checkPlayServices())
		{
			if(gcm == null)
			{
				gcm=GoogleCloudMessaging.getInstance(SignupActivity.this);
			}
			regid=session.getRegistrationId();
			Utility.printLog( "BackgroundForUpdateToken login regid test ......."+regid);
			if (regid==null)
			{
				new BackgroundForRegistrationId().execute();
			} else
			{
				deviceid=getDeviceId(context);
			}

			Utility.printLog("doInBackground regid.........."+regid);

		}
		else
		{
			Utility.printLog("No valid Google Play Services found.");
		}
	}

	private void initialize()
	{
		firstName=(EditText)findViewById(R.id.first_name);
		lastName=(EditText)findViewById(R.id.last_name);
		email=(EditText)findViewById(R.id.signup_email);
		mobileNo=(EditText)findViewById(R.id.signup_phone);
		referral_code=(EditText)findViewById(R.id.signup_referal_code);
		password=(EditText)findViewById(R.id.signup_password);
		chkBox=(CheckBox)findViewById(R.id.chkbox_TandC);
		profile_pic=(ImageView)findViewById(R.id.profile_pic);
		back=(Button)findViewById(R.id.signup_back);
		next=(Button)findViewById(R.id.signup_next);
		Terms_Cond=(TextView)findViewById(R.id.txt_TandC);
		back.setOnClickListener(this);
		next.setOnClickListener(this);
		profile_pic.setOnClickListener(this);
		Terms_Cond.setOnClickListener(this);

		referral_code.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			public void onFocusChange(View v, boolean hasFocus)
			{
				Utility.printLog("setOnFocusChangeListener hasFocus= "+hasFocus);
				if(!hasFocus)
				{

					if(!(referral_code.getText().toString().trim().isEmpty()))
					{
						ValidatePromoCode(referral_code.getText().toString().trim());
					}

				}
			}
		});


		firstName.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			public void onFocusChange(View v, boolean hasFocus)
			{
				Utility.printLog("setOnFocusChangeListener hasFocus= "+hasFocus);
				if(!hasFocus)
				{

					if(firstName.getText().toString().trim().isEmpty())
					{
						//showAlert(getResources().getString(R.string.first_name_empty));
						firstName.setError(getResources().getString(R.string.first_name_empty));
					}

				}
			}
		});

		firstName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				firstName.setError(null);
			}
		});


		password.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				Utility.printLog("setOnFocusChangeListener hasFocus= " + hasFocus);
				if (!hasFocus) {

					if (password.getText().toString().trim().isEmpty()) {
						//showAlert(getResources().getString(R.string.password_empty));
						password.setError(getResources().getString(R.string.password_empty));
					}

				}
			}
		});

		mobileNo.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			public void onFocusChange(View v, boolean hasFocus)
			{
				Utility.printLog("setOnFocusChangeListener hasFocus= "+hasFocus);
				if(!hasFocus)
				{

					if(mobileNo.getText().toString().trim().isEmpty())
					{
						mobileNo.setError(getResources().getString(R.string.mobile_empty));
					}
					else
					{
						BackgroundValidateMobileNo();
					}
				}
			}
		});

		mobileNo.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				mobileNo.setError(null);
			}
		});

		email.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			public void onFocusChange(View v, boolean hasFocus)
			{
				Utility.printLog("setOnFocusChangeListener hasFocus= " + hasFocus);
				if (!hasFocus) {

					if (email.getText().toString().trim().isEmpty())
					{
						//showAlert(getResources().getString(R.string.email_empty));
						email.setError(getResources().getString(R.string.email_empty));
					} else
					{
						if (!validateEmail(email.getText().toString().trim()))
						{
							email.setError(getResources().getString(R.string.enter_valid_email));
						} else
						{
							BackgroundValidateEmail();
						}
					}
				}
			}
		});

		email.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable)
			{
				email.setError(null);
			}
		});

		Bundle bundle=getIntent().getExtras();
		if(bundle!=null)
		{
			email.setText(bundle.getString("EMAIL"));
			password.setText(bundle.getString("PASSWORD"));
			firstName.setText(bundle.getString("FIRST_NAME"));
			lastName.setText(bundle.getString("LAST_NAME"));
			Utility.printLog("user pic "+bundle.getString("USER_PIC"));
			if(!bundle.getString("USER_PIC").equals(null))
			{
				fbPic=bundle.getString("USER_PIC");
			}
			type=bundle.getString("TYPE");
			password.setVisibility(View.GONE);

			if(!fbPic.equals(""))
			{
				/**
				 * to set the image into image view and
				 * add the write the image in the file
				 */
				profile_pic.setTag(setTarget());
				Picasso.with(SignupActivity.this).load(fbPic).into((Target) profile_pic.getTag()) ;
				picSelected=true;
				Picasso.with(SignupActivity.this)
						.load(fbPic)
						.into(new Target() {
							@Override
							public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
								new Thread(new Runnable() {
									@Override
									public void run() {
										try {
											Log.i("TAG", "i am inside try for file conversion" + mFileTemp);
											FileOutputStream ostream = new FileOutputStream(mFileTemp);
											bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
											ostream.close();
										} catch (Exception e) {
											Log.i("", "exception in bitmap" + e);
										}
									}
								}).start();
							}
							@Override
							public void onBitmapFailed(Drawable errorDrawable) {
							}
							@Override
							public void onPrepareLoad(Drawable placeHolderDrawable) {
								Log.i("", "bitmap failed while prepareload" + placeHolderDrawable);
							}
						});
			}
			else
			{
				picSelected=false;
			}
		}
	}

	public Target setTarget()
	{
		Target target= new Target() {
			@Override
			public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
			{
				/**
				 * to set the image from fb to imageview after making circular imagee
				 */
				bitmap = Bitmap.createScaledBitmap(bitmap, getResources().getDrawable(R.drawable.createaccount_thumbnail).getMinimumWidth(),
						getResources().getDrawable(R.drawable.createaccount_thumbnail).getMinimumHeight(), true);
				Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
				BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
				Paint paint = new Paint();
				paint.setShader(shader);
				paint.setAntiAlias(true);
				Canvas c = new Canvas(circleBitmap);
				c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);
				profile_pic.setImageBitmap(circleBitmap);
				/**
				 * to put the bitmap image into the file for uploading
				 */
				FileOutputStream ostream = null;
				try {
					ostream = new FileOutputStream(mFileTemp);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
					ostream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onBitmapFailed(Drawable errorDrawable) {
			}
			@Override
			public void onPrepareLoad(Drawable placeHolderDrawable)
			{
			}
		};
		return target;
	}



	private boolean checkPlayServices()
	{
		Log.d(TAG, "onCreate checkPlayServices ");
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS)
		{
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
			{
				Utility.printLog( "This device is supported.");
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			}
			else
			{
				Utility.printLog( "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}


	public  String getDeviceId(Context context)
	{
		/*TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();*/

		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		Utility.printLog("country in signup "+telephonyManager.getNetworkCountryIso());

		return telephonyManager.getDeviceId();

	}



	@Override
	public void onClick(View v)
	{
		if(v.getId()==R.id.signup_back)
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					SignupActivity.this);

			// set title
			alertDialogBuilder.setTitle(getResources().getString(R.string.cancel_account_creation));

			// set dialog message
			alertDialogBuilder
					.setMessage(getResources().getString(R.string.cancel_account_creation_alert))
					.setCancelable(false)
					.setPositiveButton(getResources().getString(R.string.yes),new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id)
						{
							session.setSplahVideo(true);
							SignupActivity.this.finish();
							overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);

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
		if(v.getId()==R.id.signup_next)
		{
			if(!email.getText().toString().isEmpty())
			{
				if(validateEmail(email.getText().toString()))
				{
					BackgroundValidateEmail();
				}
				else
				{
					showAlert(getResources().getString(R.string.enter_valid_email));
				}
			}
			else
			{
				boolean isValid=validateFields();
				if(isValid)
				{
					if(validateEmail(email.getText().toString()))
					{
						if(!mobileNo.getText().toString().isEmpty())
						{
							if(password.getText().toString().length()>=6)
							{
								if(referral_code.getText().toString().trim().equals("")) {
									session.storeCurrencySymbol("AED");
									session.storeRegistrationId(regid);
									session.storeDeviceId(deviceid);

									if (Utility.isNetworkAvailable(SignupActivity.this))
										BackgroundForGettingVerificationCode();
									else
										Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SignupActivity.this);
									overridePendingTransition(R.anim.anim_two, R.anim.anim_one);
								}
								else
								{
									ValidatePromoCodeOnNextClick(referral_code.getText().toString());
								}
							}
							else
							{
								showAlert(getResources().getString(R.string.passValidation));
							}

						}

						else
						{
							showAlert(getResources().getString(R.string.mobile_empty));
						}
					}
					else
					{
						showAlert(getResources().getString(R.string.enter_valid_email));
					}
				}
				else
				{
					//showAlert(getResources().getString(R.string.email_empty));
				}
			}

		}
		if(v.getId()==R.id.profile_pic)
		{
			AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(SignupActivity.this);

			// Setting Dialog Message
			alertDialog2.setMessage(getResources().getString(R.string.selecto_photo));


			// Setting Positive "Yes" Btn
			alertDialog2.setPositiveButton(getResources().getString(R.string.gallery),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which)
						{
							openGallery();
						}
					});

			// Setting Negative "NO" Btn
			alertDialog2.setNegativeButton(getResources().getString(R.string.camera),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							takePicture();
						}
					});

			// Showing Alert Dialog
			alertDialog2.show();

		}

		if(v.getId()==R.id.txt_TandC)
		{
			Intent intent = new Intent(SignupActivity.this,TermsActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
		}
	}

	private void BackgroundForGettingVerificationCode() {
		JSONObject jsonObject = new JSONObject();
		dialogL=Utility.GetProcessDialogNew(SignupActivity.this,getResources().getString(R.string.waitForVerification));
		dialogL.setCancelable(true);
		if (dialogL!=null)
		{
			dialogL.setCancelable(false);
			dialogL.show();
		}
		try {
			jsonObject.put("ent_mobile", "+91"+mobileNo.getText().toString());
			jsonObject.put("ent_user_type", "2");
			Utility.printLog("params to login "+jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "getVerificationCode", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				if(result!=null)
				{
					Gson gson = new Gson();
					verificationResponse=gson.fromJson(result,VerificationCodeResponse.class);
					if(dialogL!=null)
					{
						dialogL.dismiss();
					}
					if(verificationResponse!=null)
					{
						if(verificationResponse.getErrFlag().equals("0"))
						{
							VariableConstants.COUNTER=0;
							Intent intent=new Intent(SignupActivity.this,SignupVerificationActivity.class);
							intent.putExtra("REGID",regid);
							intent.putExtra("DEVICEID",deviceid);
							Utility.printLog("Before sendin deviceId"+deviceid);
							Utility.printLog("Before sendin regId"+regid);
							intent.putExtra("FIRSTNAME",firstName.getText().toString());
							if(lastName.getText()!=null)
								intent.putExtra("LASTNAME",lastName.getText().toString());
							if(referral_code.getText()!=null)
								intent.putExtra("REFERAL",referral_code.getText().toString());
							intent.putExtra("EMAIL",email.getText().toString());
							intent.putExtra("PASSWORD",password.getText().toString());
							intent.putExtra("MOBILE", "+91"+mobileNo.getText().toString());
							intent.putExtra("BOOLEAN",picSelected);
							intent.putExtra("LATITUDE",String.valueOf(currentLatitude)) ;
							intent.putExtra("LONGITUDE",String.valueOf(currentLongitude));
							intent.putExtra("TYPE",type);
							startActivity(intent);
							overridePendingTransition(R.anim.anim_two, R.anim.anim_one);
						}
						else
						{
							email.setText(null);
							showAlert(verificationResponse.getErrMsg());
						}


					}
					else
					{

						email.setText(null);
						//isEmailVaild = false;

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);

						// set title
						alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

						// set dialog message
						alertDialogBuilder
								.setMessage(getResources().getString(R.string.server_error))
								.setCancelable(false)

								.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog,int id)
									{
										// if this button is clicked, just close
										// the dialog box and do nothing
										//finish();
									}
								});
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
						// show it
						alertDialog.show();

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
				Toast.makeText(SignupActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}

	private void openGallery()
	{
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
	}

	private void takePicture()
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		try {
			Uri mImageCaptureUri = null;
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				mImageCaptureUri = Uri.fromFile(mFileTemp);
			}
			else
			{
				mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
			}
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
		} catch (ActivityNotFoundException e) {

			Log.d("", "cannot take picture", e);
		}
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode != RESULT_OK)
		{
			return;
		}

		Bitmap bitmap;

		switch (requestCode) {

			case REQUEST_CODE_GALLERY:

				try {

					InputStream inputStream = getContentResolver().openInputStream(data.getData());
					FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);

					Log.e("", "inputStream"+inputStream);
					Log.e("", "fileOutputStream"+fileOutputStream);

					copyStream(inputStream, fileOutputStream);
					fileOutputStream.close();
					inputStream.close();

					startCropImage();

				} catch (Exception e)
				{
					Log.e("", "Error while creating temp file", e);
				}

				break;
			case REQUEST_CODE_TAKE_PICTURE:

				startCropImage();
				break;
			case REQUEST_CODE_CROP_IMAGE:

				String path = data.getStringExtra(CropImage.IMAGE_PATH);
				Log.e("", "path fileOutputStream "+path);

				if(path == null)
				{
					return;
				}

				bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
				Utility.printLog("profile pic name getPath="+mFileTemp.getPath());
				Utility.printLog("profile pic name1="+mFileTemp.getName());
				profile_pic.setImageBitmap(bitmap);
				Utility.printLog("BitmapFactory file size before = "+mFileTemp.length());
				picSelected=true;

				break;


		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	private void startCropImage()
	{
		Intent intent = new Intent(this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
		intent.putExtra(CropImage.SCALE, true);

		intent.putExtra(CropImage.ASPECT_X, 4);
		intent.putExtra(CropImage.ASPECT_Y, 4);

		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}


	public static void copyStream(InputStream input, OutputStream output)
			throws IOException
	{

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1)
		{
			output.write(buffer, 0, bytesRead);
		}
	}


	private boolean validateFields()
	{
		if(firstName.getText().toString().isEmpty() || firstName.getText().toString().equals(""))
		{
			firstName.setError(getResources().getString(R.string.first_name_empty));
			return false;
		}



		else if(mobileNo.getText().toString().isEmpty())
		{
			mobileNo.setError(getResources().getString(R.string.mobile_empty));
			return false;
		}

		else if(email.getText().toString().isEmpty())
		{
			email.setError(getResources().getString(R.string.email_empty));
			return false;
		}



		else if(password.getText().toString().isEmpty())
		{
			password.setError(getResources().getString(R.string.password_empty));
			return false;
		}


		else if(!chkBox.isChecked())
		{
			showAlert(getResources().getString(R.string.accept_terms_conditions));
			return  false;
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



	public boolean validateEmail(String email) {
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



	private class BackgroundForRegistrationId extends AsyncTask<String, Void, String>
	{
		private ProgressDialog dialogL;


		@Override
		protected String doInBackground(String... params)
		{

			try {

				deviceid=getDeviceId(context);
				regid=gcm.register(SENDER_ID);

				session.storeRegistrationId(regid);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialogL=Utility.GetProcessDialog(SignupActivity.this);
			dialogL.setCancelable(false);
			if (dialogL!=null) {
				dialogL.show();
			}
		}
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			if (dialogL!=null)
			{
				dialogL.dismiss();
			}

			if(regid==null)
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);

				// set title
				alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

				// set dialog message
				alertDialogBuilder
						.setMessage(getResources().getString(R.string.network_connection_fail))
						.setCancelable(false)
						.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
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

	private void BackgroundValidateMobileNo() {
		final ProgressDialog dialogL=Utility.GetProcessDialogNew(SignupActivity.this,getResources().getString(R.string.validating_mobile));
		dialogL.setCancelable(true);
		if (dialogL!=null)
		{
			dialogL.setCancelable(false);
			dialogL.show();
		}
		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("ent_mobile","+91"+mobileNo.getText().toString());
			jsonObject.put("ent_user_type","2");
			Utility.printLog("params to login "+jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "checkMobile", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				if(result!=null)
				{
					Gson gson = new Gson();
					mobileResponse=gson.fromJson(result,CheckMobileNoResponse.class);
					if(dialogL!=null)
					{
						dialogL.dismiss();
					}
					if(mobileResponse!=null)
					{
						Utility.printLog("akbar"+mobileResponse.getErrFlag());
						Utility.printLog("akbar"+mobileResponse.getErrMsg());


						if(mobileResponse.getErrFlag().equals("0"))
						{
							//isMobileValid = true;

							session.storeCurrencySymbol("$");
							session.storeRegistrationId(regid);
							session.storeDeviceId(deviceid);

							//showAlert(mobileResponse.getErrMsg());

						}
						else
						{
							//isMobileValid = false;
							showAlert(mobileResponse.getErrMsg());
							mobileNo.setText(null);
						}
					}
					else
					{

						//isMobileValid = false;
						mobileNo.setText(null);
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);

						// set title
						alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

						// set dialog message
						alertDialogBuilder
								.setMessage(getResources().getString(R.string.server_error))
								.setCancelable(false)

								.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog,int id)
									{
										// if this button is clicked, just close
										// the dialog box and do nothing
										//finish();
									}
								});
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
						// show it
						alertDialog.show();


					}
				}

			}
			@Override
			public void onError(String error) {
				dialogL.dismiss();
				Utility.printLog("on error for the login "+error);
				Toast.makeText(SignupActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}

	private void BackgroundValidateEmail() {
		final ProgressDialog dialogL=Utility.GetProcessDialogNew(SignupActivity.this,getResources().getString(R.string.validating_email));
		dialogL.setCancelable(true);
		if (dialogL!=null)
		{
			dialogL.setCancelable(false);
			dialogL.show();
		}
		JSONObject jsonObject = new JSONObject();
		try {

			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			jsonObject.put("ent_email", email.getText().toString());
			//jsonObject.put("zip_code",zipcode.getText().toString());
			//jsonObject.put("zip_code","90637");
			jsonObject.put("ent_user_type","2");
			jsonObject.put("ent_date_time", curenttime);
			Utility.printLog("params to login "+jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "validateEmailZip", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				if(result!=null)
				{
					Gson gson = new Gson();
					response=gson.fromJson(result,BookAppointmentResponse.class);
					if(dialogL!=null)
					{
						dialogL.dismiss();
					}
					if(response!=null)
					{
						if(response.getErrFlag().equals("0"))
						{

							boolean isValid=validateFields();
							if(isValid)
							{
								if(validateEmail(email.getText().toString()))
								{
									if(!mobileNo.getText().toString().isEmpty())
									{
										if(password.getText().toString().length()>=6)
										{
											if(referral_code.getText().toString().trim().equals("")) {
												session.storeCurrencySymbol("AED");
												session.storeRegistrationId(regid);
												session.storeDeviceId(deviceid);

												if (Utility.isNetworkAvailable(SignupActivity.this))
													BackgroundForGettingVerificationCode();
												else
													Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SignupActivity.this);
												overridePendingTransition(R.anim.anim_two, R.anim.anim_one);
											}
											else
											{
												ValidatePromoCodeOnNextClick(referral_code.getText().toString());
											}
										}
										else
										{
											showAlert(getResources().getString(R.string.passValidation));
										}
									}
									else
									{
										showAlert(getResources().getString(R.string.mobile_empty));
									}
								}
								else
								{
									showAlert(getResources().getString(R.string.enter_valid_email));
								}
							}
							else
							{
								//showAlert(getResources().getString(R.string.email_empty));
							}


						}
						else
						{
							email.setText("");
							showAlert(response.getErrMsg());
							email.setText(null);
						}
					}
					else
					{
						email.setText("");

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);

						// set title
						alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

						// set dialog message
						alertDialogBuilder
								.setMessage(getResources().getString(R.string.server_error))
								.setCancelable(false)

								.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog,int id)
									{
										// if this button is clicked, just close
										// the dialog box and do nothing
										//finish();
									}
								});
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
						// show it
						alertDialog.show();
					}
				}

			}
			@Override
			public void onError(String error) {
				dialogL.dismiss();
				Utility.printLog("on error for the login "+error);
				Toast.makeText(SignupActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}


	private void  ValidatePromoCode(final String promoCode){
		JSONObject jsonObject = new JSONObject();
		final ProgressDialog dialogL=Utility.GetProcessDialogNew(SignupActivity.this,getResources().getString(R.string.validateReferal));
		dialogL.setCancelable(true);
		if (dialogL!=null)
		{
			dialogL.show();
		}
		try {
			Utility.printLog("ValidatePromoCode promoCode="+promoCode);
			Utility.printLog("ValidatePromoCode promoCode currentLatitude ="+currentLatitude);
			Utility.printLog("ValidatePromoCode promoCode currentLongitude ="+currentLongitude);

			jsonObject.put("ent_coupon",promoCode);
			jsonObject.put("ent_lat",""+currentLatitude);
			jsonObject.put("ent_long",""+currentLongitude);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "verifyCode", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				Utility.printLog("Success of getting ValidatePromoCode "+result);
				JSONObject jsnResponse;
				try
				{
					dialogL.dismiss();
					jsnResponse = new JSONObject(result);
					String mErrNum = jsnResponse.getString("errNum");
					Utility.printLog("jsonErrorParsing is ---> "+mErrNum);

					if(jsnResponse.getString("errFlag").equals("0"))
					{
						session.storeCurrencySymbol("₹");
						session.storeRegistrationId(regid);
						session.storeDeviceId(deviceid);

					}

					else
					{
						referral_code.setText(null);
						Utility.ShowAlert(jsnResponse.getString("errMsg"), SignupActivity.this);
					}

				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			@Override
			public void onError(String error) {
				dialogL.dismiss();
				Toast.makeText(SignupActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
				Utility.printLog("Error for volley");	}
		});
	}

	private void  ValidatePromoCodeOnNextClick(final String promoCode){
		JSONObject jsonObject = new JSONObject();
		final ProgressDialog dialogL=Utility.GetProcessDialogNew(SignupActivity.this,getResources().getString(R.string.validateReferal));
		dialogL.setCancelable(true);
		if (dialogL!=null)
		{
			dialogL.show();
		}
		try {
			Utility.printLog("ValidatePromoCode promoCode="+promoCode);
			Utility.printLog("ValidatePromoCode promoCode currentLatitude ="+currentLatitude);
			Utility.printLog("ValidatePromoCode promoCode currentLongitude ="+currentLongitude);

			jsonObject.put("ent_coupon",promoCode);
			jsonObject.put("ent_lat",""+currentLatitude);
			jsonObject.put("ent_long",""+currentLongitude);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "verifyCode", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				Utility.printLog("Success of getting ValidatePromoCode "+result);
				JSONObject jsnResponse;
				try
				{
					dialogL.dismiss();
					jsnResponse = new JSONObject(result);
					String mErrNum = jsnResponse.getString("errNum");
					Utility.printLog("jsonErrorParsing is ---> "+mErrNum);

					if(jsnResponse.getString("errFlag").equals("0"))
					{
						session.storeCurrencySymbol("₹");

						session.storeRegistrationId(regid);
						session.storeDeviceId(deviceid);

						if (Utility.isNetworkAvailable(SignupActivity.this))
							BackgroundForGettingVerificationCode();
						else
							Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SignupActivity.this);
						overridePendingTransition(R.anim.anim_two, R.anim.anim_one);

					}

					else
					{
						referral_code.setText(null);
						Utility.ShowAlert(jsnResponse.getString("errMsg"), SignupActivity.this);
					}

				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			@Override
			public void onError(String error) {
				dialogL.dismiss();
				Toast.makeText(SignupActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
				Utility.printLog("Error for volley");	}
		});
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		initialize();
	}


	@Override
	public void onBackPressed()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);
		// set title
		alertDialogBuilder.setTitle(getResources().getString(R.string.cancel_account_creation));
		// set dialog message
		alertDialogBuilder
				.setMessage(getResources().getString(R.string.cancel_account_creation_alert))
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.yes),new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
					/*Intent intent=new Intent(SignupActivity.this,SplashActivity.class);
					intent.putExtra("NO_ANIMATION",true);
					startActivity(intent);*/
						session.setSplahVideo(true);
						SignupActivity.this.finish();

						overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
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

