package com.roadyo.passenger.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gc.materialdesign.views.ProgressBarCircularIndetermininate;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;
import com.roadyo.passenger.pojo.FareCalculation;
import com.roadyo.passenger.pojo.GeocodingResponse;
import com.roadyo.passenger.pojo.GetAppointmentDetails;
import com.roadyo.passenger.pojo.GetCarDetails;
import com.roadyo.passenger.pojo.GetCardResponse;
import com.roadyo.passenger.pojo.LiveBookingResponse;
import com.roadyo.passenger.pojo.LoginResponse;
import com.roadyo.passenger.pojo.StatusInformation;
import com.roadyo.passenger.pojo.TimeForNearestPojo;
import com.roadyo.passenger.pojo.TipResponse;
import com.roadyo.passenger.pubnu.pojo.PubnubResponseNew;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ourcabby.passenger.R;
import com.threembed.utilities.CircleTransform;
import com.threembed.utilities.NetworkNotifier;
import com.threembed.utilities.NetworkUtil;
import com.threembed.utilities.OkHttpRequest;
import com.threembed.utilities.OkHttpRequestObject;
import com.threembed.utilities.PicassoMarker;
import com.threembed.utilities.Scaler;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.UltilitiesDate;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Time;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import eu.janmuller.android.simplecropimage.Util;

import static android.view.View.VISIBLE;

public class HomePageFragment extends Fragment implements OnClickListener,NetworkNotifier
{
	public static GoogleMap googleMap;
	private double currentLatitude,currentLongitude;
	private double searchlocatinlat,searchlocatinlng;
	private TextView review;
	boolean flag;
	private static final String TAG ="HomePage";
	private Button Request_Pick_up_here,Fare_Quote,promeCode,Driver_on_the_way_txt,AddLocation;
	private ImageButton Btn_Back,Img_Map,Img_Dropoff,Mid_Pointer;
	private ImageButton addressSearchButton,myPosition;
	private ImageView Card_Image,Driver_Profile_Pic;
	private TextView current_address,Txt_Pickup,Driver_Name,Driver_Time,Driver_Car_Num;
	private TextView pickupLocationAddress,Dropoff_Location_Address,Card_Info,pickup_Distance,pickupaddresstv;
	private String pick_up,getCancelResponse;
	private static View view;
	private boolean isSetDropoffLocation=false,isBackPressed=false;
	public static int cardChecked=-1;
	private Timer myTimer;
	private TimerTask myTimerTask;
	NetworkUtil networkUtil;
	private RelativeLayout pickup,show_address_relative,relativePickupLocation,Relative_Pickup_Navigation,Relative_Dropoff_Location,Relative_Card_Info;
	private RelativeLayout RL_homepage,Rl_distance_time,all_types_layout;
	private LoginResponse response = new LoginResponse();
	public static Activity mActivity;
	private boolean isFareQuotePressed=false;
	private String from_latitude,from_longitude,to_latitude,to_longitude,mDROPOFF_ADDRESS,mPICKUP_ADDRESS;
	private String Default_Card_Id,carDetailsResponse;
	private String Car_Type_Id=null;
	private  Pubnub pubnub;
	private HashMap<String,Marker> marker_map;
	private HashMap<String,Marker> marker_map_on_the_way,marker_map_arrived;
	private HashMap<String,Location> type1_markers,type2_markers,type3_markers,type4_markers;
	private ArrayList<String> type1_channels_list,type2_channels_list,type3_channels_list,type4_channels_list;
	private IntentFilter filter;
	private BroadcastReceiver receiver;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private View popupLayout;
	private PopupWindow popup_share;//popup_driver;
	private Button message_shar,whatsapp_sahre,email_share,cancel_share;//facebook_share,twitter_share
	private Button pay_cancel,pay_cash,pay_card,now_button,later_button;//,pay_cash,,pay_card
	private RelativeLayout driver_parent,choose_payment_layout;
	private LiveBookingResponse CancelBooking;
	private ProgressDialog dialogL;
	private SessionManager session;
	private GetAppointmentDetails appointmentResponse;
	private StatusInformation statusResponse;
	private GetCarDetails getCarDetails;
	private Marker driver_on_the_way_marker,driver_arrived;
	private Marker picupmarker;
	private String payment_type;
	private ArrayList<String> old_subscribed_driver_channels = new ArrayList<String>();
	private ArrayList<String> type1NearestDrivers,type2NearestDrivers,type3NearestDrivers,type4NearestDrivers;
	private ArrayList<String> nearestDrivers;
	private String locationName,formattedAddress,car_name,distance="--";
	private float rotation_angle;
	private Timer pollingTimer = new Timer();
	private Timer myTimer_publish;
	private TimerTask myTimerTask_publish;
	private static boolean visibility=false;
	private ProgressBarCircularIndetermininate pubnubProgressDialog;
	private int height,width,current_widht=0;
	private ImageView image,three_image,four_image;
	private ImageButton one_car_button,two_cars_button1,two_cars_button2,three_cars_button1,three_cars_button2,three_cars_button3;
	private ImageButton four_cars_button1,four_cars_button2,four_cars_button3,four_cars_button4;
	private RelativeLayout one_car_layout,two_cars_layout,three_cars_layout,four_cars_layout,now_later_status;
	private TextView one_car_Type_Name,two_cars_Type1_Name,two_cars_Type2_Name,three_cars_Type1_Name,three_cars_Type2_Name,three_cars_Type3_Name;
	private TextView four_cars_Type1_Name,four_cars_Type2_Name,four_cars_Type3_Name,four_cars_Type4_Name;//,booked_now_later
	private Geocoder geocoder;
	// rahul : car type index
	private boolean IsreturnFromSearch=false;
	private int car_type_index=0;
	private String selectedImage,selectedType,surgePrice="";
	TimerTask myTimerTask_ETA;
	Timer myTimer_ETA;
	private boolean isSetClicked=false;
	private boolean isType1MarkersPloted=false,isType2MarkersPloted=false,isType3MarkersPloted=false,isType4MarkersPloted=false;
	private boolean firstTimeMarkersPlotting = true,isCouponValid = false;
	private String laterBookingDate=null,mPromoCode=null;
	private String later_year,later_month,later_day,later_hour,later_min;
	private ImageButton hybrid_view;
	private LinearLayout farePromoLayouy,share_eta,contact_driver,cancel_trip;//
	private double[] dblArray;
	public boolean gpsEnabled;
	public boolean gpsFix;
	public double latitude;
	public double longitude;
	private Dialog dialog;
	//private LocationManager locationManager;
	//private MyGpsListener gpsListener;
	private long DURATION_TO_FIX_LOST_MS = 10000;
	private long MINIMUM_UPDATE_TIME = 0;
	private float MINIMUM_UPDATE_DISTANCE = 0.0f;
	private boolean isGpsEnable = false;
	private TipResponse tipResponse;
	private String eta_latitude;
	private String eta_longitude;
	private TextView rate_unit;
	ProgressDialog dialog2;
	private Dialog mDialog;
	HorizontalScrollView scrollView;
	ArrayList<String> imagesForTheSelectedTypes=new ArrayList<String>();
	int typeSize=0;
	ArrayList<String> unselectedImages=new ArrayList<String>();
	ArrayList<String> mapImages=new ArrayList<String>();
	ArrayList<String> carName=new ArrayList<String>();
	private String nearestStringDriverDistance;
	private String current_master_type_id = "-1",current_master_type_name= "",type_image="";
	public static ArrayList<Boolean> markerPlotingFirstTime = new ArrayList<Boolean>();
	private ArrayList<String> new_channels_to_subscribe = new ArrayList<String>();
	private  LinearLayout allTypeLinearLayout;
	HorizontalScrollView horizontal_scrollView;
	private PubnubResponseNew filterResponse = new PubnubResponseNew();
	private ImageView imageView;
	static int isTypeTapped=1;
	HashMap<String, String> markerId_Email_map,nearestDriverDistance;
	double lat=0;
	double longi=0;
	TextView drivvehicalrating;
	ImageView drivehicalimg;
	public static int displayWidth;
	private  ImageView previousArrow,nextArrow;
	private boolean surgeAvailable;
	String imageUrlToSendToSurge;
	private Dialog surgePopup;
	SupportMapFragment fm;
	LinearLayout compressedLayout,expandedLayout;
	TextView new_dropoff_location_address;
	Animation slideUp,slideDown;
	boolean correctDate=false;
	private RatingBar ratingBar;
	Location mCurrentLoc,location,mPreviousLoc;
	private EditText text;
	Typeface typeFaceBold;
	Typeface typeFace;
	private  TextView driver_name_new,driver_car_plate_no_new,drivvehicalrating_new,driver_time_new,txt_roadyo;
	private  ImageView drivehicalimg_new,driver_profile_pic_new;
	private  String fare;
	private TextView textFare;
	private String reviewString;
	private PicassoMarker driverMarker;
	Dialog invoiceDialogue;
	boolean flagForDialog=false;
	private int n;
	private TextView drivsnd_new,drivsnd,driver_dist,eta_text,driver_distcom,eta_textcom;
	String getFareResponse;
	private FareCalculation getFare;
	String faremount = "";
	boolean isdropoffclicked=false;
	private View dummy_view;
	private boolean dropOffSet=false;
	private double walletAmt=0.00;
	Marker picupmarker1, picupmarker2;
	private LinearLayout now_later_layout;
	private boolean cameraMoved;

	@SuppressWarnings("unchecked")
	private void _publish(final String channel, final String message)
	{
		Utility.printLog("CONTROL INSIDE _publish");
		@SuppressWarnings("rawtypes")
		Hashtable args = new Hashtable(2);
		Utility.printLog("message for publishing "+message+"channel "+channel);

		if(args.get("message") == null)
		{
			try {
				Integer i = Integer.parseInt(message);
				args.put("message", i);
			} catch (Exception e) {

			}
		}
		if (args.get("message") == null)
		{
			try
			{
				Double d = Double.parseDouble(message);
				args.put("message", d);
			} catch (Exception e)
			{

			}
		}
		if (args.get("message") == null)
		{
			try {
				JSONArray js = new JSONArray(message);
				args.put("message", js);
			} catch (Exception e)
			{

			}
		}
		if (args.get("message") == null)
		{
			try
			{
				JSONObject js = new JSONObject(message);
				args.put("message", js);
			} catch (Exception e)
			{
			}
		}
		if(args.get("message") == null)
		{
			args.put("message", message);
		}

		// Publish Message
		args.put("channel", channel); // Channel Name

		Utility.printLog("publishing channel is rk "+channel);

		pubnub.publish(args, new Callback() {
			@Override
			public void successCallback(String channel, Object message) {
				Utility.printLog("CONTROL INSIDE _publish successCallback");
				Utility.printLog("success from akbar PUBLISH message: " + message);
			}

			@Override
			public void errorCallback(String channel, final PubnubError error) {

				if (isAdded())
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if(!VariableConstants.CONFIRMATION_CALLED) {
								pubnubProgressDialog.setVisibility(View.INVISIBLE);
								pickup_Distance.setVisibility(VISIBLE);
							}

							Utility.printLog("success from akbar PUBLISH message: error" + error.getErrorString());

						}
					});
			}
		});
	}

	private void subscribe(String[] channels)
	{


		//Hashtable args = new Hashtable(1);
		Utility.printLog("CONTROL INSIDE subscribe");
		for(int i=0;i<channels.length;i++)
			//args.put("channel",session.getChannelName());
			try
			{
				pubnub.subscribe(channels, new Callback()
				{
					@Override
					public void connectCallback(String channel,Object message)
					{
						Utility.printLog("success from akbar connect " );

					}
					@Override
					public void disconnectCallback(String channel,Object message)
					{
						Utility.printLog("success from disconnect");

					}

					@Override
					public void reconnectCallback(String channel,Object message)
					{
						Utility.printLog("success from akbar reconect" );

					}

					@Override
					public void successCallback(String channel, final Object message) {
						Utility.printLog("success from akbar" + message.toString());
						if(getActivity()!=null) {
							if (isAdded())
							{
								try
								{
									Utility.printLog("dropOffSet tag "+dropOffSet);
									getActivity().runOnUiThread(new Runnable() {
										@Override
										public void run() {
											pubnubProgressDialog.setVisibility(View.INVISIBLE);
											String responsebid = "";//Roshani
											String sessionbid = "";//Roshani
											if (message.toString() != null) {
												Gson gson = new Gson();
												filterResponse = gson.fromJson(message.toString(), PubnubResponseNew.class);
												Utility.printLog("confiration called  " + VariableConstants.CONFIRMATION_CALLED);
												if(!VariableConstants.CONFIRMATION_CALLED)
												{
													scrollView.setVisibility(VISIBLE);
													all_types_layout.setVisibility(VISIBLE);
													allTypeLinearLayout.setVisibility(VISIBLE);
												}

												PubnubResponseNew pubnubRespose = new PubnubResponseNew();
												pubnubRespose.setMasArr(filterResponse.getMasArr());
												pubnubRespose.setTypes(filterResponse.getTypes());

												String pubnubBackup = gson.toJson(pubnubRespose, PubnubResponseNew.class);
												session.storeCarTypes(pubnubBackup);

												Utility.printLog("backup from the pubnub " + session.getCarTypes());


												Gson gson1 = new Gson();
												response = gson1.fromJson(message.toString(), LoginResponse.class);
												responsebid = filterResponse.getBid();//Roshani
												if (responsebid != null && responsebid.contains("BID")) {
													responsebid = responsebid.replace("BID :", "");
												}
												Utility.printLog(" successCallback MSG getA=" + filterResponse.getA());

												if (session.isDriverOnWay() || session.isDriverOnArrived() || session.isTripBegin()) {
													//googleMap.getUiSettings().setMyLocationButtonEnabled(false);
													hybrid_view.setVisibility(View.GONE);
													scrollView.setVisibility(View.GONE);
													all_types_layout.setVisibility(View.GONE);
													allTypeLinearLayout.setVisibility(View.GONE);
												} else
												{
													googleMap.getUiSettings().setMyLocationButtonEnabled(true);
													hybrid_view.setVisibility(VISIBLE);
												}

												sessionbid = session.getBookingId();

												if ("1".equals(filterResponse.getFlag())) {
													horizontal_scrollView.removeAllViews();

													allTypeLinearLayout.removeAllViews();

													nextArrow.setVisibility(View.GONE);
													previousArrow.setVisibility(View.GONE);
												}

												if (message.toString() != null && filterResponse != null && filterResponse.getMasArr() != null && filterResponse.getMasArr().size() > 0 && filterResponse.getTypes().size() > 0) {
													if (surgePrice.equals(""))
													{
														if (VariableConstants.TYPE.equals("")) {
															if (!filterResponse.getTypes().get(0).getSurg_price().equals("")) {

																surgePrice = filterResponse.getTypes().get(0).getSurg_price();
																surgeAvailable = true;

															} else {
																surgeAvailable = false;
																surgePrice = "";
															}
														}
														else

														{
															if (!filterResponse.getTypes().get(Integer.parseInt(VariableConstants.TYPE)).getSurg_price().equals("")) {

																surgePrice = filterResponse.getTypes().get(Integer.parseInt(VariableConstants.TYPE)).getSurg_price();
																surgeAvailable = true;

															} else {
																surgeAvailable = false;
																surgePrice = "";
															}
														}

													}


													if (!VariableConstants.isPubnubCalled) {
														VariableConstants.pubnubSize = filterResponse.getTypes().size();
														VariableConstants.isPubnubCalled = true;
														addScrollView();

														if (filterResponse.getTypes().size() == 1 || filterResponse.getTypes().size() == 2 || filterResponse.getTypes().size() == 3) {
															nextArrow.setVisibility(View.GONE);
															previousArrow.setVisibility(View.GONE);
														} else {
															nextArrow.setVisibility(View.VISIBLE);
															previousArrow.setVisibility(View.VISIBLE);
														}

														imagesForTheSelectedTypes.clear();
														typeSize = filterResponse.getTypes().size();
														unselectedImages.clear();
														for (int i = 0; i < filterResponse.getTypes().size(); i++) {
															imagesForTheSelectedTypes.add(filterResponse.getTypes().get(i).getVehicle_img());
															unselectedImages.add(filterResponse.getTypes().get(i).getVehicle_img_off());
															mapImages.add(filterResponse.getTypes().get(i).getMapIcon());
															carName.add(filterResponse.getTypes().get(i).getType_name());
														}

														session.setSelectedImage(mapImages.get(0));


														if (VariableConstants.TYPE.equals("")) {
															current_master_type_id = filterResponse.getTypes().get(0).getType_id();

															current_master_type_name = filterResponse.getTypes().get(0).getType_name();

															carName.add(filterResponse.getTypes().get(0).getType_name());

															car_name = current_master_type_name;

															type_image = filterResponse.getTypes().get(0).getVehicle_img();

														} else {
															current_master_type_id = filterResponse.getTypes().get(Integer.parseInt(VariableConstants.TYPE)).getType_id();


															current_master_type_name = filterResponse.getTypes().get(Integer.parseInt(VariableConstants.TYPE)).getType_name();

															carName.add(filterResponse.getTypes().get(Integer.parseInt(VariableConstants.TYPE)).getType_name());

															car_name = current_master_type_name;

															type_image = filterResponse.getTypes().get(Integer.parseInt(VariableConstants.TYPE)).getVehicle_img();

														}


														for (int i = 0; i < filterResponse.getTypes().size(); i++) {
															markerPlotingFirstTime.add(i, true);
														}


													}

												}


											}
											if (message.toString() != null && filterResponse != null && filterResponse.getMasArr() != null && filterResponse.getMasArr().size() > 0 && filterResponse.getTypes().size() > 0) {
												if (VariableConstants.pubnubSize != filterResponse.getTypes().size()) {
													VariableConstants.isPubnubCalled = false;

												}

											}

											// rahul : here we are checking for booking status as we have to handle the case where the app may have been killed or from background to foreground
											if ((filterResponse != null && filterResponse.getA().equals("2")) && !(session.isDriverOnWay() || session.isDriverOnArrived()
													|| session.isTripBegin() || session.isInvoiceRaised())) {
												Utility.printLog("INSIDE DRIVERS AROUND YOU message=" + message.toString());


												if (filterResponse.getTypes() != null && filterResponse.getTypes().size() > 0) {
													boolean newCarTypesFound = false;
													if (response == null || response.getCarsdetails() == null) {
														newCarTypesFound = true;
														response = new LoginResponse();
													} else if (filterResponse.getTypes().size() != response.getCarsdetails().size()) {
														newCarTypesFound = true;
													}

													if (!newCarTypesFound)
														for (int i = 0; i < filterResponse.getTypes().size(); i++) {
															for (int j = 0; j < response.getCarsdetails().size(); j++) {


																if (filterResponse.getTypes().get(i).getType_name().equals(response.getCarsdetails().get(j).getType_name())) {
																	newCarTypesFound = false;
																	break;
																} else {
																	newCarTypesFound = true;
																}
															}

															if (newCarTypesFound) {
																break;
															}
														}
													Utility.printLog("NEW CAR TYPES FOUND status: " + newCarTypesFound);

													if (newCarTypesFound) {
														try {
															Gson gson1 = new Gson();
															response = gson1.fromJson(message.toString(), LoginResponse.class);
														} catch (Exception e) {
															e.printStackTrace();
														}
														marker_map.clear();
														if(!VariableConstants.CONFIRMATION_CALLED)
														{
															googleMap.clear();
														}


														session.storeCarTypes(message.toString());

														Utility.printLog("INSIDE DRIVERS AROUND YOU CAR TYPES CHANGED");

														if (filterResponse.getTypes() != null && filterResponse.getTypes().size() > 0) {
															{
																firstTimeMarkersPlotting = true;   //Akbar commented
																current_master_type_id = filterResponse.getTypes().get(0).getType_id();

																current_master_type_name = filterResponse.getTypes().get(0).getType_name();

																type_image = filterResponse.getTypes().get(0).getVehicle_img();

															}
															for (int i = 0; i < response.getCarsdetails().size(); i++) {
																markerPlotingFirstTime.add(i, true);
															}

															addScrollView();
														} else {
															Toast.makeText(getActivity(), getResources().getString(R.string.not_available_area), Toast.LENGTH_SHORT).show();
															allTypeLinearLayout.removeAllViews();
														}
													} else {
														Utility.printLog("INSIDE DRIVERS AROUND YOU CAR TYPES NOT CHANGED");
													}

													ArrayList<String> driver_channels = new ArrayList<String>();


													if (filterResponse.getMasArr().size() > 0)
													{


														int flag = 0;
														for (int i = 0; i < filterResponse.getMasArr().size(); i++) {
															if (flag > 0) {
																break;
															} else
															{

																pickup_Distance.setVisibility(View.INVISIBLE);
																rate_unit.setVisibility(View.GONE);
																type1NearestDrivers.clear();
																nearestDrivers.clear();
																if(!VariableConstants.CONFIRMATION_CALLED) {
																	Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
																}

															}
															PubnubResponseNew pubnubRespose = new PubnubResponseNew();

															Gson gson1 = new Gson();
															pubnubRespose = gson1.fromJson(session.getCarTypes(), PubnubResponseNew.class);
															Utility.printLog("ilterResponse.getMass size" + filterResponse.getMasArr().size());
															Utility.printLog("stored.getMass size" + pubnubRespose.getMasArr().size());


															if (pubnubRespose.getMasArr().get(i).getMas().size() == filterResponse.getMasArr().get(i).getMas().size()) {
																Utility.printLog("i am inside the if ");

																if (pubnubRespose.getMasArr().get(i).getTid().equals(current_master_type_id)) {
																	flag++;
																	for (int j = 0; j < pubnubRespose.getMasArr().get(i).getMas().size(); j++) {
																		driver_channels.add(pubnubRespose.getMasArr().get(i).getMas().get(j).getChn());


																		if (pubnubRespose.getMasArr().get(i).getMas() != null && pubnubRespose.getMasArr().get(i).getMas().size() > 0) {
																			if (pubnubRespose.getMasArr().get(i).getMas().get(j) != null) {

																				if(!VariableConstants.CONFIRMATION_CALLED) {
																					Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));
																				}
																				if(!VariableConstants.CONFIRMATION_CALLED) {
																					pickup_Distance.setVisibility(VISIBLE);
																					rate_unit.setVisibility(VISIBLE);
																					String[] params = new String[4];

																					params[0] = String.valueOf(currentLatitude);
																					params[1] = String.valueOf(currentLongitude);
																					params[2] = pubnubRespose.getMasArr().get(i).getMas().get(j).getLt();
																					params[3] = pubnubRespose.getMasArr().get(i).getMas().get(j).getLg();

																					new getETA().execute(params);
																					Utility.printLog("pickup_Distance 15");
																				}



																				break;

																			} else {

																				pickup_Distance.setVisibility(View.INVISIBLE);
																				rate_unit.setVisibility(View.GONE);
																				type1NearestDrivers.clear();
																				nearestDrivers.clear();
																				if(!VariableConstants.CONFIRMATION_CALLED)
																					Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
																				Utility.printLog("INSIDE DRIVERS NOT FOUND 20");
																			}
																		} else {
																			pickup_Distance.setVisibility(View.INVISIBLE);
																			rate_unit.setVisibility(View.GONE);
																			type1NearestDrivers.clear();
																			nearestDrivers.clear();
																			if(!VariableConstants.CONFIRMATION_CALLED)
																				Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
																		}


																	}
																}
															} else {
																if (filterResponse.getMasArr().get(i).getTid().equals(current_master_type_id)) {
																	Utility.printLog("i am inside the else ");

																	flag++;
																	for (int j = 0; j < filterResponse.getMasArr().get(i).getMas().size(); j++) {
																		driver_channels.add(filterResponse.getMasArr().get(i).getMas().get(j).getChn());


																		if (filterResponse.getMasArr().get(i).getMas() != null && filterResponse.getMasArr().get(i).getMas().size() > 0) {
																			if (filterResponse.getMasArr().get(i).getMas().get(j) != null) {
																				if(!VariableConstants.CONFIRMATION_CALLED)
																				{
																					Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));
																					String dis = filterResponse.getMasArr().get(i).getMas().get(j).getD();
																					pickup_Distance.setVisibility(VISIBLE);
																					rate_unit.setVisibility(VISIBLE);
																					String[] params = new String[4];
																					params[0] = String.valueOf(currentLatitude);
																					params[1] = String.valueOf(currentLongitude);
																					params[2] = filterResponse.getMasArr().get(i).getMas().get(j).getLt();
																					params[3] = filterResponse.getMasArr().get(i).getMas().get(j).getLg();
																					new getETA().execute(params);
																					Utility.printLog("pickup_Distance 15");
																					distance = Double.parseDouble(dis) / 1000+"";
																				}
																				break;

																			} else {

																				pickup_Distance.setVisibility(View.INVISIBLE);
																				rate_unit.setVisibility(View.GONE);
																				type1NearestDrivers.clear();
																				nearestDrivers.clear();
																				if(!VariableConstants.CONFIRMATION_CALLED)
																					Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
																				Utility.printLog("INSIDE DRIVERS NOT FOUND 20");
																			}
																		} else {
																			pickup_Distance.setVisibility(View.INVISIBLE);
																			rate_unit.setVisibility(View.GONE);
																			type1NearestDrivers.clear();
																			nearestDrivers.clear();
																			if(!VariableConstants.CONFIRMATION_CALLED)
																				Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
																		}


																	}
																}
															}


														}
													}

													if (driver_channels != null && driver_channels.size() > 0) {


														if (isAdded() && firstTimeMarkersPlotting) {
															Utility.printLog("PlotFirstTime  getMas size = " + filterResponse.getMasArr().get(0).getMas().size());

															new_channels_to_subscribe.clear();
															if (filterResponse.getTypes().get(0).getType_name() != null)
																PlotFirstTime(filterResponse.getTypes().get(0).getType_name());
														}

														boolean DriverFound = false;

														for (int k = 0; k < filterResponse.getMasArr().size(); k++) {
															if (filterResponse.getMasArr().get(k).getTid().equals(current_master_type_id)) {
																for (int i = 0; i < filterResponse.getMasArr().get(k).getMas().size(); i++) {
																	for (int j = 0; j < new_channels_to_subscribe.size(); j++) {
																		if (filterResponse.getMasArr().get(k).getMas().get(i).getChn().equals(new_channels_to_subscribe.get(j))) {
																			DriverFound = true;
																			break;
																		} else {
																			DriverFound = false;
																		}
																	}

																	if (!DriverFound) {
																		Utility.printLog("wallah driver not found channel = " + filterResponse.getMasArr().get(k).getMas().get(i).getChn());
																		Utility.printLog("walla driver plotting marker channel " + filterResponse.getMasArr().get(k).getMas().get(i).getChn());
																		String lat = filterResponse.getMasArr().get(k).getMas().get(i).getLt();
																		String lng = filterResponse.getMasArr().get(k).getMas().get(i).getLg();

																		LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

																		double width = dblArray[0] * 40;
																		double height = dblArray[1] * 80;

																		if (mapImages.get(i) != null) {
																			try {
																				if(!VariableConstants.CONFIRMATION_CALLED)
																				{
																					driverMarker = new PicassoMarker(googleMap.addMarker(new MarkerOptions().position(latLng)), getActivity());
																					Picasso.with(getActivity()).load(VariableConstants.IMAGE_URL + mapImages.get(i)).resize((int) width, (int) height).into(driverMarker);
																				}

																			} catch (IllegalArgumentException e) {

																			}

																		}

																/*	*//**
																		 * to show the image on map on the marker
																		 *//*
																	Bitmap bitmap =null;
																	Bitmap resized=null;
																	try {
																		bitmap = new DownloadImageTask().execute(VariableConstants.IMAGE_URL+mapImages.get(i)).get();
																		resized = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
																	} catch (InterruptedException e) {
																		e.printStackTrace();
																	} catch (ExecutionException e) {
																		e.printStackTrace();
																	}
																	drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
																			.fromBitmap(resized)));*/


																		//setUpMap(drivermarker,filterResponse.getMasArr().get(k).getMas().get(i).getI(),latLng);


																		marker_map.put(filterResponse.getMasArr().get(k).getMas().get(i).getChn(), driverMarker.getmMarker());
																		new_channels_to_subscribe.add(filterResponse.getMasArr().get(k).getMas().get(i).getChn());

																		//KK COMMIT

																		//markerId_Email_map.put(drivermarker.getId(), filterResponse.getMasArr().get(k).getMas().get(i).getE());
																		//markerId_Email_map.put("akbar", filterResponse.getMasArr().get(k).getMas().get(i).getE());
																		type1NearestDrivers.add(filterResponse.getMasArr().get(k).getMas().get(i).getE());
																		Car_Type_Id = filterResponse.getMasArr().get(0).getTid();
																		//kk
																		nearestStringDriverDistance = filterResponse.getMasArr().get(k).getMas().get(0).getD();


																	} else {
																		Utility.printLog("wallah driver found channel = " + filterResponse.getMasArr().get(k).getMas().get(i).getChn());
																	}
																}
															}
														}

														boolean itemFound = false;
														ArrayList<String> channels_to_unsubscribe = new ArrayList<String>();

														for (int k = 0; k < filterResponse.getMasArr().size(); k++) {
															if (filterResponse.getMasArr().get(k).getTid().equals(current_master_type_id)) {
																for (int i = 0; i < new_channels_to_subscribe.size(); i++) {
																	for (int j = 0; j < filterResponse.getMasArr().get(k).getMas().size(); j++) {
																		if (new_channels_to_subscribe.get(i).equals(filterResponse.getMasArr().get(k).getMas().get(j).getChn())) {

																			itemFound = true;
																			break;
																		} else {
																			itemFound = false;
																		}
																	}

																	if (!itemFound) {
																		if (marker_map.containsKey(new_channels_to_subscribe.get(i))) {
																			Marker marker = marker_map.get(new_channels_to_subscribe.get(i));
																			if (marker != null) {
																				marker.remove();
																			}

																			marker_map.get(new_channels_to_subscribe.get(i)).remove();
																			marker_map.remove(new_channels_to_subscribe.get(i));
																			channels_to_unsubscribe.add(new_channels_to_subscribe.get(i));
																			new_channels_to_subscribe.remove(i);


																		}
																	}
																}

																//UnSubscribing the channels
																if (channels_to_unsubscribe.size() > 0) {
																	//Putting ArrayList data to String[]
																	String[] unsubscribe_channels = new String[channels_to_unsubscribe.size()];
																	unsubscribe_channels = channels_to_unsubscribe.toArray(unsubscribe_channels);

																	Utility.printLog("abcdefgh unsubscribe_channels size=" + unsubscribe_channels.length);
																	//UnSubcribing
																	new BackgroundUnSubscribeChannels().execute(unsubscribe_channels);
																}

															}

														}
													} else {
														if (new_channels_to_subscribe.size() > 0) {
															//Putting ArrayList data to String[]
															String[] new_channels = new String[new_channels_to_subscribe.size()];
															new_channels = new_channels_to_subscribe.toArray(new_channels);
															//UnSubcribing
															new BackgroundUnSubscribeChannels().execute(new_channels);
														}

														marker_map.clear();
														Utility.printLog("marker_map_on_the_way 1");
														if(!VariableConstants.CONFIRMATION_CALLED)
														{
															googleMap.clear();

														}
														Utility.printLog("googleMap.clear 2");
														new_channels_to_subscribe.clear();


														//KK COMMIT

														type1NearestDrivers.clear();

													}
												} else//clear the google map if there is no drivers to show in map
												{
													if (new_channels_to_subscribe.size() > 0) {
														//Putting ArrayList data to String[]
														String[] new_channels = new String[new_channels_to_subscribe.size()];
														new_channels = new_channels_to_subscribe.toArray(new_channels);
														//UnSubcribing
														new BackgroundUnSubscribeChannels().execute(new_channels);
													}
													pickup_Distance.setVisibility(View.INVISIBLE);
													rate_unit.setVisibility(View.GONE);
													nearestDrivers.clear();
													new_channels_to_subscribe.clear();
													//KK COMMIT
													type1NearestDrivers.clear();
													Toast.makeText(getActivity(), getResources().getString(R.string.not_available_area), Toast.LENGTH_LONG).show();
												}


											} else if (filterResponse != null && filterResponse.getA().equals("5") && filterResponse.getBid() != null && filterResponse.getBid().equals(session.getBookingId())) //Driver cancelled the request
											{
												Utility.printLog("INSIDE DRIVER CANCELLED BOOKING message=" + message.toString());

												DriverCancelledAppointment();
											} else if (filterResponse != null && filterResponse.getA().equals("6"))//Driver on the way
											{
												Utility.printLog("INSIDE DRIVER ON THE WAY: 6 message=" + message.toString());

												session.setDriverOnWay(true);
												Utility.printLog("Wallah set as true Homepage 6");
												session.setDriverArrived(false);
												session.setTripBegin(false);
												session.setInvoiceRaised(false);

												setHasOptionsMenu(false);
												if(session.getDriverEmail()!=null)
												{
													getCarDetails();

												}
												if (session.getLat_DOW() == null || session.getLon_DOW() == null) {
													session.storeLat_DOW(filterResponse.getLt());
													session.storeLon_DOW(filterResponse.getLg());
													if(session.getAptDate()!=null)
													{
														getAppointmentDetails_Volley(session.getAptDate());

													}
												}

												eta_latitude = filterResponse.getLt();
												eta_longitude = filterResponse.getLg();

												String[] params=new String[4];

												params[0]=session.getPickuplat();
												params[1]=session.getPickuplng();
												params[2]=eta_latitude;
												params[3]=eta_longitude;

												new getEtaWIthTimer().execute(params);
												//getETAWithTimer(6);
												UpdateDriverLocation_DriverOnTheWay(filterResponse);

											} else if (filterResponse != null && filterResponse.getA().equals("4") && session.isDriverOnWay())//Driver on the way
											{
												Utility.printLog("INSIDE DRIVER ON THE WAY: 4 message=" + message.toString());
												eta_latitude = filterResponse.getLt();
												eta_longitude = filterResponse.getLg();
												//getETAWithTimer(6);
												UpdateDriverLocation_DriverOnTheWay(filterResponse);
											} else if (filterResponse != null && filterResponse.getA().equals("7"))//Driver arrived
											//filterResponse.getBid().equals(session.getBookingId()) //Roshani
											{
												Utility.printLog("INSIDE DRIVER REACHED: 7 message=" + message.toString());
												session.setDriverOnWay(false);
												Utility.printLog("Wallah set as false Homepage 7");
												session.setDriverArrived(true);
												session.setTripBegin(false);
												session.setInvoiceRaised(false);

												session.storeLat_DOW(filterResponse.getLt());
												session.storeLon_DOW(filterResponse.getLg());

												//new CallGooglePlayServices().execute();
												setHasOptionsMenu(false);
												if(session.getDriverEmail()!=null)
												{
													getCarDetails();

												}
												if(session.getAptDate()!=null)
												{
													getAppointmentDetails_Volley(session.getAptDate());

												}

												String[] params=new String[4];

												params[0]=session.getPickuplat();
												params[1]=session.getPickuplng();
												params[2]=session.getDropofflat();
												params[3]=session.getDropofflng();

												new getEtaWIthTimer().execute(params);



											}
											else if (filterResponse != null && filterResponse.getA().equals("4") && session.isDriverOnArrived())
											//Driver arrived//Roshani

											{
												Utility.printLog("INSIDE DRIVER REACHED:4 message=" + message.toString());

												eta_latitude = filterResponse.getLt();
												eta_longitude = filterResponse.getLg();
												//getETAWithTimer(7);

												UpdatDriverLocation_DriverArrived(filterResponse);
											} else if (filterResponse != null && "8".equals(filterResponse.getA()) )
											//Driver Started journey // && int foo = Integer.parseInt("1234");filterResponse.getBid().equals(session.getBookingId())//&& session.isTripBegin() Roshani
											{
												//ctivity.invoice_driver_tip.setVisibility(View.VISIBLE);
												Utility.printLog("INSIDE DRIVER REACHED: 8 message=" + message.toString());
												session.setDriverOnWay(false);
												Utility.printLog("Wallah set as false Homepage 8");
												session.setDriverArrived(false);
												session.setTripBegin(true);
												session.setInvoiceRaised(false);
												//new CallGooglePlayServices().execute();
												setHasOptionsMenu(false);
												if(session.getDriverEmail()!=null)
												{
													getCarDetails();

												}
												if(session.getAptDate()!=null)
												{
													getAppointmentDetails_Volley(session.getAptDate());

												}
												eta_latitude = filterResponse.getLt();
												eta_longitude = filterResponse.getLg();


												String[] params=new String[4];

												params[0]=eta_latitude;
												params[1]=eta_longitude;
												params[2]=session.getDropofflat();
												params[3]=session.getDropofflng();

												new getEtaWIthTimer().execute(params);
												//getETAWithTimer(8);
												UpdateDriverLocation_JourneyStarted(filterResponse);

											}
											else if (filterResponse != null && filterResponse.getA().equals("4") && session.isTripBegin())//Driver Started journey
											{
												Utility.printLog("INSIDE DRIVER TripBegin:4 message=" + message.toString());
												eta_latitude = filterResponse.getLt();
												eta_longitude = filterResponse.getLg();
												//getETAWithTimer(8);
												UpdateDriverLocation_JourneyStarted(filterResponse);
											} else if (filterResponse != null && filterResponse.getA().equals("9") )//invoice raised filterResponse.getBid().equals(session.getBookingId())
											{
												/*&& !session.isInvoiceRaised() &&
													filterResponse.getBid() != null && responsebid.equals(sessionbid)*/
												Utility.printLog("onResume INSIDE DriverInvoiceRaised");
												eta_latitude = filterResponse.getLt();
												eta_longitude = filterResponse.getLg();

												String[] subscribed_channels1 = pubnub.getSubscribedChannelsArray();
												Utility.printLog("Appointments details subscribed_channels my channel=" + session.getChannelName());
												ArrayList<String> unsubscribeChannels1= new ArrayList<String>();
												for(int i=0;i<subscribed_channels1.length;i++)
												{
													Utility.printLog("Appointments details subscribed_channels at status 9" + i + " " + subscribed_channels1[i]);


													unsubscribeChannels1.add(subscribed_channels1[i]);

													if(unsubscribeChannels1.size()>0)
													{
														Utility.printLog("Appointments details channels unsubscribeChannels channel list size status 9=" + unsubscribeChannels1.size());
														String[] new_un_sub_channels=new String[unsubscribeChannels1.size()];
														new_un_sub_channels=unsubscribeChannels1.toArray(new_un_sub_channels);
														new BackgroundUnSubscribeChannels().execute(new_un_sub_channels);
													}

												}
												AppointmentCompleted_InvoiceRaised(filterResponse);

												Utility.printLog("onResume INSIDE DriverInvoiceRaised");

												//AppointmentCompleted_InvoiceRaised(filterResponse);
											} else if (filterResponse != null && filterResponse.getA().equals("4"))//updating drivers locations who are all around you
											{
												Utility.printLog("INSIDE DRIVER Update Locations:4 message=" + message.toString());
												eta_latitude = filterResponse.getLt();
												eta_longitude = filterResponse.getLg();
												UpdateDriverLocations(filterResponse);

												Utility.printLog("INSIDE DRIVER Update Locations:4 message=" + message.toString());
											}
											else if (filterResponse != null && filterResponse.getA().equals("100") )//invoice raised filterResponse.getBid().equals(session.getBookingId())
											{
												Utility.printLog("inside tip ");
												MainActivityDrawer.driver_tip.setVisibility(View.GONE);
												MainActivityDrawer.textForTip.setVisibility(View.GONE);

											}

										}
									});
								} catch (Exception e) {
									Utility.printLog("exception in success " + e);
								}
							}

						}
					}

					@Override
					public void errorCallback(String arg0, PubnubError arg1)
					{
						super.errorCallback(arg0, arg1);

						if(isAdded())
							getActivity().runOnUiThread(new Runnable()
							{
								@Override
								public void run()
								{
									pubnubProgressDialog.setVisibility(View.INVISIBLE);
									pickup_Distance.setVisibility(View.GONE);
									rate_unit.setVisibility(View.GONE);
								}
							});

						Utility.printLog("CONTROL INSIDE subscribe errorCallback");
						Utility.printLog("success from inside error MSG IS: " + arg1.toString());
					}
				});
			}
			catch(Exception e)
			{
				Utility.printLog("Exception in subscribe " + e);
			}
	}




	private void PlotFirstTime(String type_name)
	{

		Utility.printLog("equalsIgnoreCase filterResponse.getMasArr().size() = " + filterResponse.getMasArr().size());



		for(int i=0;i<filterResponse.getMasArr().size();i++)
		{
			if(filterResponse.getMasArr().get(i).getTid().equals(current_master_type_id))
			{
				Utility.printLog("walla current_master_type_id is matched : " + current_master_type_id);
				if(filterResponse.getMasArr().get(i).getMas().size()>0)
				{
					type1NearestDrivers.clear();
					try
					{
						if(!VariableConstants.CONFIRMATION_CALLED)
						{
							googleMap.clear();

						}

					}
					catch (Exception e)
					{

					}

					for(int a=0;a<filterResponse.getTypes().size();a++)
					{
						if(filterResponse.getTypes().get(a).getType_id().equals(current_master_type_id))
						{
							//String[] Type_image = new String[2];
							//Type_image[0] = filterResponse.getTypes().get(a).getType_image();
							//new BackgroundTast_DownloadMarker().execute(Type_image);
							//Utility.printLog("BackgroundTast_DownloadMarker 1");
						}
					}
					for(int j=0;j<filterResponse.getMasArr().get(i).getMas().size();j++)
					{
						//kk Utility.printLog("walla plotting marker for "+filterResponse.getMasArr().get(i).getMas().get(j).getN());
						String lat = filterResponse.getMasArr().get(i).getMas().get(j).getLt();
						String lng = filterResponse.getMasArr().get(i).getMas().get(j).getLg();

						LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

						double width1=dblArray[0]*40;
						double height1=dblArray[1]*80;


						if(mapImages.get(i)!=null)
						{
							try
							{
								if(!VariableConstants.CONFIRMATION_CALLED)
								{
									driverMarker = new PicassoMarker(googleMap.addMarker(new MarkerOptions().position(latLng)),getActivity());
									Picasso.with(getActivity()).load(VariableConstants.IMAGE_URL+mapImages.get(i)).resize((int)width1,(int)height1).into(driverMarker);
								}

							}
							catch (IllegalArgumentException e)
							{

							}

						}

						/**
						 * to show the image on map on the marker
						 *//*
						Bitmap bitmap =null;
						Bitmap resized=null;
						try {
							bitmap = new DownloadImageTask().execute(VariableConstants.IMAGE_URL+mapImages.get(i)).get();
							resized = Bitmap.createScaledBitmap(bitmap, (int) width1, (int) height1, true);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
						drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
								.fromBitmap(resized)));*/

						/*if(driverMarker.getmMarker()!=null)
						{
							marker_map.put(filterResponse.getMasArr().get(i).getMas().get(j).getChn(), driverMarker.getmMarker());
						}*/

						new_channels_to_subscribe.add(filterResponse.getMasArr().get(i).getMas().get(j).getChn());
						//kk commit

						type1NearestDrivers.add(filterResponse.getMasArr().get(i).getMas().get(j).getE());

						nearestStringDriverDistance=filterResponse.getMasArr().get(i).getMas().get(0).getD();

					}
					//subcribing for new drivers
					if(new_channels_to_subscribe.size()>0)
					{
						//Putting ArrayList data to String[]
						String[] new_channels=new String[new_channels_to_subscribe.size()];
						new_channels=new_channels_to_subscribe.toArray(new_channels);

						Utility.printLog("addScrollView scribeChannels size=" + new_channels.length);
						for(int k=0;k<new_channels.length;k++)
						{
							Utility.printLog("PlotFirstTime scribeChannel at " + k + "=" + new_channels[k]);
						}
						//Subcribing
						//new BackgroundSubscribeChannels().execute(new_channels);  //Akbar commented
					}
					else
					{
						Utility.printLog("addScrollView scribeChannels size= 0");
					}
					return;
				}
				else
				{
					Utility.printLog("walla plotting marker no pro found");
					Utility.printLog("inside Protype onclick getMas size = 0");
				}
			}
			else {
				Utility.printLog("inside Protype onclick getMas size = 0");

			}
		}
		if(markerPlotingFirstTime.get(0))
		{
			markerPlotingFirstTime.add(0, false);
			PlotFirstTime(type_name);
		}
	}


	private void addScrollView()
	{
		horizontal_scrollView.removeAllViews();

		allTypeLinearLayout.removeAllViews();

		markerPlotingFirstTime.clear();

		if (filterResponse.getTypes() != null && filterResponse.getTypes().size() > 0)
		{
			Car_Type_Id=filterResponse.getTypes().get(0).getType_id();
			allTypeLinearLayout.setWeightSum(filterResponse.getTypes().size());
			for (int i = 0; i < filterResponse.getTypes().size(); i++)
			{

				LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
				final View f = layoutInflater.inflate(R.layout.horizontalscoll, null, false);
				imageView = (ImageView) f.findViewById(R.id.image);
				final TextView textView = (TextView) f.findViewById(R.id.text);
				final TextView tag = (TextView) f.findViewById(R.id.tag);
				final TextView id = (TextView) f.findViewById(R.id.id);

				setWidthToTypesScrollView(f);


				if(VariableConstants.TYPE.equals(""))
				{
					if (filterResponse.getTypes() != null && filterResponse.getTypes().size() > 0)
					{

						String imageViewString = VariableConstants.IMAGE_URL + filterResponse.getTypes().get(i).getVehicle_img_off();
						double width = dblArray[0] * 50;
						double height = dblArray[1] * 50;
						Picasso.with(getActivity()).load(imageViewString)
								.resize((int) width, (int) height).into(imageView);

					}


					allTypeLinearLayout.addView(f);

					nextArrow.setVisibility(VISIBLE);
					previousArrow.setVisibility(VISIBLE);


					View view1=allTypeLinearLayout.getChildAt(0);
					ImageView imageView1= (ImageView) view1.findViewById(R.id.image);
					String imageViewString = VariableConstants.IMAGE_URL + filterResponse.getTypes().get(0).getVehicle_img();
					imageUrlToSendToSurge=imageViewString;
					double width = dblArray[0] * 50;
					double height = dblArray[1] * 50;
					Picasso.with(getActivity()).load(imageViewString
					).resize((int) width, (int) height).into(imageView1);

					if(!filterResponse.getTypes().get(0).getSurg_price().equals(""))
					{
						surgePrice=filterResponse.getTypes().get(0).getSurg_price();
						surgeAvailable=true;
					}
					else
					{
						surgeAvailable=false;
						surgePrice="";
					}

					selectedImage=imageViewString;

				}

				else
				{
					if (filterResponse.getTypes() != null && filterResponse.getTypes().size() > 0)
					{

						String imageViewString = VariableConstants.IMAGE_URL + filterResponse.getTypes().get(i).getVehicle_img_off();
						double width = dblArray[0] * 50;
						double height = dblArray[1] * 50;
						Picasso.with(getActivity()).load(imageViewString)
								.resize((int) width, (int) height).into(imageView);

					}


					allTypeLinearLayout.addView(f);

					nextArrow.setVisibility(VISIBLE);
					previousArrow.setVisibility(VISIBLE);

					try
					{
						if(allTypeLinearLayout.getChildCount()==filterResponse.getTypes().size())
						{
							View view1=allTypeLinearLayout.getChildAt(Integer.parseInt(VariableConstants.TYPE));
							ImageView imageView1= (ImageView) view1.findViewById(R.id.image);
							String imageViewString = VariableConstants.IMAGE_URL + filterResponse.getTypes().get(Integer.parseInt(VariableConstants.TYPE)).getVehicle_img();
							imageUrlToSendToSurge=imageViewString;
							selectedImage=imageViewString;
							double width = dblArray[0] * 50;
							double height = dblArray[1] * 50;
							Picasso.with(getActivity()).load(imageViewString
							).resize((int) width, (int) height).into(imageView1);
						}
					}
					catch (Exception e)
					{

					}




					if(!filterResponse.getTypes().get(Integer.parseInt(VariableConstants.TYPE)).getSurg_price().equals(""))
					{
						surgePrice=filterResponse.getTypes().get(Integer.parseInt(VariableConstants.TYPE)).getSurg_price();
						surgeAvailable=true;
					}
					else
					{
						surgeAvailable=false;
						surgePrice="";
					}


				}

				markerPlotingFirstTime.add(i, true);



				tag.setText(filterResponse.getTypes().get(i).getType_id());
				textView.setText(filterResponse.getTypes().get(i).getType_name());
				id.setText(i + "");
				f.setOnClickListener(new OnClickListener() {
					@Override

					public void onClick(View v) {
						/*pubnubProgressDialog.setClickable(false);
						pubnubProgressDialog.setVisibility(VISIBLE);*/
						pickup_Distance.setVisibility(View.INVISIBLE);
						rate_unit.setVisibility(View.INVISIBLE);

						int typeId=Integer.parseInt(id.getText().toString());
						session.setSelectedImage(mapImages.get(typeId));

						if(filterResponse.getTypes()!=null)
						{
							if(!filterResponse.getTypes().get(typeId).getSurg_price().equals(""))
							{
								surgePrice=filterResponse.getTypes().get(typeId).getSurg_price();
								surgeAvailable=true;
							}
							else
							{
								surgeAvailable=false;

							}
						}


						typeId=typeId+1;


						if(current_master_type_id.equals(tag.getText().toString()))
						{
							isTypeTapped=isTypeTapped+1;

						}
						else
						{
							isTypeTapped=1;

						}


						if(isTypeTapped==2)
						{
							isTypeTapped=1;
						}


						int rotate = 1;
						if (markerPlotingFirstTime.get(Integer.parseInt(id.getText().toString()))) {
							markerPlotingFirstTime.add(Integer.parseInt(id.getText().toString()), false);
							rotate = 2;
						}


						if(typeSize>0)
						{
							for (int i = 0; i < typeSize; i++)
							{
								if (!(id.getText().toString().equals(i + "")))
								{

									plotImageForUnselectType(i);
								}

							}
						}




						for (int c = 0; c < rotate; c++)
						{
							if (!current_master_type_id.equals(tag.getText().toString()))
							{
								Utility.printLog("addScrollView clicked new item");
								googleMap.clear();
								Utility.printLog("googleMap.clear 4");

								Utility.printLog("inside Protype onclick getChildCount = " + allTypeLinearLayout.getChildCount());
								String selected_pro_tag = tag.getText().toString();
								current_master_type_id = selected_pro_tag;
								current_master_type_name = textView.getText().toString();
								selectedType=current_master_type_name;

								VariableConstants.TYPE=id.getText().toString();

								if(Txt_Pickup.getText().toString().equals(getResources().getString(R.string.no_drivers_found)))
								{
									pickup_Distance.setVisibility(View.GONE);
									rate_unit.setVisibility(View.GONE);
								}


								plotImageForType(id.getText().toString());

								if (filterResponse.getTypes() != null && filterResponse.getTypes().size() > 0)
								{
									Car_Type_Id = filterResponse.getMasArr().get(Integer.parseInt(id.getText().toString())).getTid();

								}

								try
								{
									for (int i = 0; i < filterResponse.getMasArr().size(); i++)
									{
										if (filterResponse.getMasArr().get(i).getTid().equals(selected_pro_tag))
										{

											Car_Type_Id=filterResponse.getMasArr().get(Integer.parseInt(id.getText().toString())).getTid();

											type1NearestDrivers.clear();
											marker_map.clear();
											new_channels_to_subscribe.clear();

											if (filterResponse.getMasArr().get(i).getMas().size() > 0)
											{
												if(!VariableConstants.CONFIRMATION_CALLED)
												{
													Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));
													pickup_Distance.setVisibility(VISIBLE);
													rate_unit.setVisibility(VISIBLE);
													String[] params=new String[4];
													params[0]=String.valueOf(currentLatitude);
													params[1]=String.valueOf(currentLongitude);
													params[2]=filterResponse.getMasArr().get(i).getMas().get(0).getLt();
													params[3]=filterResponse.getMasArr().get(i).getMas().get(0).getLg();

													new getETA().execute(params);
												}


												for (int j = 0; j < filterResponse.getMasArr().get(i).getMas().size(); j++)
												{
													String lat = filterResponse.getMasArr().get(i).getMas().get(j).getLt();
													String lng = filterResponse.getMasArr().get(i).getMas().get(j).getLg();

													LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

													double width=dblArray[0]*40;
													double height=dblArray[1]*80;

													if(mapImages.get(i)!=null)
													{
														try
														{
															if(!VariableConstants.CONFIRMATION_CALLED)
															{
																driverMarker = new PicassoMarker(googleMap.addMarker(new MarkerOptions().position(latLng)),getActivity());
																Picasso.with(getActivity()).load(VariableConstants.IMAGE_URL+mapImages.get(i)).resize((int)width,(int)height).into(driverMarker);

															}

														}
														catch (IllegalArgumentException e)
														{

														}

													}

												/*	*//**
												 * to show the image on map on the marker
												 *//*
													Bitmap bitmap =null;
													Bitmap resized=null;
													try {
														bitmap = new DownloadImageTask().execute(VariableConstants.IMAGE_URL+mapImages.get(i)).get();
														resized = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
													} catch (InterruptedException e) {
														e.printStackTrace();
													} catch (ExecutionException e) {
														e.printStackTrace();
													}
													drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
															.fromBitmap(resized)));

*/

													marker_map.put(filterResponse.getMasArr().get(i).getMas().get(j).getChn(), driverMarker.getmMarker());
													new_channels_to_subscribe.add(filterResponse.getMasArr().get(i).getMas().get(j).getChn());
													Utility.printLog("addScrollView scribeChannel name=" + filterResponse.getMasArr().get(i).getMas().get(j).getChn());

													type1NearestDrivers.add(filterResponse.getMasArr().get(i).getMas().get(j).getE());


												}
											}
											else
											{
												pickup_Distance.setVisibility(View.INVISIBLE);
												rate_unit.setVisibility(View.GONE);
												type1NearestDrivers.clear();
												nearestDrivers.clear();
												if(!VariableConstants.CONFIRMATION_CALLED)
													Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
												Utility.printLog("INSIDE DRIVERS NOT FOUND 20");
												//Akbar commented
												/*Utility.printLog("walla plotting marker no pro found");
												Utility.printLog("inside Protype onclick getMas size = 0");

												String[] params=new String[4];

												params[0]=String.valueOf(currentLatitude);
												params[1]=String.valueOf(currentLongitude);
												params[2]=filterResponse.getMasArr().get(1).getMas().get(0).getLt();
												params[3]=filterResponse.getMasArr().get(1).getMas().get(0).getLg();

												new getETA().execute(params);*/

											}
										}
									}

								} catch (Exception e) {
									Utility.printLog("exception=" + e);
									e.printStackTrace();
								}
							} else
							{

								Utility.printLog("sView clicked selected item");
							}

						}
					}
				});


			}
		}
		//subcribing for new drivers
		if(new_channels_to_subscribe.size()>0)
		{
			//Putting ArrayList data to String[]
			String[] new_channels=new String[new_channels_to_subscribe.size()];
			new_channels=new_channels_to_subscribe.toArray(new_channels);

			Utility.printLog("addScrollView scribeChannels size=" + new_channels.length);
			for(int i=0;i<new_channels.length;i++)
			{
				Utility.printLog("addScrollView scribeChannel at " + i + "=" + new_channels[i]);
			}
			//Subcribing
			//new BackgroundSubscribeChannels().execute(new_channels);   //Akbar commented
		}
		else
		{
			Utility.printLog("addScrollView scribeChannels size= 0");
		}

	}

	private void setWidthToTypesScrollView(View viewCreated)
	{
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int width = display.getWidth();

		switch (filterResponse.getTypes().size())
		{
			case 1:
			{
				viewCreated.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
				break;
			}
			case 2:
			{
				viewCreated.setLayoutParams(new LinearLayout.LayoutParams(width/2, ViewGroup.LayoutParams.WRAP_CONTENT));
				break;
			}
			default:
			{
				viewCreated.setLayoutParams(new LinearLayout.LayoutParams(width/3, ViewGroup.LayoutParams.WRAP_CONTENT));
				break;
			}
		}
	}


	public  void plotImageForType(String i)
	{
		View view1=allTypeLinearLayout.getChildAt(Integer.parseInt(i));
		ImageView imageView1= (ImageView) view1.findViewById(R.id.image);
		car_name=carName.get(Integer.parseInt(i));
		String imageViewString = VariableConstants.IMAGE_URL + imagesForTheSelectedTypes.get(Integer.parseInt(i));
		imageUrlToSendToSurge=imageViewString;
		double width = dblArray[0] * 50;
		double height = dblArray[1] * 50;
		Picasso.with(getActivity()).load(imageViewString
		).resize((int) width, (int) height).into(imageView1);

		selectedImage=imageViewString;
	}


	public void  plotImageForUnselectType(int i)
	{
		View view1=allTypeLinearLayout.getChildAt(i);
		ImageView imageView1= (ImageView) view1.findViewById(R.id.image);
		String imageViewString = VariableConstants.IMAGE_URL + unselectedImages.get(i);
		double width = dblArray[0] * 50;
		double height = dblArray[1] * 50;
		Picasso.with(getActivity()).load(imageViewString
		).resize((int) width, (int) height).into(imageView1);
	}

	private void animateMarker(final Marker marker, final LatLng target)
	{
		final long duration = 400;
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = googleMap.getProjection();
		Point startPoint = proj.toScreenLocation(marker.getPosition());
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final Interpolator interpolator = new LinearInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed / duration);
				double lng = t * target.longitude + (1 - t) * startLatLng.longitude;
				double lat = t * target.latitude + (1 - t) * startLatLng.latitude;
				marker.setRotation(bearingBetweenLatLngs(marker.getPosition(), target));
				marker.setPosition(new LatLng(lat, lng));
				if (t < 1.0) {
					// Post again 16ms later.
					handler.postDelayed(this, 16);
				} else {
					// animation ended
				}
			}
		});

	}
	private Location convertLatLngToLocation(LatLng latLng)
	{
		Location location = new Location("someLoc");
		location.setLatitude(latLng.latitude);
		location.setLongitude(latLng.longitude);
		return location;
	}
	private float bearingBetweenLatLngs(LatLng beginLatLng,LatLng endLatLng)
	{
		Location beginLocation = convertLatLngToLocation(beginLatLng);
		Location endLocation = convertLatLngToLocation(endLatLng);
		return beginLocation.bearingTo(endLocation);
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Utility.printLog("CONTROL INSIDE onCreate");
		session = new SessionManager(mActivity);
		VariableConstants.isComingFromSearch=false;
		VariableConstants.isComingFromScroll=false;
		mDialog = Utility.GetProcessDialog(getActivity());
		// add PhoneStateListener
		PhoneCallListener phoneListener = new PhoneCallListener();
		TelephonyManager telephonyManager = (TelephonyManager) mActivity.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
		marker_map=new HashMap<String, Marker>();
		marker_map_on_the_way = new HashMap<String, Marker>();
		marker_map_arrived = new HashMap<String, Marker>();
		type1_markers = new HashMap<String, Location>();
		type2_markers = new HashMap<String, Location>();
		type3_markers = new HashMap<String, Location>();
		type4_markers = new HashMap<String, Location>();
		type1_channels_list = new ArrayList<String>();
		type2_channels_list = new ArrayList<String>();
		type3_channels_list = new ArrayList<String>();
		type4_channels_list = new ArrayList<String>();
		type1NearestDrivers = new ArrayList<String>();
		type2NearestDrivers = new ArrayList<String>();
		type3NearestDrivers = new ArrayList<String>();
		type4NearestDrivers = new ArrayList<String>();
		nearestDrivers= new ArrayList<String>();

		VariableConstants.isPubnubCalled = false;



		filter = new IntentFilter();
		filter.addAction("com.threembed.driverapp.activity.push.popup");//com.threembed.driverapp.activity.push.popup
		receiver = new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{

				Utility.printLog("I am inside on receive ");
				if(session.isDriverOnWay())
				{
					Utility.printLog("ONRECIVE INSIDE ON_THE_WAY");
					//stop the timer to get the current address if any booking is going on
					if(myTimer!=null)
					{
						myTimer.cancel();
						myTimer=null;
					}
					//marker_map.clear();
					marker_map_on_the_way.clear();
					Utility.printLog("marker_map_on_the_way 3");
					marker_map_arrived.clear();
					googleMap.clear();

					all_types_layout.setVisibility(View.INVISIBLE);
					allTypeLinearLayout.setVisibility(View.INVISIBLE);
					scrollView.setVisibility(View.GONE);
					//new CallGooglePlayServices().execute();
					//getETAWithTimer(6);
					setHasOptionsMenu(false);
					if(session.getDriverEmail()!=null)
					{
						getCarDetails();

					}
					if(session.getAptDate()!=null)
					{
						getAppointmentDetails_Volley(session.getAptDate());

					}
					return;
				}
				else if(session.isDriverOnArrived() && !session.getBookingId().equals("0"))
				{
					Utility.printLog("ONRECIVE INSIDE Driver Arrived");
					//stop the timer to get the current address if any booking is going on
					if(myTimer!=null)
					{
						myTimer.cancel();
						myTimer=null;
					}

					all_types_layout.setVisibility(View.INVISIBLE);
					allTypeLinearLayout.setVisibility(View.INVISIBLE);
					scrollView.setVisibility(View.GONE);
					googleMap.clear();
					//marker_map.clear();
					marker_map_on_the_way.clear();
					Utility.printLog("marker_map_on_the_way 4");
					marker_map_arrived.clear();
					//new CallGooglePlayServices().execute();
					//getETAWithTimer(7);

					setHasOptionsMenu(true);
					if(session.getDriverEmail()!=null)
					{
						getCarDetails();

					}
					if(session.getAptDate()!=null)
					{
						getAppointmentDetails_Volley(session.getAptDate());

					}					return;
				}
				else if(session.isTripBegin() && !session.getBookingId().equals("0"))
				{
					Utility.printLog("ONRECIVE INSIDE Driver TripBegin");
					//stop the timer to get the current address if any booking is going on
					if(myTimer!=null)
					{
						myTimer.cancel();
						myTimer=null;
					}

					all_types_layout.setVisibility(View.INVISIBLE);
					allTypeLinearLayout.setVisibility(View.INVISIBLE);
					scrollView.setVisibility(View.GONE);
					//	getETAWithTimer(8);

					{
						googleMap.clear();
						//marker_map.clear();
						marker_map_on_the_way.clear();
						Utility.printLog("marker_map_on_the_way 6");
						marker_map_arrived.clear();
						//new CallGooglePlayServices().execute();
						setHasOptionsMenu(true);
						if(session.getDriverEmail()!=null)
						{
							getCarDetails();

						}
						if(session.getAptDate()!=null)
						{
							getAppointmentDetails_Volley(session.getAptDate());

						}
					}
					return;
				}

				else if (!session.isLoggedIn())
				{
					Utility.printLog("out side 12" + session.getRejectedFromAdmin());
					if((session.getRejectedFromAdmin()).equals("true"))
					{

						Utility.printLog("out side 12 inside");
						session=new SessionManager(getActivity());
						session.setIsUserRejectedFromAdmin("false");
						session.setIsLogin(false);


						Intent intent2=new Intent(getActivity(), SplashActivity.class);

						intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent2);
						getActivity().finish();
						Toast.makeText(getActivity(), getResources().getString(R.string.profileLogout), Toast.LENGTH_LONG).show();

					}


				}

				//else if(session.isInvoiceRaised() && !session.isBookingCancelled() && !session.getBookingId().equals("0"))
				else if(session.isInvoiceRaised() && !session.getBookingId().equals("0"))
				{
					Utility.printLog("ONRECIVE INSIDE Driver InvoiceRaised");
					Utility.printLog("ONRECIVE INSIDE apnt date "+session.getAptDate());

					Utility.printLog("mActivity=" + mActivity + " " + isAdded());
					if(mActivity!=null)
					{
						String[] subscribed_channels1 = pubnub.getSubscribedChannelsArray();
						Utility.printLog("Appointments details subscribed_channels my channel=" + session.getChannelName());
						ArrayList<String> unsubscribeChannels1= new ArrayList<String>();
						for(int i=0;i<subscribed_channels1.length;i++)
						{
							Utility.printLog("Appointments details subscribed_channels at status 9" + i + " " + subscribed_channels1[i]);


							unsubscribeChannels1.add(subscribed_channels1[i]);

							if(unsubscribeChannels1.size()>0)
							{
								Utility.printLog("Appointments details channels unsubscribeChannels channel list size status 9=" + unsubscribeChannels1.size());
								String[] new_un_sub_channels=new String[unsubscribeChannels1.size()];
								new_un_sub_channels=unsubscribeChannels1.toArray(new_un_sub_channels);
								new BackgroundUnSubscribeChannels().execute(new_un_sub_channels);
							}

						}
						session.storeBookingId("0");
						Utility.printLog("mActivity if=" + mActivity);
						/*Intent intent1 = new Intent(mActivity,InvoiceActivity.class);
						if (tipResponse != null && "0".equals(tipResponse.getErrFlag())) {
							intent1.putExtra("Tip", tipResponse);

						}
						mActivity.startActivity(intent1);*/
						if(session.getAptDate()!=null)
						{
							getAppointmentDetails_Volley(session.getAptDate());

						}
					}
					else
					{
						//new BackgroundUnSubscribeChannels().execute(session.getCurrentAptChannel());

						String[] subscribed_channels1 = pubnub.getSubscribedChannelsArray();
						Utility.printLog("Appointments details subscribed_channels my channel=" + session.getChannelName());
						ArrayList<String> unsubscribeChannels1= new ArrayList<String>();
						for(int i=0;i<subscribed_channels1.length;i++)
						{
							Utility.printLog("Appointments details subscribed_channels at status 9" + i + " " + subscribed_channels1[i]);


							unsubscribeChannels1.add(subscribed_channels1[i]);

							if(unsubscribeChannels1.size()>0)
							{
								Utility.printLog("Appointments details channels unsubscribeChannels channel list size status 9=" + unsubscribeChannels1.size());
								String[] new_un_sub_channels=new String[unsubscribeChannels1.size()];
								new_un_sub_channels=unsubscribeChannels1.toArray(new_un_sub_channels);
								new BackgroundUnSubscribeChannels().execute(new_un_sub_channels);
							}

						}

						session.storeBookingId("0");
					/*	mActivity=(MainActivityDrawer)getActivity();
						Utility.printLog("mActivity else=" + mActivity);
						Intent intent1 = new Intent(mActivity,InvoiceActivity.class);
						if (tipResponse != null && "0".equals(tipResponse.getErrFlag())) {
							intent1.putExtra("Tip", tipResponse);

						}
						mActivity.startActivity(intent1);*/
						if(session.getAptDate()!=null)
						{
							getAppointmentDetails_Volley(session.getAptDate());

						}

					}
					return;
				}
				else if(session.isDriverCancelledApt())
				{
					session.setDriverOnWay(false);
					session.setBookingCancelled(true);
					Utility.printLog("Wallah set as false Homepage cancel 2");
					session.setDriverArrived(false);
					session.setTripBegin(false);
					session.setInvoiceRaised(false);
					session.storeAptDate(null);
					session.storeBookingId("0");

					Toast.makeText(mActivity, session.getCancellationType(), Toast.LENGTH_SHORT).show();
					Intent i = new Intent(mActivity, MainActivityDrawer.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					mActivity.startActivity(i);
					mActivity.finish();
					mActivity.overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);


				}
			}
		};
		//getActivity().registerReceiver(receiver, filter);
	}

	private void getCarDetails()
	{
		Utility utility=new Utility();
		String currenttime=utility.getCurrentGmtTime();
		final RequestBody requestBody = new FormEncodingBuilder()

				.add("ent_sess_token", session.getSessionToken())
				.add("ent_dev_id", session.getDeviceId())
				.add("ent_email", session.getDriverEmail())
				.add("ent_date_time", currenttime)
				.build();
		OkHttpRequest.doJsonRequest(VariableConstants.BASE_URL+"getMasterCarDetails", requestBody, new OkHttpRequest.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {

				carDetailsResponse = result;
				Utility.printLog("Success of getting getCarDetails" + result);
				getCarInfo();
			}

			@Override
			public void onError(String error) {

				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_connection_fail), Toast.LENGTH_SHORT).show();

			}
		});

	}
	/**
	 * Handling CarDetails response
	 */
	private void getCarInfo()
	{
		try
		{
			JSONObject jsnResponse = new JSONObject(carDetailsResponse);
			String jsonErrorParsing = jsnResponse.getString("errFlag");

			Utility.printLog("jsonErrorParsing is ---> " + jsonErrorParsing);
			parseCarDetails();

			if(getCarDetails!=null)
			{
				if(getCarDetails.getErrFlag().equals("0"))
				{
					SessionManager session =new SessionManager(mActivity);
					Utility.printLog("bbbb getPlateNo=" + getCarDetails.getPlateNo());
					session.storePlateNO(getCarDetails.getPlateNo());
					session.storeCarModel(getCarDetails.getModel());
					Utility.printLog("bbbb getPlateNo=" + session.getPlateNO());
					//Driver_Car_Type.setText(getCarDetails.getModel());
					Driver_Car_Num.setText(getCarDetails.getPlateNo());
					driver_car_plate_no_new.setText(getCarDetails.getPlateNo());
				}
				else
				{
					SessionManager session =new SessionManager(mActivity);
					session.storePlateNO("--");
					session.storeCarModel("--");
				}
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			Utility.ShowAlert(getResources().getString(R.string.server_error), mActivity);
		}
	}
	//parsing CarDetails response
	private void parseCarDetails()
	{
		Gson gson = new Gson();
		getCarDetails = gson.fromJson(carDetailsResponse, GetCarDetails.class);
	}


	/**
	 * Initializing all variables in this view
	 * @param v
	 */
	private void initializeVariables(View v)
	{
		getActivity().registerReceiver(receiver, filter);
		networkUtil=new NetworkUtil(getActivity(),this);
		new_dropoff_location_address=(TextView) v.findViewById(R.id.dropaddresstv);
		pickup=(RelativeLayout)v.findViewById(R.id.relative_center);
		current_address=(TextView)v.findViewById(R.id.show_addr_text_view);
		Dropoff_Location_Address=(TextView)v.findViewById(R.id.dropoff_location_address);
		addressSearchButton=(ImageButton)v.findViewById(R.id.address_search_button);
		show_address_relative=(RelativeLayout)v.findViewById(R.id.show_address_relative);
		relativePickupLocation=(RelativeLayout)v.findViewById(R.id.relative_pickup_location);
		Relative_Pickup_Navigation=(RelativeLayout)v.findViewById(R.id.relative_pickup_navigation);
		Relative_Dropoff_Location=(RelativeLayout)v.findViewById(R.id.relative_dropoff_location);
		Relative_Card_Info=(RelativeLayout)v.findViewById(R.id.relative_card_info);
		pickupLocationAddress=(TextView)v.findViewById(R.id.pickup_location_address);
		Request_Pick_up_here = (Button)v.findViewById(R.id.request_pick_up_here);
		Fare_Quote = (Button)v.findViewById(R.id.fare_quote);
		promeCode = (Button)v.findViewById(R.id.promo_code);
		Card_Info = (TextView)v.findViewById(R.id.card_info);
		AddLocation = (Button)v.findViewById(R.id.add_drop_off_location);
		Txt_Pickup = (TextView)v.findViewById(R.id.txt_pickup);
		Btn_Back = (ImageButton)v.findViewById(R.id.btn_back);
		Card_Image = (ImageView)v.findViewById(R.id.card_image);
		dummy_view = (View)v.findViewById(R.id.dummy_view);
		RL_homepage = (RelativeLayout)v.findViewById(R.id.rl_homepage);
		drivvehicalrating= (TextView) v.findViewById(R.id.drivvehicalrating);
		Img_Map = (ImageButton)v.findViewById(R.id.img_map);
		Img_Dropoff= (ImageButton)v.findViewById(R.id.img_dropoff);
		Driver_Profile_Pic = (ImageView)v.findViewById(R.id.driver_profile_pic);
		Driver_Name = (TextView)v.findViewById(R.id.driver_name);
		Driver_on_the_way_txt = (Button)v.findViewById(R.id.driver_on_the_way);
		Mid_Pointer = (ImageButton)v.findViewById(R.id.mid_pointer);
		Rl_distance_time = (RelativeLayout)v.findViewById(R.id.rl_distance_time);
		Driver_Time = (TextView)v.findViewById(R.id.driver_time);
		Driver_Car_Num = (TextView)v.findViewById(R.id.driver_car_plate_no);
		pickup_Distance = (TextView)v.findViewById(R.id.txt_pickup_distance);
		pubnubProgressDialog = (ProgressBarCircularIndetermininate)v.findViewById(R.id.progressBar);
		now_button = (Button)v.findViewById(R.id.now_button);
		later_button = (Button)v.findViewById(R.id.later_button);
		//booked_now_later=(TextView)v.findViewById(R.id.booking_for);
		//relative_now_later_status =(RelativeLayout)v.findViewById(R.id.relative_now_later_status);
		all_types_layout=(RelativeLayout)v.findViewById(R.id.relative_all_car_types);
		now_later_layout= (LinearLayout) v.findViewById(R.id.now_later_layout);
		choose_payment_layout=(RelativeLayout)v.findViewById(R.id.choose_payment_screen);
		pay_cash=(Button)v.findViewById(R.id.payment_cash);
		pay_card=(Button)v.findViewById(R.id.payment_card);
		pay_cancel=(Button)v.findViewById(R.id.payment_cancel);
		farePromoLayouy=(LinearLayout)v.findViewById(R.id.fare_promo_layouy);
		rate_unit=(TextView) v.findViewById(R.id.rate_unit);
		driver_parent=(RelativeLayout)v.findViewById(R.id.driver_parent);
		share_eta= (LinearLayout) v.findViewById(R.id.share_eta);
		contact_driver= (LinearLayout) v.findViewById(R.id.contact_driver);
		cancel_trip= (LinearLayout) v.findViewById(R.id.cancel_trip);
		allTypeLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
		dialogL= Utility.GetProcessDialog(getActivity());
		dialogL.setCancelable(false);
		MainActivityDrawer.driver_tip.setTextSize(20);
		drivsnd_new= (TextView) v.findViewById(R.id.drivsnd_new);
		drivsnd= (TextView) v.findViewById(R.id.drivsnd);
		driver_dist= (TextView) v.findViewById(R.id.driver_dist);
		eta_text= (TextView) v.findViewById(R.id.eta_text);
		driver_distcom= (TextView) v.findViewById(R.id.driver_distcom);
		eta_textcom= (TextView) v.findViewById(R.id.eta_textcom);
		txt_roadyo= (TextView) v.findViewById(R.id.txt_roadyo);

		share_eta.setOnClickListener(this);
		contact_driver.setOnClickListener(this);
		cancel_trip.setOnClickListener(this);
		driver_parent.setOnClickListener(this);
		slideUp= AnimationUtils.loadAnimation(getActivity(), R.anim.move_up);
		slideDown=AnimationUtils.loadAnimation(getActivity(),R.anim.move_down);

		dblArray = Scaler.getScalingFactor(getActivity());

		hybrid_view = (ImageButton)v.findViewById(R.id.map_hybrid_view);

		new_dropoff_location_address.setOnClickListener(this);
		pay_cash.setOnClickListener(this);
		pay_card.setOnClickListener(this);
		pay_cancel.setOnClickListener(this);
		now_button.setOnClickListener(this);
		later_button.setOnClickListener(this);
		pickup.setOnClickListener(this);
		addressSearchButton.setOnClickListener(this);
		AddLocation.setOnClickListener(this);
		Card_Info.setOnClickListener(this);
		Btn_Back.setOnClickListener(this);
		Fare_Quote.setOnClickListener(this);
		promeCode.setOnClickListener(this);
		Request_Pick_up_here.setOnClickListener(this);
		Relative_Card_Info.setOnClickListener(this);
		RL_homepage.setOnClickListener(this);
		relativePickupLocation.setOnClickListener(this);
		Relative_Dropoff_Location.setOnClickListener(this);
		Img_Map.setOnClickListener(this);
		Img_Dropoff.setOnClickListener(this);
		hybrid_view.setOnClickListener(this);
		choose_payment_layout.setOnClickListener(this);
		Typeface roboto_regular = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Regular.ttf");
		Typeface roboto_condensed = Typeface.createFromAsset(getActivity().getAssets(),"fonts/RobotoCondensed-Light.ttf");

		Driver_Name.setTypeface(roboto_regular);
		drivsnd_new.setTypeface(roboto_regular);
		drivsnd.setTypeface(roboto_regular);
		Driver_on_the_way_txt.setTypeface(roboto_regular);
		Driver_Car_Num.setTypeface(roboto_condensed);

		horizontal_scrollView = (HorizontalScrollView)view.findViewById(R.id.horizontalScrollView);
		scrollView= (HorizontalScrollView) view.findViewById(R.id.scrollView);

		drivehicalimg= (ImageView) v.findViewById(R.id.drivehicalimg);
		pickupaddresstv= (TextView) v.findViewById(R.id.pickupaddresstv);
		expandedLayout= (LinearLayout) v.findViewById(R.id.expandedLayout);
		compressedLayout= (LinearLayout) v.findViewById(R.id.compressedLayout);
		drivehicalimg_new= (ImageView) v.findViewById(R.id.drivehicalimg_new);
		driver_profile_pic_new= (ImageView) v.findViewById(R.id.driver_profile_pic_new);
		drivvehicalrating_new= (TextView) v.findViewById(R.id.drivvehicalrating_new);
		driver_name_new= (TextView) v.findViewById(R.id.driver_name_new);
		driver_car_plate_no_new= (TextView) v.findViewById(R.id.driver_car_plate_no_new);   //drivvehicalrating_new,driver_name_new,driver_car_plate_no_new;
		driver_car_plate_no_new.setTypeface(roboto_condensed);



		previousArrow= (ImageView) v.findViewById(R.id.previous);
		nextArrow= (ImageView) v.findViewById(R.id.next);
		expandedLayout.setOnClickListener(this);
		compressedLayout.setOnClickListener(this);
		driver_time_new= (TextView) v.findViewById(R.id.driver_time_new);
		textFare= (TextView) v.findViewById(R.id.textFare);

		MainActivityDrawer.driver_tip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				getTipOnClick();
			}
		});

		ViewTreeObserver vto = allTypeLinearLayout.getViewTreeObserver();

		vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				ViewTreeObserver obs = allTypeLinearLayout.getViewTreeObserver();
				obs.removeGlobalOnLayoutListener(this);
				Display display = getActivity().getWindowManager().getDefaultDisplay();
				displayWidth = display.getWidth();
			}

		});
		scrollView.setOnTouchListener(listenerScrollViewTouch);

	}


	private OnTouchListener listenerScrollViewTouch = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			showHideViews();
			return false;
		}
	};



	public  void showHideViews()
	{
		int maxScrollX = scrollView.getChildAt(0).getMeasuredWidth()- displayWidth;

		if (scrollView.getScrollX() == 0)
		{
			previousArrow.setVisibility(View.GONE);
		}
		else
		{
			previousArrow.setVisibility(VISIBLE);
			nextArrow.setVisibility(VISIBLE);
		}
		if (scrollView.getScrollX() == maxScrollX)
		{
			previousArrow.setVisibility(VISIBLE);
			nextArrow.setVisibility(View.GONE);
		}

	}



	private void showPopupForShare()
	{
		LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupLayout = inflater.inflate(R.layout.popup_share_new,(ViewGroup)view.findViewById(R.id.share_parent));

		message_shar=(Button)popupLayout.findViewById(R.id.message_share);
		whatsapp_sahre=(Button)popupLayout.findViewById(R.id.whatsapp_share);
		email_share=(Button)popupLayout.findViewById(R.id.email_share);
		cancel_share=(Button)popupLayout.findViewById(R.id.cancel_share);

		message_shar.setOnClickListener(this);
		whatsapp_sahre.setOnClickListener(this);
		email_share.setOnClickListener(this);
		cancel_share.setOnClickListener(this);

		popup_share = new PopupWindow(popupLayout, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{

		setHasOptionsMenu(true);
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}

		try {
			session = new SessionManager(getActivity());
			view = inflater.inflate(R.layout.homepage, container, false);
			visibility = true;
		} catch (InflateException e) {
			/* map is already there, just return view as it is */
		}

		initializeVariables(view);

		markerId_Email_map = new HashMap<String, String>();
		fm = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
		googleMap = fm.getMap();
		googleMap.setMyLocationEnabled(true);
		googleMap.getUiSettings().setZoomControlsEnabled(false);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		int value=(int)Math.round(50*dblArray[0]);
		fm.getMap().setPadding(0, value, 0, 0);

		initImageLoader();
		imageLoader = ImageLoader.getInstance();

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.driver_image_border)
				.showImageForEmptyUri(R.drawable.driver_image_border)
				.showImageOnFail(R.drawable.driver_image_border)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(0))
				.build();



		if(lat==0.0 && longi==0.0)

		{

			/*if(dialogL!=null)
			{
				dialogL.show();
			}*/

		}

		{
			if(session.getAptDate()!=null)
			{
				AppStatus(session.getAptDate());
			}
			else
			{
				AppStatus1();

			}
		}



		if (filterResponse.getTypes() != null && filterResponse.getTypes().size() > 0)
		{
			current_master_type_id = filterResponse.getTypes().get(0).getType_id();

			current_master_type_name = filterResponse.getTypes().get(0).getType_name();

			car_name=current_master_type_name;

			for (int i = 0; i < filterResponse.getTypes().size(); i++)
			{
				markerPlotingFirstTime.add(i, true);
			}
		}

		addScrollView();


		googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition arg0) {
				Handler handler = new Handler();
				if(VariableConstants.CONFIRMATION_CALLED){
					cameraMoved=true;
				}
				handler.postDelayed(new Runnable() {
					public void run() {
						IsreturnFromSearch = false;
						if(pickup.getVisibility()==VISIBLE)
						{
							dropOffSet=false;
						}
						if(!VariableConstants.CONFIRMATION_CALLED)
						{
							pubnubProgressDialog.setClickable(false);
							pubnubProgressDialog.setVisibility(VISIBLE);
							pickup_Distance.setVisibility(View.GONE);
							rate_unit.setVisibility(View.GONE);
						}
						VariableConstants.isPubnubCalled = false;
					}
				}, 5000);
			}
		});

		pubnub = new Pubnub(VariableConstants.PUBNUB_PUBLISH_KEY, VariableConstants.PUBUB_SUBSCRIBE_KEY, "", true);
		pubnub.setUUID(session.getLoginId());
		try {
			pubnub.presence(session.getPresenceChn(), new Callback() {
				@Override
				public void successCallback(String channel, Object message)
				{
					Utility.printLog("success from presence "+message);
				}
			});
		} catch (PubnubException e) {
			e.printStackTrace();
			Utility.printLog("success from presence " + e);

		}

		return view;
	}

	private void AppStatus1()
	{
		Utility utility=new Utility();
		String curenttime=utility.getCurrentGmtTime();
		final RequestBody requestBody = new FormEncodingBuilder()

				.add("ent_sess_token", session.getSessionToken())
				.add("ent_dev_id", session.getDeviceId())
				.add("ent_user_type","2")
				.add("ent_date_time", curenttime)
				.build();
		OkHttpRequest.doJsonRequest(VariableConstants.BASE_URL+"getApptStatus", requestBody, new OkHttpRequest.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {

				Utility.printLog("success in aptstatus 1  " + result);
				getAppStatus(result);


			}

			@Override
			public void onError(String error) {

				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_connection_fail), Toast.LENGTH_SHORT).show();

			}
		});
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId()==R.id.later_book)
		{
			final TimePicker timep;
			final DatePicker datep;
			final Dialog picker ;
			Button set;

			picker = new Dialog(getActivity());
			picker.setContentView(R.layout.picker_frag);
			picker.setTitle("Select Date and Time");
			datep = (DatePicker)picker.findViewById(R.id.datePicker);
			timep = (TimePicker)picker.findViewById(R.id.timePicker);
			set = (Button)picker.findViewById(R.id.btnSet);

			datep.setMinDate(System.currentTimeMillis() - 1000);
			try {
				java.lang.reflect.Field[] f = datep.getClass().getDeclaredFields();
				for (java.lang.reflect.Field field : f) {
					if (field.getName().equals("mYearSpinner"))
					{
						field.setAccessible(true);
						Object yearPicker;
						yearPicker = field.get(datep);
						((View) yearPicker).setVisibility(View.GONE);
					}
				}
			}
			catch (SecurityException e) {
				Log.d("ERROR", e.getMessage());
			}
			catch (IllegalArgumentException e) {
				Log.d("ERROR", e.getMessage());
			}
			catch (IllegalAccessException e) {
				Log.d("ERROR", e.getMessage());
			}

			set.setOnClickListener(new OnClickListener()
			{
				public void onClick(View view)
				{
					// later_month = String.valueOf(datep.getMonth());
					// later_day = String.valueOf(datep.getDayOfMonth());
					// later_year = String.valueOf(datep.getYear());
					later_hour = String.valueOf(timep.getCurrentHour());
					later_min = String.valueOf(timep.getCurrentMinute());

					if((String.valueOf(later_hour).length()) == 1)
					{
						later_hour = "0"+later_hour;
					}
					if((String.valueOf(later_min).length()) == 1)
					{
						later_min = "0"+later_min;
					}


					laterBookingDate = later_year+"-"+later_month+"-"+later_day+" "+later_hour+":"+later_min+":00";
					picker.dismiss();
					Toast.makeText(getActivity(),laterBookingDate, Toast.LENGTH_SHORT).show();
				}
			});
			picker.show();

			selectDate(view);
		}

		if(item.getItemId()==R.id.now_book)
		{
			laterBookingDate = null;
			//later.setTitle(R.string.later_unselected);
			//now.setTitle(R.string.now_selected);
		}	return super.onOptionsItemSelected(item);
	}


	public void selectDate(View view)
	{
		DialogFragment newFragment = new SelectDateFragment();
		newFragment.show(getFragmentManager(), "DatePicker");
	}
	public void populateSetDate(int year, int month, int day)
	{
		//"YYYY-MM-DD HH:MM:SS"
		String later_booking_date= year+"-"+month+"-"+day;
		later_year = String.valueOf(year);
		later_month = String.valueOf(month);
		later_day = String.valueOf(day);
		Toast.makeText(getActivity(), "Appointment Date: "+(month+"/"+day+"/"+year), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void updatedInfo(String info)
	{

	}

	@Override
	public void locationUpdates(Location location) {
		if(location!=null)
		{
			if(!VariableConstants.isComingFromSearch) {
				VariableConstants.isComingFromSearch=true;
				lat = location.getLatitude();
				longi = location.getLongitude();
				LatLng latLng = new LatLng(lat, longi);
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
			}
		}
	}

	@Override
	public void locationFailed(String message) {

	}

	@SuppressLint("ValidFragment")
	public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
	{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);
		}

		public void onDateSet(DatePicker view, int yy, int mm, int dd) {
			populateSetDate(yy, mm+1, dd);
		}
	}

	@Override
	public void onClick(View v)
	{
		if(v.getId()==R.id.now_button)
		{

			laterBookingDate = null;
			//booked_now_later.setText("YOU ARE BOOKING FOR NOW");
			//now_later_status.setVisibility(View.VISIBLE);
			//relative_now_later_status.setVisibility(View.VISIBLE);
			now_button.setTextColor(getResources().getColor(R.color.dark_orange));
			later_button.setTextColor(getResources().getColor(R.color.white));
		}
		if(v.getId()==R.id.later_button)
		{
			final TimePicker timep;
			final DatePicker datep;
			final Dialog picker ;
			final Button set;

			picker = new Dialog(getActivity());
			picker.setContentView(R.layout.picker_frag);
			picker.setTitle("Select Date and Time");
			datep = (DatePicker)picker.findViewById(R.id.datePicker);
			timep = (TimePicker)picker.findViewById(R.id.timePicker);
			set = (Button)picker.findViewById(R.id.btnSet);

			datep.setMinDate(System.currentTimeMillis() - 1000);
			try
			{
				java.lang.reflect.Field[] f = datep.getClass().getDeclaredFields();
				for (java.lang.reflect.Field field : f)
				{
					if (field.getName().equals("mYearSpinner"))
					{
						field.setAccessible(true);
						Object yearPicker = new Object();
						yearPicker = field.get(datep);
						((View) yearPicker).setVisibility(View.GONE);
					}
				}
			}
			catch (SecurityException e)
			{
				Log.d("ERROR", e.getMessage());
			}
			catch (IllegalArgumentException e)
			{
				Log.d("ERROR", e.getMessage());
			}
			catch (IllegalAccessException e)
			{
				Log.d("ERROR", e.getMessage());
			}
			int hours = new Time(System.currentTimeMillis()).getHours();
			hours++;
			timep.setCurrentHour(hours);

			timep.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
				@Override
				public void onTimeChanged(TimePicker timePicker, int i, int i1) {
					Calendar cal = Calendar.getInstance();
					Calendar calendar = Calendar.getInstance();
					calendar.clear();
					calendar.set(Calendar.YEAR, datep.getYear());
					calendar.set(Calendar.DAY_OF_MONTH, datep.getDayOfMonth());
					calendar.set(Calendar.MONTH, datep.getMonth());
					calendar.set(Calendar.HOUR_OF_DAY, timep.getCurrentHour());
					calendar.set(Calendar.MINUTE, timep.getCurrentMinute());
					Log.i("", "time change minutee " + timePicker.getCurrentMinute());
					cal.add(Calendar.HOUR,1);
					if (validateTime(cal.getTimeInMillis() / 1000L, calendar.getTimeInMillis() / 1000L)) {
						SimpleDateFormat format = new SimpleDateFormat("hh:mm a dd/MMM/yyyy",Locale.US);
						Date date = calendar.getTime();
						set.setEnabled(true);
					}
					else
					{
						set.setEnabled(false);
						Utility.ShowAlert(getResources().getString(R.string.bookingsNotSelected),getActivity());
					}
				}
			});

			set.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View view)
				{
					String  hour = String.valueOf(timep.getCurrentHour());
					String min = String.valueOf(timep.getCurrentMinute());

					if((String.valueOf(hour).length()) == 1)
					{
						hour = "0"+hour;
					}
					if((String.valueOf(min).length()) == 1)
					{
						min = "0"+min;
					}

					int year = datep.getYear();
					int month = datep.getMonth()+1;
					int day = datep.getDayOfMonth();

					laterBookingDate = year+"-"+month+"-"+day+" "+hour+":"+min+":00";
					//booked_now_later.setText("YOU ARE BOOKING FOR LATER : "+day+"-"+month+"-"+year+" "+hour+":"+min);

					Utility.printLog("laterBookingDate="+laterBookingDate);
					picker.dismiss();
				}
			});
			picker.show();
		}
		if(v.getId()==R.id.map_hybrid_view)
		{
			if(googleMap.getMapType() != GoogleMap.MAP_TYPE_HYBRID)
			{
				googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				hybrid_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_map_selector));
			}
			else if(googleMap.getMapType() != GoogleMap.MAP_TYPE_NORMAL)
			{
				googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				hybrid_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.hybrid_map_selector));
			}
		}

		if (v.getId()==R.id.address_search_button)
		{
			VariableConstants.TYPE="";
			VariableConstants.SEARCH_CALLED=true;
			Intent addressIntent=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", getResources().getString(R.string.pickup_location));
			startActivityForResult(addressIntent, 18);
			getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;
		}
		if(v.getId()==R.id.relative_pickup_location)
		{
			Intent addressIntent=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", getResources().getString(R.string.pickup_location));
			startActivityForResult(addressIntent, 18);
			getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;
		}
		if(v.getId()==R.id.img_map)
		{
			Intent addressIntent=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", getResources().getString(R.string.pickup_location));
			startActivityForResult(addressIntent, 18);
			getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;
		}
		if(v.getId()==R.id.relative_dropoff_location)
		{
			Intent addressIntent=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", getResources().getString(R.string.drop_location));
			startActivityForResult(addressIntent, 16);
			getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;
		}


		if(v.getId()==R.id.dropaddresstv)
		{
			isdropoffclicked = true;
			Relative_Dropoff_Location.setVisibility(View.GONE);
			Intent addressIntent=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", getResources().getString(R.string.drop_location));
			startActivityForResult(addressIntent, 30);
			getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;

		}


		if(v.getId()==R.id.img_dropoff)
		{
			Intent addressIntent=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", getResources().getString(R.string.drop_location));
			startActivityForResult(addressIntent, 16);
			getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;
		}

		if(v.getId()==R.id.relative_center)
		{
			for(int j=0;j<type1NearestDrivers.size();j++)
			{

				nearestDrivers=type1NearestDrivers;
			}
			if((nearestDrivers!=null) && (nearestDrivers.size()>0))
			{

				pubnubProgressDialog.setClickable(false);
				pubnubProgressDialog.setVisibility(View.INVISIBLE);
				pickup_Distance.setVisibility(View.INVISIBLE);
				rate_unit.setVisibility(View.INVISIBLE);

				if(surgeAvailable && Double.parseDouble(surgePrice)>1)
				{
					surgePopup = new Dialog(getActivity());
					surgePopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
					surgePopup.setContentView(R.layout.surge_popup);

					// Gets linearlayout
					LinearLayout layout = (LinearLayout)surgePopup.findViewById(R.id.surgeLayout);
					// Gets the layout params that will allow you to resize the layout
					LayoutParams params = (LayoutParams) layout.getLayoutParams();
					// Changes the height and width to the specified *pixels*
					params.height = 100;
					params.height  = (int) (dblArray[1] * 150);
					layout.setLayoutParams(params);


					TextView surgeText= (TextView) surgePopup.findViewById(R.id.surgeText);
					TextView type= (TextView) surgePopup.findViewById(R.id.type);
					ImageView image= (ImageView) surgePopup.findViewById(R.id.carImage);
					TextView acceptHigherRate= (TextView) surgePopup.findViewById(R.id.acceptHigherRate);
					TextView cancel= (TextView) surgePopup.findViewById(R.id.cancel);

					acceptHigherRate.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v)
						{

							surgePopup.dismiss();

							textFare.setVisibility(View.GONE);
							Fare_Quote.setText(getResources().getString(R.string.fare_quote));

							getActivity().getActionBar().hide();
							//relative_now_later_status.setVisibility(View.GONE);
							isBackPressed=true;
							show_address_relative.setVisibility(View.INVISIBLE);
							//Txt_Pickup.setVisibility(View.INVISIBLE);
							//pickup.setVisibility(View.INVISIBLE);
							Txt_Pickup.setText(getResources().getString(R.string.set_drop_location));
							all_types_layout.setVisibility(View.INVISIBLE);
							allTypeLinearLayout.setVisibility(View.INVISIBLE);
							scrollView.setVisibility(View.GONE);
							allTypeLinearLayout.setVisibility(View.GONE);

							pickupLocationAddress.setText(mPICKUP_ADDRESS);
							pick_up=mPICKUP_ADDRESS;
							if(car_name!=null)
							{
								if(laterBookingDate!=null)
								{
									String[] StartTime1 = laterBookingDate.split(" ");
									String[] date = StartTime1[0].split("-");
									String[] time = StartTime1[1].split(":");

									String hour = time[0];
									String min = time[1];

									String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

									if(Integer.parseInt(hour)==12)
									{
										String later_date = date[2]+" "+MONTHS[Integer.parseInt(date[1])-1]+" "+date[0]+" "+hour+":"+min+" PM";
										Request_Pick_up_here.setText(getResources().getString(R.string.request_on)+" "+later_date);
									}
									else if(Integer.parseInt(hour)>12)
									{
										int hr = Integer.parseInt(hour)-12;
										String later_date = date[2]+" "+MONTHS[Integer.parseInt(date[1])-1]+" "+date[0]+" "+hr+":"+min+" PM";
										Request_Pick_up_here.setText(getResources().getString(R.string.request_on)+" "+later_date);
									}
									else
									{
										String later_date = date[2]+" "+MONTHS[Integer.parseInt(date[1])-1]+" "+date[0]+" "+hour+":"+min+" AM";
										Request_Pick_up_here.setText(getResources().getString(R.string.request_on)+" "+later_date);
									}
								}
								else
								{
									Request_Pick_up_here.setText(getResources().getString(R.string.request)+" "+car_name.toUpperCase());
								}
							}
							else
							{
								Request_Pick_up_here.setText(getResources().getString(R.string.request)+" ");
							}
							relativePickupLocation.setVisibility(VISIBLE);
							Relative_Dropoff_Location.setVisibility(VISIBLE);
							Relative_Pickup_Navigation.setVisibility(VISIBLE);

							txt_roadyo.setText(getResources().getString(R.string.you_going));
							if(VariableConstants.CONFIRMATION_CALLED)
							{
								Fare_Quote.setVisibility(VISIBLE);
								farePromoLayouy.setVisibility(VISIBLE);
								Card_Info.setVisibility(VISIBLE);
								Relative_Card_Info.setVisibility(VISIBLE);
								Card_Image.setVisibility(VISIBLE);
								Request_Pick_up_here.setVisibility(VISIBLE);

								LatLng latLng=new LatLng(Double.parseDouble(from_latitude),Double.parseDouble(from_longitude));
								picupmarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.new_map_icon_pickup))
										.rotation(0)
										.flat(true));

								LatLng latLng1=new LatLng(Double.parseDouble(to_latitude),Double.parseDouble(to_longitude));
								picupmarker = googleMap.addMarker(new MarkerOptions().position(latLng1)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.new_map_icon_dropoff))
										.rotation(0)
										.flat(true));
								pickup.setVisibility(View.INVISIBLE);
								txt_roadyo.setText(getResources().getString(R.string.good_trip));
							}

							VariableConstants.CONFIRMATION_CALLED=true;
						}
					});

					cancel.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							surgePopup.dismiss();
						}
					});
					surgeText.setText(surgePrice + "x");
					type.setText(current_master_type_name + " Fares");
					double width = dblArray[0] * 50;
					double height = dblArray[1] * 50;
					Picasso.with(getActivity()).load(imageUrlToSendToSurge
					).resize((int) width, (int) height).into(image);
					surgePopup.setCancelable(true);
					surgePopup.show();

				}
				else
				{
					getActivity().getActionBar().hide();
					isBackPressed=true;
					show_address_relative.setVisibility(View.INVISIBLE);
					Txt_Pickup.setText(getResources().getString(R.string.set_drop_location));
					all_types_layout.setVisibility(View.INVISIBLE);
					allTypeLinearLayout.setVisibility(View.INVISIBLE);
					scrollView.setVisibility(View.GONE);
					now_later_layout.setVisibility(View.INVISIBLE);
					allTypeLinearLayout.setVisibility(View.GONE);

					textFare.setVisibility(View.GONE);
					Fare_Quote.setText(getResources().getString(R.string.fare_quote));


					pickupLocationAddress.setText(mPICKUP_ADDRESS);
					pick_up=mPICKUP_ADDRESS;
					if(car_name!=null)
					{
						if(laterBookingDate!=null)
						{
							String[] StartTime1 = laterBookingDate.split(" ");
							String[] date = StartTime1[0].split("-");
							String[] time = StartTime1[1].split(":");

							String hour = time[0];
							String min = time[1];

							String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

							if(Integer.parseInt(hour)==12)
							{
								String later_date = date[2]+" "+MONTHS[Integer.parseInt(date[1])-1]+" "+date[0]+" "+hour+":"+min+" PM";
								Request_Pick_up_here.setText(getResources().getString(R.string.request_on)+" "+later_date);
							}
							else if(Integer.parseInt(hour)>12)
							{
								int hr = Integer.parseInt(hour)-12;
								String later_date = date[2]+" "+MONTHS[Integer.parseInt(date[1])-1]+" "+date[0]+" "+hr+":"+min+" PM";
								Request_Pick_up_here.setText(getResources().getString(R.string.request_on)+" "+later_date);
							}
							else
							{
								String later_date = date[2]+" "+MONTHS[Integer.parseInt(date[1])-1]+" "+date[0]+" "+hour+":"+min+" AM";
								Request_Pick_up_here.setText(getResources().getString(R.string.request_on)+" "+later_date);
							}
						}
						else
						{
							Request_Pick_up_here.setText(getResources().getString(R.string.request)+" "+car_name.toUpperCase());
						}
					}
					else
					{
						Request_Pick_up_here.setText(getResources().getString(R.string.request)+" ");
					}
					relativePickupLocation.setVisibility(VISIBLE);
					Relative_Dropoff_Location.setVisibility(VISIBLE);
					Relative_Pickup_Navigation.setVisibility(VISIBLE);
					txt_roadyo.setText(getResources().getString(R.string.you_going));

					if(VariableConstants.CONFIRMATION_CALLED)
					{
						dropOffSet=true;
						Relative_Dropoff_Location.setEnabled(false);
						Img_Dropoff.setEnabled(false);
						AddLocation.setEnabled(false);
						Fare_Quote.setVisibility(VISIBLE);
						farePromoLayouy.setVisibility(VISIBLE);
						Card_Info.setVisibility(VISIBLE);
						Relative_Card_Info.setVisibility(VISIBLE);
						Card_Image.setVisibility(VISIBLE);
						Request_Pick_up_here.setVisibility(VISIBLE);

						if(to_latitude!=null && to_longitude!=null && from_latitude!=null && from_longitude!=null)
						{
							LatLng latLng=new LatLng(Double.parseDouble(from_latitude),Double.parseDouble(from_longitude));
							picupmarker1 = googleMap.addMarker(new MarkerOptions().position(latLng)
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.new_map_icon_pickup))
									.rotation(0)
									.flat(true));

							LatLng latLng1=new LatLng(Double.parseDouble(to_latitude),Double.parseDouble(to_longitude));
							picupmarker2 = googleMap.addMarker(new MarkerOptions().position(latLng1)
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.new_map_icon_dropoff))
									.rotation(0)
									.flat(true));

							LatLngBounds.Builder builder = new LatLngBounds.Builder();
							builder.include(picupmarker1.getPosition());
							builder.include(picupmarker2.getPosition());
							LatLngBounds bounds = builder.build();
							CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, dummy_view.getWidth(),dummy_view.getHeight(),200);
							googleMap.animateCamera(cu);
						}

						pickup.setVisibility(View.INVISIBLE);
						txt_roadyo.setText(getResources().getString(R.string.good_trip));

						googleMap.getUiSettings().setScrollGesturesEnabled(false);
						googleMap.getUiSettings().setZoomGesturesEnabled(false);
					}
					VariableConstants.CONFIRMATION_CALLED=true;
				}
			}

		}

		if(v.getId() == R.id.rl_homepage)
		{
			if(picupmarker1!=null && picupmarker2!=null)
			{
				picupmarker1.remove();
				picupmarker2.remove();
			}

			dropOffSet=false;
			cameraMoved=false;
			Relative_Dropoff_Location.setEnabled(true);
			Img_Dropoff.setEnabled(true);
			AddLocation.setEnabled(true);
			googleMap.getUiSettings().setScrollGesturesEnabled(true);
			googleMap.getUiSettings().setZoomGesturesEnabled(true);
			VariableConstants.CONFIRMATION_CALLED=false;
			Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));
			getActivity().getActionBar().show();
			//ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);
			isSetDropoffLocation=false;
			isBackPressed=false;
			isFareQuotePressed=false;
			to_latitude=null;
			to_longitude=null;
			mDROPOFF_ADDRESS=null;
			//isCardSelected=false;
			Dropoff_Location_Address.setText("");
			new_dropoff_location_address.setText(getResources().getString(R.string.addDropOfLocation));
			show_address_relative.setVisibility(VISIBLE);
			Txt_Pickup.setVisibility(VISIBLE);
			pickup.setVisibility(VISIBLE);
			all_types_layout.setVisibility(VISIBLE);
			allTypeLinearLayout.setVisibility(VISIBLE);
			scrollView.setVisibility(VISIBLE);
			now_later_layout.setVisibility(VISIBLE);
			relativePickupLocation.setVisibility(View.INVISIBLE);
			Request_Pick_up_here.setVisibility(View.INVISIBLE);
			Fare_Quote.setVisibility(View.INVISIBLE);
			farePromoLayouy.setVisibility(View.INVISIBLE);
			Relative_Card_Info.setVisibility(View.INVISIBLE);
			Card_Info.setVisibility(View.INVISIBLE);
			Card_Image.setVisibility(View.INVISIBLE);
			Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
			Relative_Pickup_Navigation.setVisibility(View.INVISIBLE);
			LatLng latLng = new LatLng(Double.parseDouble(from_latitude),Double.parseDouble(from_longitude));
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
		}

		if(v.getId()==R.id.add_drop_off_location)
		{
			if(AddLocation.getText().toString().trim().equals("+"))
			{
				Intent addressIntent=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
				addressIntent.putExtra("chooser", getResources().getString(R.string.drop_location));
				startActivityForResult(addressIntent, 16);
				getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_down_acvtivity);
			}
			else
			{
				Dropoff_Location_Address.setText("");
				new_dropoff_location_address.setText(getResources().getString(R.string.addDropOfLocation));
				AddLocation.setText("+");
				to_latitude = null;
				to_longitude = null;
				mDROPOFF_ADDRESS=null;
				isSetDropoffLocation=false;
			}
			return;
		}
		if(v.getId() == R.id.promo_code)
		{
			showPromoCodeAlert();
		}

		if(v.getId()==R.id.relative_card_info)
		{
			choose_payment_layout.setVisibility(VISIBLE);
			YoYo.with(Techniques.SlideInUp)
					.duration(700)
					.playOn(view.findViewById(R.id.choose_payment_screen));
			return;
		}
		if(v.getId()==R.id.card_info)
		{
			choose_payment_layout.setVisibility(VISIBLE);
			YoYo.with(Techniques.SlideInUp)
					.duration(700)
					.playOn(view.findViewById(R.id.choose_payment_screen));
			return;
		}
		if(v.getId()==R.id.payment_cash)
		{
			//choose_payment_layout.setVisibility(View.GONE);
			YoYo.with(Techniques.SlideOutDown)
					.duration(700)
					.playOn(view.findViewById(R.id.choose_payment_screen));

			payment_type="2";
			Card_Info.setText(getResources().getString(R.string.cash));
			Card_Image.setImageBitmap(null);

			return;
		}
		if(v.getId()==R.id.payment_card)
		{
			//choose_payment_layout.setVisibility(View.GONE);
			YoYo.with(Techniques.SlideOutDown)
					.duration(700)
					.playOn(view.findViewById(R.id.choose_payment_screen));
			Intent addressIntent=new Intent(getActivity(), ChangeCardActivity.class);
			startActivityForResult(addressIntent, 17);
			getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_up);

			return;
		}
		if(v.getId()==R.id.payment_cancel)
		{
			//choose_payment_layout.setVisibility(View.GONE);
			YoYo.with(Techniques.SlideOutDown)
					.duration(700)
					.playOn(view.findViewById(R.id.choose_payment_screen));
			return;
		}
		if(v.getId()==R.id.choose_payment_screen)
		{
			choose_payment_layout.setVisibility(View.INVISIBLE);
			return;
		}

		if(v.getId()==R.id.fare_quote)
		{

			if((from_latitude!=to_latitude || from_longitude!=to_longitude) && to_latitude!=null && to_longitude!=null)
			{
				isSetDropoffLocation=true;
			}

			if(isSetDropoffLocation)
			{
				getFareEstimate();
			}
		}

		if(v.getId()==R.id.request_pick_up_here)
		{
			if((payment_type!=null ) && (from_latitude!=to_latitude) && (from_longitude!=to_longitude))
			{
				if(walletAmt==0.00 || walletAmt<0)
				{
					if(car_type_index==0)
					{
						nearestDrivers = type1NearestDrivers;
					}
					else if(car_type_index==1)
					{
						nearestDrivers = type2NearestDrivers;
					}
					else if(car_type_index==2)
					{
						nearestDrivers = type3NearestDrivers;
					}
					else if(car_type_index==3)
					{
						nearestDrivers = type4NearestDrivers;
					}
					if((payment_type!=null ) && (from_latitude!=to_latitude) && (from_longitude!=to_longitude))
					{
						VariableConstants.CONFIRMATION_CALLED=false;
						if(laterBookingDate!=null )
						{
							sendLaterBooking("0");
						}
						else
						{
							SessionManager session = new SessionManager(getActivity());
							session.setBookingCancelled(false);
							AddLocation.setText("+");
							Intent intent = new Intent(getActivity(),RequestPickup.class);
							if(from_latitude!=null && from_longitude!=null)
							{
								double lat =  Double.parseDouble(from_latitude);
								double lon = Double.parseDouble(from_longitude);
								String zipcode=getAddress(getActivity(),lat,lon);
								if(zipcode!=null)
								{

								}
								else
								{
									zipcode = "560024";
								}
								intent.putExtra("Zipcode", zipcode);
							}


							if(isCouponValid)
							{
								intent.putExtra("coupon", mPromoCode);
								mPromoCode = null;
								promeCode.setText(getResources().getString(R.string.promo_code));
							}
							intent.putExtra("PICKUP_ADDRESS", pickupLocationAddress.getText().toString());
							intent.putExtra("FromLatitude", from_latitude);
							intent.putExtra("FromLongitude", from_longitude);
							if(mDROPOFF_ADDRESS!=null && !mDROPOFF_ADDRESS.isEmpty())
							{
								intent.putExtra("DROPOFF_ADDRESS", mDROPOFF_ADDRESS);
								intent.putExtra("ToLatitude", to_latitude);
								intent.putExtra("ToLongitude", to_longitude);
							}
							intent.putExtra("my_drivers", nearestDrivers);
							intent.putExtra("Car_Type", current_master_type_id);
							intent.putExtra("PAYMENT_TYPE", payment_type);
							intent.putExtra("Later_Booking_Date", laterBookingDate);
							intent.putExtra("SURGE",surgePrice);
							session.storePickuplat(from_latitude);
							session.storePickuplng(from_longitude);
							session.storeDropofflat(to_latitude);
							session.storeDropofflng(to_longitude);
							startActivityForResult(intent,19);
						}
					}
					else if(payment_type==null)
					{
						Utility.ShowAlert(getResources().getString(R.string.select_payment_type), getActivity());
					}
				}
				else
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							getActivity());

					// set title
					alertDialogBuilder.setTitle(getResources().getString(R.string.note));

					// set dialog message
					alertDialogBuilder
							.setMessage(getResources().getString(R.string.balnce_is)+" "+getResources().getString(R.string.currencuSymbol)+" "+walletAmt+". "+getResources().getString(R.string.use_wallet))
							.setCancelable(true)
							.setPositiveButton(getResources().getString(R.string.yes),new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id)
								{
									if(car_type_index==0)
									{
										nearestDrivers = type1NearestDrivers;
									}
									else if(car_type_index==1)
									{
										nearestDrivers = type2NearestDrivers;
									}
									else if(car_type_index==2)
									{
										nearestDrivers = type3NearestDrivers;
									}
									else if(car_type_index==3)
									{
										nearestDrivers = type4NearestDrivers;
									}
									//add these lines when drop location is mandatory
									//if((nearestDrivers.size()>0) && (isSetDropoffLocation) && (payment_type!=null ) && (from_latitude!=to_latitude) && (from_longitude!=to_longitude))
									if((payment_type!=null ) && (from_latitude!=to_latitude) && (from_longitude!=to_longitude))
									{
										if(laterBookingDate!=null )
										{
											sendLaterBooking("1");

										}
										else
										{
											SessionManager session = new SessionManager(getActivity());
											session.setBookingCancelled(false);

											AddLocation.setText("+");
											Intent intent = new Intent(getActivity(),RequestPickup.class);


											double lat =  Double.parseDouble(from_latitude);
											double lon = Double.parseDouble(from_longitude);
											String zipcode=getAddress(getActivity(),lat,lon);
											if(zipcode!=null)
											{

											}
											else
											{
												zipcode = "560024";
											}

											intent.putExtra("PICKUP_ADDRESS", pickupLocationAddress.getText().toString());
											intent.putExtra("FromLatitude", from_latitude);
											intent.putExtra("FromLongitude", from_longitude);
											if(mDROPOFF_ADDRESS!=null && !mDROPOFF_ADDRESS.isEmpty())
											{
												intent.putExtra("DROPOFF_ADDRESS", mDROPOFF_ADDRESS);
												intent.putExtra("ToLatitude", to_latitude);
												intent.putExtra("ToLongitude", to_longitude);
											}
											intent.putExtra("my_drivers", nearestDrivers);
											intent.putExtra("Zipcode", zipcode);
											intent.putExtra("Car_Type", current_master_type_id);
											intent.putExtra("PAYMENT_TYPE", payment_type);
											intent.putExtra("Later_Booking_Date", laterBookingDate);
											intent.putExtra("WALLET","1");

											session.storePickuplat(from_latitude);
											session.storePickuplng(from_longitude);
											session.storeDropofflat(to_latitude);
											session.storeDropofflng(to_longitude);
											startActivityForResult(intent,19);
										}


									}

									else if(payment_type==null)
									{
										Utility.ShowAlert(getResources().getString(R.string.select_payment_type), getActivity());
									}
								}
							})
							.setNegativeButton(getResources().getString(R.string.no),new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id)
								{
									if(car_type_index==0)
									{
										nearestDrivers = type1NearestDrivers;
									}
									else if(car_type_index==1)
									{
										nearestDrivers = type2NearestDrivers;
									}
									else if(car_type_index==2)
									{
										nearestDrivers = type3NearestDrivers;
									}
									else if(car_type_index==3)
									{
										nearestDrivers = type4NearestDrivers;
									}
									//add these lines when drop location is mandatory
									//if((nearestDrivers.size()>0) && (isSetDropoffLocation) && (payment_type!=null ) && (from_latitude!=to_latitude) && (from_longitude!=to_longitude))
									if((payment_type!=null ) && (from_latitude!=to_latitude) && (from_longitude!=to_longitude))
									{
										if(laterBookingDate!=null )
										{
											sendLaterBooking("0");

										}
										else
										{
											SessionManager session = new SessionManager(getActivity());
											session.setBookingCancelled(false);

											AddLocation.setText("+");
											Intent intent = new Intent(getActivity(),RequestPickup.class);


											double lat =  Double.parseDouble(from_latitude);
											double lon = Double.parseDouble(from_longitude);
											String zipcode=getAddress(getActivity(),lat,lon);
											if(zipcode!=null)
											{

											}
											else
											{
												zipcode = "560024";
											}

											intent.putExtra("PICKUP_ADDRESS", pickupLocationAddress.getText().toString());
											intent.putExtra("FromLatitude", from_latitude);
											intent.putExtra("FromLongitude", from_longitude);
											if(mDROPOFF_ADDRESS!=null && !mDROPOFF_ADDRESS.isEmpty())
											{
												intent.putExtra("DROPOFF_ADDRESS", mDROPOFF_ADDRESS);
												intent.putExtra("ToLatitude", to_latitude);
												intent.putExtra("ToLongitude", to_longitude);
											}
											intent.putExtra("my_drivers", nearestDrivers);
											intent.putExtra("Zipcode", zipcode);
											intent.putExtra("Car_Type", current_master_type_id);
											intent.putExtra("PAYMENT_TYPE", payment_type);
											intent.putExtra("Later_Booking_Date", laterBookingDate);
											intent.putExtra("WALLET","0");
											intent.putExtra("SURGE",surgePrice);

											session.storePickuplat(from_latitude);
											session.storePickuplng(from_longitude);
											session.storeDropofflat(to_latitude);
											session.storeDropofflng(to_longitude);


											startActivityForResult(intent,19);
										}


									}
									else if(payment_type==null)
									{
										Utility.ShowAlert(getResources().getString(R.string.select_payment_type), getActivity());
									}
								}
							});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();
				}
			}
			else if(payment_type==null)
			{
				Utility.ShowAlert(getResources().getString(R.string.select_payment_type), getActivity());
			}
		}

		if(v.getId()==R.id.share_eta)
		{
			driver_parent.setVisibility(View.GONE);
			showPopupForShare();
			popup_share.showAtLocation(popupLayout,Gravity.CENTER, 0,0);
		}


		if(v.getId()==R.id.contact_driver)
		{
			SessionManager session = new SessionManager(mActivity);
			//popup_driver.dismiss();
			driver_parent.setVisibility(View.GONE);
			if(session.getDocPH()!=null)
			{
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"+session.getDocPH()));
				mActivity.startActivity(callIntent);
			}
			else
			{
				Toast.makeText(mActivity, getResources().getString(R.string.driver_phone_num_not_available), Toast.LENGTH_SHORT).show();
			}
		}

		if(v.getId()==R.id.cancel_trip)
		{
			driver_parent.setVisibility(View.GONE);
			CancelAppointment();
		}


		if(v.getId()==R.id.message_share)
		{
			popup_share.dismiss();
			String smsBody="Track my journey in " + getResources().getString(R.string.app_name) + "@" + session.getShareLink();

			Intent intentsms = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:"));
			intentsms.putExtra("sms_body", smsBody);
			startActivity(intentsms);
		}
		if(v.getId()==R.id.whatsapp_share)
		{
			popup_share.dismiss();

			PackageManager pm = getActivity().getPackageManager();
			try
			{

				String smsBody="Track my journey in " + getResources().getString(R.string.app_name) + "@" + session.getShareLink();
				Intent waIntent = new Intent(Intent.ACTION_SEND);
				waIntent.setType("text/plain");

				PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
				//Check if package exists or not. If not then code
				//in catch block will be called
				waIntent.setPackage("com.whatsapp");

				waIntent.putExtra(Intent.EXTRA_TEXT, smsBody);
				startActivity(Intent.createChooser(waIntent, "Share with"));

			} catch (NameNotFoundException e) {
				Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
			}

		}

		if(v.getId()==R.id.email_share)
		{
			popup_share.dismiss();

			File _sdCard = Utility.getSdCardPath();

			//creating directory
			File _picDir  = new File(_sdCard, getResources().getString(R.string.imagedire));
			Utility.deleteNon_EmptyDir(_picDir);

			//create file
			File imageFile= Utility.createFile(getResources().getString(R.string.imagedire), (getResources().getString(R.string.imagefilename) + "_twit" + ".jpg"));

			//get a bitmap object from this image
			Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.ic_launcher);
			OutputStream outStream = null;

			try {
				outStream = new FileOutputStream(imageFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);

			try {
				outStream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				outStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//Storing path in a variable "image_path"
			//String image_path=imageFile.getAbsolutePath();
			String smsBody="Track my journey in " + getResources().getString(R.string.app_name) + "@" + session.getShareLink();

			String mail="";
			Uri uri = Uri.fromFile(new File(imageFile.getAbsolutePath()));
			Intent email = new Intent(Intent.ACTION_SEND/*Intent.ACTION_VIEW*/);
			email.setType("message/rfc822");
			email.putExtra(Intent.EXTRA_SUBJECT,getResources().getString(R.string.app_name));
			email.putExtra(Intent.EXTRA_STREAM, uri);
			email.putExtra(Intent.EXTRA_EMAIL,mail);
			email.putExtra(Intent.EXTRA_TEXT, smsBody);
			startActivity(Intent.createChooser(email, "Choose an Email client :"));
		}
		if(v.getId()==R.id.cancel_share)
		{
			popup_share.dismiss();
		}
		if(v.getId()==R.id.driver_parent)
		{
			driver_parent.setVisibility(View.GONE);
			//popup_driver.dismiss();
		}


		if(v.getId()==R.id.expandedLayout)
		{

			expandedLayout.startAnimation(slideDown);

			Timer t = new Timer(false);
			t.schedule(new TimerTask() {
				@Override
				public void run() {
					getActivity().runOnUiThread(new Runnable()
					{
						public void run()
						{
							expandedLayout.setVisibility(View.GONE);
							compressedLayout.setVisibility(VISIBLE);
						}
					});
				}
			}, 500);

		}
		if(v.getId()==R.id.compressedLayout)
		{
			expandedLayout.setVisibility(VISIBLE);
			compressedLayout.setVisibility(View.GONE);
			expandedLayout.startAnimation(slideUp);
		}
	}

	private boolean validateTime(long current, long selected) {
		if(selected > current)
			return true;
		return false;
	}

	private void sendLaterBooking(String wallet) {
		final ProgressDialog dialogL= Utility.GetProcessDialogNew(getActivity(), getResources().getString(R.string.sendingRequest));
		if (dialogL!=null)
		{
			dialogL.show();
		}
		JSONObject jsonObject = new JSONObject();
		try {
			SessionManager session = new SessionManager(getActivity());
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Utility.printLog("sendLaterBooking Car_Type_Id = " + Car_Type_Id);
			jsonObject.put("ent_sess_token",session.getSessionToken());
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_wrk_type",current_master_type_id);
			jsonObject.put("ent_addr_line1",pickupLocationAddress.getText().toString());
			jsonObject.put("ent_lat",from_latitude);
			jsonObject.put("ent_long",from_longitude);
			Utility.printLog("akbar params" + jsonObject);

			if(mDROPOFF_ADDRESS!=null)
			{
				jsonObject.put("ent_drop_addr_line1",mDROPOFF_ADDRESS);
				jsonObject.put("ent_drop_addr_line2"," ");
				jsonObject.put("ent_drop_lat",to_latitude);
				jsonObject.put("ent_drop_long",to_longitude);
			}
			if(isCouponValid)
			{
				jsonObject.put("ent_coupon",mPromoCode);
				mPromoCode = null;
				promeCode.setText(getResources().getString(R.string.promo_code));
			}
			jsonObject.put("ent_payment_type",payment_type);
			jsonObject.put("ent_zipcode","560024");
			jsonObject.put("ent_later_dt",laterBookingDate);
			jsonObject.put("ent_date_time",curenttime);
			jsonObject.put("ent_wallet",wallet);
			jsonObject.put("ent_dri_email",nearestDrivers.get(0));
			Utility.printLog("The value of jsonObject later booking ::  " + jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "liveBooking", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				if (dialogL!=null)
				{
					dialogL.dismiss();

				}
				Utility.printLog("Success of getting later booking response=" + response);
				mPromoCode = null;
				parseSendLaterBooking(result);
			}
			@Override
			public void onError(String error) {
				Utility.printLog("on error for the login "+error);
				if (dialogL!=null)
				{
					dialogL.dismiss();
				}
				Utility.printLog("Success of getting later booking error=" + error);
			}
		});
	}


	private void parseSendLaterBooking(String liveBookingStatus)
	{
		Gson gson = new Gson();
		GetCarDetails response=gson.fromJson(liveBookingStatus,GetCarDetails.class);

		if(response!=null)
		{
			if(response.getErrFlag().equals("0"))
			{
				getActivity().getActionBar().show();
				googleMap.getUiSettings().setScrollGesturesEnabled(true);
				googleMap.getUiSettings().setZoomGesturesEnabled(true);
				//ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);
				isSetDropoffLocation=false;
				isBackPressed=false;
				isFareQuotePressed=false;
				isSetDropoffLocation=false;
				to_latitude=null;
				to_longitude=null;
				mDROPOFF_ADDRESS=null;
				Dropoff_Location_Address.setText("");
				new_dropoff_location_address.setText(getResources().getString(R.string.addDropOfLocation));
				show_address_relative.setVisibility(VISIBLE);
				Txt_Pickup.setVisibility(VISIBLE);
				pickup.setVisibility(VISIBLE);
				relativePickupLocation.setVisibility(View.INVISIBLE);
				Request_Pick_up_here.setVisibility(View.INVISIBLE);
				Fare_Quote.setVisibility(View.INVISIBLE);
				farePromoLayouy.setVisibility(View.INVISIBLE);
				Relative_Card_Info.setVisibility(View.INVISIBLE);
				Card_Info.setVisibility(View.INVISIBLE);
				Card_Image.setVisibility(View.INVISIBLE);
				Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
				Relative_Pickup_Navigation.setVisibility(View.INVISIBLE);
				all_types_layout.setVisibility(VISIBLE);
				allTypeLinearLayout.setVisibility(View.VISIBLE);
				scrollView.setVisibility(VISIBLE);
				now_later_layout.setVisibility(VISIBLE);

				laterBookingDate = null;//after completing the booking resetting the booking date
				Utility.ShowAlert(response.getErrMsg(), getActivity());
			}
			else
			{
				Utility.printLog("inside RESULT_OK true");
				getActivity().getActionBar().show();
				googleMap.getUiSettings().setScrollGesturesEnabled(true);
				googleMap.getUiSettings().setZoomGesturesEnabled(true);
				//ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);
				isSetDropoffLocation=false;
				isBackPressed=false;
				isFareQuotePressed=false;
				isSetDropoffLocation=false;
				to_latitude=null;
				to_longitude=null;
				mDROPOFF_ADDRESS=null;
				Dropoff_Location_Address.setText("");
				new_dropoff_location_address.setText(getResources().getString(R.string.addDropOfLocation));
				show_address_relative.setVisibility(VISIBLE);
				Txt_Pickup.setVisibility(VISIBLE);
				pickup.setVisibility(VISIBLE);
				relativePickupLocation.setVisibility(View.INVISIBLE);
				Request_Pick_up_here.setVisibility(View.INVISIBLE);
				Fare_Quote.setVisibility(View.INVISIBLE);
				farePromoLayouy.setVisibility(View.INVISIBLE);
				Relative_Card_Info.setVisibility(View.INVISIBLE);
				Card_Info.setVisibility(View.INVISIBLE);
				Card_Image.setVisibility(View.INVISIBLE);
				Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
				Relative_Pickup_Navigation.setVisibility(View.INVISIBLE);
				all_types_layout.setVisibility(VISIBLE);
				allTypeLinearLayout.setVisibility(View.VISIBLE);
				scrollView.setVisibility(VISIBLE);
				now_later_layout.setVisibility(VISIBLE);

				laterBookingDate = null;//after completing the booking resetting the booking date
				Utility.ShowAlert(response.getErrMsg(), getActivity());
			}
		}
		else
		{
			Utility.printLog("inside RESULT_OK true");
			getActivity().getActionBar().show();
			googleMap.getUiSettings().setScrollGesturesEnabled(true);
			googleMap.getUiSettings().setZoomGesturesEnabled(true);
			//ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);
			isSetDropoffLocation=false;
			isBackPressed=false;
			isFareQuotePressed=false;
			isSetDropoffLocation=false;
			to_latitude=null;
			to_longitude=null;
			mDROPOFF_ADDRESS=null;
			Dropoff_Location_Address.setText("");
			new_dropoff_location_address.setText(getResources().getString(R.string.addDropOfLocation));
			show_address_relative.setVisibility(VISIBLE);
			Txt_Pickup.setVisibility(VISIBLE);
			pickup.setVisibility(VISIBLE);
			relativePickupLocation.setVisibility(View.INVISIBLE);
			Request_Pick_up_here.setVisibility(View.INVISIBLE);
			Fare_Quote.setVisibility(View.INVISIBLE);
			farePromoLayouy.setVisibility(View.INVISIBLE);
			Relative_Card_Info.setVisibility(View.INVISIBLE);
			Card_Info.setVisibility(View.INVISIBLE);
			Card_Image.setVisibility(View.INVISIBLE);
			Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
			Relative_Pickup_Navigation.setVisibility(View.INVISIBLE);
			all_types_layout.setVisibility(VISIBLE);
			allTypeLinearLayout.setVisibility(View.VISIBLE);
			scrollView.setVisibility(VISIBLE);
			now_later_layout.setVisibility(VISIBLE);

			laterBookingDate = null;//after completing the booking resetting the booking date
		}
	}


	private void getFareEstimate() {
		final ProgressDialog dialogL=Utility.GetProcessDialog(getActivity());
		dialogL.show();
		JSONObject jsonObject = new JSONObject();

		try {
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Utility.printLog("dataandTime "+curenttime);
			jsonObject.put("ent_sess_token", session.getSessionToken());
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_type_id",current_master_type_id);
			jsonObject.put("ent_curr_lat",String.valueOf(currentLatitude));
			jsonObject.put("ent_curr_long",String.valueOf(currentLongitude));
			jsonObject.put("ent_from_lat",from_latitude);
			jsonObject.put("ent_from_long",from_longitude);
			jsonObject.put("ent_to_lat",to_latitude);
			jsonObject.put("ent_to_long",to_longitude);
			jsonObject.put("ent_date_time",curenttime);
			Utility.printLog("params to get fare "+jsonObject);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "fareCalculator", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				getFareResponse=result;
				Utility.printLog("fares estimate "+result);
				getUserInfo(dialogL);
			}
			@Override
			public void onError(String error) {
				dialogL.dismiss();
				Utility.printLog("error for estimate "+error);
				Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}

	private void getUserInfo(ProgressDialog dialogL) {
		dialogL.dismiss();
		Gson gson = new Gson();
		getFare = gson.fromJson(getFareResponse, FareCalculation.class);
		if (getFare != null) {
			if (getFare.getErrFlag().equals("0")) {
				textFare.setVisibility(VISIBLE);
				Fare_Quote.getLayoutParams().height= LayoutParams.WRAP_CONTENT;
				Fare_Quote.setText(getResources().getString(R.string.currencuSymbol)+" "+getFare.getFare());
			} else {
				Toast.makeText(getActivity(), getFare.getErrMsg(), Toast.LENGTH_SHORT).show();
			}
		} else {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			// set title
			alertDialogBuilder.setTitle(getResources().getString(R.string.error));
			// set dialog message
			alertDialogBuilder
					.setMessage(getResources().getString(R.string.server_error))
					.setCancelable(false)
					.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
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


	private void showPromoCodeAlert()
	{
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.promo_code_layout);

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int width = metrics.widthPixels;

		dialog.getWindow().setLayout((6 * width) / 7, LayoutParams.WRAP_CONTENT);

		final EditText text = (EditText) dialog.findViewById(R.id.et_promoCode);
		Button submit = (Button) dialog.findViewById(R.id.apply_promo);
		submit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(text.getText().toString().trim().equals(""))
				{
					Utility.ShowAlert(getResources().getString(R.string.pls_enter_promo_code), getActivity());
				}
				else
				{
					dialog.dismiss();
					ValidatePromoCode(text.getText().toString().trim());
				}
			}
		});

		Button cancel = (Button) dialog.findViewById(R.id.cancel_promo);
		cancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void  ValidatePromoCode(final String promoCode) {
		JSONObject jsonObject = new JSONObject();
		if (dialogL!=null)
		{
			dialogL.show();
		}
		try {
			SessionManager session=new SessionManager(mActivity);
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			jsonObject.put("ent_sess_token", session.getSessionToken() );
			jsonObject.put("ent_dev_id", session.getDeviceId());
			jsonObject.put("ent_coupon",promoCode);
			jsonObject.put("ent_lat",""+currentLatitude);
			jsonObject.put("ent_long",""+currentLongitude);
			jsonObject.put("ent_date_time",curenttime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "checkCoupon", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				getCancelResponse = result;
				JSONObject jsnResponse;
				try
				{
					jsnResponse = new JSONObject(result);
					String mErrNum = jsnResponse.getString("errFlag");
					String errFlag=jsnResponse.getString("errFlag");
					Utility.printLog("error for the promo "+mErrNum+" flag "+errFlag);

					if(mErrNum.equals("0"))
					{
						if (dialogL!=null)
						{
							dialogL.dismiss();
							dialogL = null;
						}

						mPromoCode = promoCode;
						isCouponValid = true;
						promeCode.setText(promoCode);
					}
					else
					{

						if (dialogL!=null)
						{
							dialogL.dismiss();
							dialogL = null;
						}

						Toast.makeText(mActivity,jsnResponse.getString("errMsg"), Toast.LENGTH_LONG).show();
						promeCode.setText(getResources().getString(R.string.promo_code));
						mPromoCode = null;
						isCouponValid = false;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onError(String error) {
				if (dialogL!=null)
				{
					dialogL.dismiss();
					dialogL = null;
				}

				mPromoCode = null;
				isCouponValid = false;
				Toast.makeText(mActivity, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();	}
		});
	}

	private void  CancelAppointment() {
		JSONObject jsonObject = new JSONObject();
		dialogL= Utility.GetProcessDialogNew(mActivity, getResources().getString(R.string.cancelling_trip));
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
			jsonObject.put("ent_appnt_dt",session.getAptDate());
			jsonObject.put("ent_dri_email",session.getDriverEmail());
			jsonObject.put("ent_date_time",curenttime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "cancelAppointment", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				JSONObject jsnResponse = null;
				getCancelResponse = result;
				getCancelInfo();
			}
			@Override
			public void onError(String error) {
				if(dialogL!=null)
				{
					dialogL.dismiss();
					dialogL = null;
				}

				Toast.makeText(mActivity, getResources().getString(R.string.server_error)+error, Toast.LENGTH_LONG).show();
			}
		});
	}

	private void getCancelInfo()
	{
		Gson gson = new Gson();
		CancelBooking = gson.fromJson(getCancelResponse, LiveBookingResponse.class);
		Utility.printLog("response for the cancel "+getCancelResponse);
		if(CancelBooking!=null)
		{
			if(CancelBooking.getErrFlag().equals("0"))
			{

				SessionManager session = new SessionManager(mActivity);
				session.setDriverOnWay(false);
				session.setBookingCancelled(true);
				Utility.printLog("Wallah set as false Homepage cancel 2");
				session.setDriverArrived(false);
				session.setTripBegin(false);
				session.setInvoiceRaised(false);
				session.storeAptDate(null);
				session.storeBookingId("0");
				//relative_now_later_status.setVisibility(View.VISIBLE);
				Toast.makeText(mActivity, CancelBooking.getErrMsg(), Toast.LENGTH_SHORT).show();
				if(dialogL!=null)
				{
					dialogL.dismiss();
					dialogL = null;
				}

				Intent i = new Intent(mActivity, MainActivityDrawer.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				mActivity.startActivity(i);
				mActivity.overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
			}
			else if(CancelBooking.getErrNum().equals("49"))
			{
				if(dialogL!=null)
				{
					dialogL.dismiss();
					dialogL = null;
				}


				SessionManager session = new SessionManager(mActivity);
				session.setDriverOnWay(false);
				session.setBookingCancelled(true);
				Utility.printLog("Wallah set as false Homepage cancel 2");
				session.setDriverArrived(false);
				session.setTripBegin(false);
				session.setInvoiceRaised(false);
				session.storeAptDate(null);
				session.storeBookingId("0");

				Toast.makeText(mActivity, CancelBooking.getErrMsg(), Toast.LENGTH_SHORT).show();

				Intent i = new Intent(mActivity, MainActivityDrawer.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				mActivity.startActivity(i);
				mActivity.overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
				//Toast.makeText(mActivity, CancelBooking.getErrMsg(), Toast.LENGTH_SHORT).show();
			}
		}

	}


	public String getAddress(Context ctx, double latitude, double longitude)
	{
		StringBuilder result = new StringBuilder();
		String Zipcode = null;
		try {
			Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
			List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
			if (addresses.size() > 0) {
				Address address = addresses.get(0);

				String locality=address.getLocality();
				String city=address.getCountryName();
				String region_code=address.getCountryCode();
				String zipcode=address.getPostalCode();
				Utility.printLog("zip=" + zipcode);

				result.append(locality+" ");
				result.append(city+" "+ region_code+" ");
				result.append(zipcode);
			}
		}
		catch(IOException e)
		{
		}

		return Zipcode;
	}

	private static void showSettingsAlert()
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);

		// Setting Dialog Title
		alertDialog.setTitle(mActivity.getResources().getString(R.string.gps_settings));

		// Setting Dialog Message
		alertDialog.setMessage(mActivity.getResources().getString(R.string.gps_alert_message));

		// On pressing Settings button
		alertDialog.setPositiveButton(mActivity.getResources().getString(R.string.settings), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog,int which)
			{
				dialog.dismiss();
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mActivity.startActivity(intent);
				mActivity.finish();
			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton(mActivity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				mActivity.finish();
			}
		});

	}


	/**
	 * Making the selected card as default card while booking
	 * @author embed-pc
	 *
	 */
	private void BackGroundChangeDefault() {
		final BookAppointmentResponse[] response = new BookAppointmentResponse[1];
		if (dialogL!=null) {
			dialogL.show();
		}
		JSONObject jsonObject = new JSONObject();
		try {
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Utility.printLog("dataandTime=" + curenttime);

			SessionManager session=new SessionManager(getActivity());
			jsonObject.put("ent_sess_token",session.getSessionToken());
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_cc_id",Default_Card_Id);
			jsonObject.put("ent_date_time",curenttime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "makeCardDefault", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				Gson gson = new Gson();
				response[0] =gson.fromJson(result,BookAppointmentResponse.class);
				if (dialogL !=null)
				{
					dialogL.dismiss();
					dialogL = null;

				}
			}
			@Override
			public void onError(String error) {
				if (dialogL !=null)
				{
					dialogL.dismiss();
				}
				Utility.printLog("on error for the login "+error);
				Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode==1)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				Utility.printLog("on sucess inside if");
				from_latitude=data.getStringExtra("FROM_LATITUDE");
				from_longitude=data.getStringExtra("FROM_LONGITUDE");
				to_latitude=data.getStringExtra("TO_LATITUDE");
				to_longitude=data.getStringExtra("TO_LONGITUDE");
				fare=data.getStringExtra("FARE");
				String from_searchAddress=data.getStringExtra("from_SearchAddress");
				String to_searchAddress=data.getStringExtra("to_SearchAddress");
				pickupLocationAddress.setText(from_searchAddress);
				Dropoff_Location_Address.setText(to_searchAddress);
				new_dropoff_location_address.setText(to_searchAddress);
				textFare.setVisibility(VISIBLE);
				Fare_Quote.getLayoutParams().height= LayoutParams.WRAP_CONTENT;
				Fare_Quote.setText(fare);

				LatLng latLng = new LatLng(Double.parseDouble(to_latitude), Double.parseDouble(to_longitude));
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
				Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
			}
			else
			{
				Utility.printLog("on sucess inside else");
			}
		}

		if(requestCode==2)
		{
			if(resultCode==Activity.RESULT_OK)
			{
				Utility.printLog("inside on activty result 2");
				googleMap.clear();
				_publish(session.getServerChannel(),"{a:1,pid:"+session.getLoginId()+",lt:"+currentLatitude+",lg:"+currentLongitude+",chn:"+session.getChannelName()+",st:3,tp:"+current_master_type_id+"}");
			}
		}

		if(requestCode==16)
		{
			{
				if(resultCode == Activity.RESULT_OK)
				{
					dropOffSet=true;
					cameraMoved=true;
					Utility.printLog("flag isFareQuotePressed "+isFareQuotePressed);
					if(isFareQuotePressed)
					{
						//isSetDropoffLocation=true;
						String latitudeString=data.getStringExtra("LATITUDE_SEARCH");
						String logitudeString=data.getStringExtra("LONGITUDE_SEARCH");
						String searchAddress=data.getStringExtra("SearchAddress");
						formattedAddress = data.getStringExtra("SearchAddress");
						locationName = 	data.getStringExtra("ADDRESS_NAME");

						to_latitude=latitudeString;
						to_longitude=logitudeString;
						Utility.printLog("onActivityResult latitudeString. if ..."+latitudeString+"...logitudeString..."+logitudeString);
						if(searchAddress!=null)
						{
							isSetDropoffLocation=true;
							if(locationName!=null && !locationName.isEmpty())
							{
								mDROPOFF_ADDRESS=locationName+" "+searchAddress;
								Dropoff_Location_Address.setText(locationName+" "+searchAddress);
								new_dropoff_location_address.setText(locationName+" "+searchAddress);
							}
							else
							{
								mDROPOFF_ADDRESS=searchAddress;
								Dropoff_Location_Address.setText(searchAddress);
								new_dropoff_location_address.setText(searchAddress);
							}
						}
						//Placing marker for current location
						Intent intent = new Intent(getActivity(),FareQuoteActivity.class);
						Utility.printLog("fare_quote from_latitude="+from_latitude+" from_longitude="+from_longitude+" to_latitude="+to_latitude+" to_longitude="+to_longitude);
						Utility.printLog("fare_quote pick_up add="+pick_up+" drop_off="+mDROPOFF_ADDRESS);
						Utility.printLog("fare_quote id="+Car_Type_Id+" "+Car_Type_Id);
						session.storeDropofflat(to_latitude);
						session.storeDropofflng(to_longitude);
						Utility.printLog("fare_quote from session = "+session.getPickuplat()+" "+session.getPickuplng()+" "+session.getDropofflat()
								+" "+session.getDropofflng());
						intent.putExtra("PICKUP_ADDRESS", mPICKUP_ADDRESS);
						intent.putExtra("DROPOFF_ADDRESS", mDROPOFF_ADDRESS);
						intent.putExtra("FromLatitude", from_latitude);
						intent.putExtra("FromLongitude", from_longitude);
						intent.putExtra("ToLatitude", to_latitude);
						intent.putExtra("ToLongitude", to_longitude);
						intent.putExtra("TypeId", current_master_type_id);
						//startActivity(intent);
						startActivityForResult(intent, 1);
						getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);

						//farecotemethod(mPICKUP_ADDRESS, mDROPOFF_ADDRESS, from_latitude, from_longitude, to_latitude, to_longitude, Car_Type_Id);
					}
					else
					{
						isSetDropoffLocation = true;
						String latitudeString = data.getStringExtra("LATITUDE_SEARCH");
						String logitudeString = data.getStringExtra("LONGITUDE_SEARCH");
						String searchAddress = data.getStringExtra("SearchAddress");
						to_latitude = latitudeString;
						to_longitude = logitudeString;
						Utility.printLog("onActivityResult latitudeString. else ..." + latitudeString + "...logitudeString..." + logitudeString);
						LatLng latLng = new LatLng(Double.parseDouble(to_latitude), Double.parseDouble(to_longitude));
						googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
						if (searchAddress != null) {
							AddLocation.setText("-");
							//Relative_Dropoff_Location.setVisibility(View.VISIBLE);
							isSetDropoffLocation = true;
							mDROPOFF_ADDRESS = searchAddress;
							Dropoff_Location_Address.setText(searchAddress);
							new_dropoff_location_address.setText(searchAddress);

							//Akbar for updating the new address after the booking accepted by driver

							if (Utility.isNetworkAvailable(getActivity())) {
								//new BackgroundUpdateAddress().execute();
								farecotemethod(mPICKUP_ADDRESS, mDROPOFF_ADDRESS, from_latitude, from_longitude, to_latitude, to_longitude, Car_Type_Id);

							}

						}
					}
				}
			}
		}
		if(requestCode==17)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				int select_card_position=data.getIntExtra("position", 0);
				String card_no=data.getStringExtra("NUMB");
				Bitmap bitmap = (Bitmap)data.getParcelableExtra("Image");
				Default_Card_Id = data.getStringExtra("ID");
				Card_Image.setImageBitmap(bitmap);
				cardChecked=select_card_position;
				payment_type="1";
				Utility.printLog("set checked=" + cardChecked);
				Card_Info.setText("**** **** **** "+card_no);
				BackGroundChangeDefault();
			}
		}

		if(requestCode==18)
		{

			if(data!=null)
			{
				VariableConstants.isPubnubCalled=false;
				IsreturnFromSearch=true;
				String latitudeString=data.getStringExtra("LATITUDE_SEARCH");
				String logitudeString=data.getStringExtra("LONGITUDE_SEARCH");

				mPICKUP_ADDRESS=data.getStringExtra("SearchAddress");

				formattedAddress = data.getStringExtra("SearchAddress");
				locationName = 	data.getStringExtra("ADDRESS_NAME");

				if(mPICKUP_ADDRESS!=null)
				{
					current_address.setText(mPICKUP_ADDRESS);
					pickupLocationAddress.setText(mPICKUP_ADDRESS);
				}

				from_latitude=latitudeString;
				from_longitude=logitudeString;
				//Placing marker for current location
				searchlocatinlat= Double.parseDouble(latitudeString);
				searchlocatinlng = Double.parseDouble(logitudeString);

				currentLatitude =Double.parseDouble(latitudeString);
				currentLongitude =Double.parseDouble(logitudeString);
				LatLng latLng = new LatLng(searchlocatinlat, searchlocatinlng);
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
			}
		}

		if(requestCode==19)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				Utility.printLog("inside RESULT_OK true");
				getActivity().getActionBar().show();
				isSetDropoffLocation=false;
				isBackPressed=false;
				isFareQuotePressed=false;
				isSetDropoffLocation=false;
				to_latitude=null;
				to_longitude=null;
				mDROPOFF_ADDRESS=null;
				new_dropoff_location_address.setText(getResources().getString(R.string.addDropOfLocation));
				Dropoff_Location_Address.setText("");
				show_address_relative.setVisibility(VISIBLE);
				Txt_Pickup.setVisibility(VISIBLE);
				pickup.setVisibility(VISIBLE);
				relativePickupLocation.setVisibility(View.INVISIBLE);
				Request_Pick_up_here.setVisibility(View.INVISIBLE);
				Fare_Quote.setVisibility(View.INVISIBLE);
				farePromoLayouy.setVisibility(View.INVISIBLE);
				Relative_Card_Info.setVisibility(View.INVISIBLE);
				Card_Info.setVisibility(View.INVISIBLE);
				Card_Image.setVisibility(View.INVISIBLE);
				Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
				Relative_Pickup_Navigation.setVisibility(View.INVISIBLE);
				all_types_layout.setVisibility(View.GONE);
				allTypeLinearLayout.setVisibility(View.GONE);
				scrollView.setVisibility(View.GONE);
				nearestDrivers.clear();
				VariableConstants.CONFIRMATION_CALLED=false;
				googleMap.getUiSettings().setScrollGesturesEnabled(true);
				googleMap.getUiSettings().setZoomGesturesEnabled(true);
				now_later_layout.setVisibility(VISIBLE);
			}
			else
			{
				Utility.printLog("inside RESULT_OK false");
				getActivity().getActionBar().show();
				googleMap.getUiSettings().setScrollGesturesEnabled(true);
				VariableConstants.CONFIRMATION_CALLED=false;
				googleMap.getUiSettings().setZoomGesturesEnabled(true);
				isSetDropoffLocation=false;
				isBackPressed=false;
				isFareQuotePressed=false;
				to_latitude=null;
				to_longitude=null;
				mDROPOFF_ADDRESS=null;
				Dropoff_Location_Address.setText("");
				new_dropoff_location_address.setText(getResources().getString(R.string.addDropOfLocation));
				show_address_relative.setVisibility(VISIBLE);
				Txt_Pickup.setVisibility(VISIBLE);
				pickup.setVisibility(VISIBLE);
				relativePickupLocation.setVisibility(View.INVISIBLE);
				Request_Pick_up_here.setVisibility(View.INVISIBLE);
				Fare_Quote.setVisibility(View.INVISIBLE);
				farePromoLayouy.setVisibility(View.INVISIBLE);
				Relative_Card_Info.setVisibility(View.INVISIBLE);
				Card_Info.setVisibility(View.INVISIBLE);
				Card_Image.setVisibility(View.INVISIBLE);
				Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
				Relative_Pickup_Navigation.setVisibility(View.INVISIBLE);
				all_types_layout.setVisibility(VISIBLE);
				allTypeLinearLayout.setVisibility(VISIBLE);
				scrollView.setVisibility(VISIBLE);
				nearestDrivers.clear();
				now_later_layout.setVisibility(VISIBLE);
			}
		}


		if(requestCode==20)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				Utility.printLog("inside RESULT_OK true");
				getActivity().getActionBar().show();
				isSetDropoffLocation=false;
				isBackPressed=false;
				isFareQuotePressed=false;
				isSetDropoffLocation=false;
				to_latitude=null;
				to_longitude=null;
				mDROPOFF_ADDRESS=null;
				Dropoff_Location_Address.setText("");
				new_dropoff_location_address.setText(getResources().getString(R.string.addDropOfLocation));
				show_address_relative.setVisibility(VISIBLE);
				Txt_Pickup.setVisibility(VISIBLE);
				pickup.setVisibility(VISIBLE);
				relativePickupLocation.setVisibility(View.INVISIBLE);
				Request_Pick_up_here.setVisibility(View.INVISIBLE);
				Fare_Quote.setVisibility(View.INVISIBLE);
				farePromoLayouy.setVisibility(View.INVISIBLE);
				Relative_Card_Info.setVisibility(View.INVISIBLE);
				Card_Info.setVisibility(View.INVISIBLE);
				Card_Image.setVisibility(View.INVISIBLE);
				Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
				Relative_Pickup_Navigation.setVisibility(View.INVISIBLE);
				all_types_layout.setVisibility(VISIBLE);
				allTypeLinearLayout.setVisibility(VISIBLE);
				scrollView.setVisibility(VISIBLE);
				now_later_layout.setVisibility(VISIBLE);

				laterBookingDate = null;//after completing the booking resetting the booking date
				Utility.ShowAlert(data.getStringExtra("LaterMsg"), getActivity());
			}
			else
			{
				Utility.printLog("inside RESULT_OK true");
				getActivity().getActionBar().show();
				//ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);
				isSetDropoffLocation=false;
				isBackPressed=false;
				isFareQuotePressed=false;
				isSetDropoffLocation=false;
				to_latitude=null;
				to_longitude=null;
				mDROPOFF_ADDRESS=null;
				Dropoff_Location_Address.setText("");
				new_dropoff_location_address.setText(getResources().getString(R.string.addDropOfLocation));
				show_address_relative.setVisibility(VISIBLE);
				Txt_Pickup.setVisibility(VISIBLE);
				pickup.setVisibility(VISIBLE);
				relativePickupLocation.setVisibility(View.INVISIBLE);
				Request_Pick_up_here.setVisibility(View.INVISIBLE);
				Fare_Quote.setVisibility(View.INVISIBLE);
				farePromoLayouy.setVisibility(View.INVISIBLE);
				Relative_Card_Info.setVisibility(View.INVISIBLE);
				Card_Info.setVisibility(View.INVISIBLE);
				Card_Image.setVisibility(View.INVISIBLE);
				Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
				Relative_Pickup_Navigation.setVisibility(View.INVISIBLE);
				all_types_layout.setVisibility(VISIBLE);
				now_later_layout.setVisibility(VISIBLE);
				scrollView.setVisibility(VISIBLE);
				allTypeLinearLayout.setVisibility(View.VISIBLE);
				laterBookingDate = null;//after completing the booking resetting the booking date

				//Utility.ShowAlert(data.getStringExtra("LaterMsg"), getActivity());

			}
		}

		if(requestCode==30)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				if(isFareQuotePressed) {
					//isSetDropoffLocation=true;
					String latitudeString = data.getStringExtra("LATITUDE_SEARCH");
					String logitudeString = data.getStringExtra("LONGITUDE_SEARCH");
					String searchAddress = data.getStringExtra("SearchAddress");
					formattedAddress = data.getStringExtra("SearchAddress");
					locationName = data.getStringExtra("ADDRESS_NAME");

					to_latitude = latitudeString;
					to_longitude = logitudeString;
					Utility.printLog("onActivityResult latitudeString...." + latitudeString + "...logitudeString..." + logitudeString);
					if (searchAddress != null) {
						isSetDropoffLocation = true;
						if (locationName != null && !locationName.isEmpty()) {
							mDROPOFF_ADDRESS = locationName + " " + searchAddress;
							Dropoff_Location_Address.setText(locationName + " " + searchAddress);
							new_dropoff_location_address.setText(locationName + " " + searchAddress);
						} else {
							mDROPOFF_ADDRESS = searchAddress;
							Dropoff_Location_Address.setText(searchAddress);
							new_dropoff_location_address.setText(searchAddress);

						}
					}

				}
				else
				{
					isSetDropoffLocation = true;
					String latitudeString = data.getStringExtra("LATITUDE_SEARCH");
					String logitudeString = data.getStringExtra("LONGITUDE_SEARCH");
					String searchAddress = data.getStringExtra("SearchAddress");
					to_latitude = latitudeString;
					to_longitude = logitudeString;
					Utility.printLog("onActivityResult latitudeString...." + latitudeString + "...logitudeString..." + logitudeString);
					if (searchAddress != null) {
						AddLocation.setText("-");
						//Relative_Dropoff_Location.setVisibility(View.VISIBLE);
						isSetDropoffLocation = true;
						mDROPOFF_ADDRESS = searchAddress;
						Dropoff_Location_Address.setText(searchAddress);
						new_dropoff_location_address.setText(searchAddress);

						//Akbar for updating the new address after the booking accepted by driver

						if (Utility.isNetworkAvailable(getActivity())) {
							BackgroundUpdateAddress();
							//farecotemethod(mPICKUP_ADDRESS, mDROPOFF_ADDRESS, from_latitude, from_longitude, to_latitude, to_longitude, Car_Type_Id);
						}

					}
				}
			}
		}

	}

	private void getAppointmentDetails_Volley(final String date)
	{

		Utility.printLog("params to service "+date);

		Utility utility=new Utility();
		String curenttime=utility.getCurrentGmtTime();

		final RequestBody requestBody=new FormEncodingBuilder()

				.add("ent_sess_token", session.getSessionToken())
				.add("ent_dev_id", session.getDeviceId())
				.add("ent_email", session.getDriverEmail())
				.add("ent_appnt_dt", date)
				.add("ent_user_type","2")
				.add("ent_date_time", curenttime)
				.build();
		OkHttpRequest.doJsonRequest(VariableConstants.BASE_URL+"getAppointmentDetails", requestBody, new OkHttpRequest.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				Utility.printLog("VALETE_CHECK_OUT result " + result);
				if(isAdded())
				{
					fetchAptData(result);
				}
			}

			@Override
			public void onError(String error) {
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_connection_fail), Toast.LENGTH_SHORT).show();
			}
		});


	}

	private void fetchAptData(String jsonResponse)
	{
		int height;
		int width;

		width = getResources().getDrawable(R.drawable.invoice_profile_default_image).getMinimumWidth();
		height = getResources().getDrawable(R.drawable.invoice_profile_default_image).getMinimumHeight();

		Utility.printLog("Appointments details inside fetchAptData");

		try
		{
			Gson gson = new Gson();
			appointmentResponse=gson.fromJson(jsonResponse,GetAppointmentDetails.class);
		}
		catch (JsonSyntaxException e)
		{
			Utility.printLog("syntax exception "+e);
		}


		Utility.printLog("Response in getApppointment details " + jsonResponse);

		if(appointmentResponse!=null)
		{
			if(appointmentResponse.getErrFlag().equals("0"))
			{
				Utility.printLog("Appointments details success response 0");

				session.setPickUpAddress(appointmentResponse.getAddr1());
				session.setDropAddress(appointmentResponse.getDropAddr1());
				session.storeDocName(appointmentResponse.getfName()+" "+appointmentResponse.getlName());
				session.storeDocPic(VariableConstants.IMAGE_BASE_URL+appointmentResponse.getpPic());
				//session.storeAptDate(appointmentResponse.getApptDt());
				session.storeDocPH(appointmentResponse.getMobile());
				session.storeDocDist(appointmentResponse.getDis());
				session.storeBookingId(appointmentResponse.getBid());
				session.storeCurrentAptChannel(appointmentResponse.getChn());
				session.storeDriverEmail(appointmentResponse.getEmail());
				session.storePlateNO(appointmentResponse.getPlateNo());
				session.storeCarModel(appointmentResponse.getModel());
				session.storeShareLink(appointmentResponse.getShare());
				session.storeDriverRating(appointmentResponse.getR());
				session.setCarImage(appointmentResponse.getCarImage());

				if(driver_parent.getVisibility() == VISIBLE)
				{
					driver_parent.setVisibility(View.GONE);
				}

				Utility.printLog("Appointments details Response: getCurrentAptChannel=" + session.getCurrentAptChannel() + "  getBookingId=" + session.getBookingId());

				String[] subscribed_channels = pubnub.getSubscribedChannelsArray();
				Utility.printLog("Appointments details subscribed_channels my channel=" + session.getChannelName());
				ArrayList<String> unsubscribeChannels= new ArrayList<String>();

				boolean isCurrentDriverChannelSubscribed=false;
				boolean isMyChannelSubscribed=false;
				for(int i=0;i<subscribed_channels.length;i++)
				{
					Utility.printLog("Appointments details subscribed_channels at" + i + " " + subscribed_channels[i]);

					if(subscribed_channels[i].equals(session.getCurrentAptChannel()))
					{
						isCurrentDriverChannelSubscribed=true;
					}
					else if(subscribed_channels[i].equals(session.getChannelName()))
					{
						isMyChannelSubscribed=true;
					}
					else
					{
						unsubscribeChannels.add(subscribed_channels[i]);
					}
				}

				//unsubscribing all previously subscribed channels except current apt channel
				Utility.printLog("Appointments details BackgroundSubscribeMyChannel unsubscribeChannels size=" + unsubscribeChannels.size());
				if(unsubscribeChannels.size()>0)
				{
					Utility.printLog("Appointments details channels unsubscribeChannels channel list size=" + unsubscribeChannels.size());
					String[] new_un_sub_channels=new String[unsubscribeChannels.size()];
					new_un_sub_channels=unsubscribeChannels.toArray(new_un_sub_channels);
					new BackgroundUnSubscribeChannels().execute(new_un_sub_channels);
				}
				Utility.printLog("Appointments details test BackgroundSubscribeMyChannel status =" + isMyChannelSubscribed);
				if(appointmentResponse.getStatCode().equals("9"))
				{
					String[] subscribed_channels1 = pubnub.getSubscribedChannelsArray();
					Utility.printLog("Appointments details subscribed_channels my channel=" + session.getChannelName());
					ArrayList<String> unsubscribeChannels1= new ArrayList<String>();
					for(int i=0;i<subscribed_channels1.length;i++)
					{
						Utility.printLog("Appointments details subscribed_channels at status 9" + i + " " + subscribed_channels1[i]);


						unsubscribeChannels1.add(subscribed_channels1[i]);

						if(unsubscribeChannels1.size()>0)
						{
							Utility.printLog("Appointments details channels unsubscribeChannels channel list size status 9=" + unsubscribeChannels.size());
							String[] new_un_sub_channels=new String[unsubscribeChannels1.size()];
							new_un_sub_channels=unsubscribeChannels1.toArray(new_un_sub_channels);
							new BackgroundUnSubscribeChannels().execute(new_un_sub_channels);
						}

					}
				}
				else
				{
					if(!isMyChannelSubscribed)
					{
						Utility.printLog("Appointments details BackgroundSubscribeMyChannel");
						new BackgroundSubscribeMyChannel().execute();
					}
					Utility.printLog("Appointments details test isCurrentDriverChannelSubscribed status =" + isCurrentDriverChannelSubscribed);

					if(!session.isInvoiceRaised())
					{
						if(!isCurrentDriverChannelSubscribed)
						{
							Utility.printLog("Appointments details isCurrentDriverChannelSubscribed");
							String[] my_driver_channel=new String[1];
							my_driver_channel[0]=session.getCurrentAptChannel();
							Utility.printLog("Appointments details channels null current apt channel not present in old channels" + my_driver_channel[0]);
							new BackgroundSubscribeChannels().execute(my_driver_channel);
						}
					}

				}

				//if channel is not null then subscribing only for that channel to get driver on way the response
				if (appointmentResponse.getStatCode().equals("6"))
				{
					Utility.printLog("Appointments details inside isDriverOnWay");
					//marker_map.clear();
					marker_map_on_the_way.clear();
					Utility.printLog("marker_map_on_the_way 1");
					marker_map_arrived.clear();
					googleMap.clear();
					googleMap.getUiSettings().setScrollGesturesEnabled(true);
					googleMap.getUiSettings().setZoomGesturesEnabled(true);
					if(appointmentResponse.getDropAddr1().equals(""))
					{
						new_dropoff_location_address.setText(getResources().getString(R.string.addDropOfLocation));
					}
					else
					{
						new_dropoff_location_address.setText(appointmentResponse.getDropAddr1());
					}

					String[] params=new String[4];

					if(eta_longitude!=null && eta_latitude !=null)
					{
						params[0]=session.getPickuplat();
						params[1]=session.getPickuplng();
						params[2]=eta_latitude;
						params[3]=eta_longitude;
					}
					else
					{
						params[0]=session.getPickuplat();
						params[1]=session.getPickuplng();
						params[2]=session.getLat_DOW();
						params[3]=session.getLon_DOW();
					}


					new getEtaWIthTimer().execute(params);


					//Plot the path of the driver
					if(session.getLat_DOW()!=null && session.getLon_DOW()!=null)
					{
						LatLng latLng=new LatLng(Double.parseDouble(session.getLat_DOW()),Double.parseDouble(session.getLon_DOW()));

						//Showing the driver location in Google Map & Zoom in the Google Map
						googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));


						double width1=dblArray[0]*50;
						double height1=dblArray[1]*80;

						Utility.printLog("selected image " + session.getSelectedImage() + " latlong " + latLng);
						try
						{
							if(!session.getSelectedImage().equals(""))
							{
								driverMarker = new PicassoMarker(googleMap.addMarker(new MarkerOptions().position(latLng)),getActivity());
								//Picasso.with(getActivity()).load(VariableConstants.IMAGE_URL+session.getSelectedImage()).resize((int)width1,(int)height1).into(driverMarker);
								Picasso.with(getActivity()).load(VariableConstants.IMAGE_URL+session.getSelectedImage()).resize((int)width1,(int)height1).into(driverMarker);

							}
						}
						catch (Exception e)
						{

						}


						marker_map_on_the_way.put(session.getCurrentAptChannel(), driverMarker.getmMarker());


					}

					if(session.getPickuplat()!=null && session.getPickuplng()!=null)
					{

						LatLng latLng=new LatLng(Double.parseDouble(session.getPickuplat()),Double.parseDouble(session.getPickuplng()));
						picupmarker = googleMap.addMarker(new MarkerOptions().position(latLng)
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_markers_pickup))
								.rotation(0)
								.flat(true));

					}

					Utility.printLog("marker_map_on_the_way get app details=" + session.getCurrentAptChannel());
					Utility.printLog("marker_map_on_the_way get app details status=" + marker_map_on_the_way.containsKey(session.getCurrentAptChannel()));

					if(session.getCarImage()!=null && !session.getCarImage().equalsIgnoreCase(""))
					{
						drivehicalimg.setTag(getTarget_api(drivehicalimg));
						Picasso.with(getActivity())
								.load(VariableConstants.IMAGE_URL+session.getCarImage())
								.transform(new CircleTransform())
								.resize(width,height)
								.into((Target) drivehicalimg.getTag());

						drivehicalimg_new.setTag(getTarget_api(drivehicalimg_new));
						Picasso.with(getActivity())
								.load(VariableConstants.IMAGE_URL+session.getCarImage())
								.transform(new CircleTransform())
								.resize(width,height)
								.into((Target) drivehicalimg_new.getTag());



					}

					if(session.getDocPic() != null && !session.getDocPic().equalsIgnoreCase("null") && !session.getDocPic().equalsIgnoreCase(""))
					{


						Driver_Profile_Pic.setTag(getTarget_api(Driver_Profile_Pic));
						Picasso.with(getActivity()).load(session.getDocPic())
								//.centerCrop()
								.transform(new CircleTransform())
								.placeholder(R.drawable.invoice_profile_default_image)
								.resize(width,height)
								.into((Target) Driver_Profile_Pic.getTag());

						driver_profile_pic_new.setTag(getTarget_api(driver_profile_pic_new));
						Picasso.with(getActivity()).load(session.getDocPic())
								.transform(new CircleTransform())
								.placeholder(R.drawable.invoice_profile_default_image)
								.resize(width,height)
								.into((Target) driver_profile_pic_new.getTag());


					}
					else
					{
						Driver_Profile_Pic.setImageResource(R.drawable.ic_launcher);
					}

					pickupaddresstv.setText(session.getPickUpAddress());

					Driver_Name.setText(appointmentResponse.getModel());
					driver_name_new.setText(appointmentResponse.getModel());
					drivsnd_new.setText(session.getDocName());
					drivsnd.setText(session.getDocName());

					Rl_distance_time.setVisibility(VISIBLE);
					show_address_relative.setVisibility(View.INVISIBLE);
					pickup.setVisibility(View.INVISIBLE);
					Txt_Pickup.setVisibility(View.INVISIBLE);
					Mid_Pointer.setVisibility(View.INVISIBLE);
					all_types_layout.setVisibility(View.INVISIBLE);
					allTypeLinearLayout.setVisibility(View.INVISIBLE);
					scrollView.setVisibility(View.GONE);
					Driver_on_the_way_txt.setVisibility(VISIBLE);

					Driver_on_the_way_txt.setText(getResources().getString(R.string.driver_on_the_way)+": " + session.getBookingId());
					if(session.getDriverRating()!=null)
					{
						drivvehicalrating_new.setText(session.getDriverRating());
						drivvehicalrating.setText(session.getDriverRating());

					}

				}
				else if (appointmentResponse.getStatCode().equals("7"))
				{
					Utility.printLog("Appointments details inside isDriverOnArrived");

					//marker_map.clear();
					marker_map_on_the_way.clear();
					Utility.printLog("marker_map_on_the_way 15");
					marker_map_arrived.clear();
					googleMap.clear();
					googleMap.getUiSettings().setScrollGesturesEnabled(true);
					googleMap.getUiSettings().setZoomGesturesEnabled(true);

					if(appointmentResponse.getDropAddr1().equals(""))
					{
						new_dropoff_location_address.setText(getResources().getString(R.string.addDropOfLocation));

					}
					else
					{
						new_dropoff_location_address.setText(appointmentResponse.getDropAddr1());
					}

					String[] params=new String[4];

					params[0]=session.getPickuplat();
					params[1]=session.getPickuplng();
					params[2]=session.getDropofflat();
					params[3]=session.getDropofflng();

					new getEtaWIthTimer().execute(params);


					if(session.getLat_DOW()!=null && session.getLon_DOW()!=null)
					{
						LatLng latLng=new LatLng(Double.parseDouble(session.getLat_DOW()),Double.parseDouble(session.getLon_DOW()));
/*
						driver_arrived = googleMap.addMarker(new MarkerOptions().position(latLng));
*/
						double width1=dblArray[0]*50;
						double height1=dblArray[1]*80;

						if(!session.getSelectedImage().equals(""))
						{
							driverMarker = new PicassoMarker(googleMap.addMarker(new MarkerOptions().position(latLng)),getActivity());
							Picasso.with(getActivity()).load(VariableConstants.IMAGE_URL + session.getSelectedImage()).resize((int) width1, (int) height1).into(driverMarker);

						}

						/**
						 * to show the image on map on the marker
						 */

						//driver_arrived.setTitle("Driver reached");
						googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));

						marker_map_arrived.put(session.getCurrentAptChannel(), driverMarker.getmMarker());
					}
					if(session.getPickuplat()!=null && session.getPickuplng()!=null)
					{

						LatLng latLng=new LatLng(Double.parseDouble(session.getPickuplat()),Double.parseDouble(session.getPickuplng()));
						//googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17.0f));
						picupmarker = googleMap.addMarker(new MarkerOptions().position(latLng)
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_markers_pickup))
								.rotation(0)
								.flat(true));

					}

					//Plot the polyline from pickup location to destination location
					if(session.getDropofflat()!=null && session.getDropofflng()!=null)
					{

						LatLng latLng=new LatLng(Double.parseDouble(session.getDropofflat()),Double.parseDouble(session.getDropofflng()));
						//googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17.0f));
						picupmarker = googleMap.addMarker(new MarkerOptions().position(latLng)
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_markers_dropoff))
								.rotation(0)
								.flat(true));


					}

					if(session.getCarImage()!=null && !session.getCarImage().equalsIgnoreCase(""))
					{
						drivehicalimg.setTag(getTarget_api(drivehicalimg));
						Picasso.with(getActivity())
								.load(VariableConstants.IMAGE_URL+session.getCarImage())
								.transform(new CircleTransform())
								.resize(width, height)
								.into((Target) drivehicalimg.getTag());

						drivehicalimg_new.setTag(getTarget_api(drivehicalimg_new));
						Picasso.with(getActivity())
								.load(VariableConstants.IMAGE_URL+session.getCarImage())
								.transform(new CircleTransform())
								.resize(width,height)
								.into((Target) drivehicalimg_new.getTag());


					}

					if(session.getDocPic() != null && !session.getDocPic().equalsIgnoreCase("null") && !session.getDocPic().equalsIgnoreCase(""))
					{
						Driver_Profile_Pic.setTag(getTarget_api(Driver_Profile_Pic));
						Picasso.with(getActivity()).load(session.getDocPic())
								//.centerCrop()
								.transform(new CircleTransform())
								.placeholder(R.drawable.invoice_profile_default_image)
								.resize(width,height)
								.into((Target) Driver_Profile_Pic.getTag());

						driver_profile_pic_new.setTag(getTarget_api(driver_profile_pic_new));
						Picasso.with(getActivity()).load(session.getDocPic())
								.transform(new CircleTransform())
								.placeholder(R.drawable.invoice_profile_default_image)
								.resize(width,height)
								.into((Target) driver_profile_pic_new.getTag());

					}
					else
					{
						Driver_Profile_Pic.setImageResource(R.drawable.ic_launcher);
					}

					pickupaddresstv.setText(session.getPickUpAddress());


					Driver_Name.setText(appointmentResponse.getModel());
					driver_name_new.setText(appointmentResponse.getModel());
					drivsnd_new.setText(session.getDocName());
					drivsnd.setText(session.getDocName());

					//Driver_Confirmation.setVisibility(View.VISIBLE);
					Rl_distance_time.setVisibility(VISIBLE);

					show_address_relative.setVisibility(View.INVISIBLE);
					pickup.setVisibility(View.INVISIBLE);
					Txt_Pickup.setVisibility(View.INVISIBLE);
					Mid_Pointer.setVisibility(View.INVISIBLE);
					all_types_layout.setVisibility(View.INVISIBLE);
					allTypeLinearLayout.setVisibility(View.INVISIBLE);
					scrollView.setVisibility(View.GONE);
					now_later_layout.setVisibility(View.INVISIBLE);
					Driver_on_the_way_txt.setVisibility(VISIBLE);
					Driver_on_the_way_txt.setText(getResources().getString(R.string.driverreached)+": " + session.getBookingId());
					//relative_now_later_status.setVisibility(View.GONE);

					if(session.getDriverRating()!=null)
					{
						drivvehicalrating_new.setText(session.getDriverRating());
						drivvehicalrating.setText(session.getDriverRating());

					}


				}
				else if (appointmentResponse.getStatCode().equals("8"))
				{
					Utility.printLog("Appointments details inside isTripBegin");
					//marker_map.clear();

					marker_map_on_the_way.clear();
					Utility.printLog("marker_map_on_the_way 7");
					marker_map_arrived.clear();
					googleMap.clear();

					//getETAWithTimer(8);
					googleMap.getUiSettings().setScrollGesturesEnabled(true);
					googleMap.getUiSettings().setZoomGesturesEnabled(true);
					MainActivityDrawer.driver_tip.setVisibility(VISIBLE);

					if(appointmentResponse.getDropAddr1().equals(""))
					{
						new_dropoff_location_address.setText(getResources().getString(R.string.addDropOfLocation));

					}
					else
					{
						new_dropoff_location_address.setText(appointmentResponse.getDropAddr1() );
					}

					String[] params=new String[4];
					Utility.printLog("params to eta ssss "+eta_latitude+" "+eta_longitude+" "+session.getPickuplat()+" "+session.getPickuplng());

					if(eta_latitude!=null && eta_longitude!=null)
					{
						params[0]=eta_latitude;
						params[1]=eta_longitude;
						params[2]=session.getDropofflat();
						params[3]=session.getDropofflng();
					}
					else
					{
						params[0]=session.getPickuplat();
						params[1]=session.getPickuplng();
						params[2]=session.getDropofflat();
						params[3]=session.getDropofflng();
					}


					new getEtaWIthTimer().execute(params);

					if(session.getLat_DOW()!=null && session.getLon_DOW()!=null)//else adding driver current location
					{
						LatLng latLng=new LatLng(Double.parseDouble(session.getLat_DOW()),Double.parseDouble(session.getLon_DOW()));
				/*		driver_arrived = googleMap.addMarker(new MarkerOptions().position(latLng)
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon)));*/
						double width1=dblArray[0]*50;
						double height1=dblArray[1]*80;

						if(!session.getSelectedImage().equals(""))
						{
							driverMarker = new PicassoMarker(googleMap.addMarker(new MarkerOptions().position(latLng)),getActivity());
							Picasso.with(getActivity()).load(VariableConstants.IMAGE_URL + session.getSelectedImage()).resize((int) width1, (int) height1).into(driverMarker);

						}

					/*	*//**
					 * to show the image on map on the marker
					 */
						marker_map_arrived.put(session.getCurrentAptChannel(), driverMarker.getmMarker());


						googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
					}

					//Plot the polyline from pickup to destination
					if(session.getDropofflat()!=null && session.getDropofflng()!=null)
					{
						LatLng latLng=new LatLng(Double.parseDouble(session.getDropofflat()),Double.parseDouble(session.getDropofflng()));
						//googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17.0f));
						picupmarker = googleMap.addMarker(new MarkerOptions().position(latLng)
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_markers_dropoff))
								.rotation(0)
								.flat(true));

						//Akbar commented to remove the path plotting
							/*String url = getMapsApiDirectionsFromTourl();
							Utility.printLog("getMapsApiDirectionsFromTourl ="+url);

							ReadTask downloadTask = new ReadTask();
							downloadTask.execute(url);*/
					}
					if(session.getPickuplat()!=null && session.getPickuplng()!=null)
					{

						LatLng latLng=new LatLng(Double.parseDouble(session.getPickuplat()),Double.parseDouble(session.getPickuplng()));
						//googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17.0f));
						picupmarker = googleMap.addMarker(new MarkerOptions().position(latLng)
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_markers_pickup))
								.rotation(0)
								.flat(true));

					}

					if(session.getCarImage()!=null && !session.getCarImage().equalsIgnoreCase(""))
					{
						drivehicalimg.setTag(getTarget_api(drivehicalimg));
						Picasso.with(getActivity())
								.load(VariableConstants.IMAGE_URL+session.getCarImage())
								.transform(new CircleTransform())
								.resize(width, height)
								.into((Target) drivehicalimg.getTag());

						drivehicalimg_new.setTag(getTarget_api(drivehicalimg_new));
						Picasso.with(getActivity())
								.load(VariableConstants.IMAGE_URL+session.getCarImage())
								.transform(new CircleTransform())
								.resize(width,height)
								.into((Target) drivehicalimg_new.getTag());


					}

					if(session.getDocPic() != null && !session.getDocPic().equalsIgnoreCase("null") && !session.getDocPic().equalsIgnoreCase(""))
					{
						Driver_Profile_Pic.setTag(getTarget_api(Driver_Profile_Pic));
						Picasso.with(getActivity()).load(session.getDocPic())
								.transform(new CircleTransform())
								.placeholder(R.drawable.invoice_profile_default_image)
								.resize(width,height)
								.into((Target) Driver_Profile_Pic.getTag());

						driver_profile_pic_new.setTag(getTarget_api(driver_profile_pic_new));
						Picasso.with(getActivity()).load(session.getDocPic())
								.transform(new CircleTransform())
								.placeholder(R.drawable.invoice_profile_default_image)
								.resize(width,height)
								.into((Target) driver_profile_pic_new.getTag());


					}
					else
					{
						Driver_Profile_Pic.setImageResource(R.drawable.ic_launcher);
					}

					pickupaddresstv.setText(session.getPickUpAddress());

					Driver_Name.setText(appointmentResponse.getModel());
					driver_name_new.setText(appointmentResponse.getModel());
					drivsnd_new.setText(session.getDocName());
					drivsnd.setText(session.getDocName());
					//Driver_Confirmation.setVisibility(View.VISIBLE);
					cancel_trip.setVisibility(View.INVISIBLE);
					show_address_relative.setVisibility(View.INVISIBLE);
					pickup.setVisibility(View.INVISIBLE);
					Txt_Pickup.setVisibility(View.INVISIBLE);
					Mid_Pointer.setVisibility(View.INVISIBLE);
					all_types_layout.setVisibility(View.INVISIBLE);
					allTypeLinearLayout.setVisibility(View.INVISIBLE);
					scrollView.setVisibility(View.GONE);
					now_later_layout.setVisibility(View.INVISIBLE);
					Driver_on_the_way_txt.setVisibility(VISIBLE);
					Driver_on_the_way_txt.setText(getResources().getString(R.string.journeystarted)+": "+session.getBookingId());
					Rl_distance_time.setVisibility(VISIBLE);
					//relative_now_later_status.setVisibility(View.GONE);

					if(session.getDriverRating()!=null)
					{
						drivvehicalrating_new.setText(session.getDriverRating());
						drivvehicalrating.setText(session.getDriverRating());

					}
				}

				else
				{
					if (appointmentResponse.getStatCode().equals("9"))
					{
						googleMap.clear();

						//	new BackgroundUnSubscribeChannels().execute("qd_37530DED-7E63-4975-989F-D4A801B75D26");

						String[] subscribed_channels1 = pubnub.getSubscribedChannelsArray();
						Utility.printLog("Appointments details subscribed_channels my channel=" + session.getChannelName());
						ArrayList<String> unsubscribeChannels1= new ArrayList<String>();
						for(int i=0;i<subscribed_channels1.length;i++)
						{
							Utility.printLog("Appointments details subscribed_channels at status 9" + i + " " + subscribed_channels1[i]);


							unsubscribeChannels1.add(subscribed_channels1[i]);

							if(unsubscribeChannels1.size()>0)
							{
								Utility.printLog("Appointments details channels unsubscribeChannels channel list size status 9=" + unsubscribeChannels.size());
								String[] new_un_sub_channels=new String[unsubscribeChannels1.size()];
								new_un_sub_channels=unsubscribeChannels1.toArray(new_un_sub_channels);
								new BackgroundUnSubscribeChannels().execute(new_un_sub_channels);
							}

						}
						Utility.printLog("usubcribe vvvv channel " + session.getCurrentAptChannel());
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
						ratingBar = (RatingBar) invoiceDialogue.findViewById(R.id.invoice_driver_rating);
						review = (TextView) invoiceDialogue.findViewById(R.id.invoice_review);
						Button submit = (Button) invoiceDialogue.findViewById(R.id.submitButton);
						TextView pickDate = (TextView) invoiceDialogue.findViewById(R.id.pickDate);
						TextView dropDate = (TextView) invoiceDialogue.findViewById(R.id.dropDate);

						review.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								reviewAlert();
							}
						});

						if (!appointmentResponse.getPickupDt().equals(""))
						{
							pickDate.setText(getResources().getString(R.string.pickup) + " (" + (appointmentResponse.getPickupDt()) + ")");

						}
						if (!appointmentResponse.getDropDt().equals(""))
						{
							dropDate.setText(getResources().getString(R.string.drop) + " (" + (appointmentResponse.getDropDt()) + ")");

						}


						submit.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (Utility.isNetworkAvailable(getActivity()))
								{
									BackgroundSubmitReview();
								} else
								{
									Toast.makeText(getActivity(), getResources().getString(R.string.network_connection_fail), Toast.LENGTH_LONG).show();
								}
							}
						});

						typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");

						receipt.setOnClickListener(new OnClickListener() {
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
								//TextView tollValue = (TextView) receiptDialogue.findViewById(R.id.tollValue);
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
								TextView distanceText= (TextView) receiptDialogue.findViewById(R.id.distanceText);
								TextView timeText= (TextView) receiptDialogue.findViewById(R.id.timeText);
								TextView invoice_subtotal_txt= (TextView) receiptDialogue.findViewById(R.id.invoice_subtotal_txt);
								//TextView parkingText= (TextView) receiptDialogue.findViewById(R.id.parkingText);
								TextView airportText= (TextView) receiptDialogue.findViewById(R.id.airportText);
								//TextView tollTExt= (TextView) receiptDialogue.findViewById(R.id.tollTExt);
								TextView invoice_discount_txt= (TextView) receiptDialogue.findViewById(R.id.invoice_discount_txt);
								TextView subtotalLayout= (TextView) receiptDialogue.findViewById(R.id.subtotalLayout);
								TextView invoice_newsubtotal_txt= (TextView) receiptDialogue.findViewById(R.id.invoice_newsubtotal_txt);
								TextView invoice_min_fare_txt= (TextView) receiptDialogue.findViewById(R.id.invoice_min_fare_txt);
								TextView minFare= (TextView) receiptDialogue.findViewById(R.id.minFare);
								RelativeLayout invoice_newsubtotal_layout= (RelativeLayout) receiptDialogue.findViewById(R.id.invoice_newsubtotal_layout);
								RelativeLayout invoice_min_fare_layout= (RelativeLayout) receiptDialogue.findViewById(R.id.invoice_min_fare_layout);
								RelativeLayout wallet_deducted_layout= (RelativeLayout) receiptDialogue.findViewById(R.id.wallet_deducted_layout);
								TextView wallet_deuct_amt= (TextView) receiptDialogue.findViewById(R.id.wallet_deuct_amt);

								meterFee.setTypeface(typeFace);
								//	parkingValue.setTypeface(typeFace);
								airportValue.setTypeface(typeFace);
								//tollValue.setTypeface(typeFace);
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
								distanceText.setTypeface(typeFace);
								timeText.setTypeface(typeFace);
								invoice_subtotal_txt.setTypeface(typeFace);
								//parkingText.setTypeface(typeFace);
								airportText.setTypeface(typeFace);
								//tollTExt.setTypeface(typeFace);
								invoice_discount_txt.setTypeface(typeFace);
								subtotalLayout.setTypeface(typeFace);
								invoice_newsubtotal_txt.setTypeface(typeFace);
								invoice_min_fare_txt.setTypeface(typeFace);
								minFare.setTypeface(typeFace);
								timeText.setTypeface(typeFace);
								invoice_newsubtotal_txt.setTypeface(typeFace);
								invoice_discount_txt.setTypeface(typeFace);



								if(!appointmentResponse.getDis().equals("") && !appointmentResponse.getDis().equals(null))
								{
									distanceText.setText(getResources().getString(R.string.distnceFee)+"("+appointmentResponse.getDis()+" "+getResources().getString(R.string.distanceUnit)+")");
								}

								if(!appointmentResponse.getDur().equals("") && !appointmentResponse.getDur().equals(null))
								{
									timeText.setText(getResources().getString(R.string.timeFare)+"("+getDurationString(Integer.parseInt(appointmentResponse.getDur()))+")");
								}

								if(!appointmentResponse.getTipPercent().equals("") && !appointmentResponse.getTipPercent().equals(null))
								{
									invoice_newsubtotal_txt.setText(getResources().getString(R.string.tip)+"("+appointmentResponse.getTipPercent()+"%"+")");
								}

								minFare.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(appointmentResponse.getMin_fare())));

								if(Double.parseDouble(appointmentResponse.getSubTotal())>Double.parseDouble(appointmentResponse.getMin_fare()))
								{
									invoice_newsubtotal_layout.setVisibility(View.VISIBLE);
									invoice_min_fare_layout.setVisibility(View.GONE);
								}
								else
								{
									invoice_newsubtotal_layout.setVisibility(View.VISIBLE);
									invoice_min_fare_layout.setVisibility(View.VISIBLE);
								}

								if(!appointmentResponse.getCode().equals("") && !appointmentResponse.getCode().equals(null))
								{
									if(appointmentResponse.getDiscountType().equals("1"))
									{
										invoice_discount_txt.setText(getResources().getString(R.string.discunt)+"("+appointmentResponse.getCode()+")"
												+"("+appointmentResponse.getDiscountVal()+"%"+")");

									}
									else
									{
										invoice_discount_txt.setText(getResources().getString(R.string.discunt)+"("+appointmentResponse.getCode()+")"
												+"("+getResources().getString(R.string.currencuSymbol)+appointmentResponse.getDiscountVal()+")");
									}
								}

								if(!appointmentResponse.getWalletDeducted().equals(""))
								{
									if(Double.parseDouble(appointmentResponse.getWalletDeducted())>0)
									{
										wallet_deducted_layout.setVisibility(VISIBLE);
										wallet_deuct_amt.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(appointmentResponse.getWalletDeducted())));

									}
								}



								bookingId.setText("Booking Id :"+session.getBookingId());

								submitButton.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										receiptDialogue.dismiss();
									}
								});
								if (!appointmentResponse.getBaseFee().equals(""))
									meterFee.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(appointmentResponse.getBaseFee())));
								//	parkingValue.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(appointmentResponse.getParkingFee())));
								airportValue.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(appointmentResponse.getAirportFee())));
								//	tollValue.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(appointmentResponse.getTollFee())));
								tip.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(appointmentResponse.getTip())));
								invoice_discount_amount.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(appointmentResponse.getDiscount())));
								invoice_total_amount.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(appointmentResponse.getAmountWoutwallet())));
								subtotalValue.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(appointmentResponse.getSubTotal())));

								if(appointmentResponse.getPayType().equals("2"))
									total.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(appointmentResponse.getCashCollected())));
								else
									total.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(appointmentResponse.getAmountWoutwallet())));

								distanceValue.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(appointmentResponse.getDistanceFee())));
								timeValue.setText(getResources().getString(R.string.currencuSymbol) + " " + round(Double.parseDouble(appointmentResponse.getTimeFee())));

								if (appointmentResponse.getPayType().equals("2"))
								{
									type.setText(getResources().getString(R.string.cash));

								} else
								{
									type.setText(getResources().getString(R.string.card));

								}


								receiptDialogue.show();
							}
						});

						needHelp.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								showDisputeAlert();
							}
						});

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
						review.setTypeface(typeFace);
						submit.setTypeface(typeFace);
						pickDate.setTypeface(typeFaceBold);
						dropDate.setTypeface(typeFaceBold);

						if (session.getDocPic() != null && !session.getDocPic().equalsIgnoreCase("null") && !session.getDocPic().equalsIgnoreCase("")) {

							width = getResources().getDrawable(R.drawable.on_the_way_profile_default).getMinimumWidth();
							height = getResources().getDrawable(R.drawable.on_the_way_profile_default).getMinimumHeight();
							driverImage.setTag(getTarget_api(driverImage));
							Picasso.with(getActivity()).load(session.getDocPic())
									.transform(new CircleTransform())
									.placeholder(R.drawable.on_the_way_profile_default)
									.resize(width, height)
									.into((Target) driverImage.getTag());

						} else
						{
							driverImage.setImageResource(R.drawable.ic_launcher);
						}
						driverName.setText(session.getDocName());
						amount.setText(getResources().getString(R.string.currencuSymbol)+" "+appointmentResponse.getAmount());
						pickupaddresstv.setText(session.getPickUpAddress());
						Utility.printLog("drop addresss "+session.getDropAddress());
						dropaddresstv.setText(session.getDropAddress());

						rideDate.setText(appointmentResponse.getApptDt());    //to convert the date to required format

						if(!flagForDialog)
						{
							invoiceDialogue.show();
							flagForDialog=true;
						}
					}
				}
			}
			else
			{
				Toast.makeText(getActivity(), appointmentResponse.getErrMsg(),Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			// set title
			alertDialogBuilder.setTitle(getResources().getString(R.string.error));
			// set dialog message
			alertDialogBuilder
					.setMessage(getResources().getString(R.string.error_message))
					.setCancelable(false)
					.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							if(session.getAptDate()!=null)
							{
								AppStatus(session.getAptDate());
							}
							else
							{
								AppStatus1();

							}							dialog.dismiss();
						}
					});
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();
		}
		/*}
		catch(Exception e)
		{
			//Utility.ShowAlert("Error. Please Retry!!", getActivity());
		}*/
	}

	private String round(double value)
	{
		String s = String.format(Locale.ENGLISH,"%.2f", value);
		Utility.printLog("rounded value="+s);
		return s;
	}

	private String convertTiTime(String timeString)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);   //2016-05-26 12:05:49
		DateFormat targetFormat = new SimpleDateFormat("hh:mm:ss a", Locale.US);
		String formattedDate = null;
		Date convertedDate = new Date();
		try {
			convertedDate = dateFormat.parse(timeString);
			formattedDate = targetFormat.format(convertedDate);
		}
		catch (java.text.ParseException e)
		{
			e.printStackTrace();
		}
		return formattedDate;
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

	private String convertToRequiredFormat(String dateString)
	{

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US);
		String formattedDate = null;
		Date convertedDate = new Date();
		try {
			convertedDate = dateFormat.parse(dateString);
			formattedDate = targetFormat.format(convertedDate);
		}
		catch (java.text.ParseException e)
		{
			e.printStackTrace();
		}
		return formattedDate;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
	}



	private void showDisputeAlert()

	{
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_dialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		text = (EditText) dialog.findViewById(R.id.user_dispute_text);

		TextView title= (TextView) dialog.findViewById(R.id.title);
		TextView backButton = (TextView) dialog.findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});
		text.setTypeface(typeFace);
		title.setTypeface(typeFaceBold);
		backButton.setTypeface(typeFace);
		Button submit = (Button) dialog.findViewById(R.id.submit);
		submit.setTypeface(typeFace);

		submit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				Utility.printLog("user msg=" + text.getText().toString().trim());
				if (text.getText().toString().trim().equals(""))
				{
					Utility.ShowAlert(getResources().getString(R.string.provide_valid_reason_for_dispute), getActivity());
				} else {
					dialog.dismiss();
					getDisputeDetails(text.getText().toString().trim());
				}
			}
		});


		dialog.show();
	}


	private void reviewAlert()
	{
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.leave_comment_layout);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setTitle(getResources().getString(R.string.leaveCommentTitle));

		text = (EditText) dialog.findViewById(R.id.user_dispute_text);
		TextView title= (TextView) dialog.findViewById(R.id.title);
		TextView backButton= (TextView) dialog.findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		title.setTypeface(typeFaceBold);
		text.setTypeface(typeFace);
		backButton.setTypeface(typeFace);
		Button submit = (Button) dialog.findViewById(R.id.submit);
		submit.setTypeface(typeFace);

		if(!session.getComments().equals(""))
		{
			text.setText(session.getComments());
		}

		submit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Utility.printLog("user msg=" + text.getText().toString().trim());

				reviewString=text.getText().toString();

				session.setComments(reviewString);

				dialog.dismiss();

			}
		});



		dialog.show();
	}

	private void  getDisputeDetails(final String disputeMsg) {
		JSONObject jsonObject = new JSONObject();
		mDialog =  Utility.GetProcessDialog(getActivity());

		if(mDialog!=null)
		{
			mDialog.show();
		}
		try {

			SessionManager session=new SessionManager(getActivity());
			Utility utility=new Utility();
			String currenttime=utility.getCurrentGmtTime();

			jsonObject.put("ent_sess_token",session.getSessionToken());
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_appnt_dt",session.getAptDate());
			jsonObject.put("ent_report_msg",disputeMsg);
			jsonObject.put("ent_date_time",currenttime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "reportDispute", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				Utility.printLog("Success of getting getDisputeDetails=" + result);
				parseDisputeInfo(result);
			}
			@Override
			public void onError(String error) {
				if (mDialog!=null)
				{
					mDialog.dismiss();
					mDialog = null;
				}
				Utility.printLog("Error for volley");
			}
		});
	}

	/**
	 * Parsing the dispute response
	 */
	private void parseDisputeInfo(String disputeResponse)
	{
		try
		{
			JSONObject jsnResponse = new JSONObject(disputeResponse);
			String jsonErrorParsing = jsnResponse.getString("errFlag");

			Utility.printLog("jsonErrorParsing is ---> " + jsonErrorParsing);

			BookAppointmentResponse response;
			Gson gson = new Gson();
			response = gson.fromJson(disputeResponse, BookAppointmentResponse.class);

			if(response!=null)
			{
				if(response.getErrFlag().equals("0"))
				{
					SessionManager session =new SessionManager(getActivity());
					session.setInvoiceRaised(false);
					session.setDriverArrived(false);
					session.setTripBegin(false);
					session.setDriverOnWay(false);
					BackgroundSubmitReview();
				}
				else
				{

				}
			}
		}
		catch(JSONException e)
		{
			Utility.printLog("exp " + e);
			e.printStackTrace();
			Utility.ShowAlert("Server error!!", getActivity());
		}
	}

	private void BackgroundSubmitReview() {
		final BookAppointmentResponse[] response = new BookAppointmentResponse[1];
		JSONObject jsonObject = new JSONObject();
		if(dialogL!=null)
		{
			dialogL.show();
		}
		try {
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			jsonObject.put("ent_sess_token",session.getSessionToken());
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_dri_email", session.getDriverEmail());
			jsonObject.put("ent_appnt_dt", session.getAptDate());
			jsonObject.put("ent_rating_num",""+(int)ratingBar.getRating());

			if(reviewString!=null)
				jsonObject.put("ent_review_msg",reviewString);

			jsonObject.put("ent_date_time",curenttime);
			Utility.printLog("params to login "+jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "updateSlaveReview", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				Gson gson = new Gson();
				response[0] =gson.fromJson(result,BookAppointmentResponse.class);
				if (dialogL!=null)
				{
					dialogL.dismiss();
					dialogL = null;
				}

				if(response[0] !=null)
				{
					if(response[0].getErrFlag().equals("0"))
					{
						VariableConstants.CONFIRMATION_CALLED=false;
						invoiceDialogue.dismiss();
						session.setComments("");
						session.storeAptDate(null);
						session.storeBookingId("0");
						session.setInvoiceRaised(false);
						session.setDriverArrived(false);
						session.setTripBegin(false);
						session.setDriverOnWay(false);

						String[] subscribed_channels1 = pubnub.getSubscribedChannelsArray();
						Utility.printLog("Appointments details subscribed_channels my channel=" + session.getChannelName());
						ArrayList<String> unsubscribeChannels1= new ArrayList<String>();
						for(int i=0;i<subscribed_channels1.length;i++)
						{
							Utility.printLog("Appointments details subscribed_channels at status 9" + i + " " + subscribed_channels1[i]);


							unsubscribeChannels1.add(subscribed_channels1[i]);

							if(unsubscribeChannels1.size()>0)
							{
								Utility.printLog("Appointments details channels unsubscribeChannels channel list size status 9=" + unsubscribeChannels1.size());
								String[] new_un_sub_channels=new String[unsubscribeChannels1.size()];
								new_un_sub_channels=unsubscribeChannels1.toArray(new_un_sub_channels);
								new BackgroundUnSubscribeChannels().execute(new_un_sub_channels);
							}

						}

						Toast.makeText(getActivity(), getResources().getString(R.string.bookingCompleted), Toast.LENGTH_LONG).show();
						Intent i = new Intent(getActivity(), MainActivityDrawer.class);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(i);
						getActivity().finish();
						getActivity().overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);

					}
					else
					{
						session.storeAptDate(null);
						session.storeBookingId("0");

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

						// set title
						alertDialogBuilder.setTitle(getResources().getString(R.string.error));

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
										Intent i = new Intent(getActivity(), MainActivityDrawer.class);
										// set the new task and clear flags
										i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
										startActivity(i);


										getActivity().overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
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
				Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}
	/**
	 * Initialization of variables for Image loader
	 */
	private void initImageLoader() {
		int memoryCacheSize;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			int memClass = ((ActivityManager)
					getActivity().getSystemService(Context.ACTIVITY_SERVICE))
					.getMemoryClass();
			memoryCacheSize = (memClass / 8) * 1024 * 1024;
		} else {
			memoryCacheSize = 2 * 1024 * 1024;
		}

		final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity()).threadPoolSize(5)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(memoryCacheSize)
				.memoryCache(new FIFOLimitedMemoryCache(memoryCacheSize-1000000))
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)//.enableLogging()
				.build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		networkUtil.disconnectGoogleApiClient();


		Utility.printLog("CONTROL INSIDE onPause");

		getActivity().unregisterReceiver(receiver);

		visibility=false;

		if(myTimer_publish!=null)
		{
			myTimer_publish.cancel();
			myTimer_publish=null;
		}

		if(myTimer!=null)
		{
			myTimer.cancel();
			myTimer=null;
		}

		String[] subscribed_channels1 = pubnub.getSubscribedChannelsArray();
		Utility.printLog("Appointments details subscribed_channels my channel=" + session.getChannelName());
		ArrayList<String> unsubscribeChannels1= new ArrayList<String>();
		for(int i=0;i<subscribed_channels1.length;i++)
		{
			Utility.printLog("Appointments details subscribed_channels at status 9" + i + " " + subscribed_channels1[i]);


			unsubscribeChannels1.add(subscribed_channels1[i]);

			if(unsubscribeChannels1.size()>0)
			{
				Utility.printLog("Appointments details channels unsubscribeChannels channel list size status 9=" + unsubscribeChannels1.size());
				String[] new_un_sub_channels=new String[unsubscribeChannels1.size()];
				new_un_sub_channels=unsubscribeChannels1.toArray(new_un_sub_channels);
				new BackgroundUnSubscribeChannels().execute(new_un_sub_channels);
			}

		}

	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Utility.printLog("CONTROL INSIDE onDestroy");
		visibility=false;
		networkUtil.disconnectGoogleApiClient();
		VariableConstants.isComingFromSearch=false;
		VariableConstants.isComingFromScroll=false;
	/*	if(locationManager != null)
		{
			// remove the delegates to stop the GPS
			locationManager.removeGpsStatusListener(gpsListener);
			locationManager.removeUpdates(gpsListener);
			locationManager = null;
		}*/
		//new BackgroundShutdownPubnub().execute();


	}

	@Override
	public void onResume()
	{
		super.onResume();

		networkUtil.connectGoogleApiClient();
		//pubnub = new Pubnub(VariableConstants.PUBNUB_PUBLISH_KEY, VariableConstants.PUBUB_SUBSCRIBE_KEY, "", true);


		visibility=true;
		initializeVariables(view);
		NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

		if(notificationManager!=null)
		{
			notificationManager.cancelAll();

		}


		getActivity().registerReceiver(receiver, filter);


		if(Utility.isNetworkAvailable(getActivity()))
		{
			if(session.getAptDate()!=null)
			{
				AppStatus(session.getAptDate());
			}
			else
			{
				AppStatus1();

			}
		}
		else
		{
			getResources().getString(R.string.network_connection_fail);
		}


		/**
		 * Akbar commented this
		 */
/*
		if(Utility.isNetworkAvailable(getActivity()))
		{
			if(myTimer!=null)
			{
				myTimer.cancel();
				myTimer=null;
			}
			//new CallGooglePlayServices().execute();
			setHasOptionsMenu(false);
			getCarDetails();
			getAppointmentDetails_Volley(session.getAptDate());
		}
		else
		{
			getResources().getString(R.string.network_connection_fail);
		}*/


		Utility.printLog("onResume session.isdriverOnWay() " + session.isDriverOnWay());
		Utility.printLog("onResume session.isdriverOnArrived() " + session.isDriverOnArrived());
		Utility.printLog("onResume session.isInvoiceRaised() " + session.isInvoiceRaised());
		Utility.printLog("onResume session.isTripBegin() " + session.isTripBegin());

		if(myTimer != null)
		{
			return;
		}
		else
		{
			myTimer = new Timer();
			myTimerTask = new TimerTask()
			{
				@Override
				public void run()
				{
					if(getActivity()!=null)
					{
						getActivity().runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								// TODO Auto-generated method stub
								System.out.println("Calling Location...");
								VisibleRegion visibleRegion = googleMap.getProjection()
										.getVisibleRegion();
								Point x1 = googleMap.getProjection().toScreenLocation(
										visibleRegion.farRight);
								Point y = googleMap.getProjection().toScreenLocation(
										visibleRegion.nearLeft);
								Point centerPoint = new Point(x1.x / 2, y.y / 2);
								LatLng centerFromPoint = googleMap.getProjection().fromScreenLocation(centerPoint);
								double lat = centerFromPoint.latitude;
								double lon = centerFromPoint.longitude;
								if((lat==currentLatitude&&lon==currentLongitude))
								{
									//Pointer is in same location, so dont call the service to get address
									Utility.printLog("Update Address: FALSE");
								}
								else
								{
									SessionManager session = new SessionManager(mActivity);
									Utility.printLog("on location change car id=" + Car_Type_Id);
									//To update current location
									Utility.printLog("sss PUBLISHING a:3...");
									if(lat!=0.0 && lon!=0.0)
									{
										_publish(session.getServerChannel(),"{a:3,chn:"+session.getChannelName()+",lt:"+lat+",lg:"+lon+",st:3,tp:"+current_master_type_id+"}");

										Utility.printLog("Update Address: TRUE" + Car_Type_Id);

										currentLatitude=lat;
										currentLongitude=lon;
										if(!VariableConstants.CONFIRMATION_CALLED)
										{
											from_latitude=String.valueOf(lat);
											from_longitude=String.valueOf(lon);
										}

										String[] params=new String[]{""+lat,""+lon};
										if(isAdded())
										{
											if(!IsreturnFromSearch)
											{
												Utility.printLog("here addres search start");
												new BackgroundGetAddress().execute(params);
											}
										}
									}
									//new BackgroundGetAddress().execute(params);
								}
							}
						});
					}
				}
			};
			myTimer.schedule(myTimerTask, 000, 2000);
		}

		//driver on the way
		if(session.isDriverOnWay())
		{
			Utility.printLog("onResume INSIDE ON_THE_WAY");

			//stop the timer to get the current address if any booking is going on
			if(myTimer!=null)
			{
				myTimer.cancel();
				myTimer=null;
			}
			//new CallGooglePlayServices().execute();
			setHasOptionsMenu(false);
			if(session.getDriverEmail()!=null)
			{
				getCarDetails();

			}
			if(session.getAptDate()!=null)
			{
				getAppointmentDetails_Volley(session.getAptDate());

			}
			return;
		}

		else if(session.isDriverOnArrived())
		{
			Utility.printLog("onResume INSIDE Driver Arrived");

			//stop the timer to get the current address if any booking is going on
			if(myTimer!=null)
			{
				myTimer.cancel();
				myTimer=null;
			}

			//new CallGooglePlayServices().execute();
			setHasOptionsMenu(false);
			if(session.getDriverEmail()!=null)
			{
				getCarDetails();

			}
			if(session.getAptDate()!=null)
			{
				getAppointmentDetails_Volley(session.getAptDate());

			}			return;
		}

		else if(session.isTripBegin())
		{
			Utility.printLog("onResume INSIDE Driver Arrived");

			//stop the timer to get the current address if any booking is going on
			if(myTimer!=null)
			{
				myTimer.cancel();
				myTimer=null;
			}
			//new CallGooglePlayServices().execute();
			setHasOptionsMenu(false);
			if(session.getDriverEmail()!=null)
			{
				getCarDetails();

			}
			if(session.getAptDate()!=null)
			{
				getAppointmentDetails_Volley(session.getAptDate());

			}
			return;
		}
		else if(session.isInvoiceRaised() && !session.isBookingCancelled())
		{
			Utility.printLog("onResume INSIDE DriverInvoiceRaised");

			if(mActivity!=null)
			{
				//new BackgroundUnSubscribeChannels().execute(session.getCurrentAptChannel());

				String[] subscribed_channels1 = pubnub.getSubscribedChannelsArray();
				Utility.printLog("Appointments details subscribed_channels my channel=" + session.getChannelName());
				ArrayList<String> unsubscribeChannels1= new ArrayList<String>();
				for(int i=0;i<subscribed_channels1.length;i++)
				{
					Utility.printLog("Appointments details subscribed_channels at status 9" + i + " " + subscribed_channels1[i]);


					unsubscribeChannels1.add(subscribed_channels1[i]);

					if(unsubscribeChannels1.size()>0)
					{
						Utility.printLog("Appointments details channels unsubscribeChannels channel list size status 9=" + unsubscribeChannels1.size());
						String[] new_un_sub_channels=new String[unsubscribeChannels1.size()];
						new_un_sub_channels=unsubscribeChannels1.toArray(new_un_sub_channels);
						new BackgroundUnSubscribeChannels().execute(new_un_sub_channels);
					}

				}

				pollingTimer.cancel();
				session.storeBookingId("0");
			/*	Utility.printLog("mActivity if="+mActivity);
				Intent intent1 = new Intent(mActivity,InvoiceActivity.class);
				mActivity.startActivity(intent1);*/
				if(session.getAptDate()!=null)
				{
					getAppointmentDetails_Volley(session.getAptDate());

				}
			}
			else
			{
				//new BackgroundUnSubscribeChannels().execute(session.getCurrentAptChannel());4

				String[] subscribed_channels1 = pubnub.getSubscribedChannelsArray();
				Utility.printLog("Appointments details subscribed_channels my channel=" + session.getChannelName());
				ArrayList<String> unsubscribeChannels1= new ArrayList<String>();
				for(int i=0;i<subscribed_channels1.length;i++)
				{
					Utility.printLog("Appointments details subscribed_channels at status 9" + i + " " + subscribed_channels1[i]);


					unsubscribeChannels1.add(subscribed_channels1[i]);

					if(unsubscribeChannels1.size()>0)
					{
						Utility.printLog("Appointments details channels unsubscribeChannels channel list size status 9=" + unsubscribeChannels1.size());
						String[] new_un_sub_channels=new String[unsubscribeChannels1.size()];
						new_un_sub_channels=unsubscribeChannels1.toArray(new_un_sub_channels);
						new BackgroundUnSubscribeChannels().execute(new_un_sub_channels);
					}

				}

				pollingTimer.cancel();
				session.storeBookingId("0");
				/*mActivity=(MainActivityDrawer)getActivity();
				Utility.printLog("mActivity else="+mActivity);
				Intent intent1 = new Intent(mActivity,InvoiceActivity.class);
				mActivity.startActivity(intent1);*/
				if(session.getAptDate()!=null)
				{
					getAppointmentDetails_Volley(session.getAptDate());

				}
			}
			return;
		}


		new BackgroundSubscribeMyChannel().execute();


		if(!VariableConstants.CONFIRMATION_CALLED) {
			pubnubProgressDialog.setClickable(false);
			pubnubProgressDialog.setVisibility(VISIBLE);
			pickup_Distance.setVisibility(View.INVISIBLE);
		}
		startPublishingWithTimer();
		Utility.printLog("publishing 2 ");



	}

	/**
	 * publishing for every 7 seconds to get the new drivers and also to remove the drivers who are going out
	 */
	private void startPublishingWithTimer()
	{
		Utility.printLog("CONTROL INSIDE startPublishingWithTimer");

		if(myTimer_publish!= null)
		{
			Utility.printLog("Timer already started");
			return;
		}
		myTimer_publish = new Timer();

		myTimerTask_publish = new TimerTask()
		{
			@Override
			public void run()
			{
				if(currentLatitude==0.0 || currentLongitude==0.0)
				{
					Utility.printLog("startPublishingWithTimer getServerChannel no location");
				}
				else
				{
					Utility.printLog("startPublishingWithTimer getServerChannel=" + session.getServerChannel());
					Utility.printLog("startPublishingWithTimer pubnub message=" + "{a:1,pid:" + session.getLoginId() + ",lt:" + currentLatitude + ",lg:" + currentLongitude + ",chn:" + session.getChannelName() + ",st:3,tp:" + Car_Type_Id + "}");
					_publish(session.getServerChannel(), "{a:1,pid:" + session.getLoginId() + ",lt:" + currentLatitude + ",lg:" + currentLongitude + ",chn:" + session.getChannelName() + ",st:3,tp:" + "-1" + "}");
					Utility.printLog("type id " + Car_Type_Id);
				}
			}
		};
		myTimer_publish.schedule(myTimerTask_publish, 000, 5000);
	}

	@Override
	public void onAttach(Activity activity)
	{
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Utility.printLog("CONTROL INSIDE onAttach");
		mActivity = getActivity();
	}


	private class PhoneCallListener extends PhoneStateListener
	{
		String LOG_TAG = "LOGGING 123";
		@Override
		public void onCallStateChanged(int state, String incomingNumber)
		{
			if (TelephonyManager.CALL_STATE_RINGING == state)
			{
				// phone ringing
			}
			if (TelephonyManager.CALL_STATE_OFFHOOK == state)
			{
				// active
			}
		}
	}

	/**
	 * checking is there any pending appointments or not while opening the application
	 */
	private void AppStatus(String date)
	{
		if(getActivity()!=null)
		{

			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			final RequestBody requestBody = new FormEncodingBuilder()

					.add("ent_sess_token", session.getSessionToken())
					.add("ent_dev_id", session.getDeviceId())
					.add("ent_appnt_dt", date)
					.add("ent_user_type","2")
					.add("ent_date_time", curenttime)
					.build();
			OkHttpRequest.doJsonRequest(VariableConstants.BASE_URL+"getApptStatus", requestBody, new OkHttpRequest.JsonRequestCallback() {
				@Override
				public void onSuccess(String result) {

					Utility.printLog("success in aptstatus " + result);
					getAppStatus(result);


				}

				@Override
				public void onError(String error) {

					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_connection_fail), Toast.LENGTH_SHORT).show();

				}
			});


		}
	}
	private void getAppStatus(String getAppStatus)
	{
		Gson gson = new Gson();
		statusResponse = gson.fromJson(getAppStatus, StatusInformation.class);
		Utility.printLog("response in getAppt status " + getAppStatus);

		/*VariableConstants.PUBUB_SUBSCRIBE_KEY=statusResponse.getSub();
		VariableConstants.PUBNUB_PUBLISH_KEY=statusResponse.getPub();*/
		session.setPresenceChn(statusResponse.getPresenseChn());

		if(statusResponse!=null)
		{
			Utility.printLog("statusResponse getErrFlag=" + statusResponse.getErrFlag());
			if(statusResponse.getErrNum().equals("6") || statusResponse.getErrNum().equals("7") ||
					statusResponse.getErrNum().equals("94") || statusResponse.getErrNum().equals("96"))
			{
				Toast.makeText(getActivity(), statusResponse.getErrMsg(),Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getActivity(), SplashActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				getActivity().startActivity(i);
				getActivity().overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
			}
			else	if(statusResponse.getWalletamt().equals(""))
			{
				walletAmt=0.0;
			}
			else
			{
				walletAmt=Double.parseDouble(statusResponse.getWalletamt());
			}

			if(statusResponse.getErrFlag().equals("0"))
			{

				String status_code=null;
				String rateStatus =null;

				if(statusResponse.getData()!=null)
				{
					status_code=statusResponse.getData().get(0).getStatus();
					rateStatus=statusResponse.getData().get(0).getRateStatus();
					Utility.printLog("statusResponse status_code=" + status_code);
				}
				else
				{
					status_code=statusResponse.getStatus();
					rateStatus=statusResponse.getRateStatus();
					session.storeDropofflat(statusResponse.getDropLat());
					session.storeDropofflng(statusResponse.getDropLong());
					session.storePickuplat(statusResponse.getPickLat());
					session.storePickuplng(statusResponse.getPickLong());
					session.setStripeKey(statusResponse.getStipeKey());
					Utility.printLog("statusResponse status_code=" + status_code);
				}

				//store the data when the booking is valid
				if(status_code!=null && session.getAptDate()==null)//if apt date not sending
				{
					if(status_code.equals("6") || status_code.equals("7") || status_code.equals("8") || (status_code.equals("9") ))
					{
						session.storeAptDate(statusResponse.getData().get(0).getApptDt());
						session.storeDriverEmail(statusResponse.getData().get(0).getEmail());
						session.storeDocPic(VariableConstants.IMAGE_BASE_URL+statusResponse.getData().get(0).getpPic());
						session.storeBookingId(statusResponse.getData().get(0).getBid());
						session.storeDocName(statusResponse.getData().get(0).getfName()+" "+statusResponse.getData().get(0).getlName());
						session.storeDocPH(statusResponse.getData().get(0).getMobile());
						session.storeDropofflat(statusResponse.getData().get(0).getDropLat());
						session.storeDropofflng(statusResponse.getData().get(0).getDropLong());
						session.storePickuplat(statusResponse.getData().get(0).getPickLat());
						session.storePickuplng(statusResponse.getData().get(0).getPickLong());
					}
				}
				else if(status_code!=null)//if apt date sent
				{
					if(status_code.equals("6") || status_code.equals("7") || status_code.equals("8") || (status_code.equals("9") ) )
					{
						session.storeAptDate(statusResponse.getApptDt());
						session.storeDriverEmail(statusResponse.getEmail());
						session.storeDocPic(VariableConstants.IMAGE_BASE_URL+statusResponse.getpPic());
						session.storeBookingId(statusResponse.getBid());
						session.storeDocName(statusResponse.getfName());
						session.storeDocPH(statusResponse.getMobile());
					}
				}


				if(status_code!=null)
					if(status_code.equals("5"))//Driver cancelled apt
					{
						session.setDriverCancelledApt(false);
						session.setDriverOnWay(false);
						Utility.printLog("Wallah set as false Homepage cancel 3");
						session.setDriverArrived(false);
						session.setInvoiceRaised(false);
						session.setTripBegin(false);

						//marker_map.clear();
						marker_map_on_the_way.clear();
						Utility.printLog("marker_map_on_the_way 10");
						marker_map_arrived.clear();
						googleMap.clear();

						startPublishingWithTimer();
						Utility.printLog("publishing 3 ");


						if(!(getActivity().getActionBar().isShowing()))
						{
							getActivity().getActionBar().show();
						}
						//ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);

						isSetDropoffLocation=false;
						isBackPressed=false;
						isFareQuotePressed=false;
						to_latitude=null;
						to_longitude=null;
						mDROPOFF_ADDRESS=null;
						//Driver_Confirmation.setVisibility(View.INVISIBLE);
						show_address_relative.setVisibility(VISIBLE);
						pickup.setVisibility(VISIBLE);
						Txt_Pickup.setVisibility(VISIBLE);
						Mid_Pointer.setVisibility(VISIBLE);
						Driver_on_the_way_txt.setVisibility(View.INVISIBLE);
						Rl_distance_time.setVisibility(View.GONE);

					}


					else if(status_code.equals("6") && !session.isDriverOnWay())
					{
						Utility.printLog("getAppStatus INSIDE DriverOnWay");

						session.setDriverOnWay(true);
						Utility.printLog("Wallah set as true Homepage DOW");
						session.setDriverArrived(false);
						session.setTripBegin(false);
						session.setInvoiceRaised(false);

						LatLng latLng = new LatLng(Double.parseDouble(session.getPickuplat()), Double.parseDouble(session.getPickuplng()));

						googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));

						setHasOptionsMenu(false);
						if(session.getDriverEmail()!=null)
						{
							getCarDetails();

						}
						if(session.getAptDate()!=null)
						{
							getAppointmentDetails_Volley(session.getAptDate());

						}
						return;
					}
					else if(status_code.equals("7") && !session.isDriverOnArrived())
					{
						Utility.printLog("getAppStatus INSIDE DriverOnArrived");

						session.setDriverOnWay(false);
						Utility.printLog("Wallah set as false Homepage DA");
						session.setDriverArrived(true);
						session.setTripBegin(false);
						session.setInvoiceRaised(false);

						LatLng latLng = new LatLng(Double.parseDouble(session.getPickuplat()), Double.parseDouble(session.getPickuplng()));
						// Showing the current location in Google Map & Zoom in the Google Map
						googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));

						setHasOptionsMenu(false);
						if(session.getDriverEmail()!=null)
						{
							getCarDetails();

						}
						if(session.getAptDate()!=null)
						{
							getAppointmentDetails_Volley(session.getAptDate());

						}

						return;
					}
					else if(!session.isTripBegin() && status_code.equals("8"))
					{
						Utility.printLog("getAppStatus INSIDE Driver TripBegin");
						session.setDriverOnWay(false);
						Utility.printLog("Wallah set as false Homepage TB");
						session.setDriverArrived(false);
						session.setTripBegin(true);
						session.setInvoiceRaised(false);

						LatLng latLng = null;
						if(!session.getPickuplat().equals("") && !session.getPickuplng().equals(""))
						{
							latLng = new LatLng(Double.parseDouble(session.getPickuplat()), Double.parseDouble(session.getPickuplng()));

						}
						// Showing the current location in Google Map & Zoom in the Google Map
						googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));

						setHasOptionsMenu(false);
						if(session.getDriverEmail()!=null)
						{
							getCarDetails();

						}
						if(session.getAptDate()!=null)
						{
							getAppointmentDetails_Volley(session.getAptDate());

						}
						return;
					}
					else if(((status_code.equals("9"))  && (rateStatus.equals("2"))) && !(session.isInvoiceRaised()))
					{
						Utility.printLog("getAppStatus INSIDE InvoiceRaised");
						if(mActivity!=null)
						{
							//new BackgroundUnSubscribeChannels().execute(session.getCurrentAptChannel());

							String[] subscribed_channels1 = pubnub.getSubscribedChannelsArray();
							Utility.printLog("Appointments details subscribed_channels my channel=" + session.getChannelName());
							ArrayList<String> unsubscribeChannels1= new ArrayList<String>();
							for(int i=0;i<subscribed_channels1.length;i++)
							{
								Utility.printLog("Appointments details subscribed_channels at status 9" + i + " " + subscribed_channels1[i]);


								unsubscribeChannels1.add(subscribed_channels1[i]);

								if(unsubscribeChannels1.size()>0)
								{
									Utility.printLog("Appointments details channels unsubscribeChannels channel list size status 9=" + unsubscribeChannels1.size());
									String[] new_un_sub_channels=new String[unsubscribeChannels1.size()];
									new_un_sub_channels=unsubscribeChannels1.toArray(new_un_sub_channels);
									new BackgroundUnSubscribeChannels().execute(new_un_sub_channels);
								}

							}

							pollingTimer.cancel();
							session.storeBookingId("0");
								/*Utility.printLog("mActivity if="+mActivity);
								Intent intent1 = new Intent(mActivity,InvoiceActivity.class);
								mActivity.startActivity(intent1);*/
							if(session.getAptDate()!=null)
							{
								getAppointmentDetails_Volley(session.getAptDate());

							}
						}
						else
						{
							new BackgroundUnSubscribeChannels().execute(session.getCurrentAptChannel());
							pollingTimer.cancel();
							session.storeBookingId("0");
							/*	mActivity=(MainActivityDrawer)getActivity();
								Utility.printLog("mActivity else="+mActivity);
								Intent intent1 = new Intent(mActivity,InvoiceActivity.class);
								mActivity.startActivity(intent1);*/
							if(session.getAptDate()!=null)
							{
								getAppointmentDetails_Volley(session.getAptDate());

							}
						}
					}
			}

		}

	}


	/**
	 *subscribing to my server channel to listen all available drivers around you
	 */
	class BackgroundSubscribeMyChannel extends AsyncTask<String,Void,String>
	{
		@Override
		protected String doInBackground(String... params)
		{
			Utility.printLog("CONTROL INSIDE MyChannel Subscribe "+session.getChannelName());
			String[] new_channels=new String[1];
			new_channels[0]=session.getChannelName();
			subscribe(new_channels);
			return null;
		}
	}



	/**
	 * subscribing to driver channels to update the locations of drivers in map
	 */
	class BackgroundSubscribeChannels extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... params)
		{
			Utility.printLog("CONTROL INSIDE Subscribe Channels length=" + params.length);
			subscribe(params);
			//subscribeMultipleChannels(params);
			return null;
		}
	}

	/**
	 * unsubscribing the drivers channels who are going out of the application
	 */
	class BackgroundUnSubscribeChannels extends AsyncTask<String,Void,String>
	{
		@Override
		protected String doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			Utility.printLog("CONTROL INSIDE UnSubscribe Channels");
			Utility.printLog("CONTROL INSIDE UnSubscribe"+n++);
			pubnub.unsubscribe(params);
			return null;
		}
	}

	/**
	 *unsubscribing the all channels
	 *
	 */
	class BackgroundUnSubscribeAll extends AsyncTask<String,Void,String>
	{
		@Override
		protected String doInBackground(String... params)
		{
			pubnub.unsubscribeAll();
			return null;
		}
	}

	/**
	 * checking whether this fragment is in foreground or not
	 */
	public static boolean visibleStatus()
	{
		return visibility;
	}

	/**
	 * Updating all drivers locations in map screen
	 * @param filterResponse
	 *
	 */
	private void UpdateDriverLocations(PubnubResponseNew filterResponse)
	{
		//Utility.printLog("INSIDE UPDATE DRIVERS marker_map.containsKey="+marker_map.containsKey(filterResponse.getChn()));

		double latitude=Double.parseDouble(filterResponse.getLt());
		double longitude=Double.parseDouble(filterResponse.getLg());

		LatLng latLng=new LatLng(latitude,longitude);

		if( filterResponse!=null && filterResponse.getMasArr()!=null && filterResponse.getMasArr().size()>0 && filterResponse.getTypes().size()>0)
		{
			if(filterResponse!=null && filterResponse.getTypes().size()>0 && filterResponse.getTp()!=null)
				if(filterResponse.getTypes().size()>0 && filterResponse.getTp().equals(filterResponse.getTypes().get(0).getType_id()))
				{
					if(marker_map.containsKey(filterResponse.getChn()))
					{
						//Update Lat-Lng
						//marker_map.get(filterResponse.getChn()).setPosition(latLng);
						animateMarker(marker_map.get(filterResponse.getChn()), latLng);

					}
				}
				else if(response.getCarsdetails().size()>1 && filterResponse.getTp().equals(filterResponse.getTypes().get(1).getType_id()))
				{
					if(marker_map.containsKey(filterResponse.getChn()))
					{
						//Update Lat-Lng
						//marker_map.get(filterResponse.getChn()).setPosition(latLng);
						animateMarker(marker_map.get(filterResponse.getChn()), latLng);
					}
				}
				else if(response.getCarsdetails().size()>2 && filterResponse.getTp().equals(response.getCarsdetails().get(2).getType_id()))
				{
					if(marker_map.containsKey(filterResponse.getChn()))
					{
						//Update Lat-Lng
						//marker_map.get(filterResponse.getChn()).setPosition(latLng);
						animateMarker(marker_map.get(filterResponse.getChn()), latLng);
					}
				}
				else if(response.getCarsdetails().size()>3 && filterResponse.getTp().equals(response.getCarsdetails().get(3).getType_id()))
				{
					if(marker_map.containsKey(filterResponse.getChn()))
					{
						//Update Lat-Lng
						Utility.printLog("UDATING marker: " + filterResponse.getN());
						//marker_map.get(filterResponse.getChn()).setPosition(latLng);
						animateMarker(marker_map.get(filterResponse.getChn()), latLng);
					}
				}
		}

	}
	/**
	 *  Updating driver location when Driver On The Way
	 * @param filterResponse
	 *
	 */
	private void UpdateDriverLocation_DriverOnTheWay(PubnubResponseNew filterResponse)
	{

		double driver_current_latitude=Double.parseDouble(filterResponse.getLt());
		double driver_cuttent_longitude=Double.parseDouble(filterResponse.getLg());

		Utility.printLog("marker_map_on_the_way inside channel contains LAT:" + filterResponse.getLt());
		Utility.printLog("marker_map_on_the_way inside channel contains LON:" + filterResponse.getLg());

		session.storeLat_DOW(filterResponse.getLt());
		session.storeLon_DOW(filterResponse.getLg());

		/********************************************************************************************************/


		Location driverLocation = new Location("starting_point");
		driverLocation.setLatitude(driver_current_latitude);
		driverLocation.setLongitude(driver_cuttent_longitude);

		mCurrentLoc=driverLocation;

		if(mPreviousLoc==null)
		{
			mPreviousLoc=driverLocation;
		}

		final float bearing = mPreviousLoc.bearingTo(mCurrentLoc);
		if(driverMarker.getmMarker()!=null)
		{
			driverMarker.getmMarker().setPosition(new LatLng(driverLocation.getLatitude(), driverLocation.getLongitude()));
			driverMarker.getmMarker().setAnchor(0.5f, 0.5f);
			driverMarker.getmMarker().setRotation(bearing);
			driverMarker.getmMarker().setFlat(true);

			final Handler handler = new Handler();
			final long start = SystemClock.uptimeMillis();
			Projection proj = googleMap.getProjection();
			Point startPoint = proj.toScreenLocation(new LatLng(mPreviousLoc.getLatitude(),mPreviousLoc.getLongitude()));
			final LatLng startLatLng = proj.fromScreenLocation(startPoint);
			final long duration = 500;

			final Interpolator interpolator = new LinearInterpolator();

			handler.post(new Runnable() {
				@Override
				public void run() {
					long elapsed = SystemClock.uptimeMillis() - start;
					float t = interpolator.getInterpolation((float) elapsed
							/ duration);
					double lng = t * mCurrentLoc.getLongitude() + (1 - t)
							* startLatLng.longitude;
					double lat = t * mCurrentLoc.getLatitude() + (1 - t)
							* startLatLng.latitude;
					driverMarker.getmMarker().setPosition(new LatLng(lat, lng));
					driverMarker.getmMarker().setAnchor(0.5f, 0.5f);
					driverMarker.getmMarker().setRotation(bearing);
					driverMarker.getmMarker().setFlat(true);

					if (t < 1.0) {
						// Post again 16ms later.
						handler.postDelayed(this, 16);
					}
				}
			});
		}

		mPreviousLoc=mCurrentLoc;
/********************************************************************************************************/

		LatLng latLng=new LatLng(driver_current_latitude,driver_cuttent_longitude);
		googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));



	}

	/**
	 * Updating driver location when Driver Arrived
	 * @param filterResponse
	 *
	 */
	private void UpdatDriverLocation_DriverArrived(PubnubResponseNew filterResponse)
	{
		double driver_current_latitude=Double.parseDouble(filterResponse.getLt());
		double driver_cuttent_longitude=Double.parseDouble(filterResponse.getLg());

		session.storeLat_DOW(filterResponse.getLt());
		session.storeLon_DOW(filterResponse.getLg());

		Utility.printLog("INSIDE DRIVER REACHED:4 current lat=" + driver_current_latitude +
				" lng=" + driver_cuttent_longitude);

		//getETAWithTimer(7);
		LatLng latLng=new LatLng(driver_current_latitude,driver_cuttent_longitude);
		googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


		/********************************************************************************************************/


		Location driverLocation = new Location("starting_point");
		driverLocation.setLatitude(driver_current_latitude);
		driverLocation.setLongitude(driver_cuttent_longitude);

		mCurrentLoc=driverLocation;

		if(mPreviousLoc==null)
		{
			mPreviousLoc=driverLocation;
		}

		final float bearing = mPreviousLoc.bearingTo(mCurrentLoc);
		if(driverMarker.getmMarker()!=null)
		{
			driverMarker.getmMarker().setPosition(new LatLng(driverLocation.getLatitude(), driverLocation.getLongitude()));
			driverMarker.getmMarker().setAnchor(0.5f, 0.5f);
			driverMarker.getmMarker().setRotation(bearing);
			driverMarker.getmMarker().setFlat(true);

			final Handler handler = new Handler();
			final long start = SystemClock.uptimeMillis();
			Projection proj = googleMap.getProjection();
			Point startPoint = proj.toScreenLocation(new LatLng(mPreviousLoc.getLatitude(),mPreviousLoc.getLongitude()));
			final LatLng startLatLng = proj.fromScreenLocation(startPoint);
			final long duration = 500;

			final Interpolator interpolator = new LinearInterpolator();

			handler.post(new Runnable() {
				@Override
				public void run() {
					long elapsed = SystemClock.uptimeMillis() - start;
					float t = interpolator.getInterpolation((float) elapsed
							/ duration);
					double lng = t * mCurrentLoc.getLongitude() + (1 - t)
							* startLatLng.longitude;
					double lat = t * mCurrentLoc.getLatitude() + (1 - t)
							* startLatLng.latitude;
					driverMarker.getmMarker().setPosition(new LatLng(lat, lng));
					driverMarker.getmMarker().setAnchor(0.5f, 0.5f);
					driverMarker.getmMarker().setRotation(bearing);
					driverMarker.getmMarker().setFlat(true);

					if (t < 1.0) {
						// Post again 16ms later.
						handler.postDelayed(this, 16);
					}
				}
			});
		}

		mPreviousLoc=mCurrentLoc;
/********************************************************************************************************/

	}

	/**
	 * Updating driver location when Driver Journey Started
	 * @param filterResponse
	 */
	private void UpdateDriverLocation_JourneyStarted(PubnubResponseNew filterResponse)
	{
		double driver_current_latitude=Double.parseDouble(filterResponse.getLt());
		double driver_cuttent_longitude=Double.parseDouble(filterResponse.getLg());

		Utility.printLog("INSIDE DRIVER TripBegin:4 current lat=" + driver_current_latitude
				+ " lng=" + driver_cuttent_longitude);
		session.storeLat_DOW(filterResponse.getLt());
		session.storeLon_DOW(filterResponse.getLg());
/********************************************************************************************************/


		Location driverLocation = new Location("starting_point");
		driverLocation.setLatitude(driver_current_latitude);
		driverLocation.setLongitude(driver_cuttent_longitude);

		mCurrentLoc=driverLocation;

		if(mPreviousLoc==null)
		{
			mPreviousLoc=driverLocation;
		}

		final float bearing = mPreviousLoc.bearingTo(mCurrentLoc);
		if(driverMarker.getmMarker()!=null)
		{
			driverMarker.getmMarker().setPosition(new LatLng(driverLocation.getLatitude(), driverLocation.getLongitude()));
			driverMarker.getmMarker().setAnchor(0.5f, 0.5f);
			driverMarker.getmMarker().setRotation(bearing);
			driverMarker.getmMarker().setFlat(true);

			final Handler handler = new Handler();
			final long start = SystemClock.uptimeMillis();
			Projection proj = googleMap.getProjection();
			Point startPoint = proj.toScreenLocation(new LatLng(mPreviousLoc.getLatitude(),mPreviousLoc.getLongitude()));
			final LatLng startLatLng = proj.fromScreenLocation(startPoint);
			final long duration = 500;

			final Interpolator interpolator = new LinearInterpolator();

			handler.post(new Runnable() {
				@Override
				public void run() {
					long elapsed = SystemClock.uptimeMillis() - start;
					float t = interpolator.getInterpolation((float) elapsed
							/ duration);
					double lng = t * mCurrentLoc.getLongitude() + (1 - t)
							* startLatLng.longitude;
					double lat = t * mCurrentLoc.getLatitude() + (1 - t)
							* startLatLng.latitude;
					driverMarker.getmMarker().setPosition(new LatLng(lat, lng));
					driverMarker.getmMarker().setAnchor(0.5f, 0.5f);
					driverMarker.getmMarker().setRotation(bearing);
					driverMarker.getmMarker().setFlat(true);

					if (t < 1.0) {
						// Post again 16ms later.
						handler.postDelayed(this, 16);
					}
				}
			});
		}

		mPreviousLoc=mCurrentLoc;
/********************************************************************************************************/
		LatLng latLng=new LatLng(driver_current_latitude,driver_cuttent_longitude);
		googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

	}

	/**
	 *  Updating invoice screen when appointment completed
	 * @param filterResponse
	 */
	private void AppointmentCompleted_InvoiceRaised(PubnubResponseNew filterResponse)
	{
		if(mActivity!=null)
		{
			Utility.printLog("inside the invoice raised if"+filterResponse.getA());
			//new BackgroundUnSubscribeChannels().execute(session.getCurrentAptChannel());
			String[] subscribed_channels1 = pubnub.getSubscribedChannelsArray();
			Utility.printLog("Appointments details subscribed_channels my channel=" + session.getChannelName());
			ArrayList<String> unsubscribeChannels1= new ArrayList<String>();
			for(int i=0;i<subscribed_channels1.length;i++)
			{
				Utility.printLog("Appointments details subscribed_channels at status 9" + i + " " + subscribed_channels1[i]);


				unsubscribeChannels1.add(subscribed_channels1[i]);

				if(unsubscribeChannels1.size()>0)
				{
					Utility.printLog("Appointments details channels unsubscribeChannels channel list size status 9=" + unsubscribeChannels1.size());
					String[] new_un_sub_channels=new String[unsubscribeChannels1.size()];
					new_un_sub_channels=unsubscribeChannels1.toArray(new_un_sub_channels);
					new BackgroundUnSubscribeChannels().execute(new_un_sub_channels);
				}

			}
			pollingTimer.cancel();
			session.storeBookingId("0");
			Utility.printLog("mActivity if=" + mActivity);
		/*	Intent intent1 = new Intent(mActivity,InvoiceActivity.class);
			mActivity.startActivity(intent1);*/
			if(session.getAptDate()!=null)
			{
				getAppointmentDetails_Volley(session.getAptDate());

			}
		}
		else
		{
			Utility.printLog("inside the invoice raised else"+filterResponse.getA());

			//new BackgroundUnSubscribeChannels().execute(session.getCurrentAptChannel());

			String[] subscribed_channels1 = pubnub.getSubscribedChannelsArray();
			Utility.printLog("Appointments details subscribed_channels my channel=" + session.getChannelName());
			ArrayList<String> unsubscribeChannels1= new ArrayList<String>();
			for(int i=0;i<subscribed_channels1.length;i++)
			{
				Utility.printLog("Appointments details subscribed_channels at status 9" + i + " " + subscribed_channels1[i]);


				unsubscribeChannels1.add(subscribed_channels1[i]);

				if(unsubscribeChannels1.size()>0)
				{
					Utility.printLog("Appointments details channels unsubscribeChannels channel list size status 9=" + unsubscribeChannels1.size());
					String[] new_un_sub_channels=new String[unsubscribeChannels1.size()];
					new_un_sub_channels=unsubscribeChannels1.toArray(new_un_sub_channels);
					new BackgroundUnSubscribeChannels().execute(new_un_sub_channels);
				}

			}

			pollingTimer.cancel();
			session.storeBookingId("0");
			mActivity= getActivity();
			Utility.printLog("mActivity else=" + mActivity);
			/*Intent intent1 = new Intent(mActivity,InvoiceActivity.class);
			mActivity.startActivity(intent1);*/
			if(session.getAptDate()!=null)
			{
				getAppointmentDetails_Volley(session.getAptDate());

			}
		}
	}

	/**
	 *
	 *  Driver cancelled the appointment
	 *
	 *
	 */

	private void DriverCancelledAppointment()
	{
		session.setDriverCancelledApt(false);
		session.setDriverOnWay(false);
		Utility.printLog("Wallah set as false Homepage 5");
		session.setDriverArrived(false);
		session.setInvoiceRaised(false);
		session.setTripBegin(false);
		Toast.makeText(getActivity(), "Driver cancelled the request", Toast.LENGTH_LONG).show();
		//marker_map.clear();
		marker_map_on_the_way.clear();
		Utility.printLog("marker_map_on_the_way 2");
		marker_map_arrived.clear();
		googleMap.clear();

		startPublishingWithTimer();
		Utility.printLog("publishing 4 ");

		VariableConstants.isPubnubCalled=false;


		if(!(getActivity().getActionBar().isShowing()))
		{
			getActivity().getActionBar().show();
		}
		//ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);

		isSetDropoffLocation=false;
		isBackPressed=false;
		isFareQuotePressed=false;
		to_latitude=null;
		to_longitude=null;

		//	Driver_Confirmation.setVisibility(View.INVISIBLE);
		show_address_relative.setVisibility(VISIBLE);
		pickup.setVisibility(VISIBLE);
		Txt_Pickup.setVisibility(VISIBLE);
		Mid_Pointer.setVisibility(VISIBLE);
		Driver_on_the_way_txt.setVisibility(View.INVISIBLE);
		Rl_distance_time.setVisibility(View.GONE);
		now_later_layout.setVisibility(VISIBLE);


	}

	public void getTipOnClick()
	{
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
		//builderSingle.setIcon(R.drawable.ic_launcher);
		builderSingle.setTitle("Select Driver Tip:");
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				getActivity(),
				android.R.layout.select_dialog_singlechoice);

		if(MainActivityDrawer.driver_tip.getText().equals(getResources().getString(R.string.driver_tip)))
		{

			for(int i=0;i<=30;i+=5)
			{
				arrayAdapter.add(i + "%");
			}
		}
		else
		{

			arrayAdapter.add("Remove Tip");
			for(int i=0;i<=30;i+=5)
			{
				arrayAdapter.add(i + "%");



			}
		}

		builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (which == 0)
				{
					if (MainActivityDrawer.driver_tip.getText().equals(getResources().getString(R.string.driver_tip)))
					{
						MainActivityDrawer.driver_tip.setText(arrayAdapter.getItem(which));
						UpdateTipforDriver("" + arrayAdapter.getItem(which));
						MainActivityDrawer.textForTip.setVisibility(VISIBLE);
						MainActivityDrawer.driver_tip.setTextSize(12);
					}
					else
					{
						MainActivityDrawer.driver_tip.setText(getResources().getString(R.string.driver_tip));
						UpdateTipforDriver("0");
						MainActivityDrawer.textForTip.setVisibility(View.GONE);
						MainActivityDrawer.driver_tip.setTextSize(20);
					}
				} else
				{
					MainActivityDrawer.driver_tip.setText(arrayAdapter.getItem(which));
					UpdateTipforDriver("" + arrayAdapter.getItem(which));
					MainActivityDrawer.textForTip.setVisibility(VISIBLE);
					MainActivityDrawer.driver_tip.setTextSize(12);
				}
			}
		});
		builderSingle.show();

	}

	private void  UpdateTipforDriver(final String driverTip) {
		JSONObject jsonObject = new JSONObject();
		final ProgressDialog dialogL= Utility.GetProcessDialogNew(getActivity(), getResources().getString(R.string.please_wait));
		dialogL.setCancelable(true);
		if(dialogL!=null)
		{
			dialogL.show();
		}
		try {

			SessionManager session=new SessionManager(getActivity());
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Utility.printLog("dataandTime=" + curenttime);
			//	Utility.printLog("UpdateTipforDriver ent_amount="+tip);
			Utility.printLog("UpdateTipforDriver getEmail=" + response.getEmail());
			//	Utility.printLog("UpdateTipforDriver getApptDt="+response.getApptDt());

			jsonObject.put("ent_sess_token",session.getSessionToken());
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_tip",driverTip);
			jsonObject.put("ent_booking_id",session.getBookingId());
			jsonObject.put("ent_date_time",curenttime);
			Utility.printLog("UpdateTipforDriver session token=" + session.getSessionToken());
			Utility.printLog("UpdateTipforDriver = " + jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "updateTip", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				JSONObject jsnResponse;
				try
				{
					dialogL.dismiss();
					jsnResponse = new JSONObject(result);
					String mErrNum = jsnResponse.getString("errNum");
					Utility.printLog("jsonErrorParsing is ---> " + mErrNum);

					Toast.makeText(getActivity(),jsnResponse.getString("errMsg"), Toast.LENGTH_SHORT).show();
					Gson gson = new Gson();
					tipResponse = gson.fromJson(result,TipResponse.class);
					Utility.printLog("tipResponse = " + tipResponse.getTip());

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onError(String error) {
				dialogL.dismiss();
				Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
				Utility.printLog("Error for volley");		}
		});
	}

	//to get the address on the homescreen
	class BackgroundGetAddress extends AsyncTask<String, Void, String>
	{
		List<Address> address;
		String lat,lng;
		@Override
		protected String doInBackground(String... params) {


			try {

				lat = params[0];
				lng = params[1];

				if(lat!=null && lng!=null)
				{
					if(getActivity()!=null)
					{
						geocoder=new Geocoder(getActivity());
					}

					if(geocoder!=null)
					{
						Utility.printLog("geocoder adddress inside " + geocoder);
						address=geocoder.getFromLocation(Double.parseDouble(params[0]), Double.parseDouble(params[1]), 1);
						if(address.isEmpty())
						{
							Utility.printLog("geocoder adddress empty " + address);
							new BackgroundGeocodingTask().execute();
						}
					}
				}
			} catch (IOException e) {

				Utility.printLog("geocoder adddress excpetion " + e);
				e.printStackTrace();
				new BackgroundGeocodingTask().execute();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			if(address!=null && address.size()>0)
			{
				Utility.printLog("mmm my addre out side" + address);
				if(!VariableConstants.CONFIRMATION_CALLED)
				{
					pickupLocationAddress.setText(address.get(0).getAddressLine(0)+", "+address.get(0).getAddressLine(1));
					mPICKUP_ADDRESS=pickupLocationAddress.getText().toString();
					Utility.printLog("mmm my addre in side" + address.get(0).getAddressLine(0) + ", " + address.get(0).getAddressLine(1));
					current_address.setText(address.get(0).getAddressLine(0)+", "+address.get(0).getAddressLine(1));
				}
				else
				{
					if(!dropOffSet && cameraMoved)
					{
						to_latitude=lat;
						to_longitude=lng;
						Dropoff_Location_Address.setText(address.get(0).getAddressLine(0)+", "+address.get(0).getAddressLine(1));
						mDROPOFF_ADDRESS = Dropoff_Location_Address.getText().toString();
					}
				}
				Utility.printLog("akbar:location" + mPICKUP_ADDRESS);
			}

		}
	}

	class BackgroundGeocodingTask extends AsyncTask<String, Void, String>
	{
		GeocodingResponse response;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {

			String url="https://maps.googleapis.com/maps/api/geocode/json?latlng="+currentLatitude+","+currentLongitude+"&sensor=false&key="+ VariableConstants.GOOGLE_SERVER_API_KEY;
			final String[] result = new String[1];
			OkHttpClient client = new OkHttpClient();
			Utility.printLog("geocoder adddress url " + url);
			com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
					.url(url)
					.build();
			Response response = null;
			try
			{
				response = client.newCall(request).execute();
				result[0] =response.body().string();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				Utility.printLog("result for exception "+e);
			}
			return result[0];
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			if(result!=null)
			{
				Gson gson=new Gson();
				response=gson.fromJson(result, GeocodingResponse.class);
				if(response!=null)
				{
					Utility.printLog("akbar:location cameraMoved" + cameraMoved);
					if(response.getStatus().equals("OK"))
					{
						if(!VariableConstants.CONFIRMATION_CALLED)
						{
							pickupLocationAddress.setText(response.getResults().get(0).getFormatted_address());
							mPICKUP_ADDRESS=pickupLocationAddress.getText().toString();
							current_address.setText(response.getResults().get(0).getFormatted_address());
						}
						else
						{
							if(!dropOffSet && cameraMoved)
							{
								to_latitude= String.valueOf(currentLatitude);
								to_longitude= String.valueOf(currentLongitude);
								Dropoff_Location_Address.setText(response.getResults().get(0).getFormatted_address());
								mDROPOFF_ADDRESS = Dropoff_Location_Address.getText().toString();
							}
						}

						Utility.printLog("akbar:location" + pickupLocationAddress);
					}
				}
			}
		}
	}

	private void BackgroundUpdateAddress()
	{
		final GetCardResponse[] response = new GetCardResponse[1];

		if(mDialog!=null)
		{
			mDialog.dismiss();
			mDialog = null;
		}
		JSONObject jsonObject=new JSONObject();
		try
		{
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			jsonObject.put("ent_sess_token",session.getSessionToken());
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_booking_id",session.getBookingId());
			jsonObject.put("ent_drop_addr1",mDROPOFF_ADDRESS);
			jsonObject.put("ent_lat",to_latitude);
			jsonObject.put("ent_long",to_longitude);
			jsonObject.put("ent_date_time",curenttime);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL+"updateDropOff", jsonObject, new OkHttpRequestObject.JsonRequestCallback()
		{
			@Override
			public void onSuccess(String result)
			{
				Gson gson = new Gson();
				response[0] =gson.fromJson(result,GetCardResponse.class);
				if (mDialog!=null)
				{
					mDialog.dismiss();
					mDialog.cancel();
					mDialog = null;
				}
				if(response[0] !=null)
				{
					if(response[0].getErrFlag().equals("0"))
					{
						Utility.GetProcessDialogNew(getActivity(), "Thank you for confirming your destination address on update drop off location");
						new_dropoff_location_address.setText(mDROPOFF_ADDRESS);
						//new_add_drop_off_location.setVisibility(View.GONE);
						if(!isdropoffclicked)
						{
							farecotemethod(mPICKUP_ADDRESS, mDROPOFF_ADDRESS, from_latitude, from_longitude, to_latitude,to_longitude,Car_Type_Id);

						}
					}
					else
					{

					}
				}
				else
				{
					Utility.ShowAlert(getResources().getString(R.string.server_error), getActivity());
				}
			}

			@Override
			public void onError(String error)
			{
				if (mDialog!=null)
				{
					mDialog.dismiss();
					mDialog.cancel();
					mDialog = null;
				}
				Utility.printLog("on error for the login "+error);
				Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}

	private void farecotemethod(String mPICKUP_address, String mDROPOFF_address, final String from_latitude,
								final String from_longitude, final String to_latitude, final String to_longitude, final String car_type_id)
	{
		JSONObject jsonObject = new JSONObject();
		final ProgressDialog dialogL;
		dialogL=Utility.GetProcessDialog(getActivity());

		if (dialogL!=null) {
			dialogL.show();
		}
		try {
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			jsonObject.put("ent_sess_token",session.getSessionToken() );
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_type_id",car_type_id);
			jsonObject.put("ent_curr_lat",String.valueOf(currentLatitude));
			jsonObject.put("ent_curr_long",String.valueOf(currentLongitude));
			jsonObject.put("ent_from_lat",from_latitude);
			jsonObject.put("ent_from_long",from_longitude);
			jsonObject.put("ent_to_lat",to_latitude);
			jsonObject.put("ent_to_long",to_longitude);
			jsonObject.put("ent_date_time",curenttime);
			Log.i("HOmefrag","Farecotereq "+jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "fareCalculator", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				Gson gson = new Gson();
				getFareResponse = result;
				getFare = gson.fromJson(getFareResponse, FareCalculation.class);
				Utility.printLog("Success of getting user Info"+response);
				Log.i("HOmefrag", "Farecoteres " + response);
				getUserInfo(dialogL,getFare);

			}
			@Override
			public void onError(String error) {
				dialogL.dismiss();
				Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();		}
		});
	}

	private void getUserInfo(ProgressDialog dialogL,FareCalculation getFare)
	{
		dialogL.dismiss();
		Utility.printLog("response in gaye fare "+getFare);
		if(getFare!=null)
		{
			if(getFare.getErrFlag().equals("0"))
			{
				faremount=getFare.getFare();
				Fare_Quote.setText(getFare.getFare()+" "+getResources().getString(R.string.currencuSymbol));
			}
			else
			{
				Toast.makeText(getActivity(), getFare.getErrMsg(),Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

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

	class  getETA extends AsyncTask<String, Void, String>
	{
		TimeForNearestPojo response;

		@Override
		protected String doInBackground(String... params)
		{
			String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+params[0]+","+params[1]+"&destinations="+params[2]+","+params[3];
			final String[] result = new String[1];
			OkHttpClient client = new OkHttpClient();

			com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
					.url(url)
					.build();
			Response response = null;
			try
			{
				response = client.newCall(request).execute();
				result[0] =response.body().string();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				Utility.printLog("result for exception "+e);
			}
			return result[0];
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			Utility.printLog("result for thef time "+result);
			if(result!=null)
			{
				Gson gson=new Gson();
				response=gson.fromJson(result, TimeForNearestPojo.class);
				if(response!=null)
				{
					Utility.printLog("get eta status" + response.getStatus());

					if(response.getStatus().equals("OK"))
					{
						Utility.printLog("Test ETA DIstance: " + eta_latitude);
						Utility.printLog("Test ETA Value: " + eta_longitude);
						try
						{
							pubnubProgressDialog.setVisibility(View.INVISIBLE);
							pickup_Distance.setVisibility(VISIBLE);
							String etaTime=response.getRows().get(0).getElements().get(0).getDuration().getText();
							etaTime = etaTime.replaceAll("[^\\d.]", "");
							etaTime.replace(" ", "");
							rate_unit.setVisibility(VISIBLE);
							rate_unit.setText(getResources().getString(R.string.minute));
							pickup_Distance.setText(etaTime);

						}
						catch (Exception e)
						{
							e.printStackTrace();
						}


					}
					else
					{
						rate_unit.setText(getResources().getString(R.string.exceed));

					}
				}
			}
		}
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}
	};

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

	class  getEtaWIthTimer extends AsyncTask<String, Void, String>
	{
		TimeForNearestPojo response;
		@Override
		protected String doInBackground(String... params)
		{

			String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+params[0]+","+params[1]+"&destinations="+params[2]+","+params[3];
			final String[] result = new String[1];
			OkHttpClient client = new OkHttpClient();

			com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
					.url(url)
					.build();
			Response response = null;
			try
			{
				response = client.newCall(request).execute();
				result[0] =response.body().string();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				Utility.printLog("result for exception "+e);
			}
			return result[0];
		}

		@Override
		protected void onPostExecute(String result)
		{

			super.onPostExecute(result);

			if(result!=null)
			{
				Gson gson=new Gson();
				response=gson.fromJson(result, TimeForNearestPojo.class);
				Utility.printLog("get eta status" + response.getStatus());

				if(response.getStatus().equals("OK"))
				{
					Utility.printLog("Test ETA DIstance: " + eta_latitude);
					Utility.printLog("Test ETA Value: " + eta_longitude);
					try
					{
						pubnubProgressDialog.setVisibility(View.INVISIBLE);
						pickup_Distance.setVisibility(VISIBLE);
						String etaTime=response.getRows().get(0).getElements().get(0).getDuration().getText();
						etaTime = etaTime.replaceAll("[^\\d.]", "");
						etaTime.replace(" ", "");
						rate_unit.setVisibility(VISIBLE);
						eta_text.setText("Time :"+" "+etaTime+" Min");
						eta_textcom.setText("Time :"+" "+etaTime+" Min");

						String etaDist=response.getRows().get(0).getElements().get(0).getDistance().getValue();
						double newDist=Double.parseDouble(etaDist)/1000;

						Utility.printLog("new distanc for eta "+roundToOneDigit(newDist));
						driver_dist.setText("Distance :"+" "+roundToOneDigit(newDist)+" "+getResources().getString(R.string.distanceUnit));
						driver_distcom.setText("Distance :"+" "+roundToOneDigit(newDist)+" "+getResources().getString(R.string.distanceUnit));


					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}

			}
		}
	}

	public static String roundToOneDigit(double paramFloat) {
		return String.format("%.1f", paramFloat);//		String s = String.format("%.2f", value);

	}
}