package com.roadyo.passenger.main;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.roadyo.passenger.pojo.AppointmentResponse;
import com.roadyo.passenger.pojo.AppointmentsPojo;
import com.roadyo.passenger.pojo.GetAppointmentDetails;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ourcabby.passenger.R;
import com.threembed.utilities.CircleTransform;
import com.threembed.utilities.OkHttpRequestObject;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.UltilitiesDate;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;
import org.json.JSONException;
import org.json.JSONObject;

import static android.view.View.VISIBLE;

public class Appointments extends Fragment implements OnItemClickListener{

	private View view;
	private ListView listView;
	private List<AppointmentsPojo> rowItems = new ArrayList<AppointmentsPojo>();
	private CustomListViewAdapter adapter;
	private AppointmentResponse response;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ProgressDialog dialogL;
	private String appointmentMonth,appointmentYear;
	private SessionManager session;
	Typeface typeFaceBold;
	Typeface typeFace;
	private String email,apntDate;
	RelativeLayout layout;
	View footerView;
	Typeface typeFace2,typeFace3;
	private ImageView emptyscreen;
	private int visibleThreshold = 5;
	private int currentPage = 0;
	private int previousTotal = 0;
	private boolean loading = true;
	private boolean bookingsFinished=false;
	private Typeface latoRegular,latoBold;
	private RatingBar invoice_driver_rating;
	private boolean laterBooking=false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		if (view != null)
		{
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		try
		{
			view = inflater.inflate(R.layout.appointment, container, false);


		} catch (InflateException e)
		{
			/* map is already there, just return view as it is */
			Log.e("", "onCreateView  InflateException "+e);
		}

		session =  new SessionManager(getActivity());

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.profile_image_frame)
				.showImageForEmptyUri(R.drawable.profile_image_frame)
				.showImageOnFail(R.drawable.profile_image_frame)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(0))
				.build();

		initializeVariables(view);


		SetDate();

		if(Utility.isNetworkAvailable(getActivity()))
		{
			BackgroundAppointmentMonthChange_Volley(appointmentYear+"-"+appointmentMonth);
		}
		else
		{
			Intent homeIntent=new Intent("com.ourcabby.passenger.internetStatus");
			homeIntent.putExtra("STATUS", "0");
			getActivity().sendBroadcast(homeIntent);
		}



		return view;
	}

	private void initializeVariables(View view2)
	{
		listView=(ListView)view2.findViewById(R.id.list_appointment);
		listView.setOnItemClickListener(this);
		layout= (RelativeLayout) view2.findViewById(R.id.layout);
		adapter=new CustomListViewAdapter(getActivity(),R.layout.list_row_test, rowItems);
		emptyscreen= (ImageView) view2.findViewById(R.id.emptyscreen);

		latoRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");
		latoBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Bold.ttf");
		typeFace2=Typeface.createFromAsset(getActivity().getAssets(),"fonts/Zurich Light Condensed.ttf");
		typeFace3=Typeface.createFromAsset(getActivity().getAssets(),"fonts/Zurich Condensed.ttf");
		listView.setAdapter(adapter);

		listView.setOnScrollListener(new AbsListView.OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
								 int visibleItemCount, int totalItemCount) {
				if (loading)
				{
					if (totalItemCount > previousTotal) {
						loading = false;
						previousTotal = totalItemCount;
						currentPage++;
					}
				}
				if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
					// I load the next page of gigs using a background task,
					// but you can call any function here.
					VariableConstants.COUNTER++;
					if(!bookingsFinished)
					{
						BackgroundAppointmentMonthChange_Volley(appointmentYear+"-"+appointmentMonth);

					}

					loading = true;
				}
			}

		});

		VariableConstants.COUNTER=0;


		MainActivityDrawer.driver_tip.setVisibility(View.INVISIBLE);

	}


	private void SetDate()
	{
		Utility utility=new Utility();
		String curenttime=utility.getCurrentGmtTime();
		Utility.printLog("MasterJobStarted curenttime="+curenttime);
		//Local Time_date 2014-12-02 13:10:52

		String[] date_time = curenttime.split(" ");
		String[] date = date_time[0].split("-");

		String year = date[0];
		String month = date[1];

		appointmentYear = year;
		appointmentMonth = month;

	}


	class CustomListViewAdapter extends ArrayAdapter<AppointmentsPojo> {


		Context context;
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		// Typeface typeFace1=Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaNeue-Light.otf");


		public CustomListViewAdapter(Context context, int resourceId,
									 List<AppointmentsPojo> items) {
			super(context, resourceId, items);
			this.context = context;

		}


		private class ViewHolder
		{
			ImageView driver_pic;
			TextView name;
			TextView drop;
			TextView time;
			TextView pickup;
			TextView amount;
			TextView status,img_distance;
			RelativeLayout drop_layout;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			ViewHolder holder = null;
			AppointmentsPojo rowItem = getItem(position);


			if(convertView == null)
			{
				convertView = mInflater.inflate(R.layout.list_row_test, null);
				holder = new ViewHolder();

				holder.driver_pic=(ImageView)convertView.findViewById(R.id.driver_profile_pic_appointment);
				holder.name=(TextView)convertView.findViewById(R.id.driver_name_appointment);
				holder.pickup=(TextView)convertView.findViewById(R.id.pickup);
				holder.drop=(TextView)convertView.findViewById(R.id.drop);
				holder.time=(TextView)convertView.findViewById(R.id.duration);
				holder.amount=(TextView)convertView.findViewById(R.id.amount);
				holder.status=(TextView)convertView.findViewById(R.id.apt_status);
				holder.drop_layout = (RelativeLayout)convertView.findViewById(R.id.rl_drop);
				holder.img_distance= (TextView) convertView.findViewById(R.id.img_distance);

				holder.pickup.setTypeface(typeFace2);
				holder.drop.setTypeface(typeFace2);
				holder.time.setTypeface(typeFace3);
				holder.status.setTypeface(typeFace3);
				holder.amount.setTypeface(typeFace2);
				holder.img_distance.setTypeface(typeFace2);

				holder.name.setTypeface(latoBold);
				holder.pickup.setTypeface(latoRegular);
				holder.drop.setTypeface(latoRegular);
				holder.amount.setTypeface(latoRegular);
				holder.img_distance.setTypeface(latoBold);

				convertView.setTag(holder);
			}
			else
				holder = (ViewHolder) convertView.getTag();

			String url=VariableConstants.IMAGE_BASE_URL+rowItem.getPic();
			imageLoader.displayImage(url, holder.driver_pic, options, animateFirstListener);

			holder.name.setText(rowItem.getName());
			holder.pickup.setText(rowItem.getLocation());
			if(rowItem.getDrop_address()==null || rowItem.getDrop_address().isEmpty() )
			{
				holder.drop_layout.setVisibility(View.GONE);
			}
			else
			{
				holder.drop_layout.setVisibility(View.VISIBLE);
				holder.drop.setText(rowItem.getDrop_address());
			}
			holder.time.setText(rowItem.getDate()+" "+rowItem.getTime());
			holder.status.setText(rowItem.getStatus());
			holder.amount.setText(rowItem.getAmount());

			return convertView;
		}
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

	private void BackgroundAppointmentMonthChange_Volley(final String date) {
		dialogL=Utility.GetProcessDialog(getActivity());
		dialogL.setCancelable(false);
		if(dialogL!=null)
		{
			dialogL.show();
		}

		JSONObject jsonObject = new JSONObject();

		try {
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			String dateandTime=UltilitiesDate.getLocalTime(curenttime);
			jsonObject.put("ent_sess_token",session.getSessionToken() );
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_appnt_dt",date);
			jsonObject.put("ent_date_time",dateandTime);
			jsonObject.put("ent_page_index",VariableConstants.COUNTER+"");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "process.php/getSlaveAppointments", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				if (dialogL!=null)
				{
					dialogL.dismiss();
					dialogL=null;
				}


				fetchData(result);
			}
			@Override
			public void onError(String error) {

				if (dialogL!=null) {
					dialogL.dismiss();
					dialogL=null;
				}
				Utility.printLog("LoginResponse:ERROR");
				if(isAdded())
					Toast.makeText(getActivity(), "Error. Try Again!!"+error, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void fetchData(String jsonResponse)
	{
				/*try
				{*/
		Gson gson = new Gson();
		response=gson.fromJson(jsonResponse,AppointmentResponse.class);
		Utility.printLog("paramt o history response "+jsonResponse);


		Utility.printLog("booking respose  "+jsonResponse);

		if(response!=null)
		{
			//rowItems.clear();

			if(response.getErrFlag().equals("0") && isAdded())
			{
				if(response.getLastcount().equals("1"))
				{
					bookingsFinished=true;
				}

				for(int i=0;i<response.getAppointments().size();i++)
				{

					String name,date,status,pay_status,pic,time,email,phone,pickup_address,notes,apt_date,status_code,amount,drop_address,distance,cancel_status;

					name=response.getAppointments().get(i).getFname();
					date=response.getAppointments().get(i).getApntDate();
					status=response.getAppointments().get(i).getStatus();
					pay_status=response.getAppointments().get(i).getPayStatus();
					pic=response.getAppointments().get(i).getpPic();
					time=response.getAppointments().get(i).getApntTime();

					email=response.getAppointments().get(i).getEmail();
					phone=response.getAppointments().get(i).getPhone();
					pickup_address=response.getAppointments().get(i).getAddrLine1();
					notes=response.getAppointments().get(i).getNotes();
					apt_date=response.getAppointments().get(i).getApntDt();
					status_code=response.getAppointments().get(i).getStatCode();
					amount=response.getAppointments().get(i).getAmount();
					drop_address=response.getAppointments().get(i).getDropLine1();
					distance=response.getAppointments().get(i).getDistance();
					cancel_status=response.getAppointments().get(i).getCancel_status();

					AppointmentsPojo item=new AppointmentsPojo(name,response.getAppointments().get(i).getBid(),response.getAppointments().get(i).getApptType(),apt_date,date, status,pay_status,pic,time,email,phone,pickup_address,notes,status_code,amount,drop_address,distance,cancel_status);
					rowItems.add(item);

				}
			}
			else
			{
				emptyscreen.setVisibility(View.VISIBLE);
			}
			if(response.getErrNum().equals("6") || response.getErrNum().equals("7") ||
					response.getErrNum().equals("94") || response.getErrNum().equals("96"))
			{
				Toast.makeText(getActivity(), response.getErrMsg(),Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getActivity(), SplashActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				getActivity().startActivity(i);
				getActivity().overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
			}						else if(isAdded())


				adapter.notifyDataSetChanged();
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		Utility.printLog("CONTROL INSIDEonItemClick");
		final AppointmentsPojo rowItem=(AppointmentsPojo)arg0.getItemAtPosition(arg2);

		Utility.printLog("onItemClick apt date="+rowItem.getAptDate()+" getEmail="+rowItem.getEmail());
		Utility.printLog("onItemClick getPayStatus=" + rowItem.getPayStatus());
		Utility.printLog("row item sresoonse "+rowItem);

	/*	if(rowItem.getApptType().equals("2"))
		{
			if(rowItem.getStatCode()!=null) {
				Utility.printLog("status cde and cancel status "+rowItem.getStatCode()+" status "+rowItem.getCancel_status());
				if (rowItem.getStatCode().equals("9") || rowItem.getStatCode().equals("1")|| (rowItem.getStatCode().equals("4") && (rowItem.getCancel_status().equals("3")||rowItem.getCancel_status().equals(""))))
				{
					apntDate = rowItem.getAptDate();

					final Dialog invoiceDialogue;
					invoiceDialogue = new Dialog(getActivity());
					invoiceDialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);
					invoiceDialogue.setContentView(R.layout.invoice_popup);
					invoiceDialogue.setCancelable(false);
					invoiceDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
					ImageView driverImage = (ImageView) invoiceDialogue.findViewById(R.id.driverImage);
					TextView driverName = (TextView) invoiceDialogue.findViewById(R.id.driverName);
					TextView amount = (TextView) invoiceDialogue.findViewById(R.id.amount);
					TextView pickupaddresstv = (TextView) invoiceDialogue.findViewById(R.id.pickupaddresstv);
					TextView dropaddresstv = (TextView) invoiceDialogue.findViewById(R.id.dropaddresstv);
					TextView rideDate = (TextView) invoiceDialogue.findViewById(R.id.rideDate);
					TextView lastRide = (TextView) invoiceDialogue.findViewById(R.id.lastRide);
					TextView needHelp = (TextView) invoiceDialogue.findViewById(R.id.needHelp);
					TextView receipt = (TextView) invoiceDialogue.findViewById(R.id.receipt);
					TextView txt_rating = (TextView) invoiceDialogue.findViewById(R.id.txt_rating);
					TextView invoice_review = (TextView) invoiceDialogue.findViewById(R.id.invoice_review);
					LinearLayout mainLayout = (LinearLayout) invoiceDialogue.findViewById(R.id.mainLayout);
					invoice_driver_rating = (RatingBar) invoiceDialogue.findViewById(R.id.invoice_driver_rating);
					RelativeLayout rl_rate_journey = (RelativeLayout) invoiceDialogue.findViewById(R.id.rl_rate_journey);

					RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.WRAP_CONTENT);

					p.addRule(RelativeLayout.BELOW, R.id.driverLayout);
					p.setMargins(0, 0, 0, 40);
					invoice_driver_rating.setLayoutParams(p);

					mainLayout.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT; // For change `layout` width to `WRAP_CONTENT`
					mainLayout.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;

					invoice_review.setVisibility(View.GONE);
					needHelp.setText("BACK");
					rl_rate_journey.setVisibility(View.GONE);

					needHelp.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							invoiceDialogue.dismiss();
						}
					});


					Button submit = (Button) invoiceDialogue.findViewById(R.id.submitButton);
					TextView pickDate = (TextView) invoiceDialogue.findViewById(R.id.pickDate);
					TextView dropDate = (TextView) invoiceDialogue.findViewById(R.id.dropDate);


					pickDate.setText(getResources().getString(R.string.pickup));
					dropDate.setText(getResources().getString(R.string.drop));


					submit.setText(getActivity().getResources().getString(R.string.cancel_booking));

					submit.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							cancelPopup(rowItem.getBid(), rowItem.getAptDate());
							invoiceDialogue.dismiss();
						}
					});

					typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");

					receipt.setVisibility(View.GONE);

					txt_rating.setVisibility(View.GONE);

					typeFaceBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Bold.ttf");

					lastRide.setTypeface(typeFaceBold);
					rideDate.setTypeface(typeFace);
					needHelp.setTypeface(typeFace);
					pickupaddresstv.setTypeface(typeFace);
					dropaddresstv.setTypeface(typeFace);
					driverName.setTypeface(typeFace);
					amount.setTypeface(typeFaceBold);
					receipt.setTypeface(typeFace);
					txt_rating.setTypeface(typeFace);
					submit.setTypeface(typeFace);
					pickDate.setTypeface(typeFaceBold);
					dropDate.setTypeface(typeFaceBold);



					driverImage.setVisibility(View.INVISIBLE);
					driverName.setText(response.getAppointments().get(arg2).getFname());
					amount.setText( response.getAppointments().get(arg2).getAmount()+" "+getResources().getString(R.string.currencuSymbol));
					pickupaddresstv.setText(response.getAppointments().get(arg2).getAddrLine1());

					if (!response.getAppointments().get(arg2).getDropLine1().equals(""))

					{
						dropaddresstv.setText(response.getAppointments().get(arg2).getDropLine1());

					} else
					{
						dropaddresstv.setText("Drop off address not added");
					}


					rideDate.setText(apntDate);    //to convert the date to required format

					invoiceDialogue.show();
				} else
				{
					Utility.ShowAlert(rowItem.getStatus(), getActivity());
				}
			}

			}*/
		/*else
		{*/

		if(rowItem.getStatCode()!=null)

			Utility.printLog("statuscode  "+rowItem.getStatCode()+" cancel status "+rowItem.getCancel_status());

		{   if( rowItem.getStatCode().equals("9") || (rowItem.getStatCode().equals("4") && (rowItem.getCancel_status().equals("3") )))
		{
			if(rowItem.getPayStatus().equals("1") || rowItem.getPayStatus().equals("3"))
			{
				email=rowItem.getEmail();
				apntDate=rowItem.getAptDate();

				if(Utility.isNetworkAvailable(getActivity()))
				{
					BackgroundGetInvoiceDetails();
				}
				else
				{
					Utility.ShowAlert(getResources().getString(R.string.network_connection_fail),getActivity());
				}


			}
			else
			{


				email=rowItem.getEmail();
				apntDate=rowItem.getAptDate();
				if(Utility.isNetworkAvailable(getActivity()))
				{
					BackgroundGetInvoiceDetails();

				}
				else
				{
					Utility.ShowAlert(getResources().getString(R.string.network_connection_fail),getActivity());
				}
			}
		}
		else
		{
			Utility.ShowAlert(rowItem.getStatus(), getActivity());
		}

		}
		/*}*/


	}



	private void cancelPopup(final String bid, final String aptDate)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());

		// set title
		alertDialogBuilder.setTitle(getResources().getString(R.string.message));

		// set dialog message
		alertDialogBuilder
				.setMessage(getResources().getString(R.string.cancelYourbooking))
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						CancelAppointment(bid,aptDate);

					}
				})
				.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
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

	private void  CancelAppointment(final String bid, final String aptDate) {
		JSONObject jsonObject = new JSONObject();
		dialogL= Utility.GetProcessDialogNew(getActivity(), getResources().getString(R.string.cancelling_trip));
		dialogL.setCancelable(false);
		if (dialogL!=null)
		{
			dialogL.show();
		}
		try {

			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();

			jsonObject.put("ent_sess_token",session.getSessionToken() );
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_appnt_dt",aptDate);
			jsonObject.put("ent_date_time",curenttime);
			jsonObject.put("ent_bid",bid);
			Utility.printLog("params to cancel "+jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "cancelAppointment", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				JSONObject jsnResponse = null;
				try
				{
					jsnResponse = new JSONObject(result);
					String jsonErrorParsing = jsnResponse.getString("errFlag");
					String message=jsnResponse.getString("errMsg");
					Utility.printLog("cancel resonse "+jsonErrorParsing+" message "+message);

					if(jsonErrorParsing.equals("0"))
					{
						if(dialogL!=null)
						{
							dialogL.dismiss();
							dialogL = null;
						}
						if(Utility.isNetworkAvailable(getActivity()))
						{
							rowItems.clear();
							BackgroundAppointmentMonthChange_Volley(appointmentYear + "-" + appointmentMonth);
						}

					}
					else
					{
						if(dialogL!=null)
						{
							dialogL.dismiss();
							dialogL = null;
						}
						Utility.ShowAlert(message,getActivity());
					}


				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onError(String error) {
				if(dialogL!=null)
				{
					dialogL.dismiss();
					dialogL = null;
				}

				Toast.makeText(getActivity(), getResources().getString(R.string.server_error)+error, Toast.LENGTH_LONG).show();
			}
		});
	}

	private void BackgroundGetInvoiceDetails() {
		final GetAppointmentDetails[] response = new GetAppointmentDetails[1];
		final JSONObject[] jsonObject = {new JSONObject()};
		dialogL=Utility.GetProcessDialog(getActivity());

		if (dialogL!=null)
		{
			dialogL.show();
		}
		Utility utility=new Utility();
		String curenttime=utility.getCurrentGmtTime();
		try{
			jsonObject[0].put("ent_sess_token",session.getSessionToken());
			jsonObject[0].put("ent_dev_id",session.getDeviceId());
			jsonObject[0].put("ent_email", email);
			jsonObject[0].put("ent_appnt_dt",apntDate);
			jsonObject[0].put("ent_user_type","2");
			jsonObject[0].put("ent_date_time",curenttime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "getAppointmentDetails", jsonObject[0], new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				if (dialogL!=null)
				{
					dialogL.dismiss();
				}


				if (result!=null)
				{
					Gson gson = new Gson();
					response[0] =gson.fromJson(result,GetAppointmentDetails.class);
					Utility.printLog("response for the history "+result);
				}
				else
				{
					Toast.makeText(getActivity(),getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
				}

				if(response[0] !=null)
				{
					if(response[0].getErrFlag().equals("0"))
					{

						final Dialog invoiceDialogue;
						invoiceDialogue = new Dialog(getActivity());
						invoiceDialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);
						invoiceDialogue.setContentView(R.layout.invoice_popup);
						invoiceDialogue.setCancelable(false);
						invoiceDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
						ImageView driverImage = (ImageView) invoiceDialogue.findViewById(R.id.driverImage);
						TextView driverName = (TextView) invoiceDialogue.findViewById(R.id.driverName);
						TextView amount = (TextView) invoiceDialogue.findViewById(R.id.amount);
						TextView pickupaddresstv = (TextView) invoiceDialogue.findViewById(R.id.pickupaddresstv);
						TextView dropaddresstv = (TextView) invoiceDialogue.findViewById(R.id.dropaddresstv);
						TextView rideDate = (TextView) invoiceDialogue.findViewById(R.id.rideDate);
						TextView lastRide = (TextView) invoiceDialogue.findViewById(R.id.lastRide);
						TextView needHelp = (TextView) invoiceDialogue.findViewById(R.id.needHelp);
						TextView receipt = (TextView) invoiceDialogue.findViewById(R.id.receipt);
						TextView txt_rating = (TextView) invoiceDialogue.findViewById(R.id.txt_rating);
						TextView invoice_review= (TextView) invoiceDialogue.findViewById(R.id.invoice_review);
						LinearLayout mainLayout= (LinearLayout) invoiceDialogue.findViewById(R.id.mainLayout);
						invoice_driver_rating= (RatingBar) invoiceDialogue.findViewById(R.id.invoice_driver_rating);

						lastRide.setText(getResources().getString(R.string.booking_history));

						RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);

						p.addRule(RelativeLayout.BELOW, R.id.driverLayout);
						p.setMargins(0,0,0,40);
						invoice_driver_rating.setLayoutParams(p);

						mainLayout.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT; // For change `layout` width to `WRAP_CONTENT`
						mainLayout.getLayoutParams().height=LinearLayout.LayoutParams.WRAP_CONTENT;

						invoice_review.setVisibility(View.GONE);

						invoice_driver_rating.setRating(Float.parseFloat(response[0].getR()));
						invoice_driver_rating.setEnabled(false);


						Button submit = (Button) invoiceDialogue.findViewById(R.id.submitButton);
						TextView pickDate = (TextView) invoiceDialogue.findViewById(R.id.pickDate);
						TextView dropDate = (TextView) invoiceDialogue.findViewById(R.id.dropDate);

						submit.setText(getResources().getString(R.string.close));


						if (!response[0].getPickupDt().equals(""))
						{
							pickDate.setText(getResources().getString(R.string.pickup) + " (" + response[0].getPickupDt() + ")");

						}
						if (!response[0].getDropDt().equals(""))
						{
							dropDate.setText(getResources().getString(R.string.drop) + " (" + response[0].getDropDt() + ")");

						}


						submit.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v)
							{
								invoiceDialogue.dismiss();

							}
						});

						typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");

						receipt.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								final Dialog receiptDialogue;
								receiptDialogue = new Dialog(getActivity());
								receiptDialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);
								receiptDialogue.setContentView(R.layout.receipt_layout);
								receiptDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

								TextView meterFee = (TextView) receiptDialogue.findViewById(R.id.meterFee);
								//TextView parkingValue = (TextView) receiptDialogue.findViewById(R.id.parkingValue);
								TextView airportValue = (TextView) receiptDialogue.findViewById(R.id.airportValue);
								//	TextView tollValue = (TextView) receiptDialogue.findViewById(R.id.tollValue);
								TextView tip = (TextView) receiptDialogue.findViewById(R.id.tip);
								TextView invoice_discount_amount = (TextView) receiptDialogue.findViewById(R.id.invoice_discount_amount);
								TextView invoice_total_amount = (TextView) receiptDialogue.findViewById(R.id.invoice_total_amount);
								TextView subtotalValue = (TextView) receiptDialogue.findViewById(R.id.subtotalValue);
								TextView type = (TextView) receiptDialogue.findViewById(R.id.type);
								TextView total = (TextView) receiptDialogue.findViewById(R.id.total);
								Button submitButton = (Button) receiptDialogue.findViewById(R.id.submitButton);
								TextView distanceValue = (TextView) receiptDialogue.findViewById(R.id.distanceValue);
								TextView timeValue = (TextView) receiptDialogue.findViewById(R.id.timeValue);
								TextView chargedTexxt = (TextView) receiptDialogue.findViewById(R.id.chargedTexxt);
								TextView title = (TextView) receiptDialogue.findViewById(R.id.title);
								TextView bookingId = (TextView) receiptDialogue.findViewById(R.id.bookingId);
								TextView minFare= (TextView) receiptDialogue.findViewById(R.id.minFare);
								TextView distanceText= (TextView) receiptDialogue.findViewById(R.id.distanceText);
								TextView timeText= (TextView) receiptDialogue.findViewById(R.id.timeText);
								TextView invoice_newsubtotal_txt= (TextView) receiptDialogue.findViewById(R.id.invoice_newsubtotal_txt);
								TextView invoice_discount_txt= (TextView) receiptDialogue.findViewById(R.id.invoice_discount_txt);
								RelativeLayout invoice_min_fare_layout= (RelativeLayout) receiptDialogue.findViewById(R.id.invoice_min_fare_layout);
								RelativeLayout invoice_newsubtotal_layout= (RelativeLayout) receiptDialogue.findViewById(R.id.invoice_newsubtotal_layout);
								RelativeLayout wallet_deducted_layout= (RelativeLayout) receiptDialogue.findViewById(R.id.wallet_deducted_layout);
								TextView wallet_deuct_amt= (TextView) receiptDialogue.findViewById(R.id.wallet_deuct_amt);


								meterFee.setTypeface(typeFace);
								//	parkingValue.setTypeface(typeFace);
								airportValue.setTypeface(typeFace);
								//	tollValue.setTypeface(typeFace);
								tip.setTypeface(typeFace);
								invoice_discount_amount.setTypeface(typeFace);
								invoice_total_amount.setTypeface(typeFace);
								subtotalValue.setTypeface(typeFace);
								type.setTypeface(typeFace);
								total.setTypeface(typeFace);
								distanceValue.setTypeface(typeFace);
								timeValue.setTypeface(typeFace);
								chargedTexxt.setTypeface(typeFaceBold);
								title.setTypeface(typeFaceBold);
								bookingId.setTypeface(typeFace);
								minFare.setTypeface(typeFace);
								distanceText.setTypeface(typeFace);
								timeText.setTypeface(typeFace);
								invoice_newsubtotal_txt.setTypeface(typeFace);
								invoice_discount_txt.setTypeface(typeFace);


								bookingId.setText("Booking Id :" + response[0].getBid());

								submitButton.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										receiptDialogue.dismiss();
									}
								});

								if (!response[0].getBaseFee().equals(""))

									meterFee.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(response[0].getBaseFee())));
								//	parkingValue.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(response[0].getParkingFee())));
								airportValue.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(response[0].getAirportFee())));
								//	tollValue.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(response[0].getTollFee())));
								tip.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(response[0].getTip())));
								invoice_discount_amount.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(response[0].getDiscount())));
								invoice_total_amount.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(response[0].getAmountWoutwallet())));

								if(response[0].getPayType().equals("2"))
									total.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(response[0].getCashCollected())));
								else
									total.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(response[0].getAmountWoutwallet())));

								distanceValue.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(response[0].getDistanceFee())));
								timeValue.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(response[0].getTimeFee())));

								minFare.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(response[0].getMin_fare()+"")));
								subtotalValue.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(response[0].getSubTotal())));

								if(Double.parseDouble(response[0].getSubTotal())>Double.parseDouble(response[0].getMin_fare()))
								{
									invoice_newsubtotal_layout.setVisibility(View.VISIBLE);
									invoice_min_fare_layout.setVisibility(View.GONE);
								}
								else
								{
									invoice_newsubtotal_layout.setVisibility(View.VISIBLE);
									invoice_min_fare_layout.setVisibility(View.VISIBLE);
								}

								if(!response[0].getWalletDeducted().equals(""))
								{
									if(Double.parseDouble(response[0].getWalletDeducted())>0)
									{
										wallet_deducted_layout.setVisibility(VISIBLE);
										wallet_deuct_amt.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(response[0].getWalletDeducted())));

									}
								}

								distanceText.setText(getResources().getString(R.string.distnceFee) + "(" +response[0].getDis()+" "+getResources().getString(R.string.distanceUnit)+")");
								timeText.setText(getResources().getString(R.string.timeFare) + "(" + getDurationString(Integer.parseInt(response[0].getDur()))+")");
								invoice_newsubtotal_txt.setText(getResources().getString(R.string.tip) + "(" + response[0].getTipPercent()+"%"+")");
								if(!response[0].getCode().equals(""))
									invoice_discount_txt.setText(getResources().getString(R.string.discunt) + "(" + response[0].getCode()+")");


								if (response[0].getPayType().equals("2"))
								{
									type.setText(getResources().getString(R.string.cash));

								} else
								{
									type.setText(getResources().getString(R.string.card));

								}


								receiptDialogue.show();
							}
						});


						needHelp.setVisibility(View.GONE);
						txt_rating.setVisibility(View.GONE);

						typeFaceBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Bold.ttf");

						lastRide.setTypeface(typeFaceBold);
						rideDate.setTypeface(typeFace);
						needHelp.setTypeface(typeFace);
						pickupaddresstv.setTypeface(typeFace);
						dropaddresstv.setTypeface(typeFace);
						driverName.setTypeface(typeFace);
						amount.setTypeface(typeFaceBold);
						receipt.setTypeface(typeFace);
						txt_rating.setTypeface(typeFace);
						submit.setTypeface(typeFace);
						pickDate.setTypeface(typeFaceBold);
						dropDate.setTypeface(typeFaceBold);


						if (!response[0].getpPic().equals("") && !response[0].getpPic().equals(null))
						{

							int width,height;
							width = getResources().getDrawable(R.drawable.on_the_way_profile_default).getMinimumWidth();
							height = getResources().getDrawable(R.drawable.on_the_way_profile_default).getMinimumHeight();
							driverImage.setTag(getTarget_api(driverImage));
							Picasso.with(getActivity()).load(VariableConstants.IMAGE_URL+ response[0].getpPic())
									.transform(new CircleTransform())
									.placeholder(R.drawable.on_the_way_profile_default)
									.resize(width, height)
									.into((Target) driverImage.getTag());

						} else
						{
							driverImage.setImageResource(R.drawable.ic_launcher);
						}
						driverName.setText(response[0].getfName()+" "+ response[0].getlName());
						amount.setText(getResources().getString(R.string.currencuSymbol)+" "+response[0].getAmount() );
						pickupaddresstv.setText(response[0].getAddr1());
						dropaddresstv.setText(response[0].getDropAddr1());


						rideDate.setText(response[0].getApptDt());    //to convert the date to required format

						invoiceDialogue.show();

					}

					else
					{
						Toast.makeText(getActivity(), response[0].getErrMsg(),Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					Toast.makeText(getActivity(), getResources().getString(R.string.server_error),Toast.LENGTH_SHORT).show();
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

	private String getDurationString(int seconds) {

		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		seconds = seconds % 60;

		return twoDigitString(hours)+"H" + ":" + twoDigitString(minutes)+"M" + ":" + twoDigitString(seconds)+"S";
	}

	private String twoDigitString(int number) {

		if (number == 0) {
			return "00";
		}

		if (number / 10 == 0) {
			return "0" + number;
		}

		return String.valueOf(number);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		SetDate();
	}

	private String round(double value)
	{
		String s = String.format(Locale.ENGLISH,"%.2f", value);
		Utility.printLog("rounded value="+s);
		return s;
	}


	private Target getTarget_api(final ImageView iamge)
	{
		Target target=new Target() {
			@Override
			public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
			{
				iamge.setBackground(new BitmapDrawable(getActivity().getResources(),bitmap));
			}



			@Override
			public void onBitmapFailed(Drawable errorDrawable)
			{
				iamge.setBackground(errorDrawable);
			}

			@Override
			public void onPrepareLoad(Drawable placeHolderDrawable) {
				iamge.setBackground(placeHolderDrawable);
			}
		};
		return target;
	}
}
