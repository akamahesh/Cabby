package com.roadyo.passenger.main;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.roadyo.passenger.pojo.AddCardResponse;
import com.ourcabby.passenger.R;
import com.threembed.utilities.OkHttpRequestObject;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

public class DeleteCardActivity extends Activity implements OnClickListener
{
	private TextView card_numb_text,exp_text;
	private ImageView card_img;
	private Button delete;
	private RelativeLayout back;
	private String id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.delete_card);
		
		intializeVariables();
		
		Bundle bundle=getIntent().getExtras();
		card_numb_text.setText(bundle.getString("NUM"));
		exp_text.setText(bundle.getString("EXP"));
		byte[] byteArray = bundle.getByteArray("IMG");
		id=bundle.getString("ID");
		if(byteArray!=null)
			card_img.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
		
	}

	private void intializeVariables() {
		// TODO Auto-generated method stub
		card_numb_text=(TextView)findViewById(R.id.card_numb_delete);
		exp_text=(TextView)findViewById(R.id.exp_date_delete);
		delete=(Button)findViewById(R.id.delete_card_btn);
		card_img=(ImageView)findViewById(R.id.card_img_delete);
		back =(RelativeLayout)findViewById(R.id.rl_deletecard);
		
		delete.setOnClickListener(this);
		back.setOnClickListener(this);
		
	}
	private void BackgroundDeleteCard() {
		JSONObject jsonObject = new JSONObject();
		final AddCardResponse[] response = new AddCardResponse[1];
		final ProgressDialog dialogL=com.threembed.utilities.Utility.GetProcessDialogNew(DeleteCardActivity.this,getResources().getString(R.string.deleting_card));

		if (dialogL!=null) {
			dialogL.show();
		}
		try {
			SessionManager session=new SessionManager(DeleteCardActivity.this);
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			jsonObject.put("ent_sess_token",session.getSessionToken() );
			jsonObject.put("ent_dev_id",session.getDeviceId());
			jsonObject.put("ent_cc_id",id);
			jsonObject.put("ent_date_time",curenttime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "removeCard", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				if(result!=null)
				{
					Gson gson = new Gson();
					response[0] =gson.fromJson(result,AddCardResponse.class);
					if (dialogL!=null) {
						dialogL.dismiss();
					}

					if(response[0] !=null)
					{

						if(response[0].getErrFlag().equals("0"))
						{
							Toast.makeText(DeleteCardActivity.this,getResources().getString(R.string.card_removed), Toast.LENGTH_SHORT).show();
							Intent intent=new Intent();
							//intent.putExtra("RESPONSE",response);
							setResult(RESULT_OK,intent);
							finish();
							overridePendingTransition(R.anim.anim_three, R.anim.anim_four);
						}
						else
						{
							Utility.ShowAlert(response[0].getErrMsg(),DeleteCardActivity.this);
						}
					}
					else
					{
						Utility.ShowAlert(getResources().getString(R.string.server_error),DeleteCardActivity.this);
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
				Toast.makeText(DeleteCardActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(v.getId()==R.id.delete_card_btn)
		{
			BackgroundDeleteCard();
			
			return;
		}
		
		if(v.getId()==R.id.rl_deletecard)
		{
			
			finish();
			overridePendingTransition(R.anim.anim_three, R.anim.anim_four); 
		}
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
