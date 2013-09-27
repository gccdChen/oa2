package scau.duolian.oa.base;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BaseService extends Service{
	public FinalHttp fhttp = BaseUi.fhttp;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	protected String getMac(){
		/*SharedPreferences preferences = getPreferences(0);
		String mac = preferences.getString(C.config.mac, "");
		if(StringUtil.isBlank(mac)){
			mac = DeviceHelper.getMac(this);
			preferences.edit().putString(C.config.mac, mac).commit();
		}
		return mac;*/
		return "00:16:6D:C0:1D:5D";//test
	}
	
	public void get(String url,AjaxCallBack callBack){
		AjaxParams params = new AjaxParams();
		params.put("c", "sync");
		fhttp.get(url, params,callBack);
	}
	
	public void get(String url,AjaxParams params,AjaxCallBack callBack){
		url = getUrl(url);
		params.put("c", "sync");
		Log.i("params", params.toString());
		fhttp.get(url,params, callBack);
	}
	
	public void post(String url,AjaxParams params,AjaxCallBack callBack){
		url = getUrl(url);
		Log.i("params", params.toString());
		params.put("c", "sync");
		fhttp.post(url,params, callBack);
	}
	
	private String getUrl(String url) {
		// TODO Auto-generated method stub
		String u = null;
		if(url.startsWith("http:"))
			u = url;
		else
			u = C.api.BASE+url;
		Log.i("url", u);
		return u;
	}
}
