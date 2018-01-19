package com.roadyo.passenger.main;

import io.card.payment.CardType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.roadyo.passenger.pojo.GetCardResponse;
import com.roadyo.passenger.pojo.card_info_pojo;
import com.ourcabby.passenger.R;
import com.threembed.utilities.OkHttpRequestObject;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

public class PaymentFragment extends Fragment implements OnClickListener,OnItemClickListener
{
	private View view;
	private ListView card_list;
	private RelativeLayout add_cc_bt,Stripe_Paypal;
	private GetCardResponse response;
	private CustomListViewAdapter adapter;
	private List<card_info_pojo> rowItems;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
	}

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
			view = inflater.inflate(R.layout.payments_xml, container, false);
		} catch (InflateException e)
		{
			/* map is already there, just return view as it is */
			Log.e("", "onCreateView  InflateException "+e);
		}

		initializeVariables(view);

		if(Utility.isNetworkAvailable(getActivity()))
		{
			BackgroundGetCards();
		}
		else
		{
			Intent homeIntent=new Intent("com.ourcabby.passenger.internetStatus");
			homeIntent.putExtra("STATUS", "0");
			getActivity().sendBroadcast(homeIntent);
			
			Utility.ShowAlert("No network connection", getActivity());
		}
		
		return view;
	}

	private void initializeVariables(View view2) 
	{
		card_list=(ListView)view2.findViewById(R.id.cards_list_view);//cards_list_view
		rowItems = new ArrayList<card_info_pojo>();
		adapter = new CustomListViewAdapter(getActivity(),R.layout.card_list_row, rowItems);
		card_list.setAdapter(adapter);

		LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View fotter_view = inflater.inflate(R.layout.add_card_fotter, null);
		card_list.addFooterView(fotter_view);
		add_cc_bt=(RelativeLayout)fotter_view.findViewById(R.id.add_card_rel_fotter);
		Stripe_Paypal = (RelativeLayout)view2.findViewById(R.id.stripe_paypal);

		add_cc_bt.setOnClickListener(this);
		card_list.setOnItemClickListener(this);
		rowItems = new ArrayList<card_info_pojo>();

		MainActivityDrawer.driver_tip.setVisibility(View.INVISIBLE);

	}

	private void BackgroundGetCards() {
		final JSONObject jsonObject = new JSONObject();
		final ProgressDialog dialogL=com.threembed.utilities.Utility.GetProcessDialog(getActivity());
		if (dialogL!=null)
		{
			dialogL.show();
		}
		try {
			SessionManager session=new SessionManager(getActivity());
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			jsonObject.put("ent_sess_token",session.getSessionToken() );
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_date_time",curenttime);

			Utility.printLog("params to login "+jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "getCards", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				if (dialogL!=null) {
					dialogL.dismiss();
				}

				if(result!=null)
				{
					Gson gson = new Gson();
					response=gson.fromJson(result,GetCardResponse.class);
					if(response!=null)
					{
						if(response.getErrFlag().equals("0") && isAdded())
						{
							//LayoutParams cardLinearLayout_params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
							rowItems.clear();
							for(int i=0;i<response.getCards().size();i++)
							{
								Bitmap bitmap=setCreditCardLogo(response.getCards().get(i).getType());

								card_info_pojo item = new card_info_pojo(bitmap,response.getCards().get(i).getLast4(),response.getCards().get(i).getExp_month(),
										response.getCards().get(i).getExp_year(),response.getCards().get(i).getId());
								rowItems.add(item);
							}
							adapter = new CustomListViewAdapter(getActivity(),
									R.layout.card_list_row, rowItems);
							card_list.setAdapter(adapter);

							Stripe_Paypal.setVisibility(View.VISIBLE);
						}
						else if(response.getErrNum().equals("6") || response.getErrNum().equals("7") ||
								response.getErrNum().equals("94") || response.getErrNum().equals("96"))
						{
							Toast.makeText(getActivity(), response.getErrMsg(),Toast.LENGTH_SHORT).show();
							Intent i = new Intent(getActivity(), SplashActivity.class);
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							getActivity().startActivity(i);
							getActivity().overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
						}
						else if(isAdded())
						{
							if(response.getErrNum().equals("51"))
							{
								rowItems.clear();
								adapter = new CustomListViewAdapter(getActivity(),
										R.layout.card_list_row, rowItems);
								card_list.setAdapter(adapter);
							}

							if(response.getErrMsg()!=null)
							{
								Toast.makeText(getActivity(), response.getErrMsg(), Toast.LENGTH_SHORT).show();
							}
						}
					}
					else if(isAdded())
					{
						Utility.ShowAlert("Server error!!",getActivity());
					}
				}
			}
			@Override
			public void onError(String error) {
				if (dialogL!=null) {
					dialogL.dismiss();
				}


			}
		});
	}

	private Bitmap setCreditCardLogo(String type) {
		// TODO Auto-generated method stub

		CardType cardType;
		if(type.equals("Visa"))
		{
			cardType=CardType.VISA;
			Bitmap bitmap=cardType.imageBitmap(getActivity());
			//cardLogo.setImageBitmap(bitmap);
			return bitmap;
		}
		if(type.equals("MasterCard"))
		{
			cardType=CardType.MASTERCARD;
			Bitmap bitmap=cardType.imageBitmap(getActivity());
			//cardLogo.setImageBitmap(bitmap);
			return bitmap;
		}
		if(type.equals("American Express"))
		{
			cardType=CardType.AMEX;
			Bitmap bitmap=cardType.imageBitmap(getActivity());
			//cardLogo.setImageBitmap(bitmap);
			return bitmap;
		}
		if(type.equals("Discover"))
		{
			cardType=CardType.DISCOVER;
			Bitmap bitmap=cardType.imageBitmap(getActivity());
			//cardLogo.setImageBitmap(bitmap);
			return bitmap;
		}

		if(type.equals("JCB"))
		{
			cardType=CardType.JCB;
			Bitmap bitmap=cardType.imageBitmap(getActivity());
			//cardLogo.setImageBitmap(bitmap);
			return bitmap;
		}
		cardType=CardType.UNKNOWN;
		Bitmap bitmap=cardType.imageBitmap(getActivity());
		//cardLogo.setImageBitmap(bitmap);
		return bitmap;
	}

	class CustomListViewAdapter extends ArrayAdapter<card_info_pojo> {

		Context context;

		public CustomListViewAdapter(Context context, int resourceId,
				List<card_info_pojo> items) {
			super(context, resourceId, items);
			this.context = context;
		}


		private class ViewHolder {
			ImageView card_image;
			TextView card_numb;
			RelativeLayout payment_relative;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			ViewHolder holder = null;
			card_info_pojo rowItem = getItem(position);

			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			if (convertView == null) 
			{
				convertView = mInflater.inflate(R.layout.card_list_row, null);
				holder = new ViewHolder();

				holder.card_numb = (TextView) convertView.findViewById(R.id.card_numb_row);
				holder.card_image = (ImageView) convertView.findViewById(R.id.card_img_row);
				holder.payment_relative=(RelativeLayout)convertView.findViewById(R.id.payment_relative);
				convertView.setTag(holder);
			}
			else
				holder = (ViewHolder) convertView.getTag();

			if(position==0)
			{
				holder.payment_relative.setBackgroundResource(R.drawable.selectpayment_listview_top);
			}
			else
			{
				holder.payment_relative.setBackgroundResource(R.drawable.selectpayment_listview_bottom);
			}

			holder.card_image.setImageBitmap(rowItem.getCard_image());
			holder.card_numb.setText(rowItem.getCard_numb());

			return convertView;
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) 
	{


		if(v.getId()==R.id.add_card_rel_fotter)
		{
			Intent intent =new Intent(getActivity(),AddCardActivity.class);
			startActivityForResult(intent,1);
			getActivity().overridePendingTransition(R.anim.anim_two, R.anim.anim_one); 
		}
	}
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 1)
		{
			if(resultCode==Activity.RESULT_OK)
			{ 
				/*response=(GetCardResponse)getActivity().getIntent().getSerializableExtra("RESPONSE");
				 Utility.printLog("","res in activity result "+response);
				 Utility.printLog("",""+response);
				 adapter.notifyDataSetChanged();*/
				 BackgroundGetCards();
			}
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


		card_info_pojo row_details =(card_info_pojo)card_list.getItemAtPosition(arg2);

		Utility.printLog("Card count: "+response.getCards().size());


		String expDate=row_details.getExp_month();

		if(expDate.length()==1)
		{
			expDate="0"+expDate;
		}

		expDate=expDate+"/"+row_details.getExp_year();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		row_details.getCard_image().compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();


		Intent intent=new Intent(getActivity(), DeleteCardActivity.class);
		intent.putExtra("NUM",row_details.getCard_numb());
		intent.putExtra("EXP",expDate);
		intent.putExtra("IMG",byteArray);
		intent.putExtra("ID",row_details.getCard_id());
		intent.putExtra("COUNT",response.getCards().size());
		startActivityForResult(intent, 1);
		getActivity().overridePendingTransition(R.anim.anim_two, R.anim.anim_one);


	}






}
