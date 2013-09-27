package scau.duolian.oa.util;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

public class NetStateUtil {
	
	static public int WAP_INT = 1;
	static public int NET_INT = 2;
	static public int WIFI_INT = 3;
	static public int NONET_INT = 4;
	
	static private Uri APN_URI = null;
	
	static public int getNetType (Context ctx) {
		// has network
		ConnectivityManager conn = null;
		try {
			conn = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (conn == null) {
			return NetStateUtil.NONET_INT;
		}
		NetworkInfo info = conn.getActiveNetworkInfo();
		boolean available = info.isAvailable();
		if (!available){
			return NetStateUtil.NONET_INT;
		}
		// check use wifi
		String type = info.getTypeName();
		if (type.equals("WIFI")) {
			return NetStateUtil.WIFI_INT;
		}
		// check use wap
		APN_URI = Uri.parse("content://telephony/carriers/preferapn");
		Cursor uriCursor = ctx.getContentResolver().query(APN_URI, null, null, null, null);
		if (uriCursor != null && uriCursor.moveToFirst()) {
			String proxy = uriCursor.getString(uriCursor.getColumnIndex("proxy"));
			String port = uriCursor.getString(uriCursor.getColumnIndex("port"));
			String apn = uriCursor.getString(uriCursor.getColumnIndex("apn"));
			if (proxy != null && port != null && apn != null && apn.equals("cmwap") && port.equals("80") &&
				(proxy.equals("10.0.0.172") || proxy.equals("010.000.000.172"))) {
				return NetStateUtil.WAP_INT;
			}
		}
		return NetStateUtil.NET_INT;
	}
	
}