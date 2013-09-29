package scau.duolian.oa.util;

import java.util.Calendar;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

public class DeviceHelper {

	public static void dial(Context context, String number) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
		context.startActivity(intent);
	}

	public static void sendSms(Context context, String number, String content) {
		Uri uri = Uri.parse("smsto:" + number);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra("sms_body", content);
		context.startActivity(intent);
	}

	public boolean checkNetWork(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			for (NetworkInfo info : connectivity.getAllNetworkInfo()) {
				if (info.isConnected()) {
					Log.i("checkNetWork", "the " + info.getTypeName() + " is on;");
					return true;
				} else {
					Log.i("checkNetWork", "the " + info.getTypeName() + " is off;");
				}
			}
		}
		return false;
	}

	public static String getMac(Context context) {

		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = null;
		if (wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
			wifi.setWifiEnabled(true);
			info = wifi.getConnectionInfo();
			wifi.setWifiEnabled(false);
		} else {
			info = wifi.getConnectionInfo();
		}

		return info.getMacAddress();
	}

	/**
	 * 获取imei
	 * 
	 * @param context
	 * @return
	 */
	public static String getImei(Context context) {
		String imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		return imei;
	}

	/**
	 * 关闭输入法
	 * 
	 * @param context
	 */
	public static void closeInput(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private void abortTM(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		tm.getCallState();// int

		tm.getCellLocation();// CellLocation

		tm.getDeviceId();// String

		tm.getDeviceSoftwareVersion();// String

		tm.getLine1Number();// String

		tm.getNeighboringCellInfo();// List<NeighboringCellInfo>

		tm.getNetworkCountryIso();// String

		tm.getNetworkOperator();// String

		tm.getNetworkOperatorName();// String

		tm.getNetworkType();// int

		tm.getPhoneType();// int

		tm.getSimCountryIso();// String

		tm.getSimOperator();// String

		tm.getSimOperatorName();// String

		tm.getSimSerialNumber();// String

		tm.getSimState();// int

		tm.getSubscriberId();// String

		tm.getVoiceMailAlphaTag();// String

		tm.getVoiceMailNumber();// String

		tm.hasIccCard();// boolean

		tm.isNetworkRoaming();//
	}

	public static void sendEmail(Context context, String email) {
		// TODO Auto-generated method stub
		        String[] reciver = new String[] {email};  
//		        String[] mySbuject = new String[] { "test" };  
//		        String myCc = "cc";  
//		        String mybody = "测试Email Intent";  
		        Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);  
		        myIntent.setType("plain/text");  
		        myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);  
//		        myIntent.putExtra(android.content.Intent.EXTRA_CC, myCc);  
//		        myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mySbuject);  
//		        myIntent.putExtra(android.content.Intent.EXTRA_TEXT, mybody);  
		       context.startActivity(Intent.createChooser(myIntent, "mail test"));  
		  
	}

	private static String calanderURL = "";
	private static String calanderEventURL = "";
	private static String calanderRemiderURL = "";
	// 为了兼容不同版本的日历,2.2以后url发生改变
	static {
		if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
			calanderURL = "content://com.android.calendar/calendars";
			calanderEventURL = "content://com.android.calendar/events";
			calanderRemiderURL = "content://com.android.calendar/reminders";

		} else {
			calanderURL = "content://calendar/calendars";
			calanderEventURL = "content://calendar/events";
			calanderRemiderURL = "content://calendar/reminders";
		}
	}

	public static void addToCalendar(String title,String content, Calendar cal, ContentResolver conre) {

		String calId = "";
		Cursor userCursor = conre.query(Uri.parse(calanderURL), null, null, null, null);
		if (userCursor.getCount() > 0) {
			userCursor.moveToFirst();
			calId = userCursor.getString(userCursor.getColumnIndex("_id"));
		}
		ContentValues event = new ContentValues();
		
		event.put("title", title);
		event.put("description", content);
		event.put("calendar_id", calId);
		event.put("eventTimezone", Time.getCurrentTimezone());
		long start = cal.getTime().getTime();
		long end = cal.getTime().getTime();

		event.put("dtstart", start);
		event.put("dtend", end);
		event.put("hasAlarm", 1);

		Uri newEvent = conre.insert(Uri.parse(calanderEventURL), event);
		long id = Long.parseLong(newEvent.getLastPathSegment());
		ContentValues values = new ContentValues();
		values.put("event_id", id);
		values.put("minutes", 10);
		conre.insert(Uri.parse(calanderRemiderURL), values);
	}
}
