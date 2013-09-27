package scau.duolian.oa.base;

import android.content.Context;
import net.tsz.afinal.http.AjaxCallBack;
import scau.duolian.oa.util.AppUtil;

public class MyCallBackForService extends AjaxCallBack<String>{
	private Context context;
	
	public MyCallBackForService(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	@Override
	public void onFailure(Throwable t, String strMsg) {
		// TODO Auto-generated method stub
		super.onFailure(t, strMsg);
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	public void onSuccess(String t) {
		// TODO Auto-generated method stub
		super.onSuccess(t);
		BaseMessage bm = null;
		try {
			bm = AppUtil.getMessage(t);
			onResult(bm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onResult(BaseMessage message){}
}
