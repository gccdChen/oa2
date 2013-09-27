package scau.duolian.oa.base;

import net.tsz.afinal.http.AjaxCallBack;
import scau.duolian.oa.util.AppUtil;

public class MyCallBack extends AjaxCallBack<String>{
	private BaseUi baseUi;
	private boolean showLoadBar = false;
	
	public MyCallBack(BaseUi baseUi) {
		// TODO Auto-generated constructor stub
		this.baseUi = baseUi;
	}
	/**
	 * 
	 * @param baseUi 
	 * @param showLoadBar 是否打开loadbar
	 */
	public MyCallBack(BaseUi baseUi,boolean showLoadBar) {
		// TODO Auto-generated constructor stub
		this.baseUi = baseUi;
		this.showLoadBar = showLoadBar;
	}
	
	@Override
	public void onFailure(Throwable t, String strMsg) {
		// TODO Auto-generated method stub
		super.onFailure(t, strMsg);
		if(showLoadBar)
			baseUi.hideLoadBar();
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(showLoadBar)
			baseUi.showLoadBar();
	}
	
	@Override
	public void onSuccess(String t) {
		// TODO Auto-generated method stub
		super.onSuccess(t);
		if(showLoadBar)
			baseUi.hideLoadBar();
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
