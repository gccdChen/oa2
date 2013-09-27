package scau.duolian.oa.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

public class DeviceHelper {

	public static void dial(Context context,String number) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ number));
		context.startActivity(intent);
	}
	public static void sendSms(Context context,String number,String content){
		Uri uri = Uri.parse("smsto:"+number);            
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);            
		intent.putExtra("sms_body", content);
		context.startActivity(intent);
	}
	
	public boolean checkNetWork(Context context){
		 ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 if(connectivity != null){
			 for(NetworkInfo info : connectivity.getAllNetworkInfo()){
				 if(info.isConnected()){
					 Log.i("checkNetWork","the "+info.getTypeName()+" is on;");
					 return true;
				 }else{
					 Log.i("checkNetWork","the "+info.getTypeName()+" is off;");
				 }
			 }
		 }
		 return false;
	}
	
	public static String getMac(Context context){
		
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = null;
		if(wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLED){
			wifi.setWifiEnabled(true);
			info = wifi.getConnectionInfo();
			wifi.setWifiEnabled(false);
		}else{
			info = wifi.getConnectionInfo();
		}
		
		return info.getMacAddress();
	}
	
	/**
	 * 获取imei
	 * @param context
	 * @return
	 */
	public static String getImei(Context context){
		String imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		return imei;
	}
	
	/**
	 * 关闭输入法
	 * @param context
	 */
	public static void closeInput(Context context){
		InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	private void abortTM(Context context){
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		  tm.getCallState();//int     
		       
		  tm.getCellLocation();//CellLocation     
		       
		  tm.getDeviceId();//String     
		       
		  tm.getDeviceSoftwareVersion();//String     
		       
		  tm.getLine1Number();//String     
		       
		  tm.getNeighboringCellInfo();//List<NeighboringCellInfo>     
		       
		  tm.getNetworkCountryIso();//String     
		       
		  tm.getNetworkOperator();//String     
		       
		  tm.getNetworkOperatorName();//String     
		       
		  tm.getNetworkType();//int     
		       
		  tm.getPhoneType();//int     
		       
		  tm.getSimCountryIso();//String     
		       
		  tm.getSimOperator();//String     
		       
		  tm.getSimOperatorName();//String     
		       
		  tm.getSimSerialNumber();//String     
		       
		  tm.getSimState();//int     
		       
		  tm.getSubscriberId();//String     
		       
		  tm.getVoiceMailAlphaTag();//String     
		       
		  tm.getVoiceMailNumber();//String     
		       
		  tm.hasIccCard();//boolean     
		       
		  tm.isNetworkRoaming();//     
	}
	public static void sendEmail(Context context, String email) {
		// TODO Auto-generated method stub
		
	}
}

    
