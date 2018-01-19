package com.threembed.utilities;

import java.util.ArrayList;

import com.roadyo.passenger.pojo.Support_values;
import com.ourcabby.passenger.R;

import eu.janmuller.android.simplecropimage.Util;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
public class Support_adapter extends ArrayAdapter<Support_values>
{
	private Context context;
	private ArrayList<Support_values> supportList=new ArrayList<Support_values>();

	public Support_adapter(Context context,int resourceId, ArrayList<Support_values> supportList)
	{
		super(context,R.layout.support_list_row,supportList);
		this.context=context;
		this.supportList=supportList;
		Utility.printLog("support size 1 "+supportList.size());
		//Utility.printLog("support size"+supportList.get(0).getTag());

	}



	/*private view holder class*/
	private class ViewHolder
	{

		TextView text;

	}
 
	@Override
	public Support_values getItem(int position){
		return supportList.get(position);
	}
	
	@Override
	public int getCount() {
	int size=	supportList.size();
	
		return size;
	}

	
	/*********************************************************************************************************************/

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		Utility.printLog("support"+"I aM IN get view");
		Utility.printLog("support getview"+supportList.get(1).getTag());
	


		//convertView.setEnabled(false);

		if(convertView==null||convertView.getTag()==null)
		{
			holder = new ViewHolder();
			// inflate the layout
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.support_list_row, parent, false);
			holder.text=(TextView) convertView.findViewById(R.id.list_row_text);
			convertView.setTag(holder);

		}
		else
			holder = (ViewHolder) convertView.getTag();
		holder.text.setText(supportList.get(position).getTag());
		Utility.printLog("support getview list"+supportList.get(position).getTag());

		 

		return convertView;

	}





}
