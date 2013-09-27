package scau.duolian.oa.ui;
import net.tsz.afinal.http.AjaxParams;
import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.util.DateUtil;
import scau.duolian.oa.util.dl.HttpConnect;
import scau.duolian.oa.util.dl.QLog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 考勤
 */
public class UiSignin extends BaseUiAuth {
	private static final String TAG = UiSignin.class.getSimpleName();
	TextView tv_point;
	TextView tv_addr;
	TextView tv_time;
	EditText tv_ds;
	String addr = null;
    double nLongitude ;
    double nLatitude ;
    public Runnable mWaitRunnable = new Runnable() {
        public void run() {
			HttpConnect httpConnect = HttpConnect.getInstance();
			if(httpConnect.getCode() == 999)
			{
				nLongitude = httpConnect.nLongitude;
				nLatitude = httpConnect.nLatitude;
				addr = HttpConnect.getInstance().sAddr;
				tv_point.setText("经度:"+httpConnect.nLongitude +"\n"+"纬度:"+httpConnect.nLatitude);
				tv_time.setText(DateUtil.getDateStr());
				tv_addr.setText(addr);
			}else{
    			Toast.makeText(UiSignin.this,"【考勤时间失败】"+ httpConnect.getDesc(), 0).show();
			}
        }
    };
    
    public Runnable mWaitRunnableI = new Runnable() {
        public void run() {
			HttpConnect httpConnect = HttpConnect.getInstance();
			if(httpConnect.getCode() == 999)
			{
				nLongitude = httpConnect.nLongitude;
				nLatitude = httpConnect.nLatitude;
				addr = HttpConnect.getInstance().sAddr;
				tv_point.setText("经度:"+httpConnect.nLongitude +"\n"+"纬度:"+httpConnect.nLatitude);
				tv_time.setText(DateUtil.getDateStr());
				tv_addr.setText(HttpConnect.getInstance().sAddr);
			}else{
    			Toast.makeText(UiSignin.this,"【考勤时间失败】"+ httpConnect.getDesc(), 0).show();
			}
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_signin);
	    init();
    }
    
	private void init() {
		// TODO Auto-generated method stub
		
	}

	public void confirmjf()
	{
        final String sw = tv_ds.getText().toString();
        String sinfo = "";
        if(sw.length() <= 2)
        	sinfo = "解释的太勉强了兄弟，要不再加几个字？";
        else
        	sinfo = "上次考勤的地址和时间将会改变，确定提交？";
		AlertDialog exitDialog = new AlertDialog.Builder(UiSignin.this)
            .setTitle(sinfo)
            .setPositiveButton("确定"
            		, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// todo:
							submit();
						}
					})
			.setNegativeButton("取消", null).create();
		exitDialog.show();
	}
	
	void submit() {
		// TODO Auto-generated method stub
		String ds = tv_ds. getText().toString();
		AjaxParams params = new AjaxParams();
		params.put("a", "update");
		params.put(C.params.dlyid, getUser().dlyid);
		params.put(C.params.uid, getUser().uid);
		params.put(C.params.mac, getMac());
		params.put("cdjs", ds);
		params.put("addr", addr);
		params.put("lo", ""+nLongitude);
		params.put("la", ""+nLatitude);
		post(C.api.qiandao, params, new MyCallBack(this,false){
			@Override
			public void onResult(BaseMessage message) {
				// TODO Auto-generated method stub
				super.onResult(message);
				toast(message.getMessage());
				if(message.isSuccess())
					finish();
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下BACK键回到历史页面中
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			UiSignin.this.setResult(0);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onPause() {
		super.onPause();
	}
}
