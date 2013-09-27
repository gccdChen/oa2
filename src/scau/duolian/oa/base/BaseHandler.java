package scau.duolian.oa.base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class BaseHandler extends Handler {
	
	protected BaseUi ui;
	
	public BaseHandler (BaseUi ui) {
		this.ui = ui;
	}
	
	public BaseHandler (Looper looper) {
		super(looper);
	}
	
	@Override
	public void handleMessage(Message msg) {
		try {
			int taskId;
			String result;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}