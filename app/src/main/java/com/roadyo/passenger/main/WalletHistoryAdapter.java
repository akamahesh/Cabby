package com.roadyo.passenger.main;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ourcabby.passenger.R;
import com.roadyo.passenger.pojo.WalletList;
import com.threembed.utilities.Utility;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WalletHistoryAdapter extends ArrayAdapter<WalletList>
{
    private Context context;
    private ArrayList<WalletList> wallet_list=new ArrayList<WalletList>();
    private Typeface robotoMed;

    public WalletHistoryAdapter(Context context, int resourceId, ArrayList<WalletList> wallet_list)
    {
        super(context, R.layout.wallet_row,wallet_list);
        this.context=context;
        this.wallet_list=wallet_list;
        Utility.printLog("list size "+wallet_list.size());
    }

    private class ViewHolder
    {
        TextView amount,date,wallet_bal,rms_booking;
        ImageView arrowImage;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        Utility.printLog("list size in get view  "+wallet_list.size());

        if(convertView==null||convertView.getTag()==null)
        {
            holder = new ViewHolder();
            // inflate the layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.wallet_row, parent, false);
            holder.amount=(TextView) convertView.findViewById(R.id.amount);
            holder.date=(TextView) convertView.findViewById(R.id.date);
            holder.wallet_bal=(TextView) convertView.findViewById(R.id.wallet_bal);
            holder.arrowImage=(ImageView) convertView.findViewById(R.id.arrowImage);
            holder.rms_booking=(TextView) convertView.findViewById(R.id.rms_booking);

            robotoMed = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Light.ttf");

            holder.rms_booking.setTypeface(robotoMed);
            holder.wallet_bal.setTypeface(robotoMed);
            holder.amount.setTypeface(robotoMed);
            holder.date.setTypeface(robotoMed);
            convertView.setTag(holder);

        }
        else
            holder = (ViewHolder) convertView.getTag();
        holder.amount.setText(context.getResources().getString(R.string.currencuSymbol)+" "+wallet_list.get(position).getAmount());
        holder.date.setText(convertToRequiredFormat(wallet_list.get(position).getDate()));

        holder.wallet_bal.setText(context.getResources().getString(R.string.currencuSymbol)+" "+
                                  wallet_list.get(position).getAmount()+" "+context.getResources().getString(R.string.wallet_balance));
        if(wallet_list.get(position).getType().equals("1"))
        {
            holder.arrowImage.setImageDrawable(context.getResources().getDrawable(R.drawable.wallet_up_arrow_icon));
            holder.rms_booking.setText(context.getResources().getString(R.string.rms_recharge)+" "+wallet_list.get(position).getTxn_id());
        }
        else
        {
            holder.arrowImage.setImageDrawable(context.getResources().getDrawable(R.drawable.wallet_down_arrow_icon));
            holder.rms_booking.setText(context.getResources().getString(R.string.rms_booking));
        }
        return convertView;

    }

    private String convertToRequiredFormat(String dateString)
    {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);//2016-09-08
        DateFormat targetFormat = new SimpleDateFormat("EEE, dd MMM, yyyy", Locale.US);
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
        Utility.printLog("required format "+formattedDate);
        return formattedDate;
    }

}
