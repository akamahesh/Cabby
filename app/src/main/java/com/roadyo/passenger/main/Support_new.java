package com.roadyo.passenger.main;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.roadyo.passenger.pojo.Support_childs;
import com.roadyo.passenger.pojo.Support_new_pojo;
import com.roadyo.passenger.pojo.Support_values;
import com.threembed.utilities.OkHttpRequestObject;
import com.threembed.utilities.Support_adapter;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;
import com.ourcabby.passenger.R;

import org.json.JSONException;
import org.json.JSONObject;


public class Support_new extends Fragment implements OnItemClickListener{
	private ListView listView;
	private ArrayList<Support_values> supportview =new ArrayList<Support_values>();
	private Support_adapter adapter;
	private ProgressDialog dialogL;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view=inflater.inflate(R.layout.support_new,container,false);
		listView = (ListView) view.findViewById(R.id.supportValues_list);
		adapter = new Support_adapter(getActivity(), R.layout.support_list_row, supportview);

	    listView.setAdapter(adapter);
	    listView.setOnItemClickListener(this);
    	dialogL=Utility.GetProcessDialog(getActivity());
		dialogL.show();

		MainActivityDrawer.driver_tip.setVisibility(View.INVISIBLE);


		if(Utility.isNetworkAvailable(getActivity()))
	   {
			callSupportService();

	   }
      
	   else
	   {
		   getResources().getString(R.string.network_connection_fail);
	   }

	
		return view;

	}

	private void callSupportService() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("ent_lan","1");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpRequestObject.postRequest(VariableConstants.BASE_URL + "support", jsonObject, new OkHttpRequestObject.JsonRequestCallback() {
			@Override
			public void onSuccess(String result) {
				if(result!=null)
				{
					Utility.printLog("Support response = "+result);
					Gson gson = new Gson();
					Support_new_pojo support_pojo = gson.fromJson(result, Support_new_pojo.class);
					if(support_pojo.getErrFlag().equals("0"))
					{
						dialogL.cancel();
						supportview.clear();
						supportview.addAll(support_pojo.getSupport());

						adapter.notifyDataSetChanged();
						Utility.printLog("support count"+adapter.getCount());

						Utility.printLog("support"+support_pojo.getErrMsg());
						Utility.printLog("support size "+support_pojo.getSupport().size());
						Utility.printLog("support size list"+supportview.size());
						Utility.printLog("support Tag"+supportview.get(0).getTag());
					}
					else{
						dialogL.cancel();
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

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
										getActivity().finish();
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
				Utility.printLog("on error for the login "+error);
				Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
			}
		});
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		ArrayList<Support_childs> list=new ArrayList<Support_childs>();
		list=supportview.get(position).getChilds();
		Utility.printLog("clicked" + position);
		if(position==4 || position==5)
		{
			Intent intent=new Intent(getActivity(),SupportWebActivity.class);
			intent.putExtra("URL",supportview.get(position).getLink());
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
		}
		else
		{
			Intent intent=new Intent(getActivity(),Support_ChildActivity.class);
			intent.putExtra("list", list);
			intent.putExtra("title", supportview.get(position).getTag());
			Utility.printLog("clicked title"+supportview.get(position).getTag() );
			startActivity(intent);

		}
		
	}
	

}