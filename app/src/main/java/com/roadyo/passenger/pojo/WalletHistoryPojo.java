package com.roadyo.passenger.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Akbar on 8/9/16.
 */

public class WalletHistoryPojo implements Serializable
{
    /*    "errNum":"21",
    "errFlag":"0",
    "errMsg":"Got the details!",
    "inMoney":[],
    "outMoney":[]*/
    private String errNum;
    private String errFlag;
    private String errMsg;

    public String getLastcount() {
        return lastcount;
    }

    private String lastcount;
    private ArrayList<WalletList> walletHistory;

    public ArrayList<WalletList> getWalletHistory() {
        return walletHistory;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public String getErrFlag() {
        return errFlag;
    }

    public String getErrNum() {
        return errNum;
    }
}
