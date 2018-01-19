package com.roadyo.passenger.main;

import java.util.ArrayList;

import com.roadyo.passenger.pojo.Support_childs;
import com.ourcabby.passenger.R;
import com.threembed.utilities.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Support_ChildActivity extends Activity implements OnItemClickListener,OnClickListener {
	private ArrayList<Support_childs> child_list;
	private ListView listview ;
	private TextView title;
	private Support_adapterChild adapter;
	private ImageButton back;
	private RelativeLayout rl_TermsandCond;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{ 		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.support_child);
		initialization();
	}

	public void initialization() 
	{		
		back=(ImageButton)findViewById(R.id.back_button_terms);
		rl_TermsandCond=(RelativeLayout)findViewById(R.id.rl_TermsandCond);
		child_list=new ArrayList<Support_childs>();
		child_list = (ArrayList<Support_childs>)getIntent().getSerializableExtra("list");

		listview = (ListView) findViewById(R.id.supportChild_list);
		adapter = new Support_adapterChild(Support_ChildActivity.this, R.layout.support_list_row, child_list);
		if(child_list.size()>0)
		{
			listview.setAdapter(adapter);
		}

		listview.setOnItemClickListener(this);
		title=(TextView) findViewById(R.id.title);
		title.setText(getIntent().getStringExtra("title"));

		back.setOnClickListener(this);
		rl_TermsandCond.setOnClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent intent=new Intent(Support_ChildActivity.this,SupportWebActivity.class);
		intent.putExtra("URL",child_list.get(position).getLink());
		startActivity(intent);
		overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.back_button_terms)
		{
			finish();
			overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
		}
		if(v.getId()==R.id.rl_TermsandCond)
		{
			finish();
			overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
		}
		
	}





}