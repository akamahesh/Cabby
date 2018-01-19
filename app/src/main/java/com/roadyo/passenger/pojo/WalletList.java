package com.roadyo.passenger.pojo;

import java.io.Serializable;

/**
 * Created by rahul on 8/9/16.
 */
public class WalletList implements Serializable
{
    /*    "date":"2016-09-07",
    "creditedBy":"ADMIN",
    "amount":"20"*/
    private String date;
    private String creditedBy;
    private String amount;
    private String type;

    public String getTxn_id() {
        return txn_id;
    }

    private String txn_id;

    public String getType() {
        return type;
    }


    public String getCreditedBy() {
        return creditedBy;
    }

    public void setCreditedBy(String creditedBy) {
        this.creditedBy = creditedBy;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
