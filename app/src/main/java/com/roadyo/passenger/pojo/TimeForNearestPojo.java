package com.roadyo.passenger.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rahul on 23/3/16.
 */

/*
* {
   "destination_addresses" : [
      "202, Bhashyam Rd, Subhash Nagar, Cottonpete, Bengaluru, Karnataka 560053, India"
   ],
   "origin_addresses" : [
      "Narayanappa Building, Bayanna Layout, Hebbal, Bengaluru, Karnataka 560032, India"
   ],
   "rows" : [
      {
         "elements" : [
            {
               "distance" : {
                  "text" : "9.9 km",
                  "value" : 9887
               },
               "duration" : {
                  "text" : "26 mins",
                  "value" : 1549
               },
               "status" : "OK"
            }
         ]
      }
   ],
   "status" : "OK"
}
*/


public class TimeForNearestPojo implements Serializable
{
    ArrayList<Rows> rows;
    String status;

    public ArrayList<Rows> getRows() {
        return rows;
    }

    public void setRows(ArrayList<Rows> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
