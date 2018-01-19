package com.roadyo.passenger.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ourcabby.passenger.R;
import com.google.gson.Gson;
import com.roadyo.passenger.pojo.WalletHistoryPojo;
import com.roadyo.passenger.pojo.WalletList;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.threembed.utilities.OkHttpRequest;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

import java.util.ArrayList;

public class WalletHistory extends Activity implements View.OnClickListener
{
    private TextView all_button,money_in_button,money_out_button;
    private RelativeLayout back_layout;
    private SessionManager sessionManager;
    private ProgressDialog dialogL;
    private WalletHistoryPojo walletHistory;
    private ArrayList<WalletList> walletHistoryList;
    private ArrayList<WalletList> inMoneyList;
    private ArrayList<WalletList> outMoneyList;
    private WalletHistoryAdapter walletHistoryAdapter;
    private ListView wallet_list;
    private boolean loading = true;
    private int previousTotal = 0;
    private int currentPage = 0;
    private int visibleThreshold = 5;
    private boolean bookingsFinished=false;
    private String selectedPos="1";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wallet_history);

        initializeVariables();
    }

    private void initializeVariables()
    {
        all_button= (TextView) findViewById(R.id.all_button);
        money_in_button= (TextView) findViewById(R.id.money_in_button);
        money_out_button= (TextView) findViewById(R.id.money_out_button);
        back_layout= (RelativeLayout) findViewById(R.id.back_layout);
        wallet_list= (ListView) findViewById(R.id.wallet_list);
        walletHistoryList=new ArrayList<>();
        inMoneyList=new ArrayList<>();
        outMoneyList=new ArrayList<>();
        sessionManager=new SessionManager(this);
        VariableConstants.COUNTER=0;

        setBackgroundWhiteWithoutRadii(money_in_button);
        setBackgroundWhiteWithRightCornerRadii(money_out_button);
        setBackgroundGrayWithLeftCornerRadii(all_button);
        all_button.setTextColor(Color.WHITE);
        money_in_button.setTextColor(getResources().getColor(R.color.background));
        money_out_button.setTextColor(getResources().getColor(R.color.background));

        back_layout.setOnClickListener(this);
        money_in_button.setOnClickListener(this);
        money_out_button.setOnClickListener(this);
        all_button.setOnClickListener(this);

        dialogL= Utility.GetProcessDialog(this);
        dialogL.setCancelable(false);
        if(dialogL!=null)
        {
            dialogL.show();
        }

        wallet_list.setOnScrollListener(new AbsListView.OnScrollListener()
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
                       getWalletHistory();

                    }

                    loading = true;
                }
            }

        });


        /**
         * to call api for history
         */
        getWalletHistory();
    }

    private void setBackgroundWhiteWithLeftCornerRadii(TextView textView)
    {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[] { 8, 8, 0, 0, 0, 0, 8, 8 });   //  contains 2 values, <code>[X_radius, Y_radius]</code>. top-left, top-right, bottom-right, bottom-left. This property
        shape.setColor(Color.WHITE);
        shape.setStroke(1, getResources().getColor(R.color.background));
        textView.setBackgroundDrawable(shape);
    }

    private void setBackgroundWhiteWithRightCornerRadii(TextView textView)
    {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[] { 0, 0, 8, 8, 8, 8, 0, 0 });   //  contains 2 values, <code>[X_radius, Y_radius]</code>. top-left, top-right, bottom-right, bottom-left. This property
        shape.setColor(Color.WHITE);
        shape.setStroke(1, getResources().getColor(R.color.background));
        textView.setBackgroundDrawable(shape);
    }

    private void setBackgroundGrayWithLeftCornerRadii(TextView textView)
    {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[] { 8, 8, 0, 0, 0, 0, 8, 8 });   //  contains 2 values, <code>[X_radius, Y_radius]</code>. top-left, top-right, bottom-right, bottom-left. This property
        shape.setColor(getResources().getColor(R.color.background));
        shape.setStroke(1, getResources().getColor(R.color.black));
        textView.setBackgroundDrawable(shape);
    }

    private void setBackgroundGrayWithRightCornerRadii(TextView textView)
    {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[] { 0, 0, 8, 8, 8, 8, 0, 0 });   //  contains 2 values, <code>[X_radius, Y_radius]</code>. top-left, top-right, bottom-right, bottom-left. This property
        shape.setColor(getResources().getColor(R.color.background));
        shape.setStroke(1, getResources().getColor(R.color.black));
        textView.setBackgroundDrawable(shape);
    }

    private void setBackgroundGrayWithoutRadii(TextView textView)
    {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(getResources().getColor(R.color.background));
        shape.setStroke(1, getResources().getColor(R.color.black));
        textView.setBackgroundDrawable(shape);
    }

    private void setBackgroundWhiteWithoutRadii(TextView textView)
    {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(Color.WHITE);
        shape.setStroke(1, getResources().getColor(R.color.background));
        textView.setBackgroundDrawable(shape);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back_layout:
            {
                finish();
                break;
            }
            case R.id.all_button:
            {
                selectedPos="1";
                setBackgroundWhiteWithoutRadii(money_in_button);
                setBackgroundWhiteWithRightCornerRadii(money_out_button);
                setBackgroundGrayWithLeftCornerRadii(all_button);
                all_button.setTextColor(Color.WHITE);
                money_in_button.setTextColor(getResources().getColor(R.color.background));
                money_out_button.setTextColor(getResources().getColor(R.color.background));

                walletHistoryAdapter=new WalletHistoryAdapter(WalletHistory.this,R.layout.wallet_row,walletHistoryList);
                wallet_list.setAdapter(walletHistoryAdapter);
                break;
            }
            case R.id.money_in_button:
            {
                selectedPos="2";
                setBackgroundGrayWithoutRadii(money_in_button);
                setBackgroundWhiteWithRightCornerRadii(money_out_button);
                setBackgroundWhiteWithLeftCornerRadii(all_button);
                all_button.setTextColor(getResources().getColor(R.color.background));
                money_in_button.setTextColor(Color.WHITE);
                money_out_button.setTextColor(getResources().getColor(R.color.background));

                walletHistoryAdapter=new WalletHistoryAdapter(WalletHistory.this,R.layout.wallet_row,inMoneyList);
                wallet_list.setAdapter(walletHistoryAdapter);
                break;
            }
            case R.id.money_out_button:
            {
                selectedPos="3";
                setBackgroundWhiteWithoutRadii(money_in_button);
                setBackgroundGrayWithRightCornerRadii(money_out_button);
                setBackgroundWhiteWithLeftCornerRadii(all_button);
                all_button.setTextColor(getResources().getColor(R.color.background));
                money_in_button.setTextColor(getResources().getColor(R.color.background));
                money_out_button.setTextColor(Color.WHITE);

                walletHistoryAdapter=new WalletHistoryAdapter(WalletHistory.this,R.layout.wallet_row,outMoneyList);
                wallet_list.setAdapter(walletHistoryAdapter);
                break;
            }
        }
    }

    private void getWalletHistory()
    {
        Utility utility=new Utility();
        String curenttime=utility.getCurrentGmtTime();

        Utility.printLog("params to wallet historye "+sessionManager.getSessionToken()+"\n"
        +sessionManager.getDeviceId()+"\n"+curenttime+"\n"+ VariableConstants.COUNTER
        );

        final RequestBody requestBody=new FormEncodingBuilder()

                .add("ent_sess_token", sessionManager.getSessionToken())
                .add("ent_dev_id", sessionManager.getDeviceId())
                .add("ent_date_time", curenttime)
                .add("ent_page_index", VariableConstants.COUNTER+"")
                .build();
        OkHttpRequest.doJsonRequest(VariableConstants.BASE_URL+"GetWalletHistory", requestBody, new OkHttpRequest.JsonRequestCallback() {
            @Override
            public void onSuccess(String result)
            {
                if (dialogL!=null)
                {
                    dialogL.dismiss();
                    dialogL=null;
                }
                Utility.printLog("wllet history result " + result);
                if(result!=null)
                {
                    Gson gson=new Gson();
                    walletHistory=gson.fromJson(result,WalletHistoryPojo.class);

                    if(walletHistory.getLastcount().equals("1"))
                    {
                        bookingsFinished=true;
                    }

                    walletHistoryList.addAll(walletHistory.getWalletHistory());
                    Utility.printLog("list for wallet in history "+walletHistoryList.size());
                    walletHistoryAdapter=new WalletHistoryAdapter(WalletHistory.this,R.layout.wallet_row,walletHistoryList);

                    for(int i=0;i<walletHistory.getWalletHistory().size();i++)
                    {
                        switch (walletHistory.getWalletHistory().get(i).getType())
                        {
                            case "1":
                            {
                                inMoneyList.add(walletHistory.getWalletHistory().get(i));
                                break;
                            }
                            case "2":
                            {
                                outMoneyList.add(walletHistory.getWalletHistory().get(i));
                                break;
                            }
                        }
                    }
                    switch (selectedPos)
                    {
                        case "1":
                        {
                            wallet_list.setAdapter(walletHistoryAdapter);
                            break;
                        }
                        case "2":
                        {
                            walletHistoryAdapter=new WalletHistoryAdapter(WalletHistory.this,R.layout.wallet_row,inMoneyList);
                            wallet_list.setAdapter(walletHistoryAdapter);
                            break;
                        }
                        case "3":
                        {
                            walletHistoryAdapter=new WalletHistoryAdapter(WalletHistory.this,R.layout.wallet_row,outMoneyList);
                            wallet_list.setAdapter(walletHistoryAdapter);
                            break;
                        }
                    }
                }
                else
                {
                    if (dialogL!=null)
                    {
                        dialogL.dismiss();
                        dialogL=null;
                    }
                    Toast.makeText(WalletHistory.this, getResources().getString(R.string.network_connection_fail), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onError(String error)
            {
                if (dialogL!=null)
                {
                    dialogL.dismiss();
                    dialogL=null;
                }
                Toast.makeText(WalletHistory.this, getResources().getString(R.string.network_connection_fail), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
