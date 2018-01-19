package com.roadyo.passenger.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.roadyo.passenger.pojo.GetMyProfileResponse;
import com.roadyo.passenger.pojo.LogoutResponse;
import com.roadyo.passenger.pojo.UpdateProfile;
import com.roadyo.passenger.pojo.UploadImgeResponse;
import com.ourcabby.passenger.R;
import com.roadyo.passenger.pojo.ValidVerificationCodeResponse;
import com.threembed.utilities.InternalStorageContentProvider;
import com.threembed.utilities.OkHttpRequestObject;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.UltilitiesDate;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

import eu.janmuller.android.simplecropimage.CropImage;

import static com.roadyo.passenger.main.MainActivityDrawer.getDeviceId;
import static com.threembed.utilities.Utility.GetProcessDialog;

public class ProfileFragment extends Fragment implements OnClickListener
{
	private static final String TAG = "HomePageFragment";
	private View view;
	private EditText first_name,last_name,phone;
	private ImageView profile_pic;
	private final int REQUEST_CODE_GALLERY      = 0x1;
	private final int REQUEST_CODE_TAKE_PICTURE = 0x2;
	private final int REQUEST_CODE_CROP_IMAGE   = 0x3;
	private File mFileTemp;
	private static ImageLoader imageLoader;
	private DisplayImageOptions options;
	private GetMyProfileResponse getprofile;
	private UpdateProfile updateprofile;
	private String getprofileServerResponse;
	private boolean editpressed=true,savepressed=false;
	EditText email;
	private Typeface latoRegular;
	private TextView first,last,textMobile,textEmail;
	private Button logoutButton;
	private SessionManager session;
	ProgressDialog dialogL1;
	private ProgressDialog dialogL;
	LogoutResponse response;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//hideSoftKeyboard(getActivity());
		setHasOptionsMenu(true);

		String temp=Utility.getCurrentDateTimeStringGMT();
		if (view != null)
		{
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}

		try
		{
			view = inflater.inflate(R.layout.profile_new, container, false);

		} catch (InflateException e)
		{
			/* map is already there, just return view as it is */
			Log.e(TAG, "onCreateView  InflateException "+e);
		}

		MainActivityDrawer.edit_profile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(editpressed)
				{
					Utility.printLog("inside options editpressed");
					enableEdittext();
					MainActivityDrawer.edit_profile.setText(getResources().getString(R.string.save));
					editpressed=false;
					savepressed=true;
				}
				else if(savepressed)
				{
					Utility.printLog("inside options savepressed");

					final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

					disableEdittext();
					if((!first_name.getText().toString().trim().isEmpty()))
					{
						if(!phone.getText().toString().trim().isEmpty())
						{
							new BackgroundForUpdateProfile().execute();
							disableEdittext();
							MainActivityDrawer.edit_profile.setText(getResources().getString(R.string.edit));
							editpressed=true;
							savepressed=false;
						}
						else
						{
							showAlert(getResources().getString(R.string.mobile_empty));
							enableEdittext();
						}
					}
					else
					{
						showAlert(getResources().getString(R.string.first_name_empty));
						enableEdittext();
					}

				}
			}
		});

		initializeVariables(view);

		imageLoader = ImageLoader.getInstance();
		ProfileFragment.imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.signup_profile_image)
				.showImageForEmptyUri(R.drawable.signup_profile_image)
				.showImageOnFail(R.drawable.signup_profile_image)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(0))
				.build();

		if(Utility.isNetworkAvailable(getActivity()))
		{
			new BackgroundTaskGetProfile().execute();
		}
		else
		{
			Intent homeIntent=new Intent("com.ourcabby.passenger.internetStatus");
			homeIntent.putExtra("STATUS", "0");
			getActivity().sendBroadcast(homeIntent);
		}


		disableEdittext();

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mFileTemp = new File(Environment.getExternalStorageDirectory(), VariableConstants.TEMP_PHOTO_FILE_NAME);
		}
		else
		{
			mFileTemp = new File(getActivity().getFilesDir(),VariableConstants.TEMP_PHOTO_FILE_NAME);
		}

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		MainActivityDrawer.edit_profile.setVisibility(View.VISIBLE);

	}

	@Override
	public void onPause()
	{
		super.onPause();
		editpressed=true;
		savepressed=false;
		MainActivityDrawer.edit_profile.setText(getResources().getString(R.string.edit));
	}

	private void initializeVariables(View v) {
		first_name=(EditText) v.findViewById(R.id.profile_first_name);
		last_name=(EditText)v.findViewById(R.id.profile_last_name);
		email=(EditText)v.findViewById(R.id.profile_email);
		phone=(EditText)v.findViewById(R.id.profile_mobile_no);
		first= (TextView) v.findViewById(R.id.first);
		last= (TextView) v.findViewById(R.id.last);
		textMobile= (TextView) v.findViewById(R.id.textMobile);
		textEmail= (TextView) v.findViewById(R.id.textEmail);
		logoutButton= (Button) v.findViewById(R.id.logoutButton);
		session=new SessionManager(getActivity());
		latoRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");

		first_name.setTypeface(latoRegular);
		last_name.setTypeface(latoRegular);
		email.setTypeface(latoRegular);
		phone.setTypeface(latoRegular);
		first.setTypeface(latoRegular);
		last.setTypeface(latoRegular);
		textMobile.setTypeface(latoRegular);
		textEmail.setTypeface(latoRegular);

		logoutButton.setOnClickListener(this);
		profile_pic=(ImageView)v.findViewById(R.id.profile_image);

		MainActivityDrawer.driver_tip.setVisibility(View.INVISIBLE);
	}

	private void disableEdittext()
	{
		first_name.setFocusableInTouchMode(false);
		first_name.setFocusable(false);

		last_name.setFocusableInTouchMode(false);
		last_name.setFocusable(false);

		email.setFocusableInTouchMode(false);
		email.setFocusable(false);

		phone.setFocusableInTouchMode(false);
		phone.setFocusable(false);

		profile_pic.setOnClickListener(null);
	}

	private void enableEdittext()
	{
		first_name.setFocusableInTouchMode(true);
		first_name.setFocusable(true);

		last_name.setFocusableInTouchMode(true);
		last_name.setFocusable(true);

		email.setFocusableInTouchMode(false);
		email.setFocusable(false);

		profile_pic.setOnClickListener(this);


		phone.setFocusableInTouchMode(false);
		phone.setFocusable(false);

	}



	@Override
	public void onClick(View v)
	{
		if(v.getId()==R.id.profile_image)
		{
			AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getActivity());

			// Setting Dialog Message
			alertDialog2.setMessage(getResources().getString(R.string.selecto_photo));


			// Setting Positive "Yes" Btn
			alertDialog2.setPositiveButton(getResources().getString(R.string.gallery),
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							openGallery();
						}
					});

			// Setting Negative "NO" Btn
			alertDialog2.setNegativeButton(getResources().getString(R.string.camera),
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							takePicture();
						}
					});

			// Showing Alert Dialog
			alertDialog2.show();
		}
		if(v.getId()==R.id.logoutButton)
		{
			if(Utility.isNetworkAvailable(getActivity()))
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

				// set title
				alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

				// set dialog message
				alertDialogBuilder
						.setMessage(getResources().getString(R.string.logout_alert_message))
						.setCancelable(false)

						.setPositiveButton(getResources().getString(R.string.yes),new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,int id)
							{
								//closing the application
								dialog.dismiss();
								if(Utility.isNetworkAvailable(getActivity()))
								{
									BackgroundLogOutTask();

								}
								else
								{
									Toast.makeText(getActivity(),getResources().getString(R.string.network_connection_fail), Toast.LENGTH_LONG).show();
								}
							}
						})

						.setNegativeButton(getResources().getString(R.string.no),new DialogInterface.OnClickListener()
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
			else
			{
				Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), getActivity());
			}
		}

	}


	private void BackgroundLogOutTask() {
		JSONObject jsonObject = new JSONObject();
		dialogL= GetProcessDialog(getActivity());

		if (dialogL!=null)
		{
			dialogL.show();
		}
		try {
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Utility.printLog("dataandTime " + curenttime);
			jsonObject.put("ent_sess_token",session.getSessionToken());
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_user_type","2");
			jsonObject.put("ent_submit","Submit");
			jsonObject.put("ent_date_time",curenttime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "logout", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				if (dialogL!=null)
				{
					dialogL.dismiss();
				}

				if (result!=null)
				{
					Gson gson = new Gson();
					response=gson.fromJson(result,LogoutResponse.class);
					NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
					notificationManager.cancelAll();
				}
				else
				{
					if (dialogL!=null)
					{
						dialogL.dismiss();
					}
					Toast.makeText(getActivity(),getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
				}

				if(result!=null)
				{

					//receiver.abortBroadcast();
					session.setIsLogin(false);
					Intent intent=new Intent(getActivity(),SplashActivity.class);
					startActivity(intent);
					getActivity().finish();

				}
			}
			@Override
			public void onError(String error) {
				if (dialogL!=null)
				{
					dialogL.cancel();
				}
				Utility.printLog("on error for the login "+error);
				Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}
	private void showAlert(String message)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

		// set title
		alertDialogBuilder.setTitle(getResources().getString(R.string.message));

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
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
		} catch (ActivityNotFoundException e) {

			Log.d("", "cannot take picture", e);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		Utility.printLog("Control comming in onActivityResult: req: "+requestCode+" ,resultCode: "+resultCode);
		if (requestCode == 11)
		{
			if(resultCode==Activity.RESULT_OK)
			{
				Utility.printLog("Control comming in req=1");
				new BackgroundTaskGetProfile().execute();
			}

			return;
		}


		if (resultCode !=Activity.RESULT_OK) {



			return;
		}

		//Bitmap bitmap;

		switch (requestCode) {

			case REQUEST_CODE_GALLERY:

				Utility.printLog("Control comming in req=REQUEST_CODE_GALLERY");
				try {

					InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
					FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);

					Log.e("", "inputStream"+inputStream);
					Log.e("", "fileOutputStream"+fileOutputStream);

					copyStream(inputStream, fileOutputStream);
					fileOutputStream.close();
					inputStream.close();

					startCropImage();

				} catch (Exception e) {

					Log.e(TAG, "Error while creating temp file", e);
				}

				break;
			case REQUEST_CODE_TAKE_PICTURE:

				Utility.printLog("Control comming in req=REQUEST_CODE_TAKE_PICTURE");
				startCropImage();
				break;
			case REQUEST_CODE_CROP_IMAGE:

				Utility.printLog("Control comming in req=REQUEST_CODE_CROP_IMAGE");
				String path = data.getStringExtra(CropImage.IMAGE_PATH);
				Log.e("", "path fileOutputStream "+path);


				if (path == null) {

					return;
				}

				//bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
				//profile_pic.setImageBitmap(bitmap);

				//Call service to upload the image
				 BackGroundTaskForUploadImage();



				break;
		}
		super.onActivityResult(requestCode, resultCode, data);


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

	private void startCropImage()
	{

		Intent intent = new Intent(getActivity(), CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
		intent.putExtra(CropImage.SCALE, true);

		intent.putExtra(CropImage.ASPECT_X, 4);
		intent.putExtra(CropImage.ASPECT_Y, 4);

		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}



	class BackgroundTaskGetProfile extends AsyncTask<String,Void,String>
	{

		GetMyProfileResponse response;

		ProgressDialog dialogL;


		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialogL= GetProcessDialog(getActivity());

			if (dialogL!=null) {
				dialogL.show();
			}

		}
		@Override
		protected String doInBackground(String... params) {

			getUserProfile(dialogL);
			return null;
		}


		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);


		}
	}


	private void  getUserProfile(final ProgressDialog dialogL) {
		JSONObject jsonObject = new JSONObject();
		try {
			SessionManager session=new SessionManager(getActivity());

			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			jsonObject.put("ent_sess_token",session.getSessionToken() );
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_date_time",curenttime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "getProfile", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				getprofileServerResponse = result;
				Utility.printLog("Success of getting user Info"+result);

				getUserInfo(dialogL);
			}
			@Override
			public void onError(String error) {
				dialogL.dismiss();
				Toast.makeText(getActivity(), "System error in getting user Info please retry", Toast.LENGTH_LONG).show();
				Utility.printLog("Error for volley");	}
		});
	}

	private void getUserInfo(ProgressDialog dialogL)
	{
		dialogL.dismiss();
		try
		{

			JSONObject jsnResponse = new JSONObject(getprofileServerResponse);

			String jsonErrorParsing = jsnResponse.getString("errFlag");

			Utility.printLog("jsonErrorParsing is ---> "+jsonErrorParsing);
			parseResponse();

			if(getprofile.getErrNum().equals("6") || getprofile.getErrNum().equals("7") ||
					getprofile.getErrNum().equals("94") || getprofile.getErrNum().equals("96"))
			{
				Toast.makeText(getActivity(), getprofile.getErrMsg(),Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getActivity(), SplashActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				getActivity().startActivity(i);
				getActivity().overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
			}

			if(getprofile!=null)
			{
				if(getprofile.getErrFlag().equals("0") && isAdded())
				{
					ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
					first_name.setText(getprofile.getfName());
					last_name.setText(getprofile.getlName());
					email.setText(getprofile.getEmail());
					phone.setText(getprofile.getPhone());

					//Setting doctor pic 
					String url=VariableConstants.IMAGE_BASE_URL+getprofile.getpPic();
					imageLoader.displayImage(url, profile_pic, options, animateFirstListener);

				}


			}
			else if(isAdded())
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());

				// set title
				alertDialogBuilder.setTitle("Note :");

				// set dialog message
				alertDialogBuilder
						.setMessage("Server Error.Retry!!")
						.setCancelable(false)

						.setNegativeButton("OK",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.dismiss();
							}
						});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}


		}
		catch(JSONException e)
		{

			e.printStackTrace();
		}

	}

	private void parseResponse()
	{
		Utility.printLog("getprofile parseResponse  " + getprofileServerResponse);
		Gson gson = new Gson();
		getprofile = gson.fromJson(getprofileServerResponse, GetMyProfileResponse.class);
	}

	private void BackGroundTaskForUploadImage() {
		long chunkLength=1000*1024;
		long totalBytesRead=0;
		long bytesRemaining ;
		long FILE_SIZE = 0;
		String fileName;
		final ProgressDialog dialogL = null;
		File mFile;

		FileInputStream fin = null;
		totalBytesRead = 0;
		bytesRemaining=0;

		//mFile=new File("/sdcard"+"/SneekPeek", "picture"+".jpg");
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			//mFile= new File(Environment.getExternalStorageDirectory(), VariableConstants.TEMP_PHOTO_FILE_NAME);
			mFile=mFileTemp;
		}
		else
		{
			//mFile= new File(getFilesDir(),VariableConstants.TEMP_PHOTO_FILE_NAME);
			mFile=mFileTemp;
		}


		String temp=com.threembed.utilities.Utility.getCurrentDateTimeStringGMT();
		temp=new String(temp.trim().replace(" ", "20"));
		temp=new String(temp.trim().replace(":", "20"));
		temp=new String(temp.trim().replace("-", "20"));

		fileName="PA"+first_name.getText().toString()+temp+mFile.getName();

		try
		{
			fin= new FileInputStream(mFile);
		}
		catch (FileNotFoundException e2)
		{
			e2.printStackTrace();
		}

		if(mFile.isFile() && mFile.length()>0)
		{
			FILE_SIZE = mFile.length();
		}

		while (totalBytesRead < FILE_SIZE )
		{
			try
			{
				bytesRemaining = FILE_SIZE-totalBytesRead;

				if ( bytesRemaining < chunkLength ) // Remaining Data Part is Smaller Than CHUNK_SIZE
				{
					chunkLength = bytesRemaining;
				}

				byte []chunk = null;
				chunk=new byte[(int) chunkLength];

				byte fileContent[] = new byte[(int) chunkLength];
				try
				{
					fin.read(fileContent,0,(int)chunkLength);
				} catch (IOException e)
				{
					e.printStackTrace();
				}

				System.arraycopy(fileContent, 0, chunk, 0, (int) chunkLength);
				//byte[] encoded = Base64.encodeBase64(chunk);
				byte[] encoded = Base64.encode(chunk, Base64.NO_WRAP);
				String encodedString = new String(encoded);
				Utility utility=new Utility();
				totalBytesRead=totalBytesRead+chunkLength;

				JSONObject jsonObject = new JSONObject();
				try {
					String curenttime=utility.getCurrentGmtTime();
					jsonObject.put("ent_sess_token",session.getSessionToken());
					jsonObject.put("ent_dev_id",session.getDeviceId());
					jsonObject.put("ent_snap_name",fileName+".jpg");
					jsonObject.put("ent_snap_chunk",encodedString);
					jsonObject.put("ent_upld_from","2");
					jsonObject.put("ent_snap_type","1");
					jsonObject.put("ent_offset","1");
					jsonObject.put("ent_date_time",curenttime);
					Utility.printLog("params to upload "+jsonObject);

				} catch (JSONException e) {
					e.printStackTrace();
				}

				OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "uploadImage", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
					@Override
					public void onSuccess(String result) {
						Utility.printLog("resposne for check session "+result);
						if(result!=null)
						{
							Gson gson = new Gson();
							UploadImgeResponse response=gson.fromJson(result, UploadImgeResponse.class);
							if (dialogL !=null) {
								dialogL.dismiss();
							}

							if(response!=null)
							{
								if(response.getErrFlag().equals("0"))
								{
									new BackgroundTaskGetProfile().execute();
								}
								else
								{
									Utility.ShowAlert(response.getErrMsg(), getActivity());

								}
							}
						}
						else
						{
							Toast.makeText(getActivity(), getResources().getString(R.string.network_connection_fail), Toast.LENGTH_SHORT).show();
						}
					}
					@Override
					public void onError(String error) {
						Utility.printLog("on error for the login "+error);
						Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
					}
				});
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}



	class BackgroundForUpdateProfile extends AsyncTask<String,Void,String>
	{
		UpdateProfile response;
		ProgressDialog dialogL;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialogL= GetProcessDialog(getActivity());

			if (dialogL!=null) {
				dialogL.show();
			}

		}
		@Override
		protected String doInBackground(String... params) {
			UpdateUserProfile(dialogL);

			return null;
		}


		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

		}
	}

	private void  UpdateUserProfile(final ProgressDialog dialogL) {
		JSONObject jsonObject = new JSONObject();
		try {
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			String dataandTime=UltilitiesDate.getLocalTime(curenttime);


			SessionManager session = new SessionManager(getActivity());

			jsonObject.put("ent_sess_token",session.getSessionToken());
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_first_name",first_name.getText().toString());
			jsonObject.put("ent_last_name",last_name.getText().toString());
			jsonObject.put("ent_email",email.getText().toString());
			jsonObject.put("ent_phone",phone.getText().toString());
			jsonObject.put("ent_date_time",dataandTime);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "updateProfile", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				getprofileServerResponse = result;
				Utility.printLog("Success of getting Redeemed Info"+result);

				UpdatedUserInfo(dialogL);
			}
			@Override
			public void onError(String error) {
				dialogL.dismiss();

				Toast.makeText(getActivity(), "System error in getting Redeemed Info please retry", Toast.LENGTH_LONG).show();
				Utility.printLog("Error for volley getLoupon info");	}
		});
	}

	private void UpdatedUserInfo(ProgressDialog dialogL)
	{
		dialogL.dismiss();
		try
		{

			JSONObject jsnResponse = new JSONObject(getprofileServerResponse);

			String jsonErrorParsing = jsnResponse.getString("errFlag");

			Utility.printLog("jsonErrorParsing is ---> "+jsonErrorParsing);
			parseProfileUpdateResponse();

			if(updateprofile!=null)
			{
				if(updateprofile.getErrFlag().equals("0"))
				{

					Toast.makeText(getActivity(), updateprofile.getErrMsg(),Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getActivity(), updateprofile.getErrMsg(),Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());

				// set title
				alertDialogBuilder.setTitle(getResources().getString(R.string.error));

				// set dialog message
				alertDialogBuilder
						.setMessage(getResources().getString(R.string.server_error))
						.setCancelable(false)

						.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.dismiss();
							}
						});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}

		}
		catch(JSONException e)
		{
			e.printStackTrace();
			Utility.ShowAlert(getResources().getString(R.string.server_error), getActivity());
		}
	}

	private void parseProfileUpdateResponse()
	{
		Utility.printLog("parseResponse  " + getprofileServerResponse);
		Gson gson = new Gson();
		updateprofile = gson.fromJson(getprofileServerResponse, UpdateProfile.class);
	}





}