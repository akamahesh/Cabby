/**
 * ============================================================================
 *
 * Copyright (C) 2011 Android Museum Systems.  All rights reserved. The content 
 * presented herein may not, under any circumstances, be reproduced, in 
 * whole or in any part or form, without written permission from 
 * Museum Systems.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are NOT permitted. Neither the name of Museum Systems,
 * nor the names of contributors may be used to endorse or promote products 
 * derived from this software without specific prior written permission.
 *
 * ============================================================================
 *
 * Author: Admin
 *  
 *
 * Revision History
 * ----------------------------------------------------------------------------
 * Date                Author              Comment, bug number, fix description
 *
 * Mar 11, 2012      tuan@edge-works.net           version 1.0
 *
 * ----------------------------------------------------------------------------
 */

package com.threembed.utilities;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.TimeZone;



// TODO: Auto-generated Javadoc
/**
 * Mar 11, 2012.
 *
 * @author Admin
 * @version 1.0
 * @copyright Copyright (c) Android Museum Systems, all rights reserved
 */

public class UltilitiesDate 
{
	
	//private static final int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
	
	static int entyer;
	static int endmonth;
	static int endday;
	/** The days. */
	static int days;
	
	/** The hours. */
	static int hours;
	
	/** The minutes. */
	static int minutes;
	
	/** The seconds. */
	static int seconds;




	

	

	/**
	 * Gets the days.
	 *
	 * @return the days
	 */
	public int getDays() {

		return days;
	}

	/**
	 * Gets the hours.
	 *
	 * @return the hours
	 */
	public int getHours() {

		return hours;
	}

	/**
	 * Gets the minutes.
	 *
	 * @return the minutes
	 */
	public int getMinutes() {

		return minutes;
	}

	/**
	 * Gets the seconds.
	 *
	 * @return the seconds
	 */
	public int getSeconds() {

		return seconds;
	}

	
	
	//private static String getLocalTime( int hr, int min )
	public static String getLocalTime( String date)
	{      
		   android.util.Log.d("DateUtility......", "getLocalTime   date "+date );
			String[] date1= date.split(" ");
	        String localTime = "";
	        
	         String[] ldate= date1[1].split(":");
	         int hr= Integer.parseInt(ldate[0]);
	         int min= Integer.parseInt(ldate[1]);
	         int sec= Integer.parseInt(ldate[2]);
	      //   System.out.println("hour and min     " + hr + "    " + min);
	         
	        Calendar gmt = Calendar.getInstance();
	        
	        gmt.set( Calendar.HOUR, hr );               
	        gmt.set( Calendar.MINUTE, min );
	        gmt.set(Calendar.SECOND, sec);
	        gmt.setTimeZone( TimeZone.getTimeZone( "GMT" ) );        
	        
	        Calendar local = Calendar.getInstance();
	        local.setTimeZone( TimeZone.getDefault() );
	        local.setTime( gmt.getTime() );

	        int hour = local.get( Calendar.HOUR );           
	        int minutes = local.get( Calendar.MINUTE ); 
	        int seconds= local.get(Calendar.SECOND);
	        boolean am = local.get( Calendar.AM_PM ) == Calendar.AM;
	       // hour = hour + 12;
	        String str_hr = "";
	        String str_min = "";
	        String str_sec="";
	        String am_pm = "";
	        
	        if ( hour < 10 )
	            str_hr = "0";
	        if ( minutes < 10 )
	            str_min = "0";
	        if(seconds<10)
	        	str_sec="0";
	        
	        if( am )
	            am_pm = "AM";
	        else
	        {
	        	am_pm = "PM";
	        	//str_hr = str_hr + 12;
	        }
	             
	        
	        localTime = date1[0]+ " "+str_hr + hour + ":" + str_min + minutes + ":" + str_sec + seconds;//+" "+am_pm ;
	        //System.out.println("local time     " + localTime);
	        //SimpleDateFormat parsedFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")	;
	        return localTime;        
	}
	
	
	
	public static String GetDateString(String inputDate){ 
		  
		  Date date=null;

		  SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
		  
		  try {
		   date = inFormat.parse(inputDate);
		  } catch (ParseException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }
		  
		  Calendar calendar = Calendar.getInstance();

		        calendar.setTime(date);

		        String[] days = new String[] { "SUN", "MON", "TUE", "WED", "THUR", "FRI", "SAT" };

		        String day = days[calendar.get(Calendar.DAY_OF_WEEK)];
		        
		        final String[] payload1= inputDate.split("-");
		        
		        String[] months = new String[] { "JAN", "FEB","MAR","APR","MAY","JUNE","JULY","AUG","SEPT","OCT","NOV","DEC" };
		      //  System.out.println(months[Integer.parseInt(payload1[1])-1]);
		        
		        return day+", "+payload1[2]+" "+months[Integer.parseInt(payload1[1])-1]+" "+payload1[0];
		 }
}
