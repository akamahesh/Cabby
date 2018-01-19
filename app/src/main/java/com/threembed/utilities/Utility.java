package com.threembed.utilities;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.roadyo.passenger.main.CountryList;
import com.ourcabby.passenger.R;

public class Utility
{
	public static void printLog(String msg)
	{

		if(true)
		{
			Log.i("RoadYo",msg);
		}
	}

	public static ProgressDialog GetProcessDialog(Activity activity)
	{
		// prepare the dialog box
		ProgressDialog dialog = new ProgressDialog(activity,5);
		// make the progress bar cancelable
		dialog.setCancelable(false);
		// set a message text
		dialog.setMessage(activity.getResources().getString(R.string.please_wait));
		// show it
		return dialog;
	}

	public static ProgressDialog GetProcessDialogNew(Activity activity,String msg)
	{
		// prepare the dialog box
		ProgressDialog dialog = new ProgressDialog(activity,5);
		// make the progress bar cancelable
		dialog.setCancelable(false);
		// set a message text
		dialog.setMessage(msg);

		// show it
		return dialog;
	}

	public static String getCurrentDateTimeStringGMT() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String currentDateTimeString=dateFormat.format(date);
		String currentDateTimeWithformat=Utility.changeDateTimeFormate(currentDateTimeString,"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss");
		Date currentDateTimeDate=Utility.convertStringIntoDate(currentDateTimeWithformat, "yyyy-MM-dd hh:mm:ss");
		String gmtDateTime=Utility.getLocalTimeToGMT(currentDateTimeDate);
		String currentDateTimeGMT=Utility.changeDateFormate(gmtDateTime,"MM/dd/yyyy HH:mm:ss","yyyy-MM-dd HH:mm:ss");
		return currentDateTimeGMT;
	}

	public static String changeDateTimeFormate(String inputDate,String inputFormat,String outFormate) {

		String time24 =null;
		try {
			//String now = new SimpleDateFormat("hh:mm aa").format(new java.util.Date().getTime());
			System.out.println("onActivityResult time in 12 hour format : " + inputDate);
			SimpleDateFormat inFormat = new SimpleDateFormat(inputFormat);
			SimpleDateFormat outFormat = new SimpleDateFormat(outFormate);
			time24 = outFormat.format(inFormat.parse(inputDate));
			System.out.println("onActivityResult time in 24 hour format : " + time24);
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
		return time24;

	}

	public static Date convertStringIntoDate(String dateString,String inputFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
		//String dateInString = "7-Jun-2013";
		System.out.println("dateString......."+dateString);
		Date date=null;


		try {
			date = formatter.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(date);
		System.out.println(formatter.format(date));


		return date;
	}

	public static String changeDateFormate(String inputDate,String inputFormate,String outputFormate) {
		//String dateStr = "Jul 27, 2011 8:35:29 AM";
		DateFormat readFormat = new SimpleDateFormat(inputFormate);
		DateFormat writeFormat = new SimpleDateFormat(outputFormate);
		Date date = null;
		try
		{
			date = readFormat.parse( inputDate );
		}
		catch ( ParseException e )
		{
			e.printStackTrace();
		}
		if( date != null )
		{
			String formattedDate = writeFormat.format( date );
		}
		return writeFormat.format( date );

	}

	/**
	 * This will return all the countries. No preference is manages.
	 * Anytime new country need to be added, add it
	 * @return
	 */
	public static List<CountryList> getMasterCountries(){
		List<CountryList> countries=new ArrayList<CountryList>();
		countries.add(new CountryList("af","93","Afghanistan"));
		countries.add(new CountryList("al","355","Albania"));
		countries.add(new CountryList("dz","213","Algeria"));
		countries.add(new CountryList("ad","376","Andorra"));
		countries.add(new CountryList("ao","244","Angola"));
		countries.add(new CountryList("aq","672","Antarctica"));
		countries.add(new CountryList("ar","54","Argentina"));
		countries.add(new CountryList("am","374","Armenia"));
		countries.add(new CountryList("aw","297","Aruba"));
		countries.add(new CountryList("au","61","Australia"));
		countries.add(new CountryList("at","43","Austria"));
		countries.add(new CountryList("az","994","Azerbaijan"));
		countries.add(new CountryList("bh","973","Bahrain"));
		countries.add(new CountryList("bd","880","Bangladesh"));
		countries.add(new CountryList("by","375","Belarus"));
		countries.add(new CountryList("be","32","Belgium"));
		countries.add(new CountryList("bz","501","Belize"));
		countries.add(new CountryList("bj","229","Benin"));
		countries.add(new CountryList("bt","975","Bhutan"));
		countries.add(new CountryList("bo","591","Bolivia, Plurinational State Of"));
		countries.add(new CountryList("ba","387","Bosnia And Herzegovina"));
		countries.add(new CountryList("bw","267","Botswana"));
		countries.add(new CountryList("br","55","Brazil"));
		countries.add(new CountryList("bn","673","Brunei Darussalam"));
		countries.add(new CountryList("bg","359","Bulgaria"));
		countries.add(new CountryList("bf","226","Burkina Faso"));
		countries.add(new CountryList("mm","95","Myanmar"));
		countries.add(new CountryList("bi","257","Burundi"));
		countries.add(new CountryList("kh","855","Cambodia"));
		countries.add(new CountryList("cm","237","Cameroon"));
		countries.add(new CountryList("ca","1","Canada"));
		countries.add(new CountryList("cv","238","Cape Verde"));
		countries.add(new CountryList("cf","236","Central African Republic"));
		countries.add(new CountryList("td","235","Chad"));
		countries.add(new CountryList("cl","56","Chile"));
		countries.add(new CountryList("cn","86","China"));
		countries.add(new CountryList("cx","61","Christmas Island"));
		countries.add(new CountryList("cc","61","Cocos (keeling) Islands"));
		countries.add(new CountryList("co","57","Colombia"));
		countries.add(new CountryList("km","269","Comoros"));
		countries.add(new CountryList("cg","242","Congo"));
		countries.add(new CountryList("cd","243","Congo, The Democratic Republic Of The"));
		countries.add(new CountryList("ck","682","Cook Islands"));
		countries.add(new CountryList("cr","506","Costa Rica"));
		countries.add(new CountryList("hr","385","Croatia"));
		countries.add(new CountryList("cu","53","Cuba"));
		countries.add(new CountryList("cy","357","Cyprus"));
		countries.add(new CountryList("cz","420","Czech Republic"));
		countries.add(new CountryList("dk","45","Denmark"));
		countries.add(new CountryList("dj","253","Djibouti"));
		countries.add(new CountryList("tl","670","Timor-leste"));
		countries.add(new CountryList("ec","593","Ecuador"));
		countries.add(new CountryList("eg","20","Egypt"));
		countries.add(new CountryList("sv","503","El Salvador"));
		countries.add(new CountryList("gq","240","Equatorial Guinea"));
		countries.add(new CountryList("er","291","Eritrea"));
		countries.add(new CountryList("ee","372","Estonia"));
		countries.add(new CountryList("et","251","Ethiopia"));
		countries.add(new CountryList("fk","500","Falkland Islands (malvinas)"));
		countries.add(new CountryList("fo","298","Faroe Islands"));
		countries.add(new CountryList("fj","679","Fiji"));
		countries.add(new CountryList("fi","358","Finland"));
		countries.add(new CountryList("fr","33","France"));
		countries.add(new CountryList("pf","689","French Polynesia"));
		countries.add(new CountryList("ga","241","Gabon"));
		countries.add(new CountryList("gm","220","Gambia"));
		countries.add(new CountryList("ge","995","Georgia"));
		countries.add(new CountryList("de","49","Germany"));
		countries.add(new CountryList("gh","233","Ghana"));
		countries.add(new CountryList("gi","350","Gibraltar"));
		countries.add(new CountryList("gr","30","Greece"));
		countries.add(new CountryList("gl","299","Greenland"));
		countries.add(new CountryList("gt","502","Guatemala"));
		countries.add(new CountryList("gn","224","Guinea"));
		countries.add(new CountryList("gw","245","Guinea-bissau"));
		countries.add(new CountryList("gy","592","Guyana"));
		countries.add(new CountryList("ht","509","Haiti"));
		countries.add(new CountryList("hn","504","Honduras"));
		countries.add(new CountryList("hk","852","Hong Kong"));
		countries.add(new CountryList("hu","36","Hungary"));
		countries.add(new CountryList("in","91","India"));
		countries.add(new CountryList("id","62","Indonesia"));
		countries.add(new CountryList("ir","98","Iran, Islamic Republic Of"));
		countries.add(new CountryList("iq","964","Iraq"));
		countries.add(new CountryList("ie","353","Ireland"));
		countries.add(new CountryList("im","44","Isle Of Man"));
		countries.add(new CountryList("il","972","Israel"));
		countries.add(new CountryList("it","39","Italy"));
		countries.add(new CountryList("ci","225","Côte D&apos;ivoire"));
		countries.add(new CountryList("jp","81","Japan"));
		countries.add(new CountryList("jo","962","Jordan"));
		countries.add(new CountryList("kz","7","Kazakhstan"));
		countries.add(new CountryList("ke","254","Kenya"));
		countries.add(new CountryList("ki","686","Kiribati"));
		countries.add(new CountryList("kw","965","Kuwait"));
		countries.add(new CountryList("kg","996","Kyrgyzstan"));
		countries.add(new CountryList("la","856","Lao People&apos;s Democratic Republic"));
		countries.add(new CountryList("lv","371","Latvia"));
		countries.add(new CountryList("lb","961","Lebanon"));
		countries.add(new CountryList("ls","266","Lesotho"));
		countries.add(new CountryList("lr","231","Liberia"));
		countries.add(new CountryList("ly","218","Libya"));
		countries.add(new CountryList("li","423","Liechtenstein"));
		countries.add(new CountryList("lt","370","Lithuania"));
		countries.add(new CountryList("lu","352","Luxembourg"));
		countries.add(new CountryList("mo","853","Macao"));
		countries.add(new CountryList("mk","389","Macedonia, The Former Yugoslav Republic Of"));
		countries.add(new CountryList("mg","261","Madagascar"));
		countries.add(new CountryList("mw","265","Malawi"));
		countries.add(new CountryList("my","60","Malaysia"));
		countries.add(new CountryList("mv","960","Maldives"));
		countries.add(new CountryList("ml","223","Mali"));
		countries.add(new CountryList("mt","356","Malta"));
		countries.add(new CountryList("mh","692","Marshall Islands"));
		countries.add(new CountryList("mr","222","Mauritania"));
		countries.add(new CountryList("mu","230","Mauritius"));
		countries.add(new CountryList("yt","262","Mayotte"));
		countries.add(new CountryList("mx","52","Mexico"));
		countries.add(new CountryList("fm","691","Micronesia, Federated States Of"));
		countries.add(new CountryList("md","373","Moldova, Republic Of"));
		countries.add(new CountryList("mc","377","Monaco"));
		countries.add(new CountryList("mn","976","Mongolia"));
		countries.add(new CountryList("me","382","Montenegro"));
		countries.add(new CountryList("ma","212","Morocco"));
		countries.add(new CountryList("mz","258","Mozambique"));
		countries.add(new CountryList("na","264","Namibia"));
		countries.add(new CountryList("nr","674","Nauru"));
		countries.add(new CountryList("np","977","Nepal"));
		countries.add(new CountryList("nl","31","Netherlands"));
		countries.add(new CountryList("nc","687","New Caledonia"));
		countries.add(new CountryList("nz","64","New Zealand"));
		countries.add(new CountryList("ni","505","Nicaragua"));
		countries.add(new CountryList("ne","227","Niger"));
		countries.add(new CountryList("ng","234","Nigeria"));
		countries.add(new CountryList("nu","683","Niue"));
		countries.add(new CountryList("kp","850","Korea, Democratic People&apos;s Republic Of"));
		countries.add(new CountryList("no","47","Norway"));
		countries.add(new CountryList("om","968","Oman"));
		countries.add(new CountryList("pk","92","Pakistan"));
		countries.add(new CountryList("pw","680","Palau"));
		countries.add(new CountryList("pa","507","Panama"));
		countries.add(new CountryList("pg","675","Papua New Guinea"));
		countries.add(new CountryList("py","595","Paraguay"));
		countries.add(new CountryList("pe","51","Peru"));
		countries.add(new CountryList("ph","63","Philippines"));
		countries.add(new CountryList("pn","870","Pitcairn"));
		countries.add(new CountryList("pl","48","Poland"));
		countries.add(new CountryList("pt","351","Portugal"));
		countries.add(new CountryList("pr","1","Puerto Rico"));
		countries.add(new CountryList("qa","974","Qatar"));
		countries.add(new CountryList("ro","40","Romania"));
		countries.add(new CountryList("ru","7","Russian Federation"));
		countries.add(new CountryList("rw","250","Rwanda"));
		countries.add(new CountryList("bl","590","Saint Barthélemy"));
		countries.add(new CountryList("ws","685","Samoa"));
		countries.add(new CountryList("sm","378","San Marino"));
		countries.add(new CountryList("st","239","Sao Tome And Principe"));
		countries.add(new CountryList("sa","966","Saudi Arabia"));
		countries.add(new CountryList("sn","221","Senegal"));
		countries.add(new CountryList("rs","381","Serbia"));
		countries.add(new CountryList("sc","248","Seychelles"));
		countries.add(new CountryList("sl","232","Sierra Leone"));
		countries.add(new CountryList("sg","65","Singapore"));
		countries.add(new CountryList("sk","421","Slovakia"));
		countries.add(new CountryList("si","386","Slovenia"));
		countries.add(new CountryList("sb","677","Solomon Islands"));
		countries.add(new CountryList("so","252","Somalia"));
		countries.add(new CountryList("za","27","South Africa"));
		countries.add(new CountryList("kr","82","Korea, Republic Of"));
		countries.add(new CountryList("es","34","Spain"));
		countries.add(new CountryList("lk","94","Sri Lanka"));
		countries.add(new CountryList("sh","290","Saint Helena, Ascension And Tristan Da Cunha"));
		countries.add(new CountryList("pm","508","Saint Pierre And Miquelon"));
		countries.add(new CountryList("sd","249","Sudan"));
		countries.add(new CountryList("sr","597","Suriname"));
		countries.add(new CountryList("sz","268","Swaziland"));
		countries.add(new CountryList("se","46","Sweden"));
		countries.add(new CountryList("ch","41","Switzerland"));
		countries.add(new CountryList("sy","963","Syrian Arab Republic"));
		countries.add(new CountryList("tw","886","Taiwan, Province Of China"));
		countries.add(new CountryList("tj","992","Tajikistan"));
		countries.add(new CountryList("tz","255","Tanzania, United Republic Of"));
		countries.add(new CountryList("th","66","Thailand"));
		countries.add(new CountryList("tg","228","Togo"));
		countries.add(new CountryList("tk","690","Tokelau"));
		countries.add(new CountryList("to","676","Tonga"));
		countries.add(new CountryList("tn","216","Tunisia"));
		countries.add(new CountryList("tr","90","Turkey"));
		countries.add(new CountryList("tm","993","Turkmenistan"));
		countries.add(new CountryList("tv","688","Tuvalu"));
		countries.add(new CountryList("ae","971","United Arab Emirates"));
		countries.add(new CountryList("ug","256","Uganda"));
		countries.add(new CountryList("gb","44","United Kingdom"));
		countries.add(new CountryList("ua","380","Ukraine"));
		countries.add(new CountryList("uy","598","Uruguay"));
		countries.add(new CountryList("us","1","United States"));
		countries.add(new CountryList("uz","998","Uzbekistan"));
		countries.add(new CountryList("vu","678","Vanuatu"));
		countries.add(new CountryList("va","39","Holy See (vatican City State)"));
		countries.add(new CountryList("ve","58","Venezuela, Bolivarian Republic Of"));
		countries.add(new CountryList("vn","84","Viet Nam"));
		countries.add(new CountryList("wf","681","Wallis And Futuna"));
		countries.add(new CountryList("ye","967","Yemen"));
		countries.add(new CountryList("zm","260","Zambia"));
		countries.add(new CountryList("zw","263","Zimbabwe"));
		return countries;
	}

	public static String getLocalTimeToGMT(Date localTime) {
		//Date will return local time in Java  
		//Date localTime = new Date(); 

		//creating DateFormat for converting time from local timezone to GMT
		DateFormat converter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		//getting GMT timezone, you can get any timezone e.g. UTC
		converter.setTimeZone(TimeZone.getTimeZone("GMT"));

		System.out.println("local time : " + localTime);;
		System.out.println("time in GMT : " + converter.format(localTime));
		return converter.format(localTime);
		//Read more: http://javarevisited.blogspot.com/2012/04/how-to-convert-local-time-to-gmt-in.html#ixzz2i5QriBRI
	}


	public static boolean isNetworkAvailable(Context context)
	{

		ConnectivityManager connectivity  = null;
		boolean isNetworkAvail = false;

		try
		{
			connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (connectivity != null)
			{
				NetworkInfo[] info = connectivity.getAllNetworkInfo();

				if (info != null)
				{
					for (int i = 0; i < info.length; i++)
					{
						if (info[i].getState() == NetworkInfo.State.CONNECTED)
						{

							return true;
						}
					}
				}
			}
			return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(connectivity !=null)
			{
				connectivity = null;
			}
		}
		return isNetworkAvail;
	}

	public static void ShowAlert(String msg,Context context)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context,5);

		// set title
		alertDialogBuilder.setTitle(context.getString(R.string.note));

		// set dialog message
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)

				.setNegativeButton(context.getResources().getString(R.string.ok),new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog,int id)
					{
						//closing the application
						dialog.dismiss();
					}
				});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();

	}

	public String getCurrentGmtTime()
	{
		String curentdateTime=null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
		curentdateTime = sdf.format(new Date());

		return curentdateTime;
	}


	public static File getSdCardPath()
	{

		File sdCardPath=null;
		sdCardPath=Environment.getExternalStorageDirectory();

		return sdCardPath;
	}

	//---------------------------delete non empty directory------------------------------

	public static boolean deleteNon_EmptyDir(File dir)
	{

		if (dir.isDirectory())
		{

			String[] children = dir.list();

			if (children!=null&&children.length>0)
			{

				for (int i=0; i<children.length; i++)
				{
					boolean success = deleteNon_EmptyDir(new File(dir, children[i]));

					if (!success)
					{
						return false;
					}
				}
			}

		}
		return dir.delete();
	}

	//------------------------------ To create file---------------------------

	public static File createFile(String directoryname,String filename)
	{

		File createdirectoty,createdFileName;
		//Utility.printLog(TAG, "createFile");
		createdirectoty = new File(Environment.getExternalStorageDirectory() + "/"+directoryname);
		//Utility.printLog(TAG, "createFile  createdirectoty "+createdirectoty);
		if (!createdirectoty.exists())
		{     //Utility.printLog(TAG, "createFile directory is not created yet");
			createdirectoty.mkdir();
			//System.out.println("now signed directory is created succes is "+success);
			createdFileName = new File(createdirectoty, filename);
			//Utility.printLog(TAG, "createFile createdFileNamet  "+createdFileName);

		}
		else
		{
			//System.out.println("createFile directory is created already ");
			//Utility.printLog(TAG, "createFile  directory is created already ");
			createdFileName = new File(createdirectoty, filename);
			System.out.println("my signed file is from else block  is  "+createdFileName);
			////Utility.printLog(TAG, "createFile createdFileNamet  "+createdFileName);
			// Do something else on failure
		}
		return createdFileName;
	}



}
