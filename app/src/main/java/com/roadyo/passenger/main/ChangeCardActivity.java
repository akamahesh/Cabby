package com.roadyo.passenger.main;

import io.card.payment.CardType;

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
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.roadyo.passenger.pojo.GetCardResponse;
import com.roadyo.passenger.pojo.card_info_pojo;
import com.ourcabby.passenger.R;
import com.threembed.utilities.OkHttpRequestObject;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

public class ChangeCardActivity extends Activity implements OnClickListener,OnItemClickListener{
	
	private ListView card_list;
	private Button save;
	
	GetCardResponse response;
	CustomListViewAdapter adapter;
	List<card_info_pojo> rowItems;
	private Bitmap card_bitmap;
	private String card_numb,card_id;
	private boolean isCardChanged=false;
	private RelativeLayout add_cc_bt;
	private ImageButton back_btn;
	private RelativeLayout Rl_change_card;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.change_card);
		initializeVariables();
		BackgroundGetCards();
	}

	private void initializeVariables() {
		
		
		save=(Button)findViewById(R.id.save_change_card_btn);
		card_list=(ListView)findViewById(R.id.change_cards_list_view);
		Rl_change_card=(RelativeLayout)findViewById(R.id.rl_change_card);
		save.setText("Save");
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View fotter_view = inflater.inflate(R.layout.add_card_fotter, null);
		card_list.addFooterView(fotter_view);
		add_cc_bt=(RelativeLayout)fotter_view.findViewById(R.id.add_card_rel_fotter);
		
		back_btn = (ImageButton) findViewById(R.id.login_back_button);
		//Txt_Select_Payment =(TextView) findViewById(R.id.txt_select_payment);
		//Txt_Select_Payment.setPaintFlags(Txt_Select_Payment.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		
		back_btn.setOnClickListener(this);
		add_cc_bt.setOnClickListener(this);
		card_list.setOnItemClickListener(this);
		save.setOnClickListener(this);
		Rl_change_card.setOnClickListener(this);
		
		rowItems = new ArrayList<card_info_pojo>();
		
		
	}

	private void BackgroundGetCards() {
		JSONObject jsonObject = new JSONObject();
		final ProgressDialog dialogL=com.threembed.utilities.Utility.GetProcessDialog(ChangeCardActivity.this);
		if (dialogL!=null)
		{
			dialogL.show();
		}
		try {
			SessionManager session=new SessionManager(ChangeCardActivity.this);
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			jsonObject.put("ent_sess_token",session.getSessionToken() );
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_date_time",curenttime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "getCards", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				if(result!=null)
				{
					Gson gson = new Gson();
					response=gson.fromJson(result,GetCardResponse.class);
					if (dialogL!=null)
					{
						dialogL.dismiss();
					}

					if(response!=null)
					{

						if(response.getErrFlag().equals("0"))
						{

							rowItems.clear();
							for(int i=0;i<response.getCards().size();i++)
							{
								Bitmap bitmap=setCreditCardLogo(response.getCards().get(i).getType());


								card_info_pojo item = new card_info_pojo(bitmap,response.getCards().get(i).getLast4(),response.getCards().get(i).getExp_month(),
										response.getCards().get(i).getExp_year(),response.getCards().get(i).getId());
								rowItems.add(item);


							}
							adapter = new CustomListViewAdapter(ChangeCardActivity.this,
									R.layout.change_card_list_row, rowItems);

							card_list.setAdapter(adapter);


						}
						else
						{
							if(response.getErrNum().equals("51"))
							{
								rowItems.clear();
								adapter = new CustomListViewAdapter(ChangeCardActivity.this,
										R.layout.change_card_list_row, rowItems);
								card_list.setAdapter(adapter);
							}

							if(response.getErrMsg()!=null)
							{
								Utility.ShowAlert(response.getErrMsg(),ChangeCardActivity.this);
							}
						}


					}
					else
					{
						Utility.ShowAlert(getResources().getString(R.string.server_error),ChangeCardActivity.this);
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
				Toast.makeText(ChangeCardActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}


	private Bitmap setCreditCardLogo(String type)
	{

		CardType cardType;
		if(type.equals("Visa"))
		{
			cardType=CardType.VISA;
			Bitmap bitmap=cardType.imageBitmap(ChangeCardActivity.this);
			//cardLogo.setImageBitmap(bitmap);
			return bitmap;
		}
		if(type.equals("MasterCard"))
		{
			cardType=CardType.MASTERCARD;
			Bitmap bitmap=cardType.imageBitmap(ChangeCardActivity.this);
			//cardLogo.setImageBitmap(bitmap);
			return bitmap;
		}
		if(type.equals("American Express"))
		{
			cardType=CardType.AMEX;
			Bitmap bitmap=cardType.imageBitmap(ChangeCardActivity.this);
			//cardLogo.setImageBitmap(bitmap);
			return bitmap;
		}
		if(type.equals("Discover"))
		{
			cardType=CardType.DISCOVER;
			Bitmap bitmap=cardType.imageBitmap(ChangeCardActivity.this);
			//cardLogo.setImageBitmap(bitmap);
			return bitmap;
		}

		if(type.equals("JCB"))
		{
			cardType=CardType.JCB;
			Bitmap bitmap=cardType.imageBitmap(ChangeCardActivity.this);
			//cardLogo.setImageBitmap(bitmap);
			return bitmap;
		}
		cardType=CardType.UNKNOWN;
		Bitmap bitmap=cardType.imageBitmap(ChangeCardActivity.this);
		//cardLogo.setImageBitmap(bitmap);
		return bitmap;

	}
	
	class CustomListViewAdapter extends ArrayAdapter<card_info_pojo> {
		
		Context context;
		int selectedPosition = 0;
	    public CustomListViewAdapter(Context context, int resourceId,
	            List<card_info_pojo> items) {
	        super(context, resourceId, items);
	        this.context = context;
	    }
	    
	    
	    private class ViewHolder {
	        ImageView card_image;
	        TextView card_numb;
	        RadioButton radioButton;
	        RelativeLayout change_card_relative;
	       
	    }


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			 ViewHolder holder = null;
			 final card_info_pojo rowItem = getItem(position);
		     
			 LayoutInflater mInflater = (LayoutInflater) context
             .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		     
			 if (convertView == null) 
			 {
				 
				 convertView = mInflater.inflate(R.layout.change_card_list_row, null);
				 holder = new ViewHolder();
				 
				 holder.card_numb = (TextView) convertView.findViewById(R.id.card_numb_row_change);
				 holder.card_image = (ImageView) convertView.findViewById(R.id.card_img_row_change);
				 holder.radioButton=(RadioButton)convertView.findViewById(R.id.radio_card_change);
				 holder.change_card_relative=(RelativeLayout)convertView.findViewById(R.id.change_card_relative);
				 convertView.setTag(holder);
				 
				 
			 }
			 else
				 holder = (ViewHolder) convertView.getTag();
			 
			 
			 if(position==0)
				{
					holder.change_card_relative.setBackgroundResource(R.drawable.choose_card_top_list_selector);
					holder.radioButton.setVisibility(View.VISIBLE);
				}
				else
				{
					holder.change_card_relative.setBackgroundResource(R.drawable.fotter_payment_selector);
					holder.radioButton.setVisibility(View.INVISIBLE);
				}
			 
			 holder.card_image.setImageBitmap(rowItem.getCard_image());
			 holder.card_numb.setText(rowItem.getCard_numb());
			 
				holder.radioButton.setChecked(position == selectedPosition);
				holder.radioButton.setTag(position);
				holder.radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    	isCardChanged=true;
                        selectedPosition = (Integer)view.getTag();
                       // booked_time=rowItem.getTime();
            			card_bitmap=rowItem.getCard_image();
            			card_id=rowItem.getCard_id();
            			card_numb=rowItem.getCard_numb();
                       // Utility.printLog("","displaySelected onClick position "+position);
                        //notifyDataSetInvalidated();
                        notifyDataSetChanged();
                    }
                });
				
				holder.change_card_relative.setOnClickListener(new View.OnClickListener()
				{
					
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub

						Intent returnIntent = new Intent();
						 returnIntent.putExtra("Image",rowItem.getCard_image());
						 returnIntent.putExtra("ID",rowItem.getCard_id());
						 returnIntent.putExtra("NUMB",rowItem.getCard_numb()); 
						 setResult(RESULT_OK,returnIntent);     
						 finish();
						 overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
					}
				});
		     
			return convertView;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1)
		{

			if(resultCode==Activity.RESULT_OK)
			{ 
				/*response=(GetCardResponse)getActivity().getIntent().getSerializableExtra("RESPONSE");
				 Utility.printLog("","res in activity result "+response);
				 Utility.printLog("",""+response);
				 adapter.notifyDataSetChanged();*/
				if(Utility.isNetworkAvailable(ChangeCardActivity.this)){
					 BackgroundGetCards();
				}
				else{
					Toast.makeText(ChangeCardActivity.this,getResources().getString(R.string.network_connection_fail), Toast.LENGTH_LONG).show();
				}
			}


		}

	}

	
	@Override
	public void onClick(View v) {
		
		if(v.getId()==R.id.add_card_rel_fotter)
		{
			Intent intent =new Intent(ChangeCardActivity.this,AddCardActivity.class);
			startActivityForResult(intent,1);
			overridePendingTransition(R.anim.anim_two, R.anim.anim_one); 


		}
		
		if(v.getId()==R.id.save_change_card_btn)
		{
			if(isCardChanged){
			Intent returnIntent = new Intent();
			 returnIntent.putExtra("Image",card_bitmap);
			 returnIntent.putExtra("ID",card_id);
			 returnIntent.putExtra("NUMB",card_numb); 
			 setResult(RESULT_OK,returnIntent);     
			 finish();
			 overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
			}
			else
			{
				finish();
				overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
			}
			
		}
		
		if(v.getId()==R.id.login_back_button)
		{
			finish();
			overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
		}
		if(v.getId()==R.id.rl_change_card)
		{
			finish();
			overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
		}
		
		
		
		
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		card_info_pojo row_details =(card_info_pojo)card_list.getItemAtPosition(position);

	}
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
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
