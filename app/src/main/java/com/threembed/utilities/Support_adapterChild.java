package com.threembed.utilities;

import java.util.ArrayList;
import java.util.List;

import com.roadyo.passenger.pojo.Support_childs;
import com.roadyo.passenger.pojo.Support_values;
import com.ourcabby.passenger.R;

import eu.janmuller.android.simplecropimage.Util;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
public class Support_adapterChild extends ArrayAdapter<Support_childs>
{
	
	
	private Context context;
	private ArrayList<Support_childs> supportList=new ArrayList<Support_childs>();

	public Support_adapterChild(Context context, int resource,
			ArrayList<Support_childs> objects) {
		
		super(context, R.layout.support_list_row, objects);
		this.context=context;
		this.supportList=objects;
         Utility.printLog("aaaaa"+supportList.size());
	}


	/*private view holder class*/
	private class ViewHolder
	{

		TextView text;

	}

	@Override
	public Support_childs getItem(int position){
		return supportList.get(position);
	}

	@Override
	public int getCount() {

		return supportList.size();
	}


	/*********************************************************************************************************************/

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		//final Support_values supportlist = supportList.get(position);
		ViewHolder holder = null;
		Utility.printLog("aaaaa"+"I aM IN get view");




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
		if(position>=0)
		{
			holder.text.setText(supportList.get(position).getTag());

		}
		Utility.printLog("aaaaa support getview list"+supportList.get(position).getTag());



		return convertView;

	}





}
