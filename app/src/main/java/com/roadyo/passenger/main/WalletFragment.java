package com.roadyo.passenger.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ourcabby.passenger.R;
import com.google.gson.Gson;
import com.roadyo.passenger.pojo.GetCardResponse;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.threembed.utilities.OkHttpRequest;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.card.payment.CardType;

/**
 * Created by Akbar on 21/10/16.
 */

public class WalletFragment extends Fragment implements View.OnClickListener
{
    private TextView currencySymbol1, currencySymbol2, currencySymbol3,currencySymbol, amount_to_be_added1, amount_to_be_added2, amount_to_be_added3;
    private TextView low_bal_text,wallet_money,card_number;
    private EditText amount_to_be_added;
    private RelativeLayout amount_layout_first, amount_layout_second,amount_layout, amount_layout_third,card_type_info;
    private Button add_money_button;
    private ProgressDialog dialogL;
    private SessionManager sessionManager;
    private GetCardResponse response;
    private View view3;
    private ImageView card_type_image,right_arrow,right_arrow3;
    private RelativeLayout cash_layout;
    private LinearLayout mainllout;
    private String jsonResponse = null,Default_Card_Id;
    Typeface nexaLight;
    private ProgressBar progress_bar;
    private ArrayList<ImageView> buttons;
    private SessionManager session;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                                          android.view.ViewGroup container,
                                          android.os.Bundle savedInstanceState)
    {
        view  = inflater.inflate(R.layout.wallet_new_layout, null);
        initializeVariables(view);
        return view;
    }

    private void initializeVariables(View view) {
        session=new SessionManager(getActivity());
        TextView curr_pay_method= (TextView) view.findViewById(R.id.curr_pay_method);
        TextView cash_txt= (TextView) view.findViewById(R.id.cash_txt);
        TextView card_txt= (TextView) view.findViewById(R.id.card_txt);
        TextView topup_credit= (TextView) view.findViewById(R.id.topup_credit);
        TextView current_amount= (TextView) view.findViewById(R.id.current_amount);
        TextView add_money_text= (TextView) view.findViewById(R.id.add_money_text);
        TextView quick_safe_text= (TextView) view.findViewById(R.id.quick_safe_text);
        RelativeLayout add_card_layout= (RelativeLayout) view.findViewById(R.id.add_card_layout);
        view3= (View) view.findViewById(R.id.view3);
        add_money_button= (Button) view.findViewById(R.id.add_money_button);
        wallet_money = (TextView) view.findViewById(R.id.wallet_money);
        right_arrow = (ImageView) view.findViewById(R.id.right_arrow);
        currencySymbol1 = (TextView) view.findViewById(R.id.currencySymbol1);
        currencySymbol2 = (TextView) view.findViewById(R.id.currencySymbol2);
        currencySymbol3 = (TextView) view.findViewById(R.id.currencySymbol3);
        currencySymbol = (TextView) view.findViewById(R.id.currencySymbol3);
        low_bal_text = (TextView) view.findViewById(R.id.low_bal_text);
        amount_to_be_added1 = (TextView) view.findViewById(R.id.amount_to_be_added1);
        amount_to_be_added2 = (TextView) view.findViewById(R.id.amount_to_be_added2);
        amount_to_be_added3 = (TextView) view.findViewById(R.id.amount_to_be_added3);
        amount_to_be_added = (EditText) view.findViewById(R.id.amount_to_be_added);
        amount_layout_first = (RelativeLayout) view.findViewById(R.id.amount_layout_first);
        amount_layout_second = (RelativeLayout) view.findViewById(R.id.amount_layout_second);
        amount_layout_third = (RelativeLayout) view.findViewById(R.id.amount_layout_third);
        amount_layout = (RelativeLayout) view.findViewById(R.id.amount_layout);
       // card_type_info = (RelativeLayout) view.findViewById(R.id.card_type_info);
        cash_layout = (RelativeLayout) view.findViewById(R.id.cash_layout);
        mainllout = (LinearLayout) view.findViewById(R.id.mainllout);
        progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);
        buttons= new ArrayList<>();

        add_money_button.setOnClickListener(this);
        add_card_layout.setOnClickListener(this);
       // card_type_info.setOnClickListener(this);
        cash_layout.setOnClickListener(this);

        dialogL = Utility.GetProcessDialog(getActivity());
        dialogL.show();
        sessionManager=new SessionManager(getActivity());

        amount_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WalletHistory.class);
                startActivity(intent);
            }
        });

         BackgroundGetCards();

        amount_to_be_added.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("599")) {
                    amount_layout_first.setSelected(true);
                    amount_layout_second.setSelected(false);
                    amount_layout_third.setSelected(false);

                    currencySymbol1.setTextColor(getResources().getColor(R.color.white));
                    currencySymbol2.setTextColor(getResources().getColor(R.color.black));
                    currencySymbol3.setTextColor(getResources().getColor(R.color.black));
                    amount_to_be_added1.setTextColor(getResources().getColor(R.color.white));
                    amount_to_be_added2.setTextColor(getResources().getColor(R.color.black));
                    amount_to_be_added3.setTextColor(getResources().getColor(R.color.black));
                } else if (s.toString().equals("999")) {
                    amount_layout_first.setSelected(false);
                    amount_layout_second.setSelected(true);
                    amount_layout_third.setSelected(false);

                    currencySymbol1.setTextColor(getResources().getColor(R.color.black));
                    currencySymbol2.setTextColor(getResources().getColor(R.color.white));
                    currencySymbol3.setTextColor(getResources().getColor(R.color.black));
                    amount_to_be_added1.setTextColor(getResources().getColor(R.color.black));
                    amount_to_be_added2.setTextColor(getResources().getColor(R.color.white));
                    amount_to_be_added3.setTextColor(getResources().getColor(R.color.black));
                } else if (s.toString().equals("1999")) {
                    amount_layout_first.setSelected(false);
                    amount_layout_second.setSelected(false);
                    amount_layout_third.setSelected(true);

                    currencySymbol1.setTextColor(getResources().getColor(R.color.black));
                    currencySymbol2.setTextColor(getResources().getColor(R.color.black));
                    currencySymbol3.setTextColor(getResources().getColor(R.color.white));
                    amount_to_be_added1.setTextColor(getResources().getColor(R.color.black));
                    amount_to_be_added2.setTextColor(getResources().getColor(R.color.black));
                    amount_to_be_added3.setTextColor(getResources().getColor(R.color.white));
                } else {
                    amount_layout_first.setSelected(false);
                    amount_layout_second.setSelected(false);
                    amount_layout_third.setSelected(false);

                    currencySymbol1.setTextColor(getResources().getColor(R.color.black));
                    currencySymbol2.setTextColor(getResources().getColor(R.color.black));
                    currencySymbol3.setTextColor(getResources().getColor(R.color.black));
                    amount_to_be_added1.setTextColor(getResources().getColor(R.color.black));
                    amount_to_be_added2.setTextColor(getResources().getColor(R.color.black));
                    amount_to_be_added3.setTextColor(getResources().getColor(R.color.black));
                }
            }
            @Override
            public void afterTextChanged(final Editable s) {
                // you will probably need to use runOnUiThread(Runnable action) for some specific actions
            }
        });

        amount_layout_first.setOnClickListener(this);
        amount_layout_second.setOnClickListener(this);
        amount_layout_third.setOnClickListener(this);

        Typeface nexaBold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Lato-Bold.ttf");
        nexaLight = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Lato-Regular.ttf");

        curr_pay_method.setTypeface(nexaBold);
        topup_credit.setTypeface(nexaBold);
        add_money_button.setTypeface(nexaBold);
        add_money_text.setTypeface(nexaLight);
        cash_txt.setTypeface(nexaLight);
        card_txt.setTypeface(nexaLight);
        current_amount.setTypeface(nexaBold);
        quick_safe_text.setTypeface(nexaLight);
        amount_to_be_added.setTypeface(nexaLight);
        amount_to_be_added1.setTypeface(nexaLight);
        amount_to_be_added2.setTypeface(nexaLight);
        amount_to_be_added3.setTypeface(nexaLight);
        amount_to_be_added1.setTypeface(nexaLight);
        amount_to_be_added1.setTypeface(nexaLight);
        wallet_money.setTypeface(nexaBold);
        currencySymbol.setTypeface(nexaLight);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.amount_layout_first: {
                amount_layout_first.setSelected(true);
                amount_layout_second.setSelected(false);
                amount_layout_third.setSelected(false);

                amount_to_be_added.setText("599");
                currencySymbol1.setTextColor(getResources().getColor(R.color.white));
                currencySymbol2.setTextColor(getResources().getColor(R.color.black));
                currencySymbol3.setTextColor(getResources().getColor(R.color.black));
                amount_to_be_added1.setTextColor(getResources().getColor(R.color.white));
                amount_to_be_added2.setTextColor(getResources().getColor(R.color.black));
                amount_to_be_added3.setTextColor(getResources().getColor(R.color.black));
                break;
            }
            case R.id.amount_layout_second: {
                amount_layout_first.setSelected(false);
                amount_layout_second.setSelected(true);
                amount_layout_third.setSelected(false);

                amount_to_be_added.setText("999");
                currencySymbol1.setTextColor(getResources().getColor(R.color.black));
                currencySymbol2.setTextColor(getResources().getColor(R.color.white));
                currencySymbol3.setTextColor(getResources().getColor(R.color.black));
                amount_to_be_added1.setTextColor(getResources().getColor(R.color.black));
                amount_to_be_added2.setTextColor(getResources().getColor(R.color.white));
                amount_to_be_added3.setTextColor(getResources().getColor(R.color.black));
                break;
            }
            case R.id.amount_layout_third: {
                amount_layout_first.setSelected(false);
                amount_layout_second.setSelected(false);
                amount_layout_third.setSelected(true);

                amount_to_be_added.setText("1999");
                currencySymbol1.setTextColor(getResources().getColor(R.color.black));
                currencySymbol2.setTextColor(getResources().getColor(R.color.black));
                currencySymbol3.setTextColor(getResources().getColor(R.color.white));
                amount_to_be_added1.setTextColor(getResources().getColor(R.color.black));
                amount_to_be_added2.setTextColor(getResources().getColor(R.color.black));
                amount_to_be_added3.setTextColor(getResources().getColor(R.color.white));
                break;
            }
            case R.id.add_money_button:
            {
                if(!amount_to_be_added.getText().toString().equals(""))
                {
                    if(Double.parseDouble(amount_to_be_added.getText().toString())>=100)
                    {
                        if(!session.getCardToken().equals(""))
                            addMoneyToWallet();
                        else
                            Toast.makeText(getActivity(),getString(R.string.add_card_to_recharge),Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Utility.ShowAlert(getResources().getString(R.string.need_to_be),getActivity());
                    }

                }
                else
                {
                    Utility.ShowAlert(getResources().getString(R.string.add_money_warning),getActivity());
                }
                break;
            }
            case R.id.add_card_layout:
            {
                Intent intent =new Intent(getActivity(),AddCardActivity.class);
                startActivityForResult(intent,1);
                getActivity().overridePendingTransition(R.anim.anim_two, R.anim.anim_one);
                break;
            }
            case R.id.cash_layout:
            {
                right_arrow.setVisibility(View.VISIBLE);
                right_arrow3.setVisibility(View.GONE);
                break;
            }
            case R.id.card_type_info:
            {
                right_arrow.setVisibility(View.GONE);
                right_arrow3.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if(resultCode==Activity.RESULT_OK)
            {
                 BackgroundGetCards();
            }
        }

        if(requestCode==17)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                  Default_Card_Id = data.getStringExtra("ID");


            }
        }
    }

    private Bitmap setCreditCardLogo(String type) {

        CardType cardType;
        if(type.equals("Visa"))
        {
            cardType= CardType.VISA;
            Bitmap bitmap=cardType.imageBitmap(getActivity());
            return bitmap;
        }
        if(type.equals("MasterCard"))
        {
            cardType= CardType.MASTERCARD;
            Bitmap bitmap=cardType.imageBitmap(getActivity());
            return bitmap;
        }
        if(type.equals("American Express"))
        {
            cardType= CardType.AMEX;
            Bitmap bitmap=cardType.imageBitmap(getActivity());
            return bitmap;
        }
        if(type.equals("Discover"))
        {
            cardType= CardType.DISCOVER;
            Bitmap bitmap=cardType.imageBitmap(getActivity());
            return bitmap;
        }

        if(type.equals("JCB"))
        {
            cardType= CardType.JCB;
            Bitmap bitmap=cardType.imageBitmap(getActivity());
            return bitmap;
        }
        cardType= CardType.UNKNOWN;
        Bitmap bitmap=cardType.imageBitmap(getActivity());
        return bitmap;
    }

    private void BackgroundGetCards()
    {
        if (dialogL!=null)
        {
            dialogL.show();
        }

            final SessionManager session=new SessionManager(getActivity());
            Utility utility=new Utility();
            String curenttime=utility.getCurrentGmtTime();
            final RequestBody requestBody=new FormEncodingBuilder()

                    .add("ent_sess_token", session.getSessionToken())
                    .add("ent_dev_id", session.getDeviceId())
                    .add("ent_date_time", curenttime)
                    .build();

        OkHttpRequest.doJsonRequest(VariableConstants.BASE_URL+"getCards", requestBody, new OkHttpRequest.JsonRequestCallback()
        {
            @Override
            public void onSuccess(String result)
            {
                if (result!=null)
                {
                    progress_bar.setVisibility(View.GONE);
                        Utility.printLog("get cards response"+result);
                        Gson gson = new Gson();
                        response=gson.fromJson(result,GetCardResponse.class);
                        if (dialogL!=null) {
                            dialogL.dismiss();
                        }
                        Utility.printLog("prasms in on post exectute "+result+"wall bal "+response.getWalletBal());
                        if(response!=null) {

                            wallet_money.setText(getResources().getString(R.string.currencuSymbol) + " " + response.getWalletBal());

                            if(Double.parseDouble(response.getWalletBal())<=0)
                            {
                                low_bal_text.setVisibility(View.VISIBLE);
                            }

                            if (response.getErrFlag().equals("0") && isAdded()) {
                                if(result.equals("1"))
                                {
                                    right_arrow.setVisibility(View.GONE);
                                    right_arrow3.setVisibility(View.VISIBLE);
                                }

                                if(response.getCards().size()>0)
                                {
                                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

                                    for(int i=0;i<response.getCards().size();i++)
                                    {
                                        View row = layoutInflater.inflate(R.layout.card_single_layout, mainllout, false);
                                        Utility.printLog("tags for need help "+response.getCards().get(i).getLast4());
                                        TextView card_number= (TextView) row.findViewById(R.id.card_number);
                                        TextView ends_txt= (TextView) row.findViewById(R.id.ends_txt);
                                        ImageView card_type_image= (ImageView) row.findViewById(R.id.card_type_image);
                                        final ImageView right_arrow3= (ImageView) row.findViewById(R.id.right_arrow3);
                                        RelativeLayout card_type_info= (RelativeLayout) row.findViewById(R.id.card_type_info);

                                        buttons.add(right_arrow3);

                                        ends_txt.setTypeface(nexaLight);
                                        card_number.setTypeface(nexaLight);
                                        Bitmap bitmap = setCreditCardLogo(response.getCards().get(i).getType());
                                        card_type_image.setImageBitmap(bitmap);
                                        card_number.setText(response.getCards().get(i).getLast4());
                                        mainllout.addView(row);

                                        final int finalI = i;

                                        if(finalI==0)
                                        {
                                            right_arrow3.setVisibility(View.VISIBLE);
                                            session.setLast4Digits(response.getCards().get(finalI).getLast4());
                                            session.setCardToken(response.getCards().get(finalI).getId());
                                            session.setCardType(response.getCards().get(finalI).getType());
                                        }
                                        card_type_info.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                resetAll();
                                                right_arrow3.setVisibility(View.VISIBLE);
                                                session.setLast4Digits(response.getCards().get(finalI).getLast4());
                                                session.setCardToken(response.getCards().get(finalI).getId());
                                                session.setCardType(response.getCards().get(finalI).getType());
                                            }
                                        });
                                    }
                                }

                            } else if (response.getErrNum().equals("6") || response.getErrNum().equals("7") ||
                                    response.getErrNum().equals("94") || response.getErrNum().equals("96")) {
                                Toast.makeText(getActivity(), response.getErrMsg(), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getActivity(), SplashActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                getActivity().startActivity(i);
                                getActivity().overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                            }

                        }
                        else if(isAdded())
                        {
                            Utility.ShowAlert("Server error!!",getActivity());
                        }

                }
                else
                {
                            Toast.makeText(getActivity(),"Request Timeout !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error)
            {
                progress_bar.setVisibility(View.GONE);
                dialogL.cancel();
                Utility.printLog("on error for the getcards "+error);
                Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }});
    }

    private void resetAll(){
        for (int i=0;i<buttons.size();i++)
        {
            buttons.get(i).setVisibility(View.GONE);
        }
    }

    private void addMoneyToWallet()
    {
        dialogL.show();

        Utility utility=new Utility();
        String curenttime=utility.getCurrentGmtTime();
        final RequestBody requestBody=new FormEncodingBuilder()

                .add("ent_sess_token", session.getSessionToken())
                .add("ent_dev_id", session.getDeviceId())
                .add("ent_date_time", curenttime)
                .add("ent_token", session.getCardToken())
                .add("ent_amount", amount_to_be_added.getText().toString())
                .build();

        OkHttpRequest.doJsonRequest(VariableConstants.BASE_URL + "AddMoneyToWallet", requestBody, new OkHttpRequest.JsonRequestCallback() {
            @Override
            public void onSuccess(String result) {
                dialogL.dismiss();
                Utility.printLog("Success of getting add Wallet money " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("errMsg");
                    String errFlag = jsonObject.getString("errFlag");
                    if(errFlag.equals("0"))
                    {
                        String amount = jsonObject.getString("amount");
                        wallet_money.setText(amount);
                        amount_to_be_added.setHint("0");
                        amount_to_be_added.setText(null);
                    }
                    else
                    {
                        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    dialogL.dismiss();
                    e.printStackTrace();
                    Utility.printLog("Success of getting add Wallet money catch" + e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                dialogL.dismiss();
                Toast.makeText(getActivity(), getResources().getString(R.string.network_connection_fail), Toast.LENGTH_LONG).show();
                Utility.printLog("Error for volley");
            }
        });
    }


}
